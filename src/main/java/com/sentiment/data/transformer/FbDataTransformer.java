package com.sentiment.data.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.sentiment.common.Constants;
import com.sentiment.model.FbPostComment;
import com.sentiment.model.MatchedFbPostComment;
import com.sentiment.util.JsonUtil;

public class FbDataTransformer implements DataTransformer<FbPostComment> {
	private static final String SEARCHED_WORDS_REGEX_SPLITTER_PATTERN = "[\\s,]+";
	private static final String SEARCHED_WORDS_REGEX_JOINER = "|";
	
	private final Gson gson = JsonUtil.getInstance();
	private final Properties appProps;
	
	public FbDataTransformer(Properties... properties) {
		super();
		appProps = properties[0];
	}

	@Override
	public List<String> transform(List<FbPostComment> fbPostsComments) {
		Pattern regex = generateRegexPattern(appProps);
		
		List<FbPostComment> matchedComments = matchComments(fbPostsComments, regex);
		
		fbPostsComments.removeIf(comment -> matchedComments.stream()
				.filter(matchedComment -> 
					regex.matcher(comment.getMessage()).find()).count() != 0);
		
		return mergeTransformedComments(fbPostsComments, matchedComments);
	}

	private List<FbPostComment> matchComments(List<FbPostComment> fbPostsComments, Pattern regex) {
		return fbPostsComments.stream()
				.filter(comment -> regex.matcher(comment.getMessage()).find())
				.map(comment -> new FbPostComment(comment.getId(), comment.getMessage(), comment.getCreatedTime()))
				.collect(Collectors.toList());
	}
	
	private Pattern generateRegexPattern(Properties props) {
		String[] searchedWords = props.getProperty(Constants.COMMENTS_SEARCHED_PHRASES).split(SEARCHED_WORDS_REGEX_SPLITTER_PATTERN);
		Set<String> uniqueSearchedWords = new HashSet<>(Arrays.asList(searchedWords));
		
		return Pattern.compile(String.join(SEARCHED_WORDS_REGEX_JOINER, uniqueSearchedWords), Pattern.CASE_INSENSITIVE); 
	}

	private List<String> mergeTransformedComments(List<FbPostComment> fbPostsComments, List<FbPostComment> matchedComments) {
		List<String> fbPostsCommentsJSON = convertRegularPostsCommentsToJSON(fbPostsComments);
		List<String> matchedCommentsJSON = convertMatchedPostsCommentsToJSON(matchedComments);
		
		List<String> mergedTransformedComments = new ArrayList<>(fbPostsCommentsJSON);
		mergedTransformedComments.addAll(matchedCommentsJSON);
		
		return mergedTransformedComments;
	}
	
	private List<String> convertMatchedPostsCommentsToJSON(List<FbPostComment> matchedComments) {
		return matchedComments.stream()
				.map(MatchedFbPostComment::new)
				.map(gson::toJson)
				.collect(Collectors.toList());
	}

	private List<String> convertRegularPostsCommentsToJSON(List<FbPostComment> fbPostsComments) {
		return fbPostsComments.stream()
				.map(gson::toJson)
				.collect(Collectors.toList());
	}
}

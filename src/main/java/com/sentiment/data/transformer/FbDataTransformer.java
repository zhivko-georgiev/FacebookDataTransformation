package com.sentiment.data.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sentiment.common.Constants;
import com.sentiment.model.FbPostComment;
import com.sentiment.util.PropertiesUtil;

public class FbDataTransformer implements DataTransformer<FbPostComment> {
	private static final String COMMENTS_SEARCHED_PHRASES_DELIMITER = ",";
	private static final String SEARCHED_WORDS_REGEX_SPLITTER = " ";
	private static final String SEARCHED_WORDS_REGEX_JOINER = "|";
	
	private final Gson gson = new Gson();
	private final Properties props = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_FILENAME);

	@Override
	public List<String> transform(List<FbPostComment> fbPostsComments) {
		Pattern regex = generateRegexPattern(props);

		List<FbPostComment> matchedComments = fbPostsComments.stream()
				.filter(comment -> regex.matcher(comment.getMessage()).find())
				.map(comment -> new FbPostComment(comment.getId(), comment.getMessage(), comment.getCreatedTime()))
				.collect(Collectors.toList());
		
		fbPostsComments.removeIf(s -> matchedComments.stream()
				.filter(comment -> 
					regex.matcher(s.getMessage()).find()).count() != 0);
		
		List<String> fbPostsCommentsJSON = convertRegularPostsCommentsToJSON(fbPostsComments);
		List<String> matchedCommentsJSON = convertMatchedPostsCommentsToJSON(matchedComments);
		
		List<String> mergedTransformedComments = new ArrayList<>(fbPostsCommentsJSON);
		mergedTransformedComments.addAll(matchedCommentsJSON);
		
		return mergedTransformedComments;
	}
	
	private List<String> convertMatchedPostsCommentsToJSON(List<FbPostComment> matchedComments) {
		return matchedComments.stream()
				.map(comment -> {
					JsonObject jsonObj = new JsonObject();
					jsonObj.add(props.getProperty(Constants.COMMENTS_MATCHED_ADDITIONAL_PROPERTY), new Gson().toJsonTree(comment));
					
					return gson.toJson(jsonObj);
				})
				.collect(Collectors.toList());
		
	}

	private List<String> convertRegularPostsCommentsToJSON(List<FbPostComment> fbPostsComments) {
		return fbPostsComments.stream()
				.map(gson::toJson)
				.collect(Collectors.toList());
	}

	private Pattern generateRegexPattern(Properties props) {
		List<String> searchedPhrases = Arrays.stream
				(props.getProperty(Constants.COMMENTS_SEARCHED_PHRASES)
				.split(COMMENTS_SEARCHED_PHRASES_DELIMITER))
				.collect(Collectors.toList());
		
		Set<String> searchedWords = searchedPhrases.stream()
				.map(word -> word.split(SEARCHED_WORDS_REGEX_SPLITTER))
				.flatMap(Arrays::stream)
				.collect(Collectors.toSet());
		
		StringJoiner stringJoiner = new StringJoiner(SEARCHED_WORDS_REGEX_JOINER);
		searchedWords.forEach(stringJoiner::add);
		
		return Pattern.compile(stringJoiner.toString()); 
	}
}

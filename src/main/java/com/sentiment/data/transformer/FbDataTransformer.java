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

	@Override
	public List<String> transform(List<FbPostComment> fbPostsComments) {
		List<String> searchedPhrases = generateListOfSearchedPhrases();
		
		Set<String> searchedWords = searchedPhrases.stream()
				.map(word -> word.split(" "))
				.flatMap(Arrays::stream)
				.collect(Collectors.toSet());

		Pattern regex = generateRegexPattern(searchedWords);

		List<FbPostComment> matchedComments = fbPostsComments.stream()
				.filter(comment -> regex.matcher(comment.getMessage()).find())
				.map(comment -> new FbPostComment(comment.getId(), comment.getMessage(), comment.getCreatedTime()))
				.collect(Collectors.toList());
		
		fbPostsComments.removeIf(s -> matchedComments.stream()
				.filter(comment -> regex.matcher(s.getMessage()).find()).count() != 0);
		
		Gson gson = new Gson();

		List<String> fbPostsCommentsJSON = fbPostsComments.stream()
				.map(gson::toJson)
				.collect(Collectors.toList());
		
		List<String> matchedCommentsJSON = matchedComments.stream()
				.map(comment -> {
					JsonObject jsonObj = new JsonObject();
					jsonObj.add("test", new Gson().toJsonTree(comment));
					
					return gson.toJson(jsonObj);
				})
				.collect(Collectors.toList());
		
		List<String> mergedTransformedComments = new ArrayList<>(fbPostsCommentsJSON);
		mergedTransformedComments.addAll(matchedCommentsJSON);
		
		return mergedTransformedComments;
	}
	
	private Pattern generateRegexPattern(Set<String> searchedPhrases) {
		StringJoiner stringJoiner = new StringJoiner("|");
		searchedPhrases.forEach(stringJoiner::add);
		
		return Pattern.compile(stringJoiner.toString()); 
	}

	private List<String> generateListOfSearchedPhrases() {
		Properties props = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_FILENAME);
		String value = props.getProperty(Constants.COMMENTS_SEARCHED_PHRASES);
		String[] splittedPhrases = value.split(",");
		
		return Arrays.stream(splittedPhrases)
				.collect(Collectors.toList());
	}
}

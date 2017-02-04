package com.sentiment.data.transformer;

import java.util.Arrays;
import java.util.Date;
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
	public List<String> transform(List<FbPostComment> data) {
		List<String> searchedPhrases = generateListOfSearchedPhrases();
		
		Set<String> searchedWords = searchedPhrases.stream()
				.map(word -> word.split(" "))
				.flatMap(Arrays::stream)
				.collect(Collectors.toSet());

		Pattern regex = generateRegexPattern(searchedWords);

		List<FbPostComment> matchedComments = data.stream()
				.filter(s -> regex.matcher(s.getMessage()).find())
				.map(s -> new FbPostComment(s.getId(), s.getMessage(), s.getCreatedTime()))
				.collect(Collectors.toList());

		//TODO: Convert the comments to List of JSON Strings
		Gson gson = new Gson();
		JsonObject jobj = new JsonObject();

		// Convert POJO to JSON
		String fbComment = gson.toJson(new FbPostComment("100_200", "Gosooo", new Date()));
		
		// Assign JSON object to new property
		jobj.add("test", new Gson().toJsonTree(fbComment));
		String formattedJson = gson.toJson(jobj);

		return matchedComments.stream()
				.map(FbPostComment::getMessage)
				.collect(Collectors.toList());
		
	}
	
	private Pattern generateRegexPattern(Set<String> searchedPhrases) {
		StringJoiner stringJoiner = new StringJoiner("|");
		searchedPhrases.forEach(stringJoiner::add);
		
		return Pattern.compile(stringJoiner.toString()); 
	}

	private List<String> generateListOfSearchedPhrases() {
		Properties props = PropertiesUtil.loadPropertiesFile(Constants.FB_PROPS_FILENAME);
		String value = props.getProperty(Constants.COMMENTS_SEARCHED_PHRASES);
		String[] splittedPhrases = value.split(",");
		
		return Arrays.stream(splittedPhrases)
				.collect(Collectors.toList());
	}
}

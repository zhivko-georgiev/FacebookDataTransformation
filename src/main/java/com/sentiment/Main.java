package com.sentiment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

public class Main {
	private static final String FB_OAUTH_APPID_KEY = "oauth.appId";
	private static final String COMMENTS_CREATED_SINCE_DAYS_KEY = "comments.created.since.days";
	private static final String COMMENTS_SEARCHED_PHRASES = "comments.searched.phrases";
	private static final String PROPS_FILENAME = "facebook4j.properties";
	
	public static void main(String[] args) throws FacebookException, IOException {
		Properties props = loadPropertiesFile();
		Facebook facebook = new FacebookFactory().getInstance();
		
		// Getting all the posts
		ResponseList<Post> posts = facebook.getFeed(props.getProperty(FB_OAUTH_APPID_KEY), 
				new Reading().since(generateSinceDateFormat(props)).fields("comments"));
		
		// Extracting all posts' comments
		List<Comment> comments = posts.stream()
				.map(post -> post.getComments())
				.flatMap(postComments -> postComments.stream())
				.collect(Collectors.toList());
		
		
		// Print the comments' messages
		comments.stream()
				.map(c -> c.getMessage())
				.forEach(System.out::println);
		
		// Getting the unique words to match against
		Set<String> uniqueWords = generateListOfSearchedPhrases(props).stream()
				.map(word -> word.split(" "))
				.flatMap(s -> Arrays.stream(s))
				.collect(Collectors.toSet());
	}

	private static Properties loadPropertiesFile() throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream(PROPS_FILENAME)) {
			props.load(resourceStream);
		}

		return props;
	}
	
	private static String generateSinceDateFormat(Properties props) {
		String sinceDays = props.getProperty(COMMENTS_CREATED_SINCE_DAYS_KEY);
		
		return "-" + sinceDays + " days";
	}
	
	private static List<String> generateListOfSearchedPhrases(Properties props) {
		String value = props.getProperty(COMMENTS_SEARCHED_PHRASES);
		
		String[] splittedPhrases = value.split(",");
		
		return Arrays.stream(splittedPhrases)
				.map(s -> s.toString())
				.collect(Collectors.toList());
	}
}

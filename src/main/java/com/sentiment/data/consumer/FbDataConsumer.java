package com.sentiment.data.consumer;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.sentiment.common.Constants;
import com.sentiment.model.FbPostComment;
import com.sentiment.util.PropertiesUtil;

import facebook4j.Comment;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PagableList;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;

public class FbDataConsumer implements DataConsumer<FbPostComment> {
	
	@Override
	public Optional<List<FbPostComment>> consume() {
		Logger logger = Logger.getLogger(FbDataConsumer.class);
		Properties fbProps = PropertiesUtil.loadPropertiesFile(Constants.FB_PROPS_FILENAME);
		Properties appProps = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_FILENAME);
		Facebook facebook = new FacebookFactory().getInstance();
		
		try {
			ResponseList<Post> posts = facebook.getFeed(fbProps.getProperty(Constants.FB_OAUTH_APPID_KEY),
					new Reading()
					.since(generateSinceDateFormat(appProps))
					.fields("comments"));

			return Optional.of(posts.stream()
					.map(Post::getComments)
					.flatMap(PagableList<Comment>::stream)
					.map(comment -> new FbPostComment(comment.getId(), comment.getMessage(), comment.getCreatedTime()))
					.collect(Collectors.toList()));

		} catch (FacebookException exception) {
			logger.error("Something went wrong during communication with FB: ", exception);
		}
		
		return Optional.empty();
	}

	private String generateSinceDateFormat(Properties props) {
		String sinceDays = props.getProperty(Constants.COMMENTS_CREATED_SINCE_DAYS);
		
		return "-" + sinceDays + " days";
	}
}

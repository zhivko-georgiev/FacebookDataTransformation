package com.sentiment.data.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

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
	public List<FbPostComment> consume() {
		Properties props = PropertiesUtil.loadPropertiesFile(Constants.FB_PROPS_FILENAME);
		Facebook facebook = new FacebookFactory().getInstance();
		
		try {
			ResponseList<Post> posts = facebook.getFeed(props.getProperty(Constants.FB_OAUTH_APPID_KEY),
					new Reading()
					.since(generateSinceDateFormat(props))
					.fields("comments"));

			return posts.stream()
					.map(Post::getComments)
					.flatMap(PagableList<Comment>::stream)
					.map(comment -> new FbPostComment(comment.getId(), comment.getMessage(), comment.getCreatedTime()))
					.collect(Collectors.toList());

		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}

	private String generateSinceDateFormat(Properties props) {
		String sinceDays = props.getProperty(Constants.COMMENTS_CREATED_SINCE_DAYS_KEY);
		
		return "-" + sinceDays + " days";
	}
}

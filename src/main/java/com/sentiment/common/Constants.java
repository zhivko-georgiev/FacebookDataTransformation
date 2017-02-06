package com.sentiment.common;

public interface Constants {
	String APP_PROPS_FILENAME = "application.properties";
	String APP_PROPS_TEST_FILENAME = "application-test.properties";
	String FB_PROPS_FILENAME = "facebook4j.properties";
	
	String FB_OAUTH_APPID_KEY = "oauth.appId";

	String COMMENTS_CREATED_SINCE_DAYS = "comments.created.since.days";
	String COMMENTS_SEARCHED_PHRASES = "comments.searched.phrases";
	String COMMENTS_MATCHED_ADDITIONAL_PROPERTY = "comments.matched.additional.property";
	
	String REDIS_HOSTNAME = "redis.hostname";
	String REDIS_LIST_KEY = "redis.list.key";
}

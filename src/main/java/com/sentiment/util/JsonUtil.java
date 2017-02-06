package com.sentiment.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final Gson gson = getGson();

	static Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(DATE_TIME_FORMAT);

		return gsonBuilder.create();
	}
	
	public static String writeToJson(Object object) {
		return gson.toJson(object); 
	}
}

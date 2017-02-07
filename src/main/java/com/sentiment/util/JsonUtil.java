package com.sentiment.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	private static class Holder {
		static final Gson INSTANCE = new GsonBuilder().setDateFormat(DATE_TIME_FORMAT).create();
	}

	public static Gson getInstance() {
		return Holder.INSTANCE;
	}
}

package com.sentiment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static Properties loadPropertiesFile(String fileName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream(fileName)) {
			props.load(resourceStream);
		} catch (IOException e) {
			// TODO: handle exception
		}

		return props;
	}
}

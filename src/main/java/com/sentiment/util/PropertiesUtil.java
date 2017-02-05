package com.sentiment.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	private static final Logger logger = Logger.getLogger(PropertiesUtil.class);

	public static Properties loadPropertiesFile(String fileName) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try (InputStream resourceStream = loader.getResourceAsStream(fileName)) {
			props.load(resourceStream);
		} catch (IOException exception) {
			logger.error("Something went wrong during properties file reading: ", exception);
		}

		return props;
	}
}

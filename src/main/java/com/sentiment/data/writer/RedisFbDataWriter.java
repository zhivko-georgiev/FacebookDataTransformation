package com.sentiment.data.writer;

import java.util.List;
import java.util.Properties;

import com.sentiment.common.Constants;
import com.sentiment.util.PropertiesUtil;

import redis.clients.jedis.Jedis;

public class RedisFbDataWriter implements DataWriter {
	
	@Override
	public void write(List<String> data) {
		Properties props = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_FILENAME);
		try (Jedis jedisClient = new Jedis(props.getProperty(Constants.REDIS_HOSTNAME))) {
			data.stream()
				.forEach(string -> jedisClient.lpush(Constants.REDIS_LIST_KEY, string));
		}
	}
}

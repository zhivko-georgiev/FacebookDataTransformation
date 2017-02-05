package com.sentiment.data.writer;

import java.util.List;

import redis.clients.jedis.Jedis;

public class RedisDataWriter implements DataWriter {
	private static final String REDIS_HOSTNAME = "localhost";
	private static final String REDIS_LIST_KEY = "fb-posts_comments";
	
	private final Jedis jedisClient = new Jedis(REDIS_HOSTNAME); 

	@Override
	public void write(List<String> data) {
		data.stream()
			.forEach(string -> jedisClient.lpush(REDIS_LIST_KEY, string));
	}
}

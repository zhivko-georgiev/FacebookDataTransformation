package com.sentiment.data.writer;

import java.util.List;

import redis.clients.jedis.Jedis;

public class RedisDataWriter implements DataWriter {
	private final Jedis jedisClient = new Jedis("localhost"); 

	@Override
	public void write(List<String> data) {
		data.stream()
			.forEach(jedisClient::lpush);
	}
}

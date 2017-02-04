package com.sentiment.data.writer;

import java.util.List;

public class ConsoleDataWriter implements DataWriter {

	@Override
	public void write(List<String> data) {
		data.stream()
			.forEach(System.out::println);
	}
}

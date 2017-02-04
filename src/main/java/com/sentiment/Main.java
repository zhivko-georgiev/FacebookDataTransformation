package com.sentiment;

import java.util.List;

import com.sentiment.data.consumer.DataConsumer;
import com.sentiment.data.consumer.FbDataConsumer;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.transformer.FbDataTransformer;
import com.sentiment.data.writer.DataWriter;
import com.sentiment.data.writer.FbDataWriter;

import facebook4j.Comment;

public class Main {
	
	public static void main(String[] args) {
		// 1. Data Consumption
		DataConsumer<Comment> fbDataConsumer = new FbDataConsumer();
		List<Comment> comments = fbDataConsumer.consume();

		// 2. Data transformation
		DataTransformer<Comment> fbDataTransformer = new FbDataTransformer();
		List<String> transformedData = fbDataTransformer.transform(comments);
		
		transformedData.stream()
			.forEach(System.out::println);

		// TODO: 3. Data Writing
		DataWriter fbDataWriter = new FbDataWriter();
		fbDataWriter.write(transformedData);
	}
}

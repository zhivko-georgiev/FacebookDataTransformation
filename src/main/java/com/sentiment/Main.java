package com.sentiment;

import java.util.List;

import com.sentiment.data.consumer.DataConsumer;
import com.sentiment.data.consumer.FbDataConsumer;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.transformer.FbDataTransformer;
import com.sentiment.data.writer.ConsoleDataWriter;
import com.sentiment.data.writer.DataWriter;
import com.sentiment.model.FbPostComment;

public class Main {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		DataConsumer<FbPostComment> fbDataConsumer = new FbDataConsumer();
		List<FbPostComment> comments = fbDataConsumer.consume();

		DataTransformer<FbPostComment> fbDataTransformer = new FbDataTransformer();
		List<String> transformedData = fbDataTransformer.transform(comments);

		DataWriter consoleDataWriter = new ConsoleDataWriter();
		consoleDataWriter.write(transformedData);
		long time = System.currentTimeMillis() - start;
		System.out.println(time);
	}
}

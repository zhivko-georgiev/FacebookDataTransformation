package com.sentiment;

import java.util.List;
import java.util.Optional;

import com.sentiment.data.consumer.DataConsumer;
import com.sentiment.data.consumer.FbDataConsumer;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.transformer.FbDataTransformer;
import com.sentiment.data.writer.DataWriter;
import com.sentiment.data.writer.RedisFbDataWriter;
import com.sentiment.model.FbPostComment;

public class Main {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		DataConsumer<FbPostComment> fbDataConsumer = new FbDataConsumer();
		Optional<List<FbPostComment>> comments = fbDataConsumer.consume();
		
		if (comments.isPresent()) {
			DataTransformer<FbPostComment> fbDataTransformer = new FbDataTransformer();
			List<String> transformedData = fbDataTransformer.transform(comments.get());

			DataWriter consoleDataWriter = new RedisFbDataWriter();
			consoleDataWriter.write(transformedData);
		}

		long time = System.currentTimeMillis() - start;
		System.out.println(time);
	}
}

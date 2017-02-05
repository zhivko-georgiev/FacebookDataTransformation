package com.sentiment;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

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
		Logger logger = Logger.getLogger(Main.class);
		DataConsumer<FbPostComment> fbDataConsumer = new FbDataConsumer();
		Optional<List<FbPostComment>> commentsOptional = fbDataConsumer.consume();
		
		if (commentsOptional.isPresent()) {
			DataTransformer<FbPostComment> fbDataTransformer = new FbDataTransformer();
			List<String> transformedData = fbDataTransformer.transform(commentsOptional.get());

			DataWriter redisFbDataWriter = new RedisFbDataWriter();
			redisFbDataWriter.write(transformedData);
		} else {
			logger.error("No data consumed");
		}

		long time = System.currentTimeMillis() - start;
		System.out.println(time);
	}
}

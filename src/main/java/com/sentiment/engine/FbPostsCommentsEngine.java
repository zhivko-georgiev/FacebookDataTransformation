package com.sentiment.engine;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.sentiment.data.consumer.DataConsumer;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.writer.DataWriter;
import com.sentiment.model.FbPostComment;

public class FbPostsCommentsEngine implements Engine<FbPostComment> {
	private final DataConsumer<FbPostComment> consumer;
	private final DataTransformer<FbPostComment> transformer;
	private final DataWriter writer;
	
	public FbPostsCommentsEngine(DataConsumer<FbPostComment> consumer, DataTransformer<FbPostComment> transformer,
			DataWriter writer) {
		super();
		this.consumer = consumer;
		this.transformer = transformer;
		this.writer = writer;
	}


	@Override
	public void run() {
		long start = System.currentTimeMillis();
		Logger logger = Logger.getLogger(FbPostsCommentsEngine.class);
		Optional<List<FbPostComment>> commentsOptional = consumer.consume();
		
		if (commentsOptional.isPresent()) {
			List<String> transformedData = transformer.transform(commentsOptional.get());
			writer.write(transformedData);
		} else {
			logger.error("No data consumed");
		}

		long time = System.currentTimeMillis() - start;
		System.out.println(time);
	}

}

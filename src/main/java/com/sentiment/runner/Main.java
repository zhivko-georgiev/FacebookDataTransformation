package com.sentiment.runner;

import java.util.Properties;

import com.sentiment.common.Constants;
import com.sentiment.data.consumer.DataConsumer;
import com.sentiment.data.consumer.FbDataConsumer;
import com.sentiment.data.transformer.DataTransformer;
import com.sentiment.data.transformer.FbDataTransformer;
import com.sentiment.data.writer.DataWriter;
import com.sentiment.data.writer.RedisFbDataWriter;
import com.sentiment.engine.Engine;
import com.sentiment.engine.FbPostsCommentsEngine;
import com.sentiment.model.FbPostComment;
import com.sentiment.util.PropertiesUtil;

public class Main {

	public static void main(String[] args) {
		final Properties fbProps = PropertiesUtil.loadPropertiesFile(Constants.FB_PROPS_FILENAME);   
		final Properties appProps = PropertiesUtil.loadPropertiesFile(Constants.APP_PROPS_FILENAME); 
		
		final DataConsumer<FbPostComment> consumer = new FbDataConsumer(appProps, fbProps);
		final DataTransformer<FbPostComment> transformer = new FbDataTransformer(appProps);
		final DataWriter writer = new RedisFbDataWriter();

		Engine engine = new FbPostsCommentsEngine(consumer, transformer, writer);
		engine.run();
	}
}

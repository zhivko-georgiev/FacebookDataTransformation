package com.sentiment.data.consumer;

import java.util.List;

public interface DataConsumer<T> {
	List<T> consume();
}

package com.sentiment.data.consumer;

import java.util.List;
import java.util.Optional;

public interface DataConsumer<T> {
	Optional<List<T>> consume();
}

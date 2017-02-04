package com.sentiment.data.transformer;

import java.util.List;

public interface DataTransformer<T> {
	List<String> transform(List<T> data);
}

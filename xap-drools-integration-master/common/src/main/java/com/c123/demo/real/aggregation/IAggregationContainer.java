package com.c123.demo.real.aggregation;

import java.util.AbstractMap.SimpleEntry;

public interface IAggregationContainer {
	public void aggregate(SimpleEntry<String, Double> entry);
}

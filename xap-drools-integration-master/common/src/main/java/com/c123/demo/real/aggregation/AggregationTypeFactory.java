package com.c123.demo.real.aggregation;

import org.apache.log4j.Logger;

import com.c123.demo.real.BaseFact;

public class AggregationTypeFactory {
	private static Logger log = Logger.getLogger(AggregationTypeFactory.class);

	public static BaseFactAggregation create(BaseFact fact) {
		String factType = fact.getClass().getSimpleName();
		// log.info("Fact type:" + factType);
		if ("WagerFact".equals(factType)) {
			return new FinancialFactAggregation();
		}
		return null;
	}
	
}

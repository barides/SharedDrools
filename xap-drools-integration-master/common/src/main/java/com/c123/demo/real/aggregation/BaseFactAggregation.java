package com.c123.demo.real.aggregation;

import java.io.Serializable;

import com.c123.demo.real.BaseCustomerFact;

public abstract class BaseFactAggregation implements Serializable {

	public abstract void aggregate(BaseCustomerFact fact);


}

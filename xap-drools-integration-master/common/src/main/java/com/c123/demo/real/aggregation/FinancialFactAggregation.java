package com.c123.demo.real.aggregation;

import java.io.Serializable;

import com.c123.demo.real.BaseAccountOperationFact;
import com.c123.demo.real.BaseCustomerFact;

public class FinancialFactAggregation extends BaseFactAggregation implements Serializable {
	private double sum;
	private int count;
	private double max;
	private double min;
	
	public Double getSum() {
		return sum;
	}
	
	public void setSum(Double sum) {
		this.sum = sum;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public Double getMax() {
		return max;
	}
	
	public void setMax(Double max) {
		this.max = max;
	}
	
	public Double getMin() {
		return min;
	}
	
	public void setMin(Double min) {
		this.min = min;
	}

	@Override
	public void aggregate(BaseCustomerFact fact)
	{
		BaseAccountOperationFact currFact = (BaseAccountOperationFact)fact;
		Double amount = currFact.getActualAmount().doubleValue();
		
		if (amount > this.max)
		{
			this.max = amount;
		}
		
		if (amount < this.min)
		{
			this.min = amount;
		}
		
		this.sum += amount;
		this.count++;
	}

	@Override
	public String toString() {
		return "FinancialFactAggregation [sum=" + sum + ", count=" + count
				+ ", max=" + max + ", min=" + min + "]";
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		this.count=0;
		this.sum=0;
		this.min=0;
		this.max=0;
	}


}

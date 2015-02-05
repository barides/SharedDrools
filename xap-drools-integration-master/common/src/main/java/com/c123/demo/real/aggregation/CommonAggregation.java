package com.c123.demo.real.aggregation;

import java.io.Serializable;

public class CommonAggregation implements Serializable{
	private static final long serialVersionUID = 2131890826761980090L;
	private double sum;
	private int count;
	private double max;
	private double min;
	private int bonusCounter;
	


	public CommonAggregation(Double value) {
		this.aggregate(value);
	}

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
		this.sum = count;
	}
	
	public void aggregate(Double value)
	{		
		if (value > this.max)
			this.max = value;
		
		if (value < this.min || this.min == 0)
			this.min = value;
		
		this.sum += value;
		this.count ++;
	}
	
	public int getBonusCounter() {
		return bonusCounter;
	}

	public void setBonusCounter(int bonusCounter) {
		this.bonusCounter = bonusCounter;
	}

	@Override
	public String toString() {
		return "CommonAggregation [sum=" + sum + ", count=" + count + ", max="
				+ max + ", min=" + min + ", bonusCounter=" + bonusCounter + "]";
	}
	
}

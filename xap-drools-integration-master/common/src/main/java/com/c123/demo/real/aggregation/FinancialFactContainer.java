package com.c123.demo.real.aggregation;

import java.util.Date;

import org.openspaces.core.GigaSpace;

import com.c123.demo.real.FactContainer;

public class FinancialFactContainer extends FactContainer {

	private Double hourlySum;
	private Integer hourlyCount;
	private Double hourlyMax;
	private Double hourlyMin;
	private Double dailySum;
	private Integer dailyCount;
	private Double dailyMax;
	private Double dailyMin;
	


	public FinancialFactContainer(GigaSpace inputSpaceproxy) {
		super(inputSpaceproxy);
		// TODO Auto-generated constructor stub
	}
	
	public Double getHourlySum() {
		hourlySum = this.getHourlySum(getFact().getDate());
		return hourlySum;
	}
	public Integer getHourlyCount() {
		hourlyCount = this.getHourlyCount(getFact().getDate());
		return hourlyCount;
	}
	public Double getHourlyMax() {
		hourlyMax =  this.getHourlyMax(getFact().getDate());
		return hourlyMax;
	}
	public Double getHourlyMin() {
		hourlyMin = this.getHourlyMin(getFact().getDate());
		return hourlyMin;
	}
	public void setHourlySum(Double hourlySum) {
		this.hourlySum = hourlySum;
	}

	public void setHourlyCount(Integer hourlyCount) {
		this.hourlyCount = hourlyCount;
	}

	public void setHourlyMax(Double hourlyMax) {
		this.hourlyMax = hourlyMax;
	}

	public void setHourlyMin(Double hourlyMin) {
		this.hourlyMin = hourlyMin;
	}
	
	private Double getHourlySum(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getHourAggregation(date);
		if (hourlyAggregation == null){
			return 0d;
		}
		return hourlyAggregation.getSum();
	}
	


	private Integer getHourlyCount(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getHourAggregation(date);
		if (hourlyAggregation == null){
			return 0;
		}
		return hourlyAggregation.getCount();
	}
	
	private Double getHourlyMax(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getHourAggregation(date);
		if (hourlyAggregation == null){
			return 0d;
		}
		return hourlyAggregation.getMax();
	}
	
	private Double getHourlyMin(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getHourAggregation(date);
		if (hourlyAggregation == null){
			return 0d;
		}
		return hourlyAggregation.getMin();
	}

	public Double getDailySum() {
		dailySum = this.getDailySum(getFact().getDate());
		return dailySum;
	}

	public void setDailySum(Double dailySum) {
		this.dailySum = dailySum;
	}

	public Integer getDailyCount() {
		dailyCount = this.getDailyCount(getFact().getDate());
		return dailyCount;
	}

	public void setDailyCount(Integer dailyCount) {
		this.dailyCount = dailyCount;
	}

	public Double getDailyMax() {
		dailyMax = this.getDailyMax(getFact().getDate());
		return dailyMax;
	}

	public void setDailyMax(Double dailyMax) {
		this.dailyMax = dailyMax;
	}

	public Double getDailyMin() {
		dailyMin = this.getDailyMin(getFact().getDate());
		return dailyMin;
	}

	public void setDailyMin(Double dailyMin) {
		this.dailyMin = dailyMin;
	}	
	
	private Double getDailySum(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getDateAggregation(date);
		if (hourlyAggregation == null){
			return 0d;
		}
		return hourlyAggregation.getSum();
	}
	
	private Integer getDailyCount(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getDateAggregation(date);
		if (hourlyAggregation == null){
			return 0;
		}
		return hourlyAggregation.getCount();
	}
	
	private Double getDailyMax(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getDateAggregation(date);
		if (hourlyAggregation == null){
			return 0d;
		}
		return hourlyAggregation.getMax();
	}
	
	private Double getDailyMin(Date date) {
		FinancialFactAggregation hourlyAggregation = (FinancialFactAggregation) getAggregation().getDateAggregation(date);
		if (hourlyAggregation == null){
			return 0d;
		}
		return hourlyAggregation.getMin();
	}
	

}

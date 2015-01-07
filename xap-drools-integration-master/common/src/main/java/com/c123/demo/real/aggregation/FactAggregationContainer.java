package com.c123.demo.real.aggregation;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.SimpleFormatter;

import com.c123.demo.real.BaseAccountOperationFact;
import com.c123.demo.real.BaseCustomerFact;
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.document.DocumentProperties;

@SpaceClass
public class FactAggregationContainer<T extends BaseFactAggregation> {
	
	private Integer networkId;
    private Integer customerId;
    
    private HashMap<String, T> hourlyAggregationData;
    private HashMap<String, T> dailyAggregationData;
    
    private String latestHour;
    private String latestDate;
    

	public FactAggregationContainer () { 
    	super();
    	
    }
	
    @SpaceRouting
    public Integer getNetworkId() {
		return networkId;
	}
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}
	
	@SpaceId
	public String getId(){
		return this.networkId + "|" + this.customerId;
	}
	
	public void setId(String id){
	}
	
	
	public Integer getCustomerId() {
		return customerId;
	}
	
	
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	
	public HashMap<String, T> getHourlyAggregationData() {
		return hourlyAggregationData;
	}

	public void setHourlyAggregationData(HashMap<String, T> hourlyAggregationData) {
		this.hourlyAggregationData = hourlyAggregationData;
	}

	public HashMap<String, T> getDailyAggregationData() {
		return dailyAggregationData;
	}

	public void setDailyAggregationData(HashMap<String, T> dailyAggregationData) {
		this.dailyAggregationData = dailyAggregationData;
	}	
	
	public void addFactToAggregation(BaseCustomerFact fact) throws InstantiationException, IllegalAccessException
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fact.getDate());
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	
		if (this.hourlyAggregationData == null)
		{
			this.hourlyAggregationData = new HashMap<String, T> ();
		}
		if (! this.hourlyAggregationData.containsKey( hour) ) {
			hourlyAggregationData.put( hour , (T) new FinancialFactAggregation() );
			latestHour = hour;
		}
		hourlyAggregationData.get(hour).aggregate(fact);
		
		if (this.dailyAggregationData == null)
		{
			this.dailyAggregationData = new HashMap<String, T> ();
			
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String date = formatter.format(calendar.getTime());
		if (! this.dailyAggregationData.containsKey( date) ) {
			dailyAggregationData.put( date ,(T) new FinancialFactAggregation() );
			latestDate = date;
		}
		dailyAggregationData.get(date).aggregate(fact);
	}

	public String getLatestHour() {
		return latestHour;
	}

	public String getLatestDate() {
		return latestDate;
	}

    public void setLatestHour(String latestHour) {
		this.latestHour = latestHour;
	}

	public void setLatestDate(String latestDate) {
		this.latestDate = latestDate;
	}
	
	
	public T getLatestHourAggregation(){
		if (this.hourlyAggregationData == null || this.latestHour == null) {
			return null;
		}
		return this.hourlyAggregationData.get(latestHour);
	}
	
	public T getLatestDateAggregation(){
		if (this.dailyAggregationData == null || this.latestDate == null) {
			return null;
		}
		return this.dailyAggregationData.get(latestDate);
	}
	
	public T getHourAggregation(String hour){
		if (this.hourlyAggregationData == null) {
			return null;
		}
		return this.hourlyAggregationData.get(hour);
	}
	
	public T getDateAggregation(String date){
		if (this.dailyAggregationData == null) {
			return null;
		}
		return this.dailyAggregationData.get(date);
	}
	
}

package com.c123.demo.real.aggregation;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.openjpa.lib.log.Log;

import com.c123.demo.real.BaseCustomerFact;
import com.c123.demo.real.Identity;
import com.c123.demo.utils.FactProcessUtilities;
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@SpaceClass(persist=true)
public class SimpleFactAggregationContainer implements Serializable,Identity{
	
	private static final long serialVersionUID = -7412411044213037910L;
	
	private static Logger log = Logger.getLogger(SimpleFactAggregationContainer.class);
	
	private Integer networkId;
    private Integer customerId;
    
    private HashMap<String, BaseFactAggregation> hourlyAggregationData;
    private HashMap<String, BaseFactAggregation> dailyAggregationData;
    
    private String latestRegisteredHour;
    private String latestRegisteredDate;
    
    private HashSet<String> factIds;
    

	public SimpleFactAggregationContainer () { 
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

	
	public HashMap<String, BaseFactAggregation> getHourlyAggregationData() {
		return hourlyAggregationData;
	}

	public void setHourlyAggregationData(HashMap<String, BaseFactAggregation> hourlyAggregationData) {
		this.hourlyAggregationData = hourlyAggregationData;
	}

	public HashMap<String, BaseFactAggregation> getDailyAggregationData() {
		return dailyAggregationData;
	}

	public void setDailyAggregationData(HashMap<String, BaseFactAggregation> dailyAggregationData) {
		this.dailyAggregationData = dailyAggregationData;
	}	
	
	public void addFactToAggregation(BaseCustomerFact fact) throws InstantiationException, IllegalAccessException
	{
		boolean insert=true;
		if (factIds == null) {
			factIds = new HashSet<String>();
		} 
		StringBuffer sb = new StringBuffer();
		if (factIds.contains(fact.getId()) ) {
			log.info("fact id:" + fact.getId()  + " already exists!!!!  *********************** ");
/*			sb.append("fact id:" + fact.getId()  + " already exists!!!!  *********************** ");
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				sb.append(ste).append("********************");
			}*/
			insert=false;
		} else {
			factIds.add(fact.getId());
/*			sb.append(" New fact id:" + fact.getId()  + "  *********************** ");
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				sb.append(ste).append("********************");
			}*/
		}
		
		
		if (insert) {
			String hour = FactProcessUtilities.getDateAndHour(fact.getDate());
		
			if (this.hourlyAggregationData == null)
			{
				this.hourlyAggregationData = new HashMap<String, BaseFactAggregation> ();
			}
			if (! this.hourlyAggregationData.containsKey( hour) ) {
				hourlyAggregationData.put( hour , AggregationTypeFactory.create(fact) );
				latestRegisteredHour = hour;
			}
			hourlyAggregationData.get(hour).aggregate(fact);
			
			if (this.dailyAggregationData == null)
			{
				this.dailyAggregationData = new HashMap<String, BaseFactAggregation> ();
				
			}
			
			String date = FactProcessUtilities.getFormattedDate(fact.getDate());
			if (! this.dailyAggregationData.containsKey( date) ) {
				dailyAggregationData.put( date , AggregationTypeFactory.create(fact) );
				latestRegisteredDate = date;
			}
			dailyAggregationData.get(date).aggregate(fact);
		}
	}	
	
	public HashSet<String> getFactIds() {
		return factIds;
	}

	public void setFactIds(HashSet<String> factIds) {
		this.factIds = factIds;
	}

	public String getLatestRegisteredHour() {
		return latestRegisteredHour;
	}

	public void setLatestRegisteredHour(String latestRegisteredHour) {
		this.latestRegisteredHour = latestRegisteredHour;
	}

	public String getLatestRegisteredDate() {
		return latestRegisteredDate;
	}

	public void setLatestRegisteredDate(String latestRegisteredDate) {
		this.latestRegisteredDate = latestRegisteredDate;
	}

	public BaseFactAggregation getHourAggregation(String hour){
		if (this.hourlyAggregationData == null) {
			return null;
		}
		return this.hourlyAggregationData.get(hour);
	}
	
	public BaseFactAggregation getDateAggregation(String date){
		if (this.dailyAggregationData == null) {
			return null;
		}
		return this.dailyAggregationData.get(date);
	}
	
	public BaseFactAggregation getHourAggregation(Date date){
		String hour = FactProcessUtilities.getDateAndHour(date);
		return this.getHourAggregation(hour);
	}
	
	public BaseFactAggregation getDateAggregation(Date date){
		String formattedDate = FactProcessUtilities.getFormattedDate(date);
		return this.getDateAggregation(formattedDate);
	}

	@Override
	public String toString() {
		return "SimpleFactAggregationContainer [networkId=" + networkId
				+ ", customerId=" + customerId + ", hourlyAggregationData="
				+ hourlyAggregationData + ", dailyAggregationData="
				+ dailyAggregationData + ", latestRegisteredHour="
				+ latestRegisteredHour + ", latestRegisteredDate="
				+ latestRegisteredDate + ", factIds=" + factIds + "]";
	}
	
	public void reset(){
		for (BaseFactAggregation agg : this.hourlyAggregationData.values()){
			agg.reset();
		}
		for (BaseFactAggregation agg : this.dailyAggregationData.values()){
			agg.reset();
		}
		
	}
}

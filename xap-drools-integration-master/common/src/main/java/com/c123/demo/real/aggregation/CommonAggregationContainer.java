package com.c123.demo.real.aggregation;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;

import org.apache.log4j.Logger;
import org.apache.openjpa.lib.log.Log;
import com.c123.demo.model.facts.Fact;
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.document.DocumentProperties;

@SpaceClass
public class CommonAggregationContainer implements Serializable, Fact, IAggregationContainer{
	
	private static Logger log = Logger.getLogger(CommonAggregationContainer.class);
	private Integer networkId;
	private Integer customerId;
	private DocumentProperties data;
	private Integer state;

	@SpaceRouting
	public Integer getNetworkId() {
		return networkId;
	}

	@SpaceId
	public String getId() {
		return this.networkId + "_" + this.customerId;
	}
	
	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	public void setId(String id) {
	}

	@SpaceDynamicProperties
	public DocumentProperties getData() {
		return this.data;
	}
	
	public void setData(DocumentProperties data) {
		this.data = data;		
	}

	public CommonAggregationContainer() {     	
    }
	
	public CommonAggregationContainer(Integer networkId, Integer customerId) {
		this.networkId = networkId;
		this.customerId = customerId;
		this.state=0;
		this.data = new DocumentProperties();
	}

	public void aggregate(SimpleEntry<String, Double> entry) {
		if (this.data == null) {
			this.data = new DocumentProperties();
		}
		if (!this.data.containsKey(entry.getKey())) {
			this.data.put(entry.getKey(), new CommonAggregation(entry.getValue()));
		}
		((CommonAggregation)this.data.get(entry.getKey())).aggregate(entry.getValue());
	}
	
	public void put (SimpleEntry<String, Object> entry) {
		if (this.data == null) {
			this.data = new DocumentProperties();
		}
		this.data.put(entry.getKey(), entry.getValue());
	}
	
	public Object get (String key) {
		if (this.data == null) {
			this.data = new DocumentProperties();
			this.data.put(key, null);
		}
		return this.data.get(key);
		
	}
	
	public void plusplus (SimpleEntry<String, Integer> entry) {
		if (this.data == null) {
			this.data = new DocumentProperties();
		}
		if (!this.data.containsKey(entry.getKey())) {
			this.data.put(entry.getKey(), entry.getValue());
		} else {
			int current = (Integer) this.data.get(entry.getKey());
			this.data.put(entry.getKey(), current + entry.getValue());
		}
		
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : data.keySet()) {
			sb.append(key).append(",").append(data.get(key).toString());
		}
		return "CommonAggregationContainer [networkId=" + networkId
				+ ", customerId=" + customerId  + ", state="
				+ state 
				+ ", data=" 
				+ sb.toString()
				+ "]";
	}

}

package com.c123.demo.real.statistics;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@SpaceClass
public class EventProcessingStats {


	private String factId;
	private Integer networkId ;
	private String procesingPCId;
	private String factType;
	private long processTime;

	public EventProcessingStats() {
	}
	
	public EventProcessingStats(String factId, Integer networkId,
			String procesingPCId, String factType, long processTime) {
		super();
		this.factId = factId;
		this.networkId = networkId;
		this.procesingPCId = procesingPCId;
		this.factType = factType;
		this.processTime = processTime;
	}



	@SpaceId(autoGenerate = false)
	public String getFactId() {
		return factId;
	}

	public void setFactId(String factId) {
		this.factId = factId;
	}
	
	@SpaceRouting
	public Integer getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}
	
	public String getProcesingPCId() {
		return procesingPCId;
	}

	public void setProcesingPCId(String procesingPCId) {
		this.procesingPCId = procesingPCId;
	}

	public String getFactType() {
		return factType;
	}

	public void setFactType(String factType) {
		this.factType = factType;
	}

	public long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}

	@Override
	public String toString() {
		return "EventProcessingStats [procesingPCId=" + procesingPCId
				+ ", factType=" + factType + ", processTime=" + processTime
				+ "]";
	}



}

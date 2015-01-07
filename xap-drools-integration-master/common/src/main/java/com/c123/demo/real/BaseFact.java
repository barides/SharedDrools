package com.c123.demo.real;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.c123.demo.model.facts.Fact;

public class BaseFact extends BaseJavaSpaceEntity implements Fact, Serializable{

	private UUID correlationId;
    private Integer networkId;
    private Integer skinId;
    private Integer state;
    private UUID requestReference;
	private Date date;
	   
	public BaseFact() {
		super();
		state = 0;
	} 
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public UUID getCorrelationId() {
		return correlationId;
	}


	public void setCorrelationId(UUID correlationId) {
		this.correlationId = correlationId;
	}


	public Integer getNetworkId() {
		return networkId;
	}


	public void setNetworkId(Integer networkId) {
		this.networkId = networkId;
	}


	public Integer getSkinId() {
		return skinId;
	}


	public void setSkinId(Integer skinId) {
		this.skinId = skinId;
	}


	public Integer getState() {
		return state;
	}


	public void setState(Integer state) {
		this.state = state;
	}


	public UUID getRequestReference() {
		return requestReference;
	}


	public void setRequestReference(UUID requestReference) {
		this.requestReference = requestReference;
	}

	@Override
	public String toString() {
		return "BaseFact [correlationId=" + correlationId + ", networkId="
				+ networkId + ", skinId=" + skinId + ", state=" + state
				+ ", requestReference=" + requestReference + "]";
	}

}

package com.c123.demo.model.facts;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceProperty;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.annotation.pojo.SpaceStorageType;
import com.gigaspaces.metadata.StorageType;


@SpaceClass
public class WagerFact implements Serializable,Fact {
	private static final long serialVersionUID = 2654430594192018801L;

	private String id; 
    private Integer customerId;
    private Date date;
    private Double actualAmount;
	private String correlationID;
    private Integer networkId;
    private Integer skinId;
    private Integer state;
    private Integer offering;
    private Integer fundsType;
    private Integer operationSourceApplication;
    private String requestCurrencyCode;
    private Integer routingId;
	
    @SpaceRouting
	public Integer getRoutingId() {
		return routingId;
	}

	public void setRoutingId(Integer routingId) {
		this.routingId = routingId;
	}

	public WagerFact() {
		state=0;
	}
    
	@SpaceId(autoGenerate = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
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
	
	
	public Integer getOffering() {
		return offering;
	}

	public void setOffering(Integer offering) {
		this.offering = offering;
	}

	public Integer getFundsType() {
		return fundsType;
	}

	public void setFundsType(Integer fundsType) {
		this.fundsType = fundsType;
	}

	public Integer getOperationSourceApplication() {
		return operationSourceApplication;
	}

	public void setOperationSourceApplication(Integer operationSourceApplication) {
		this.operationSourceApplication = operationSourceApplication;
	}

	public String getRequestCurrencyCode() {
		return requestCurrencyCode;
	}

	public void setRequestCurrencyCode(String requestCurrencyCode) {
		this.requestCurrencyCode = requestCurrencyCode;
	}

	@Override
	public String toString() {
		return "WagerFact [id=" + id + ", customerId=" + customerId + ", date="
				+ date + ", actualAmount=" + actualAmount + ", correlationID="
				+ correlationID + ", networkId=" + networkId + ", skinId="
				+ skinId + ", state=" + state + ", offering=" + offering
				+ ", fundsType=" + fundsType + ", operationSourceApplication="
				+ operationSourceApplication + ", requestCurrencyCode="
				+ requestCurrencyCode + ", routingId=" + routingId + "]";
	}

}

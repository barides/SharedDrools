package com.c123.demo.real;

import java.io.Serializable;
import java.util.UUID;

public class BaseWithdrawFact extends BaseAccountOperationFact implements Serializable{

	private Integer offering;
    private Integer fundsType;
    private Integer operationSourceApplication;
    private String requestCurrencyCode;
    private Integer updateBalanceReason;
    
    
	public BaseWithdrawFact() {
		super();
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

	public Integer getUpdateBalanceReason() {
		return updateBalanceReason;
	}

	public void setUpdateBalanceReason(Integer updateBalanceReason) {
		this.updateBalanceReason = updateBalanceReason;
	}

	@Override
	public String toString() {
		return "BaseWithdrawFact [offering=" + offering + ", fundsType="
				+ fundsType + ", operationSourceApplication="
				+ operationSourceApplication + ", requestCurrencyCode="
				+ requestCurrencyCode + ", updateBalanceReason="
				+ updateBalanceReason + "]";
	}


   
}

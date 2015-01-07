package com.c123.demo.real;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceClass;


@SpaceClass(persist=true)
public class WagerFact extends BaseWithdrawFact implements Serializable{

	public WagerFact() {
		super();
	}

	@Override
	public String toString() {
		return "WagerFact [getOffering()=" + getOffering()
				+ ", getFundsType()=" + getFundsType()
				+ ", getOperationSourceApplication()="
				+ getOperationSourceApplication()
				+ ", getRequestCurrencyCode()=" + getRequestCurrencyCode()
				+ ", getRequestReference()=" + getRequestReference()
				+ ", toString()=" + super.toString() + ", getDate()="
				+ getDate() + ", getActualAmount()=" + getActualAmount()
				+ ", getCustomerId()=" + getCustomerId()
				+ ", getCorrelationId()=" + getCorrelationId()
				+ ", getNetworkId()=" + getNetworkId() + ", getSkinId()="
				+ getSkinId() + ", getState()=" + getState() + ", getId()="
				+ getId() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}


	

}

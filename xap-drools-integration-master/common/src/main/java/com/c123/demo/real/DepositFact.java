package com.c123.demo.real;

import java.io.Serializable;

public class DepositFact extends BaseWithdrawFact implements Serializable {

	public DepositFact() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "DepositFact [getOffering()=" + getOffering()
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

package com.c123.demo.real;

import java.io.Serializable;

public class BaseCustomerFact extends BaseFact implements Serializable {

	private Integer customerId;

	public BaseCustomerFact() {
		super();
	}  
	
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "BaseCustomerFact [customerId=" + customerId + "]";
	}


	
	
}

package com.c123.demo.model.facts;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;


public class AdjustmentAction implements Serializable {

	private Double amount;
	private Integer updateBalanceReason;
	private Integer actionState;
	
	public Integer getActionState() {
		return actionState;
	}

	public void setActionState(Integer actionState) {
		this.actionState = actionState;
	}

	public AdjustmentAction(){
		amount=0d;
		updateBalanceReason=0;
		actionState = 0;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getUpdateBalanceReason() {
		return updateBalanceReason;
	}
	public void setUpdateBalanceReason(Integer updateBalanceReason) {
		this.updateBalanceReason = updateBalanceReason;
	}

	@Override
	public String toString() {
		return "AdjustmentAction [amount=" + amount + ", updateBalanceReason="
				+ updateBalanceReason + ", actionState=" + actionState + "]";
	}
	
}

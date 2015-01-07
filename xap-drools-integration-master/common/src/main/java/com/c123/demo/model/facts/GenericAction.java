package com.c123.demo.model.facts;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@SpaceClass
public class GenericAction implements Serializable,Fact {

	private static final long serialVersionUID = 2932328034557122102L;
	
	private String id;
	private Double amount;
	private Integer updateBalanceReason;
	private WagerFact fact;
	private Integer state;
	private Integer actionType;
	
	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public GenericAction(){
		amount=0d;
		updateBalanceReason=0;
		state=0;
	}
	
	public WagerFact getFact() {
		return fact;
	}

	public void setFact(WagerFact fact) {
		this.fact = fact;
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

	 @SpaceId(autoGenerate=true)
	 @SpaceRouting
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "GenericAction [id=" + id + ", amount=" + amount
				+ ", updateBalanceReason=" + updateBalanceReason + ", fact="
				+ fact + ", state=" + state + ", actionType=" + actionType
				+ "]";
	}
	
	
}

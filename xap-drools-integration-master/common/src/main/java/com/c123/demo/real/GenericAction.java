package com.c123.demo.real;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.c123.demo.model.facts.Fact;
import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceProperty;
import com.gigaspaces.annotation.pojo.SpaceRouting;


@SpaceClass(persist=true)
public class GenericAction implements Serializable,Fact,Identity {
	

	private static final long serialVersionUID = -4062645554313821766L;
	
	
	private String id;
	private BigDecimal amount;
	private Integer updateBalanceReason;
	private BaseFact fact;
	private Integer state; 
	private Integer actionType;
	private Long processTime;
	


	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public GenericAction(){

	}
	
	public BaseFact getFact() {
		return fact;
	}

	public void setFact(BaseFact fact) {
		this.fact = fact;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
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

	@Override
	public String toString() {
		return "GenericAction [id=" + id + ", amount=" + bigDecimalFormat(amount)
				+ ", updateBalanceReason=" + updateBalanceReason + ", fact="
				+ fact + ", state=" + state + ", actionType=" + actionType
				+ "]";
	}
	
	private String bigDecimalFormat(BigDecimal bd){
		if (bd != null) {
			bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(0);
			df.setGroupingUsed(false);
			String result = df.format(bd);	
			return result;
		}
		return "null";
	}

	public Long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Long processTime) {
		this.processTime = processTime;
	}

}

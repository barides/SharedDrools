package com.c123.demo.model.facts;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

@SpaceClass
public class FlatGenericAction implements Serializable,Fact {

	private static final long serialVersionUID = 2932328034557122102L;
	
	private String id;
	private BigDecimal amount;
	private Integer updateBalanceReason;
	private FlatWagerFact fact;
	private Integer state;
	private Integer actionType;
	
	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public FlatGenericAction(){
		amount= new BigDecimal(0);
		updateBalanceReason=0;
		state=0;
	}
	
	public FlatWagerFact getFact() {
		return fact;
	}

	public void setFact(FlatWagerFact fact) {
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "GenericAction [id=" + id + ", amount=" + bigDecimalFormat(amount)
				+ ", updateBalanceReason=" + updateBalanceReason + ", fact="
				+ fact + ", state=" + state + ", actionType=" + actionType
				+ "]";
	}
	
	private String bigDecimalFormat(BigDecimal bd){
		bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(0);
		df.setGroupingUsed(false);
		String result = df.format(bd);	
		return result;
	}
	
}

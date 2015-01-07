package com.c123.demo.real;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BaseAccountOperationFact extends BaseCustomerFact implements Serializable {


	   private BigDecimal actualAmount;
	    
		public BaseAccountOperationFact() {
			super();
		}    
	    
	
		public BigDecimal getActualAmount() {
			return actualAmount;
		}
		public void setActualAmount(BigDecimal actualAmount) {
			this.actualAmount = actualAmount;
		}


		@Override
		public String toString() {
			return "BaseAccountOperationFact [actualAmount=" + actualAmount
					+ "]";
		}


		
}

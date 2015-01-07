package com.c123.demo.real;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;

import com.c123.demo.model.facts.Fact;
import com.c123.demo.real.aggregation.SimpleFactAggregationContainer;
import com.j_spaces.core.client.SQLQuery;

public class FactContainer implements Fact{
	
	private static Logger log = Logger.getLogger(FactContainer.class);
	
	private BaseAccountOperationFact fact;
	private Customer customer;
	// private FactAggregationContainer<BaseFactAggregation> aggregation;
	private SimpleFactAggregationContainer aggregation;
	private GigaSpace spaceproxy;
	
	public FactContainer (GigaSpace inputSpaceproxy) {
		spaceproxy = inputSpaceproxy;
	}
	
	public BaseAccountOperationFact getFact() {
		return fact;
	}
	
	public void setFact(BaseAccountOperationFact fact) {
		this.fact = fact;
	}
	
	public Customer getCustomer() {
		if (customer == null){
			System.out.println("Missing Customer read from the space");
			// System.out.println(spaceproxy.getSpace().getFinderURL());
			SQLQuery<Customer> query = new SQLQuery<Customer>(Customer.class,
					"id=" + fact.getCustomerId());
			customer = spaceproxy.read(query);
			if (customer == null) {
				System.out.println("We have a problem loading customer id " + fact.getCustomerId());
			} 
		}
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Override
	public Integer getState() {
		return 0;
	}

	@Override
	public void setState(Integer state) {
		// does nothing
		
	}

	public SimpleFactAggregationContainer getAggregation() {
		if (aggregation == null) {
			loadAggregation();
		}
		return aggregation;
	}
	
	private void loadAggregation(){
		SQLQuery<SimpleFactAggregationContainer> query = new SQLQuery<SimpleFactAggregationContainer>(SimpleFactAggregationContainer.class,"customerId=" + fact.getCustomerId() + " and networkId=" + fact.getNetworkId());
		aggregation = spaceproxy.read(query);
	}
	
	public void setAggregation(SimpleFactAggregationContainer agg) {
		this.aggregation=agg;
	}
	
}

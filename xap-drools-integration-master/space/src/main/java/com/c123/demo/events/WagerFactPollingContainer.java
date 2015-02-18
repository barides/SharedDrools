package com.c123.demo.events;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;

import com.c123.demo.model.facts.Fact;
import com.c123.demo.real.BaseWithdrawFact;
import com.c123.demo.real.GenericAction;
import com.c123.demo.real.WagerFact;
import com.c123.demo.real.WagerFactContainer;
import com.c123.demo.real.aggregation.CommonAggregationContainer;
import com.c123.demo.real.aggregation.IAggregationContainer;
import com.c123.demo.real.aggregation.SimpleFactAggregationContainer;
import com.c123.demo.real.statistics.EventProcessingStats;
import com.gigaspaces.client.ReadModifiers;
import com.gigaspaces.client.WriteModifiers;
import com.gigaspaces.droolsintegration.dao.KnowledgeBaseWrapperDao;
import com.gigaspaces.droolsintegration.model.drools.KnowledgeBaseWrapper;
import com.gigaspaces.droolsintegration.util.IterableMapWrapper;


@EventDriven
@TransactionalEvent(timeout=80)
@Polling(gigaSpace="gigaSpace" , concurrentConsumers = 2, maxConcurrentConsumers = 2 )
public class WagerFactPollingContainer extends BaseFactPollingContainer{

    @Resource
	private GigaSpace gigaSpace;  
    
    @Resource
    private KnowledgeBaseWrapperDao knowledgeBaseWrapperDao;
    
	private static Logger log = Logger.getLogger(WagerFactPollingContainer.class);
	
	// public static final String RULE_SET_VOUCHER = "WagerSet";
	// public static final String PACKAGE_NAME="InstantGameWagerComprised";
	// public static final String COMMON_PACKAGE_NAME="InstantGameWagerCommon";
    
	@PostConstruct
	public void init() {
		log.info("Init start .....");
		if (knowledgeBaseWrapperDao == null) {
			log.warn("*******************  We have a problem !!!!! *****************");
		}
		this.PACKAGE_NAME = "InstantGameWagerComprisedNewGen";
		log.info("Init end .....");
	}
	
	@EventTemplate
	public WagerFact unHandledFact() {
		WagerFact template = new WagerFact();
		template.setState(0);
        return template;
    }
	
   @SpaceDataEvent
    public WagerFact processEvents(WagerFact wagerFact) throws Exception {
	   // log.info("processEvents wagerFact " + wagerFact.getId());
	   long start = System.currentTimeMillis();
	   	   
	   // Rule Processing
       IterableMapWrapper facts = new IterableMapWrapper();
       // insert fact to drools
       facts.put(WagerFact.class.getSimpleName(), wagerFact);
       // read aggregation from the space
       IAggregationContainer aggregationContainer = this.readAggregation(wagerFact.getNetworkId(), wagerFact.getCustomerId(), this.gigaSpace);
       // if Aggregation has not been created, create one 
       if (aggregationContainer == null) {
    	   aggregationContainer = new CommonAggregationContainer(wagerFact.getNetworkId(), wagerFact.getCustomerId());
    	   gigaSpace.write(aggregationContainer, WriteModifiers.WRITE_ONLY);
       }
       facts.put(CommonAggregationContainer.class.getSimpleName(), aggregationContainer);
       
       // log.info("fact:" + wagerFact.toString());
       
       // Execute Rules
       executeRules(this.generateRuleSetNamePerNetwork(wagerFact.getNetworkId()), facts, this.gigaSpace, this.knowledgeBaseWrapperDao, null);
       // log.info("End ExecuteRules Execution");
       long end = System.currentTimeMillis();
       
       EventProcessingStats stats = new EventProcessingStats (wagerFact.getId(), wagerFact.getNetworkId() , this.toString(), WagerFact.class.getSimpleName(), end-start);
    	gigaSpace.write(stats);
       //Remove fact
    	return null;
   }
   
        
   private void resetAggregations(SimpleFactAggregationContainer agg){
	   agg.reset();
   }
   
}

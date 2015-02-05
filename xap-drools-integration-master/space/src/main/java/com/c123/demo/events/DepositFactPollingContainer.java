package com.c123.demo.events;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;

import com.c123.demo.real.DepositFact;
import com.c123.demo.real.WagerFact;
import com.c123.demo.real.aggregation.CommonAggregationContainer;
import com.c123.demo.real.aggregation.IAggregationContainer;
import com.c123.demo.real.statistics.EventProcessingStats;
import com.gigaspaces.client.WriteModifiers;
import com.gigaspaces.droolsintegration.dao.KnowledgeBaseWrapperDao;
import com.gigaspaces.droolsintegration.util.IterableMapWrapper;


@EventDriven
@TransactionalEvent(timeout=80)
@Polling(gigaSpace="gigaSpace" , concurrentConsumers = 2, maxConcurrentConsumers = 2 )
public class DepositFactPollingContainer extends BaseFactPollingContainer {

    @Resource
	private GigaSpace gigaSpace;  
    
    @Resource
    private KnowledgeBaseWrapperDao knowledgeBaseWrapperDao;
    
	private static Logger log = Logger.getLogger(DepositFactPollingContainer.class);	
	
	public DepositFactPollingContainer() {
		// TODO Auto-generated constructor stub
	}

	
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
	public DepositFact unHandledFact() {
		DepositFact template = new DepositFact();
		template.setState(0);
        return template;
    }
	
	@SpaceDataEvent
	public DepositFact processEvents(DepositFact depositFact) throws Exception {
	   // log.info("processEvents wagerFact " + wagerFact.getId());
	   long start = System.currentTimeMillis();
	   	   
	   // Rule Processing
       IterableMapWrapper facts = new IterableMapWrapper();
       // insert fact to drools
       facts.put(WagerFact.class.getSimpleName(), depositFact);
       // read aggregation from the space
       IAggregationContainer aggregationContainer = this.readAggregation(depositFact.getNetworkId(), depositFact.getCustomerId(), this.gigaSpace);
       // if Aggregation has not been created, create one 
       if (aggregationContainer == null) {
    	   aggregationContainer = new CommonAggregationContainer(depositFact.getNetworkId(), depositFact.getCustomerId());
    	   gigaSpace.write(aggregationContainer, WriteModifiers.WRITE_ONLY);
       }
       facts.put(CommonAggregationContainer.class.getSimpleName(), aggregationContainer);
       
       // log.info("fact:" + wagerFact.toString());
       
       // Execute Rules
       executeRules(this.generateRuleSetNamePerNetwork(depositFact.getNetworkId()), facts, this.gigaSpace, this.knowledgeBaseWrapperDao, null);
       // log.info("End ExecuteRules Execution");
       long end = System.currentTimeMillis();
       EventProcessingStats stats = new EventProcessingStats (depositFact.getId(), depositFact.getNetworkId() , this.toString(), WagerFact.class.getSimpleName(), end-start);
       gigaSpace.write(stats);
    	//Remove fact
    	return null;
	}
}

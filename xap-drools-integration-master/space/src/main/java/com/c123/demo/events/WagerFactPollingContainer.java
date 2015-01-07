package com.c123.demo.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.runtime.StatelessKnowledgeSession;
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
import com.c123.demo.real.aggregation.SimpleFactAggregationContainer;
import com.gigaspaces.client.ReadModifiers;
import com.gigaspaces.client.WriteModifiers;
import com.gigaspaces.droolsintegration.dao.KnowledgeBaseWrapperDao;
import com.gigaspaces.droolsintegration.model.drools.KnowledgeBaseWrapper;
import com.gigaspaces.droolsintegration.util.IterableMapWrapper;


@EventDriven
@TransactionalEvent(timeout=66)
@Polling(gigaSpace="gigaSpace" , concurrentConsumers = 4, maxConcurrentConsumers = 4 )
public class WagerFactPollingContainer extends BaseFactPollingContainer{

    @Resource
	private GigaSpace gigaSpace;  
    
    @Resource
    private KnowledgeBaseWrapperDao knowledgeBaseWrapperDao;
    
	private static Logger log = Logger.getLogger(WagerFactPollingContainer.class);
	
	public static final String RULE_SET_VOUCHER = "WagerSet";
    
	@PostConstruct
	public void init() {
		log.info("Init start .....");
		if (knowledgeBaseWrapperDao == null) {
			log.warn("*******************  We have a problem !!!!! *****************");
		}
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
	   
	   // Aggregate
	   SimpleFactAggregationContainer agg = Aggregate(wagerFact);
	   
	   // Rule Processing
       IterableMapWrapper facts = new IterableMapWrapper();
       WagerFactContainer container = new WagerFactContainer(gigaSpace);
       container.setFact(wagerFact);
       // container.setAggregation(agg);
       facts.put(WagerFactContainer.class.getSimpleName(), container);
       
       // log.info("fact:" + wagerFact.toString());
       // Execute Rules
       List<Object> resultFacts = executeRules(RULE_SET_VOUCHER, facts, null);
       	
       	// log.info("Results ExecuteRules Execution");
    	if (resultFacts != null ) {
    		Iterator<Object> items = resultFacts.iterator();
       
	       	while (items.hasNext()) {
	       		Fact fact = (Fact) items.next();
	       		if (fact.getState() > 0) {	
	       			long end = System.currentTimeMillis();
	       			((GenericAction) fact).setProcessTime(end-start);
	       			gigaSpace.write(fact);
	       		} 
	       	}
       	} else {
       		// wagerFact.setState(1);
       		throw new Exception("Error: Results Came back null!!!");
       	}
       	
       // log.info("End ExecuteRules Execution");
	   wagerFact.setState(2);
       return wagerFact;
   }
   
   private List<Object> executeRules(String ruleSet, Iterable<Object> facts,  Map<String, Object> globals) {
 		// log.info("Start executeRules method");
 		// log.info("Read: " + ruleSet);
 	   	KnowledgeBaseWrapper knowledgeBaseWrapper = knowledgeBaseWrapperDao.read(ruleSet);
 	    List<Object> outcome = new ArrayList<Object>();

 	   	if(knowledgeBaseWrapper != null) {
 	   		StatelessKnowledgeSession session = knowledgeBaseWrapper.getKnowledgeBase().newStatelessKnowledgeSession();
 	   		TrackingWorkingMemoryEventListener listener = 
 	   		        new TrackingWorkingMemoryEventListener();
 	   		session.addEventListener(listener);
 	   		/*
 	   		//Session scoped globals
 	        if(globals != null) {
 	            for(String key : globals.keySet()) {
 	                session.setGlobal(key, globals.get(key));
 	            }
 	        }
 		       */ 	
 		    session.execute(facts);	
 		    List<ObjectInsertedEvent> insertions = listener.getInsertions();
 		    for (ObjectInsertedEvent object : insertions) {
 		    	// log.info("inserted objects:" + object.getObject().toString());
 		    	outcome.add(object.getObject());
 		    }
 	   	}else {
 	   		log.error("Reading knowledgeBaseWrapper from the space returned null !!!!!!");
 	   		return null;
 	   	}
 	   	return outcome;
    }
   
  
   private SimpleFactAggregationContainer Aggregate(BaseWithdrawFact fact) throws InstantiationException, IllegalAccessException {
	   SimpleFactAggregationContainer template = new SimpleFactAggregationContainer();
	   template.setCustomerId(fact.getCustomerId());
	   template.setNetworkId(fact.getNetworkId());
	   SimpleFactAggregationContainer currentAggregation =  gigaSpace.readIfExists(template,3000);
	   if (currentAggregation == null)
	   {
		   currentAggregation = new SimpleFactAggregationContainer();
		   currentAggregation.setCustomerId(fact.getCustomerId());
		   currentAggregation.setNetworkId(fact.getNetworkId());
		   currentAggregation.addFactToAggregation(fact);
		   gigaSpace.write(currentAggregation,WriteModifiers.WRITE_ONLY);
		   
	   } else {
		   currentAggregation.addFactToAggregation(fact);
		   //gigaSpace.write(currentAggregation,WriteModifiers.UPDATE_ONLY);
		   gigaSpace.write(currentAggregation,0,100,WriteModifiers.UPDATE_ONLY);
		   
	   }
	   
	   gigaSpace.write(currentAggregation);
	   // log.info(currentAggregation.toString());
	   return currentAggregation;
	   
   }
}

package com.c123.demo.drools.client;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.remoting.ExecutorProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.c123.demo.model.facts.FlatWagerFact;
import com.c123.demo.utils.GameDataGenerator;
import com.gigaspaces.droolsintegration.service.drools.IRulesExecutionService;
import com.gigaspaces.droolsintegration.util.IterableMapWrapper;


@Component
public class DemoClient {

	private static Logger log = Logger.getLogger(DemoClient.class);
	
	public static final String RULE_SET_VOUCHER = "VoucherSet";

	@GigaSpaceContext(name = "gigaSpace")
	private GigaSpace gigaSpace;
	
    /**
     * Remoting service for executing business rules that are stored in the space.
     */
	
    @ExecutorProxy(gigaSpace = "gigaSpace")
    private IRulesExecutionService rulesExecutionService;
    
    /**
     * Main point of execution. Simply fires up the Spring context.
     *
     * @param args Command line args.
     */
    public static void main(String[] args) {  
        ClassPathXmlApplicationContext applicationContext = null;
    	try {    		
    		applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    		DemoClient demoClient = (DemoClient) applicationContext.getBean("demoClient");
    		
    		demoClient.start();
    		
    	} catch(Exception e) {
    		log.error(e);
    	} finally {
    		if(applicationContext != null) {
    			applicationContext.close(); applicationContext = null;
    		}
    	}
    }
    
    public void start(){
    	
        Thread t = new Thread(new GameEndCreatorExecuter());
        t.start();
    }
    
     
	// Thread class in charge of creating a payment every second
	private class GameEndCreatorExecuter implements Runnable {
		private long defaultDelay = 1000;
		
	    public void run() {
	        try {
	        	int id=1;
	        	log.info("DemoClient.GameEndCreatorExecuter thread has start");
	        	while (true){
	        		
	        		// New Fact Event 
	        		
	        		FlatWagerFact event = new FlatWagerFact();
	        		event.setActualAmount(GameDataGenerator.generateAmount());
	        		event.setCorrelationId(UUID.randomUUID());
	        		event.setCustomerId(GameDataGenerator.generateCustomerID());
	        		event.setDate(new Date());
	        		event.setId(String.valueOf(id++));
	        		event.setNetworkId(GameDataGenerator.generateNetworkID());
	        		event.setSkinId(GameDataGenerator.generateNetworkID());
	        		event.setOffering(1);
	        		event.setFundsType(2);
	        		event.setOperationSourceApplication(22);
	        		event.setRequestCurrencyCode("USD");
	        		event.setRoutingId(GameDataGenerator.generateNetworkID());
	        		event.setRequestReference(UUID.randomUUID());
	        		
	        		
	        		gigaSpace.write(event);
	        		
	        		log.info("DemoClient.GameEndCreatorExecuter new event: " + event.toString());

	        		Thread.sleep(defaultDelay);
	        	}
	        } catch (InterruptedException e) {
	        	log.error("DemoClient.GameEndCreatorExecuter has failed");
	        }
	    }
	    
	    public void execute(FlatWagerFact event) {
	        log.info("Starting ExecuteRules Execution");

	        
	        IterableMapWrapper facts = new IterableMapWrapper();
			facts.put(FlatWagerFact.class.getSimpleName(), event);
			
						
//	        List<String> resultKeyList = new ArrayList<String>();
//	        resultKeyList.add(Application.class.getSimpleName());
	        
	        try {
	        	// IterableMapWrapper resultFacts = (IterableMapWrapper) rulesExecutionService.executeRulesWithLimitedResults(RulesConstants.RULE_SET_APPLICANT_AGE, facts, resultKeyList);
	        	IterableMapWrapper resultFacts = (IterableMapWrapper) rulesExecutionService.executeRules(DemoClient.RULE_SET_VOUCHER, facts, null);
	        	
	        	log.info("Results ExecuteRules Execution");
	        	Iterator<Object> items = resultFacts.iterator();
	        	while (items.hasNext()) {
	        		log.info("items " + items.next().toString());
	        	}
	        	
	        	log.info("End ExecuteRules Execution");
	        	
	        }catch(Exception e) {
	            log.error(e.getMessage(), e);
	        }
	        log.info("End ExecuteRules Execution");
	       
	    }
 
    }
	
}

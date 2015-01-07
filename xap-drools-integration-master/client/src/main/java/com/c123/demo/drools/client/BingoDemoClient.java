package com.c123.demo.drools.client;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.remoting.ExecutorProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.c123.demo.real.Customer;
import com.c123.demo.real.WagerFact;
import com.c123.demo.utils.GameDataGenerator;
import com.gigaspaces.document.DocumentProperties;
import com.gigaspaces.droolsintegration.service.drools.IRulesExecutionService;

@Component
public class BingoDemoClient {
	private static Logger log = Logger.getLogger(BingoDemoClient.class);
	
	public static final String RULE_SET_VOUCHER = "VoucherSet";
	
	
	private HashMap<Integer,Customer> customerRegistry;
	private int numberOfCustomers = 10;

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
    		BingoDemoClient demoClient = (BingoDemoClient) applicationContext.getBean("bingoDemoClient");
    		
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
    	
        Thread t = new Thread(new FactCreatorExecuter());
        t.start();
    }
    
     
	// Thread class in charge of creating a payment every second
	private class FactCreatorExecuter implements Runnable {
		private long defaultDelay = 1000;
		
	    public void run() {
	        try {
	        	int id=1;
	        	log.info("FactCreatorExecuter thread has start");
	        	
	        	loadCustomerMetadata();
	        	while (true){
	        		
	        		// New Fact Event 
	        		
	        		WagerFact event = new WagerFact();
	        		event.setActualAmount(GameDataGenerator.generateAmount());
	        		event.setCorrelationId(UUID.randomUUID());
	        		Customer selCust = getRandomCustomer();
	        		event.setCustomerId(selCust.getId());
	        		event.setDate(new Date());
	        		event.setId(String.valueOf(id++));
	        		event.setNetworkId(selCust.getNetworkId());
	        		event.setSkinId(GameDataGenerator.generateID(3));
	        		event.setOffering(1);
	        		event.setFundsType(2);
	        		event.setOperationSourceApplication(22);
	        		event.setRequestCurrencyCode("USD");
	        		event.setRoutingId(selCust.getNetworkId());
	        		event.setRequestReference(UUID.randomUUID());
	        		event.setOperationSourceApplication(GameDataGenerator.generateID(15));
	        		event.setUpdateBalanceReason(GameDataGenerator.generateID(5));
	        		DocumentProperties addons = new DocumentProperties();
	        		addons.setProperty("Zone", 4);
	        		addons.setProperty("Class", "Platinum");
	        		event.setDocumentProperties(addons);

	        		gigaSpace.write(event);
	        		
	        		log.info("FactCreatorExecuter new event: " + event.toString());

	        		Thread.sleep(defaultDelay);
	        	}
	        } catch (InterruptedException e) {
	        	log.error("DemoClient.GameEndCreatorExecuter has failed");
	        }
	    }
	    
	    private void loadCustomerMetadata(){
	    	customerRegistry = new HashMap<Integer, Customer>();
	    	for (int i=1 ; i < numberOfCustomers ; i++) {
		    	Customer cust = new Customer();
		    	cust.setId(GameDataGenerator.generateCustomerID());
		    	log.info("Customer id: " + cust.getId());
		    	cust.setAddress(i + " Testi street California USA");
		    	cust.setMobilePhone("054-777777" + i);
		    	cust.setName("John Stamos " + i);
		    	cust.setNetworkId(GameDataGenerator.generateNetworkID());
		    	gigaSpace.write(cust);
		    	customerRegistry.put(cust.getId(), cust);
	    	}
	    }
	    
	    private Customer getRandomCustomer(){
	    	int id = GameDataGenerator.generateID(9);
	    	int counter=1;
	    	for (Customer cust : customerRegistry.values()) {
	    		if (counter == id){
	    			return cust;
	    		}
	    		counter++;
	    	}
	    	return null;
	    }
	    
	    /*
	    public void execute(FlatWagerFact event) {
	        log.info("Starting ExecuteRules Execution");

	        
	        IterableMapWrapper facts = new IterableMapWrapper();
			facts.put(FlatWagerFact.class.getSimpleName(), event);
			
        
	        try {
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
	    */
 
    }
}

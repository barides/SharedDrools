package com.c123.demo.drools.client.generators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.apache.log4j.Logger;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.openspaces.remoting.ExecutorProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.c123.demo.model.facts.Fact;
import com.c123.demo.real.BaseFact;
import com.c123.demo.real.Customer;
import com.c123.demo.real.DepositFact;
import com.c123.demo.real.WagerFact;
import com.c123.demo.utils.GameDataGenerator;
import com.gigaspaces.document.DocumentProperties;
import com.gigaspaces.droolsintegration.service.drools.IRulesExecutionService;

@Component
public class BingoDemoClient {
	private static Logger log = Logger.getLogger(BingoDemoClient.class);
	
	private HashMap<Integer,Customer> customerRegistry;
	private HashMap<Integer,Boolean> customerDepositRegistry;
	
	@Value("${numberOfCustomers}")
	private int numberOfCustomers;
	
	@Value("${numberOfIterations}")
	private int numberOfIterations;

	@Value("${eventsPerSecond}")
	private int eventsPerSecond;
	
	@Value("${numberOfNetworks}")
	private int numberOfNetworks;
	
	@Value("${customerBulkSize}")
	private int customerBulkSize;


	@GigaSpaceContext(name = "gigaSpace")
	private GigaSpace gigaSpace;
	
    
    /**
     * Main point of execution. Simply fires up the Spring context.
     *
     * @param args Command line args.
     */
    public static void main(String[] args) {  
        ClassPathXmlApplicationContext applicationContext = null;
    	try {    		
    		applicationContext = new ClassPathXmlApplicationContext("applicationContextBingo.xml");
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
    
	public int getNumberOfCustomers() {
		return numberOfCustomers;
	}

	public void setNumberOfCustomers(int numberOfCustomers) {
		this.numberOfCustomers = numberOfCustomers;
	}

	public int getNumberOfIterations() {
		return numberOfIterations;
	}

	public void setNumberOfIterations(int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

	public int getEventsPerSecond() {
		return eventsPerSecond;
	}

	public void setEventsPerSecond(int eventsPerSecond) {
		this.eventsPerSecond = eventsPerSecond;
	}

	public int getCustomerBulkSize() {
		return customerBulkSize;
	}

	public void setCustomerBulkSize(int customerBulkSize) {
		this.customerBulkSize = customerBulkSize;
	}
	
	// Thread class in charge of creating a payment every second
	private class FactCreatorExecuter implements Runnable {
		private long defaultDelay = 1000;
		
	    public void run() {
	        try {
	        	int iteratorCnt=1;
	        	int customerCnt=1;
	        	int factid=1000;
	        	log.info("FactCreatorExecuter thread has start");
	        	
	        	loadCustomerMetadata();
	        	while (iteratorCnt <= numberOfIterations){
	        		ArrayList<Fact> wagerBulk = new ArrayList<Fact>();
	        		ArrayList<Fact> depositBulk = new ArrayList<Fact>();
	        		// New Fact Event 
	        		for (int eventCnt=1; eventCnt <= eventsPerSecond; eventCnt++) {
	        			Customer selCust = getCustomerByID(customerCnt);
	        			// Customer selCust = getRandomCustomer();
		        		if (checkCustomerMadeDeposit(selCust.getId())) {
		        			wagerBulk.add(createWagerFact(factid, selCust));
		        		} else {
		        			depositBulk.add(createDepositFact(factid, selCust));
		        			setCustomerMadeDeposit(selCust.getId());
		        		}
		        		factid++;
		        		if (customerCnt >= numberOfCustomers){
		        			customerCnt=1;
		        		} else {
		        			customerCnt++;
		        		}
	        		}
	        		this.writeBulkToSpace( depositBulk);
	        		this.writeBulkToSpace( wagerBulk);
	        		
	        		log.info("Events written to the space deposit bulk size: " + depositBulk.size() + " wager bulk size: " + wagerBulk.size() + " iteration #"+iteratorCnt);
	        		iteratorCnt++;
	        		Thread.sleep(defaultDelay);
	        	}
	        } catch (InterruptedException e) {
	        	log.error("DemoClient.GameEndCreatorExecuter has failed");
	        }
	    }
	    
	    private void writeBulkToSpace(ArrayList<Fact> bulkFacts){
    		if (bulkFacts.size()>0) {
    			gigaSpace.writeMultiple(bulkFacts.toArray());
    		}
	    }
	    
	    private void loadCustomerMetadata(){
	    	customerRegistry = new HashMap<Integer, Customer>();
	    	customerDepositRegistry = new HashMap<Integer, Boolean>();
	    	for (int i=1 ; i <= numberOfCustomers ; i++) {
		    	Customer cust = new Customer();
		    	// cust.setId(GameDataGenerator.generateCustomerID());
		    	cust.setId(i);
		    	// log.info("Customer id: " + cust.getId());
		    	cust.setAddress(i + " Testi street California USA");
		    	cust.setMobilePhone("054-777777" + i);
		    	cust.setName("John Stamos " + i);
		    	cust.setNetworkId((i%numberOfNetworks)+1);
		    	// gigaSpace.write(cust);
		    	customerRegistry.put(cust.getId(), cust);
		    	customerDepositRegistry.put(cust.getId(), false);
	    	}
	    	
	    	
	    	int counter=1;
	    	Customer[] bulk = new Customer[customerBulkSize];
	    	for (Customer cust : customerRegistry.values()){
	    		if (counter%customerBulkSize > 0) {
	    			bulk[counter-1]=cust;
	    		} else {
	    			log.info("write bulk customer size: " + customerBulkSize);
	    			bulk[counter-1]=cust;
	    			gigaSpace.writeMultiple(bulk);
	    			bulk = new Customer[customerBulkSize];
	    			counter=0;
	    			try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}
	    		counter++;
	    	}
	    }
	    
	    private Customer getRandomCustomer(){
	    	int id = GameDataGenerator.generateID(numberOfCustomers);
	    	int counter=1;
	    	for (Customer cust : customerRegistry.values()) {
	    		if (counter == id){
	    			return cust;
	    		}
	    		counter++;
	    	}
	    	log.info("id is:" + id);
	    	return null;
	    }
	    
	    private Customer getCustomerByID(int custid){
	    	if (customerRegistry.containsKey(custid)) {
	    		return customerRegistry.get(custid);
	    	}
	    	log.warn("customer id not found:" + custid);
	    	return null;
	    }
	    
	    private boolean checkCustomerMadeDeposit(int custid){
	    	if (customerDepositRegistry.containsKey(custid)) {
	    		return customerDepositRegistry.get(custid);
	    	}
	    	return false;
	    }
	    
	    private void setCustomerMadeDeposit(int custid){
	    	if (customerDepositRegistry.containsKey(custid)) {
	    		customerDepositRegistry.put(custid, true);
	    	}
	    }
	    
	    private BaseFact createFact(int factid, Customer selCust) {
	    	boolean deposit = checkCustomerMadeDeposit(selCust.getId());
	    	if (!deposit) {
	    		setCustomerMadeDeposit(selCust.getId());
	    		return createDepositFact(factid, selCust);
	    	}
	    	return createWagerFact(factid, selCust);
	    }
	    
	    private WagerFact createWagerFact(int factid, Customer selCust){
    		
			WagerFact event = new WagerFact();
    		event.setActualAmount(GameDataGenerator.generateAmount());
    		event.setCorrelationId(UUID.randomUUID());
    		event.setCustomerId(selCust.getId());
    		event.setDate(new Date());
    		event.setId(String.valueOf(factid));
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
    		
    		return event;
	    }
	    
	    private DepositFact createDepositFact(int factid, Customer selCust){
    		
			DepositFact event = new DepositFact();
    		event.setActualAmount(new BigDecimal( 10000 ) );
    		event.setCorrelationId(UUID.randomUUID());
    		event.setCustomerId(selCust.getId());
    		event.setDate(new Date());
    		event.setId(String.valueOf(factid));
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
    		
    		return event;
	    }
    }
}

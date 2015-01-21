package com.c123.demo.cassandra.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.c123.demo.real.Customer;
import com.c123.demo.real.WagerFact;
import com.c123.demo.real.dao.CassandraDaoClient;
import com.c123.demo.utils.GameDataGenerator;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.gigaspaces.document.DocumentProperties;

public class CassandraSimpleClient {
	private static Logger log = Logger.getLogger(CassandraSimpleClient.class);
	public static String KEYSPACE_NAME= "drools";
	public static String TABLE_NAME= "data";

	public CassandraSimpleClient() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		CassandraSimpleClient client = new CassandraSimpleClient();
		client.writePOJOUsingDao();
	}

/*	public static void basicWrite() {
		Cluster cluster;
		Session session;

		// Connect to the cluster and keyspace "demo"
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("demo");

		// Insert one record into the users table
		session.execute("INSERT INTO users (id,lastname, age, city, email, firstname) VALUES (2,'Maor', 30, 'Austin', 'bob@example.com', 'Bob')");

		// Use select to get the user we just entered
		ResultSet results = session.execute("SELECT * FROM users");
		for (Row row : results) {
			System.out.format("%s %d\n", row.getString("lastname"),
					row.getInt("age"));
		}

	}*/

	
	public void writePOJOUsingDao(){
		log.info("Test start: writePOJOUsingDao");
		int numberOfCustomers=250;
		int numberOfEvents=50;
		int eventid=1;
		
		CassandraDaoClient dao = new CassandraDaoClient("127.0.0.1","9042","drools");
		dao.init();
		HashMap<Integer, Customer> custRegistry = loadCustomerMetadata(numberOfCustomers);
		ArrayList<Object> facts = new ArrayList<Object>();

		for (int eventCounter=1; eventCounter<=numberOfEvents; eventCounter++){
			facts = new ArrayList<Object>();
			for (int custCounter=1; custCounter<=numberOfCustomers; custCounter++){
				facts.add(this.createDummyFact(eventid,numberOfCustomers,custRegistry));
				eventid++;
			}
			try {
				dao.storeObjects(facts);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dao.close();
		log.info("Test end.");
	}
	
	
	private WagerFact createDummyFact(int factid, int numberOfCustomers, HashMap<Integer, Customer> customerRegistry){ 		
    		WagerFact event = new WagerFact();
    		event.setActualAmount(GameDataGenerator.generateAmount());
    		event.setCorrelationId(UUID.randomUUID());
    		Customer selCust = getRandomCustomer(numberOfCustomers,customerRegistry);
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
    		// log.info("event created: " + event.toString());
    		return event;
	}
	
	public static void writePOJO(){
		Cluster cluster;
		Session session;

		// Connect to the cluster and keyspace "demo"
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("drools");
		createGenericTable(session);
		
		HashMap<Integer, Customer> custRegistry = loadCustomerMetadata(5);
		
		for (Customer cust : custRegistry.values()){
			insertCustomer(session, cust);
			log.info("Insert customer " + cust.toString());
		}
	}
	
	public static void createGenericTable(Session session){
		session.execute(" CREATE TABLE IF NOT EXISTS " +  KEYSPACE_NAME+"." + TABLE_NAME + " (" +
				 "itemid text PRIMARY KEY, " + 
				  "item_type text," +
				  " item_content blob" +
				");");
	}

	public static void insertCustomer(Session session, Customer cust) {
		try {
	        PreparedStatement insertPreparedStatement = session.prepare(
	                "INSERT INTO "+ KEYSPACE_NAME+"." + TABLE_NAME  +" (itemid, item_type, item_content) VALUES (?, ?, ?);");
	        session.execute(insertPreparedStatement.bind(cust.getId(), cust.getClass().getName(), ByteBuffer.wrap( DataStorer.serialize(cust) )));
		} catch (IOException ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
	}

	
	
	private static HashMap<Integer, Customer> loadCustomerMetadata(int numberOfCustomers) {
		HashMap<Integer, Customer> customerRegistry = new HashMap<Integer, Customer>();
		for (int i = 1; i <= numberOfCustomers; i++) {
			Customer cust = new Customer();
			cust.setId(GameDataGenerator.generateCustomerID());
			cust.setAddress(i + " Testi street California USA");
			cust.setMobilePhone("054-777777" + i);
			cust.setName("John Stamos " + i);
			cust.setNetworkId(GameDataGenerator.generateNetworkID());
			customerRegistry.put(cust.getId(), cust);
			// write to Cassnadra
		}
		return customerRegistry;
	}
	
    private Customer getRandomCustomer(int numberOfCustomers, HashMap<Integer, Customer> customerRegistry){
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

}

package com.c123.demo.cassandra.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.c123.demo.real.Customer;
import com.c123.demo.utils.GameDataGenerator;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

public class CassandraSimpleClient {
	private static Logger log = Logger.getLogger(CassandraSimpleClient.class);
	public static String KEYSPACE_NAME= "drools";
	public static String TABLE_NAME= "data";

	public CassandraSimpleClient() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		writePOJO();
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

	public static void writePOJO(){
		Cluster cluster;
		Session session;

		// Connect to the cluster and keyspace "demo"
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("drools");
		createGenericTable(session);
		
		HashMap<Integer, Customer> custRegistry = loadCustomerMetadata();
		
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

	
	
	private static HashMap<Integer, Customer> loadCustomerMetadata() {
		HashMap<Integer, Customer> customerRegistry = new HashMap<Integer, Customer>();
		for (int i = 1; i < 5; i++) {
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

}

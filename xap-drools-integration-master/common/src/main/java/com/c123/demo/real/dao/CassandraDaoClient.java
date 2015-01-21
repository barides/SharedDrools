package com.c123.demo.real.dao;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.c123.demo.real.Customer;
import com.c123.demo.real.Identity;
import com.c123.demo.utils.CassandraSerailizeStorer;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class CassandraDaoClient {

	private static Logger log = Logger.getLogger(CassandraDaoClient.class);
	public static String TABLE_NAME= "data";
	private String keyspace;
	private String host;
	private int port;
	


	private final static CassandraConnector client = new CassandraConnector();
	private PreparedStatement insertPreparedStatement;
	private PreparedStatement retireveAllObjectsStatement;
	private PreparedStatement deleteObjectsStatement;
	
	private Stack<Identity> operations;
	private Session session;
	private boolean execute=false;
	private int maxBatchSize=3;

	public CassandraDaoClient() {
	}
	
	public CassandraDaoClient( String host, String port, String keyspace) {
		super();
		this.keyspace = keyspace;
		this.host = host;
		this.port = Integer.valueOf(port);
	}



	public void init() {
		log.info("init started");
		client.connect(host, port);	
		session = client.getSession();
		session.execute(getCreateTableSQL());
		session.execute(getCreateIndexSQL());
		String cql = "INSERT INTO "+ keyspace +"." + TABLE_NAME  +" (itemid, item_type, item_content) VALUES (?, ?, ?);";
		insertPreparedStatement = session.prepare(cql);
		cql = "Select * from "+keyspace +"." + TABLE_NAME ;
		retireveAllObjectsStatement = session.prepare(cql);
		cql = "DELETE FROM " + keyspace + "." + TABLE_NAME + " WHERE itemid = ?";
		deleteObjectsStatement = session.prepare(cql);
	}

	protected String getCreateTableSQL() {
		return " CREATE TABLE IF NOT EXISTS " + keyspace + "." +
		 TABLE_NAME + " (" + "itemid text, " + " item_type text," +
		 " item_content blob, PRIMARY KEY (itemid)" + ");"; 
	}
	
	protected String getCreateIndexSQL() {
		return " CREATE INDEX IF NOT EXISTS ON " + keyspace + "." + TABLE_NAME + " (item_type);"; 
	}
	
	public void close(){
		execute = false;
		client.close();
	}

	public void removeObject(Object obj) throws IOException, SQLException{
		if(obj instanceof Customer){
			Customer input = (Customer) obj;
			this.remove(input.getId().toString()) ;
			return;
		}
		
		if(obj instanceof Identity){
			Identity input = (Identity) obj;
			this.remove(input.getId());
			return;
		}
		
	}
	
	public void storeObject(Object obj) throws IOException, SQLException{
		// log.info("storeObject: " + obj.getClass().getName());
		if(obj instanceof Customer){
			Customer input = (Customer) obj;
			store(input.getId().toString(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			return;
		}
		
		if(obj instanceof Identity){
			Identity input = (Identity) obj;
			store(input.getId(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			return;
		}
		
	}


	public void removeObjects(ArrayList<Object> objects) throws IOException, SQLException{
		BatchStatement batch = new BatchStatement();
		for (Object obj :  objects){
				if(obj instanceof Customer){			
				Customer input = (Customer) obj;
				batch.add( deleteObjectsStatement.bind(input.getId().toString()) ); 				
			}
			
			if(obj instanceof Identity){
				Identity input = (Identity) obj;
				log.info("delete id="+input.getId() + "," + input.getClass().getName());
				batch.add( deleteObjectsStatement.bind(input.getId()) ); 
			}
		}
		this.executeBatch(batch);
		
	}	
	
	public void storeObjects(ArrayList<Object> objects) throws IOException, SQLException{
		// log.info("storeObjects start: " + objects.size());
		BatchStatement batch = new BatchStatement();
		int elmentCounter=0;
		for (Object obj :  objects){
			if(obj instanceof Customer){
				Customer input = (Customer) obj;
				store(input.getId().toString(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			}
			if(obj instanceof Identity){
				Identity input = (Identity) obj;
				log.info("store id="+input.getId() + "," + input.getClass().getName());
				batch.add(insertPreparedStatement.bind(input.getId().toString(), input.getClass().getName(), ByteBuffer.wrap( CassandraSerailizeStorer.serialize(obj) ) ) );
				elmentCounter++;
			}	
			if (elmentCounter>maxBatchSize){
				this.executeBatch(batch);
				batch = new BatchStatement();
				elmentCounter=0;
			}
		}
		if (elmentCounter>0){
			this.executeBatch(batch);
		}
		// this.executeBatch(batch);
		// log.info("batch size is " + batch.size());
	}
	
	
	
	private void executeBatch(BatchStatement batch){
		// log.info("executeBatch size " + batch.size());
		ResultSetFuture resultSetFuture = session.executeAsync(batch);
	    Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
	        @Override
	        public void onSuccess(com.datastax.driver.core.ResultSet resultSet) {
	            // do nothing
	        }

	        @Override
	        public void onFailure(Throwable throwable) {
	            log.error("Failed with: %s\n", throwable);
	        }
	    });
	}
	
	public ArrayList<Object> readObjects() throws SQLException, IOException, ClassNotFoundException {
		ArrayList<Object> outcome = new ArrayList<Object>();
		// execute cql to select all object from data table
		BoundStatement boundStatement = new BoundStatement(retireveAllObjectsStatement); 
		ResultSet resultSet = session.execute(boundStatement);
		List<Row> rows = resultSet.all(); 
		for (Row row : rows) { 
			Object current = createObjectFromDB(row);
			outcome.add(current);
		} 
		
		return outcome;
	}
	
	private Object createObjectFromDB(Row row) throws ClassNotFoundException, IOException {
		ByteBuffer serObject = row.getBytes("item_content");
		byte[] readObject = new byte[serObject.remaining()];
		serObject.get(readObject);
		return CassandraSerailizeStorer.deserialize(readObject);
	}
	

	private void store (String id, String typeName, byte[] obj) throws SQLException, IOException {		
		ResultSetFuture resultSetFuture = session.executeAsync(insertPreparedStatement.bind(id, typeName, ByteBuffer.wrap( obj )));
	    Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
	        @Override
	        public void onSuccess(com.datastax.driver.core.ResultSet resultSet) {
	            // do nothing
	        }

	        @Override
	        public void onFailure(Throwable throwable) {
	            log.error("Failed with: %s\n", throwable);
	        }
	    });
	}
	
	private void remove (String id) throws SQLException, IOException {		
		ResultSetFuture resultSetFuture = session.executeAsync(deleteObjectsStatement.bind(id));
	    Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
	        @Override
	        public void onSuccess(com.datastax.driver.core.ResultSet resultSet) {
	            // do nothing
	        }

	        @Override
	        public void onFailure(Throwable throwable) {
	            log.error("Failed with: %s\n", throwable);
	        }
	    });
	}
	
	
	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}	
	
}

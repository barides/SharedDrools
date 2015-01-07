package com.c123.demo.real.dao;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cassandra.cql.jdbc.CassandraDataSource;
import org.apache.log4j.Logger;

import com.c123.demo.real.BaseFact;
import com.c123.demo.real.Customer;
import com.c123.demo.real.GenericAction;
import com.c123.demo.real.aggregation.SimpleFactAggregationContainer;
import com.c123.demo.utils.CassandraSerailizeStorer;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class CassandraDaoClient {

	private static Logger log = Logger.getLogger(CassandraDaoClient.class);
	public static String TABLE_NAME= "data";
	public String keyspace;
	public String host;
	public int port;
	
	private final static CassandraConnector client = new CassandraConnector();
	private PreparedStatement insertPreparedStatement;
	private PreparedStatement retireveAllObjectsStatement;
	private Session session;

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
		client.close();
	}

	public void storeObject(Object obj) throws IOException, SQLException{
		// log.info("storeObject: " + obj.getClass().getName());
		if(obj instanceof Customer){
			//log.info("storeObject Customer: " + obj.getClass().getName());
			Customer input = (Customer) obj;
			//log.info("storeObject Customer: " + input.getId().toString() + "," + input.getClass().getName());
			store(input.getId().toString(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			return;
		}
		
		if(obj instanceof BaseFact){
			BaseFact input = (BaseFact) obj;
			store(input.getId(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			return;
		}
		
		if(obj instanceof GenericAction){
			GenericAction input = (GenericAction) obj;
			store(input.getId(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			return;
		}
		
		if(obj instanceof SimpleFactAggregationContainer){
			// log.info("storeObject SimpleFactAggregationContainer ");
			SimpleFactAggregationContainer input = (SimpleFactAggregationContainer) obj;
			store(input.getId(), input.getClass().getName(), CassandraSerailizeStorer.serialize(obj));
			return;
		}
		
	}

	private void store (String id, String typeName, byte[] obj) throws SQLException, IOException {		
		session.execute(insertPreparedStatement.bind(id, typeName, ByteBuffer.wrap( obj )));
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
		// String itemId = row.getString("itemid");
		// String itemType = row.getString("item_type");
		ByteBuffer serObject = row.getBytes("item_content");
		byte[] readObject = new byte[serObject.remaining()];
		serObject.get(readObject);
		return CassandraSerailizeStorer.deserialize(readObject);
	}


}

package com.c123.demo.mirror;

import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.openspaces.persistency.patterns.ManagedEntriesSpaceSynchronizationEndpoint;

import com.c123.demo.real.dao.CassandraDaoClient;
import com.gigaspaces.sync.DataSyncOperation;
import com.gigaspaces.sync.OperationsBatchData;

public class CassandraSpaceSynchronizationEndpoint extends
		ManagedEntriesSpaceSynchronizationEndpoint {
	
	private static Logger log = Logger.getLogger(CassandraSpaceSynchronizationEndpoint.class);

	public CassandraSpaceSynchronizationEndpoint() {
		super();
		log.info("create CassandraSpaceSynchronizationEndpoint object");
	}
	
	@Resource
	private HashSet<String> mEntries;
	
	// private CassandraDAO dao;
	private CassandraDaoClient dao;

	private String host;
	private String port;
	private String keyspace;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	@Override
	public Iterable<String> getManagedEntries() {
		log.info("GetManagedEntries");
		for (String types : mEntries) {
			log.info("ManagedEntries type=" + types);
		}
		return mEntries;
	}

	public HashSet<String> getmEntries() {
		return mEntries;
	}

	public void setmEntries(HashSet<String> mEntries) {
		this.mEntries = mEntries;
	}



	@PreDestroy
	public void close(){
		log.info("Close()");
		dao.close();
	}

	@PostConstruct
	public void init(){
		log.info("Init ...........");
		dao = new CassandraDaoClient(host,port,keyspace);
		dao.init();
		
	}
	
	@Override
	public void onOperationsBatchSynchronization(OperationsBatchData batchData) {
		
		super.onOperationsBatchSynchronization(batchData);
		// log.info("onOperationsBatchSynchronization");
        DataSyncOperation[] operations = batchData.getBatchDataItems();
        for (DataSyncOperation operation : operations) {
        	if (operation.supportsDataAsObject()) {
        		Object obj = operation.getDataAsObject();
        		//log.info("onOperationsBatchSynchronization: " + obj.getClass().getName());
        		//if (obj.getClass().getSimpleName().equals("Customer") ){
        		//	 log.info("onOperationsBatchSynchronization got customer object");
        		//}
        		storeObject(obj);
        	}
        }
	}
	
	public CassandraDaoClient getDao() {
		return dao;
	}

	private void storeObject(Object  obj){
		try {
			dao.storeObject(obj);
		} catch(Exception ex) {
			log.info("Faild to insert object: " + obj.toString());
			ex.printStackTrace();
		}
	}
	
}

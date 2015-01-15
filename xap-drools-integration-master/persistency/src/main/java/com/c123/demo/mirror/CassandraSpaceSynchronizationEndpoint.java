package com.c123.demo.mirror;

import java.util.ArrayList;
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
		
        DataSyncOperation[] operations = batchData.getBatchDataItems();
        log.info("onOperationsBatchSynchronization amount of operations are: " + operations.length);
        ArrayList<Object> storeInput = new  ArrayList<Object>();
        ArrayList<Object> deleteInput = new  ArrayList<Object>();
        for (DataSyncOperation operation : operations) {
        	if (operation.supportsDataAsObject()) {
        		Object obj = operation.getDataAsObject();
        		com.gigaspaces.sync.DataSyncOperationType type = operation.getDataSyncOperationType();
        		if (operation.getDataSyncOperationType() == com.gigaspaces.sync.DataSyncOperationType.REMOVE){
        			// removeObject(obj);
        			deleteInput.add(obj);
        		} else {
        			storeInput.add(obj);
        		}
        	}
        }
        
        if (deleteInput.size() > 0){
        	log.info("onOperationsBatchSynchronization delete object count:" + deleteInput.size());
        	removeObjects(deleteInput);
        } else {
        	 log.info("onOperationsBatchSynchronization no delete");
        }
        if (storeInput.size() > 0){
        	log.info("onOperationsBatchSynchronization insert object count:" + storeInput.size());
        	storeObjects(storeInput);
        } else {
       	 	log.info("onOperationsBatchSynchronization no insert");
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
	
	private void storeObjects(ArrayList<Object>  objects){
		try {
			dao.storeObjects(objects);
		} catch(Exception ex) {
			log.info("Faild to insert objects!!!!");
			ex.printStackTrace();
		}
	}
	
	private void removeObject(Object  obj){
		try {
			dao.removeObject(obj);
		} catch(Exception ex) {
			log.info("Faild to insert object: " + obj.toString());
			ex.printStackTrace();
		}
	}
	
	private void removeObjects(ArrayList<Object>  objects){
		try {
			dao.removeObjects(objects);
		} catch(Exception ex) {
			log.info("Faild to delete objects !!!!");
			ex.printStackTrace();
		}
	}
	
}

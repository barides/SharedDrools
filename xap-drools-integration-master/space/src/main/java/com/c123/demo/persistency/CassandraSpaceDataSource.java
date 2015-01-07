package com.c123.demo.persistency;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.openspaces.persistency.patterns.ManagedEntriesSpaceDataSource;

import com.c123.demo.real.dao.CassandraDaoClient;
import com.gigaspaces.datasource.DataIterator;
import com.gigaspaces.metadata.SpaceTypeDescriptor;

public class CassandraSpaceDataSource extends ManagedEntriesSpaceDataSource {
	
	private static Logger log = Logger.getLogger(CassandraSpaceDataSource.class);
	
	@Resource
	private HashSet<String> mEntries;
	
	// private CassandraDAO dao;
	private CassandraDaoClient dao;

	private String host;
	private String port;
	private String keyspace;

	public CassandraSpaceDataSource()  {
		// TODO Auto-generated constructor stub
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

	public CassandraDaoClient getDao() {
		return dao;
	}

	public void setDao(CassandraDaoClient dao) {
		this.dao = dao;
	}

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
	
	public DataIterator<SpaceTypeDescriptor> initialMetadataLoad(){
    	return null;
	}
	
	public DataIterator<Object> initialDataLoad(){
		// Load all data thru initial load using the DAO objects
		try {
			ArrayList<Object> res =  new ArrayList<Object>();
			res.addAll(dao.readObjects());
			return new CustomDataIterator(res);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}

}

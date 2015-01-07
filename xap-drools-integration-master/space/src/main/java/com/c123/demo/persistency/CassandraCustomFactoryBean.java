package com.c123.demo.persistency;

import java.util.HashSet;

import javax.annotation.Resource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CassandraCustomFactoryBean implements FactoryBean<CassandraSpaceDataSource> , InitializingBean {

	@Resource
	private HashSet<String> managedEntries;
	
	private String host;
	private String port;
	private String keyspace;

	public CassandraCustomFactoryBean() {
		// TODO Auto-generated constructor stub
	}
	
	private CassandraSpaceDataSource cassandraSpaceDataSource;

	
	@Override
	public CassandraSpaceDataSource getObject() throws Exception {
		// Get space data sources if one is not available create one
		if (cassandraSpaceDataSource == null ) {
			cassandraSpaceDataSource = new CassandraSpaceDataSource();
			cassandraSpaceDataSource.setmEntries(managedEntries);
			cassandraSpaceDataSource.setHost(host);
			cassandraSpaceDataSource.setPort(port);
			cassandraSpaceDataSource.setKeyspace(keyspace);
			cassandraSpaceDataSource.init();
		}
		
		return cassandraSpaceDataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return CassandraSpaceDataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	// Get managed entities by the space data sources
	public HashSet<String> getManagedEntries() {
		return managedEntries;
	}

	// Set managed entities by the space data sources
	public void setManagedEntries(HashSet<String> managedEntries) {
		this.managedEntries = managedEntries;
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

	public CassandraSpaceDataSource getCassandraSpaceDataSource() {
		return cassandraSpaceDataSource;
	}

	public void setCassandraSpaceDataSource(
			CassandraSpaceDataSource cassandraSpaceDataSource) {
		this.cassandraSpaceDataSource = cassandraSpaceDataSource;
	}

}

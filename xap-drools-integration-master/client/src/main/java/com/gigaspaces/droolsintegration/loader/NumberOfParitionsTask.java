package com.gigaspaces.droolsintegration.loader;

import java.util.List;

import org.openspaces.core.executor.DistributedTask;

import com.gigaspaces.async.AsyncResult;

public class NumberOfParitionsTask implements 	DistributedTask<Integer, Integer> {

	private static final long serialVersionUID = -5810963652256840815L;
	
	public Integer execute() throws Exception {
	    return 1;
	  }
	
	
	  public Integer reduce(List<AsyncResult<Integer>> results) throws Exception {
	    int count = 0;
	    for (AsyncResult<Integer> result:results) {
	      if (result.getException() != null) {
	        throw result.getException();
	      }
	      count += result.getResult();
	    }
	    return count;
	  }}

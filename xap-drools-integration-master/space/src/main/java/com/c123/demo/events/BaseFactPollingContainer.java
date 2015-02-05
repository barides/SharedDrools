package com.c123.demo.events;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.jini.core.lease.LeaseDeniedException;
import net.jini.core.lease.UnknownLeaseException;

import org.apache.log4j.Logger;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import org.drools.runtime.StatelessKnowledgeSession;
import org.openspaces.core.GigaSpace;

import com.c123.demo.model.facts.Fact;
import com.c123.demo.real.aggregation.CommonAggregationContainer;
import com.c123.demo.real.aggregation.IAggregationContainer;
import com.gigaspaces.client.WriteModifiers;
import com.gigaspaces.droolsintegration.dao.KnowledgeBaseWrapperDao;
import com.gigaspaces.droolsintegration.model.drools.KnowledgeBaseWrapper;
import com.j_spaces.core.LeaseContext;
import com.j_spaces.core.cache.ILeasedEntryCacheInfo;

public class BaseFactPollingContainer {
	
	protected String PACKAGE_NAME="None";

	private static Logger log = Logger
			.getLogger(BaseFactPollingContainer.class);

	protected void executeRules(String ruleSet, Iterable<Object> facts,
			GigaSpace gigaSpace, KnowledgeBaseWrapperDao knowledgeBaseWrapperDao, Map<String, Object> globals) throws LeaseDeniedException, UnknownLeaseException, RemoteException {
		// log.info("Start executeRules method");
		// log.info("Read: " + ruleSet);
		KnowledgeBaseWrapper knowledgeBaseWrapper = readRules(ruleSet, knowledgeBaseWrapperDao);

		if (knowledgeBaseWrapper != null) {
			StatelessKnowledgeSession session = knowledgeBaseWrapper
					.getKnowledgeBase().newStatelessKnowledgeSession();
			TrackingWorkingMemoryEventListener listener = new TrackingWorkingMemoryEventListener();
			session.addEventListener(listener);
			/*
			 * //Session scoped globals if(globals != null) { for(String key :
			 * globals.keySet()) { session.setGlobal(key, globals.get(key)); } }
			 */
			session.execute(facts);
			List<ObjectInsertedEvent> insertions = listener.getInsertions();
			List<ObjectUpdatedEvent> updates = listener.getUpdates();

			// iterate on all inserts made
			for (ObjectInsertedEvent object : insertions) {
				// log.info("inserted objects:" +
				// object.getObject().toString());

				Fact fact = (Fact) object.getObject();
				// Check state so we will no re-insert original facts
				if (fact.getState() > 0) {
					gigaSpace.write(fact);
					// gigaSpace.write(fact,3000);
				}
			}

			// Insert only unique updates
			HashSet<Fact> uniqueUpdatedObjects = new HashSet<Fact>();
			for (ObjectUpdatedEvent object : updates) {
				// log.info("updated objects:" + object.getObject().toString());
				// outcome.add(object.getObject());
				if (!uniqueUpdatedObjects.contains((Fact) object.getObject())) {
					gigaSpace.write(object.getObject(), 0, 500, WriteModifiers.UPDATE_ONLY);
					uniqueUpdatedObjects.add((Fact) object.getObject());
				}
			}

		} else {
			log.error("Reading knowledgeBaseWrapper from the space returned null !!!!!!");
		}
	}

	protected KnowledgeBaseWrapper readRules(String ruleSet,
			KnowledgeBaseWrapperDao knowledgeBaseWrapperDao) {
		return knowledgeBaseWrapperDao.read(ruleSet);
	}

	protected String generateRuleSetNamePerNetwork(int networkid) {
		return PACKAGE_NAME + "_" + networkid;
	}

	protected IAggregationContainer readAggregation(int networkId, int customerId, GigaSpace gigaSpace){
    	CommonAggregationContainer currentAggregation =  gigaSpace.readIfExists(new CommonAggregationContainer(networkId, customerId),3000);
	    return currentAggregation;
    }

	
	/**
	 * Get a diff between two dates
	 * 
	 * @param date1
	 *            the oldest date
	 * @param date2
	 *            the newest date
	 * @param timeUnit
	 *            the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	protected long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, timeUnit);
	}
}

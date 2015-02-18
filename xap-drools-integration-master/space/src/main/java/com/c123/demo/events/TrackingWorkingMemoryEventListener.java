package com.c123.demo.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

public class TrackingWorkingMemoryEventListener implements RuleRuntimeEventListener {
	private static Logger log = Logger
			.getLogger(TrackingWorkingMemoryEventListener.class);
	private List<ObjectInsertedEvent> insertions = new ArrayList<ObjectInsertedEvent>();
	private List<ObjectDeletedEvent> deletions = new ArrayList<ObjectDeletedEvent>();
	private List<ObjectUpdatedEvent> updates = new ArrayList<ObjectUpdatedEvent>();
	private List<Map<String, Object>> factChanges = new ArrayList<Map<String, Object>>();
	// private FactHandle handleFilter;
	// private Class<?> classFilter;

	/**
	 * Void constructor sets the listener to record all working memory events
	 * with no filtering.
	 */
	public TrackingWorkingMemoryEventListener() {
		// this.handleFilter = null;
	}

	/**
	 * Constructor which sets up an event filter. The listener will only record
	 * events when the event {@link FactHandle} matches the constructor
	 * argument.
	 * 
	 * @param handle
	 *            The {@link FactHandle} to filter on.
	 */
	/*
	public TrackingWorkingMemoryEventListener(FactHandle handle) {
		this.handleFilter = handle;
	}

	public TrackingWorkingMemoryEventListener(Class<?> classFilter) {
		this.handleFilter = null;
		this.classFilter = classFilter;
	}
*/
	
	public List<ObjectInsertedEvent> getInsertions() {
		return insertions;
	}

	public List<ObjectDeletedEvent> getDeletions() {
		return deletions;
	}

	public List<ObjectUpdatedEvent> getUpdates() {
		return updates;
	}

	public List<Map<String, Object>> getFactChanges() {
		return factChanges;
	}

	public String getPrintableSummary() {
		return "TrackingWorkingMemoryEventListener: " + "insertions=["
				+ insertions.size() + "], " + "retractions=["
				+ deletions.size() + "], " + "updates=[" + updates.size()
				+ "]";
	}
	/*
	 * public String getPrintableDetail() { StringBuilder report = new
	 * StringBuilder( "TrackingWorkingMemoryEventListener: " + "insertions=[" +
	 * insertions.size() + "], " + "retractions=[" + retractions.size() + "], "
	 * + "updates=[" + updates.size() + "]"); for (ObjectInsertedEvent event :
	 * insertions) { report.append("\n" +
	 * DroolsUtil.objectDetails(event.getObject())); } for (ObjectRetractedEvent
	 * event : retractions) { report.append("\n" +
	 * DroolsUtil.objectDetails(event.getOldObject())); } for
	 * (ObjectUpdatedEvent event : updates) { report.append("\n" +
	 * DroolsUtil.objectDetails(event.getObject())); } return report.toString();
	 * }
	 */

	@Override
	public void objectInserted(org.kie.api.event.rule.ObjectInsertedEvent event) {
		insertions.add(event);
		// log.info("Insertion: " + event.getObject().toString());
		
	}

	@Override
	public void objectUpdated(org.kie.api.event.rule.ObjectUpdatedEvent event) {
		updates.add(event);
		// log.info("Updates: " + event.getObject().toString());
	}

	@Override
	public void objectDeleted(ObjectDeletedEvent event) {
		deletions.add(event);
		
	}
}

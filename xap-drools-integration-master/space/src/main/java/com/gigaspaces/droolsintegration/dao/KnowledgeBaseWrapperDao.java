package com.gigaspaces.droolsintegration.dao;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.kie.internal.builder.KnowledgeBuilder;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.context.GigaSpaceContext;
import org.springframework.stereotype.Component;

import com.c123.demo.events.WagerFactPollingContainer;
import com.gigaspaces.client.ReadModifiers;
import com.gigaspaces.droolsintegration.model.drools.KnowledgeBaseWrapper;


@Component
public class KnowledgeBaseWrapperDao {
	private static Logger log = Logger.getLogger(KnowledgeBaseWrapperDao.class);

	@GigaSpaceContext(name = "gigaSpace")
	private GigaSpace gigaSpace;
	
	public KnowledgeBaseWrapper read(String ruleSet) {
		return gigaSpace.read(new KnowledgeBaseWrapper(ruleSet),0, ReadModifiers.READ_COMMITTED);
	}
	
	public void write(KnowledgeBaseWrapper knowledgeBaseWrapper) {
		gigaSpace.write(knowledgeBaseWrapper);
	}
	
	public void addKnowledgePackages(KnowledgeBaseWrapper knowledgeBaseWrapper, KnowledgeBuilder kbuilder) {
		knowledgeBaseWrapper.getKnowledgeBase().getKiePackages().addAll( kbuilder.getKnowledgePackages() );
		knowledgeBaseWrapper.setTotalKnowledgePackages(knowledgeBaseWrapper.getTotalKnowledgePackages() + 1);
        knowledgeBaseWrapper.setTotalRules(knowledgeBaseWrapper.getTotalRules() + 1);
		
        gigaSpace.write(knowledgeBaseWrapper);
	}
	
}
package com.gigaspaces.droolsintegration.model.drools;

import java.io.Serializable;
import org.kie.api.KieBase;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;


@SpaceClass
public class KnowledgeBaseWrapper implements Serializable {

    private static final long serialVersionUID = 3738043534134325175L;

    private String id;
    private String ruleSet;
    private KieBase knowledgeBase;
    private Integer totalKnowledgePackages;
    private Integer totalRules;
    private Integer routing;
    
   
    public KnowledgeBaseWrapper() {}
    
    public KnowledgeBaseWrapper(String ruleSet) {
    	this.ruleSet = ruleSet;
    }

    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }
    
    
    public String getRuleSet() {
		return ruleSet;
	}
    
    public void setId(String id) {
        this.id = id;
    }

	public void setRuleSet(String ruleSet) {
		this.ruleSet = ruleSet;
	}

	public KieBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(KieBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public Integer getTotalRules() {
		return totalRules;
	}

	public void setTotalRules(Integer totalRules) {
		this.totalRules = totalRules;
	}

	public Integer getTotalKnowledgePackages() {
		return totalKnowledgePackages;
	}

	public void setTotalKnowledgePackages(Integer totalKnowledgePackages) {
		this.totalKnowledgePackages = totalKnowledgePackages;
	}

	@SpaceRouting
	public Integer getRouting() {
		return routing;
	}

	public void setRouting(Integer routing) {
		this.routing = routing;
	}

}
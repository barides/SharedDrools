package com.gigaspaces.droolsintegration.model.drools;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

import java.io.Serializable;


@SpaceClass
public class DroolsRuleRemoveEvent implements Serializable {

	private static final long serialVersionUID = 6352155115963148562L;
	
	private String id;
    private String ruleSet;
    private String packageName;
    private String ruleName;
    private Boolean processed;
    private Integer routing;
  
    public DroolsRuleRemoveEvent() {}
    
    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }
    
    
    public String getRuleSet() {
		return ruleSet;
	}

	public void setRuleSet(String ruleSet) {
		this.ruleSet = ruleSet;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public void setId(String id) {
        this.id = id;
	}

	public Boolean isProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@SpaceRouting
	public Integer getRouting() {
		return routing;
	}

	public void setRouting(Integer routing) {
		this.routing = routing;
	}
	
}
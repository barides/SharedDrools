package com.c123.demo.real;

import java.io.Serializable;

import com.gigaspaces.annotation.pojo.SpaceDynamicProperties;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.document.DocumentProperties;

public class BaseJavaSpaceEntity implements Serializable,Identity {

	private String id; 
	private Integer routingId;
	private DocumentProperties DocumentProperties;

	public BaseJavaSpaceEntity() {
		super();
	}  
	
	@Override
	public String toString() {
		return "BaseJavaSpaceEntity [id=" + id + ", networkId=" + routingId
				+ "]";
	}

	@SpaceId(autoGenerate = false)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@SpaceRouting
	public Integer getRoutingId() {
		return routingId;
	}


	public void setRoutingId(Integer routingId) {
		this.routingId = routingId;
	}

	@SpaceDynamicProperties
	public DocumentProperties getDocumentProperties() {
		return DocumentProperties;
	}

	public void setDocumentProperties(DocumentProperties documentProperties) {
		DocumentProperties = documentProperties;
	}

}

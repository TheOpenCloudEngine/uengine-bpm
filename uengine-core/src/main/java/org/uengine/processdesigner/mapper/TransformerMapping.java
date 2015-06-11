package org.uengine.processdesigner.mapper;

import java.io.Serializable;

public class TransformerMapping implements Serializable{

	Transformer transformer;
	String linkedArgumentName;
	
	public String getLinkedArgumentName() {
		return linkedArgumentName;
	}
	public void setLinkedArgumentName(String linkedArgumentName) {
		this.linkedArgumentName = linkedArgumentName;
	}
	public Transformer getTransformer() {
		return transformer;
	}
	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}
	
	
}

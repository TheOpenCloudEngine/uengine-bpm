package org.uengine.contexts;

import java.io.Serializable;

import org.metaworks.annotation.Id;
import org.metaworks.component.Tree;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.designer.MappingCanvas;


public class MappingContext implements Serializable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	ParameterContext[] mappingElements;

	public ParameterContext[] getMappingElements() {
		return mappingElements;
	}

	public void setMappingElements(ParameterContext[] mappingElements) {
		this.mappingElements = mappingElements;
	}


	String id;
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	transient Tree mappingTreeLeft;
	public Tree getMappingTreeLeft() {
		return mappingTreeLeft;
	}
	public void setMappingTreeLeft(Tree mappingTreeLeft) {
		this.mappingTreeLeft = mappingTreeLeft;
	}

	transient Tree mappingTreeRight;
	public Tree getMappingTreeRight() {
		return mappingTreeRight;
	}
	public void setMappingTreeRight(Tree mappingTreeRight) {
		this.mappingTreeRight = mappingTreeRight;
	}

	MappingCanvas mappingCanvas;
	public MappingCanvas getMappingCanvas() {
		return mappingCanvas;
	}
	public void setMappingCanvas(MappingCanvas mappingCanvas) {
		this.mappingCanvas = mappingCanvas;
	}

}
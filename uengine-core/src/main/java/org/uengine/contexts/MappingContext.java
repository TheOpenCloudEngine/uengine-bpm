package org.uengine.contexts;

import java.io.Serializable;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ParameterContext;


public class MappingContext implements Serializable{
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	ParameterContext[] mappingElements;

	public ParameterContext[] getMappingElements() {
		return mappingElements;
	}

	public void setMappingElements(ParameterContext[] mappingElements) {
		this.mappingElements = mappingElements;
	}

}

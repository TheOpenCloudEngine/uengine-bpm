package org.uengine.contexts;

import java.io.Serializable;

import org.uengine.kernel.GlobalContext;

public class KeyValueContext implements Serializable {
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	public KeyValueContext(){}
	
	public KeyValueContext(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

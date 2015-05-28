package org.uengine.webservice;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("param")
public class ParameterProperty implements Serializable {
	
	@XStreamAsAttribute
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	String paramType;
		public String getParamType() {
			return paramType;
		}
		public void setParamType(String paramType) {
			this.paramType = paramType;
		}
	String dataType;
		public String getDataType() {
			return dataType;
		}
		public void setDataType(String dataType) {
			this.dataType = dataType;
		}
	String defaultValue;
		public String getDefaultValue() {
			return defaultValue;
		}
		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}
	
}

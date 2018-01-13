package org.uengine.kernel.bpmn;

import com.jayway.jsonpath.JsonPath;
import org.uengine.kernel.CatchingMessageEvent;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessInstance;

import java.io.Serializable;

public class CatchingRestMessageEvent extends CatchingMessageEvent {
	
	private String correlationKey;
	private String servicePath;

	public String getCorrelationKey() {
		return correlationKey;
	}

	public void setCorrelationKey(String correlationKey) {
		this.correlationKey = correlationKey;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

}

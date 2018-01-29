package org.uengine.kernel.bpmn;

import com.jayway.jsonpath.JsonPath;
import org.uengine.kernel.CatchingMessageEvent;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ServiceDefinition;

import java.io.Serializable;

public class CatchingRestMessageEvent extends CatchingMessageEvent {
	
	private String correlationKey;
		public String getCorrelationKey() {
			return correlationKey;
		}
		public void setCorrelationKey(String correlationKey) {
			this.correlationKey = correlationKey;
		}

	private String servicePath;
		public String getServicePath() {
			return servicePath;
		}
		public void setServicePath(String servicePath) {
			this.servicePath = servicePath;
		}

	ServiceDefinition operationRef; //in BPMN 2.0, QName is the property type
		public ServiceDefinition getOperationRef() {
			return operationRef;
		}
		public void setOperationRef(ServiceDefinition operationRef) {
			this.operationRef = operationRef;
		}

}

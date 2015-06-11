package org.uengine.kernel;

import java.io.Serializable;

public interface ProcessVariablePartResolver {

	public void setPart(ProcessInstance instance, String[] partPath, Object value) throws Exception;
	public Object getPart(ProcessInstance instance, String[] partPath, Object value) throws Exception;
}

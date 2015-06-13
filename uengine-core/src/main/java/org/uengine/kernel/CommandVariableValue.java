package org.uengine.kernel;

import java.io.Serializable;

public interface CommandVariableValue extends Serializable{

	public boolean doCommand(ProcessInstance instance, String variableKey) throws Exception;
}

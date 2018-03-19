package org.uengine.kernel.bpmn;

import org.uengine.kernel.ProcessInstance;

/**
 * @author Jinyoung Jang
 */

public class TerminateEndEvent extends EndEvent {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;


	@Override
	public boolean isStopAllTokens() {
		return true;
	}
}
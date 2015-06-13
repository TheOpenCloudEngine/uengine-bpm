package org.uengine.kernel;

import org.uengine.kernel.Condition;
import org.uengine.kernel.ProcessInstance;

public class DummyCondition extends Condition {
	
	private boolean flag;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public DummyCondition(boolean flag) {
		this.flag = flag;
	}

	@Override
	public boolean isMet(ProcessInstance instance, String scope) throws Exception {
		return flag;
	}

}

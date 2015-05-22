package org.uengine.scheduler;

import java.sql.Timestamp;

public class SchedulerItem {
	private int idx;
	private String instanceId;
	private String tracingTag;
	private Timestamp startDate;
	private String expression;
	private boolean newInstance;
	private String defId;
	private String globalCom;
	
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getTracingTag() {
		return tracingTag;
	}

	public void setTracingTag(String tracingTag) {
		this.tracingTag = tracingTag;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public boolean isNewInstance() {
		return newInstance;
	}

	public void setNewInstance(boolean newInstance) {
		this.newInstance = newInstance;
	}
	
	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public String getGlobalCom() {
		return globalCom;
	}
	public void setGlobalCom(String globalCom) {
		this.globalCom = globalCom;
	}
}

/*
 * Created on 2004. 10. 29.
 */
package org.uengine.security;

import java.security.acl.Permission;

/**
 * @author Jinyoung Jang
 */
public class FolderPermission implements Permission{

	boolean isDesign;
	boolean isInitiation;
	boolean isMonitoring;
	boolean isParticipation;
	boolean isAdhocDesign;
	boolean isFlowControl;
	boolean isRemove;
	
	
	public boolean isAdhocDesign() {
		return isAdhocDesign;
	}

	public boolean isDesign() {
		return isDesign;
	}

	public boolean isFlowControl() {
		return isFlowControl;
	}

	public boolean isInitiation() {
		return isInitiation;
	}

	public boolean isMonitoring() {
		return isMonitoring;
	}

	public boolean isParticipation() {
		return isParticipation;
	}

	public boolean isRemove() {
		return isRemove;
	}

	public void setAdhocDesign(boolean b) {
		isAdhocDesign = b;
	}

	public void setDesign(boolean b) {
		isDesign = b;
	}

	public void setFlowControl(boolean b) {
		isFlowControl = b;
	}

	public void setInitiation(boolean b) {
		isInitiation = b;
	}

	public void setMonitoring(boolean b) {
		isMonitoring = b;
	}

	public void setParticipation(boolean b) {
		isParticipation = b;
	}

	public void setRemove(boolean b) {
		isRemove = b;
	}

}

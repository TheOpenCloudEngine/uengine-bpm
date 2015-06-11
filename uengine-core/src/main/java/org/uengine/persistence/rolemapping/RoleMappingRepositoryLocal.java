package org.uengine.persistence.rolemapping;

//import java.rmi.RemoteException;

/**
 * @author Jinyoung Jang
 */


public interface RoleMappingRepositoryLocal extends javax.ejb.EJBLocalObject{

	public abstract Long getRoleMappingId();
	public abstract void setRoleMappingId(Long id);

	public abstract Long getInstId();
	public abstract void setInstId(Long id);

	public abstract Long getRootInstId();
	public abstract void setRootInstId(Long id);

	public abstract String getRoleName();
	public abstract void setRoleName(String roleName);

	public abstract String getValue();
	public abstract void setValue(String value);

	public abstract String getEndpoint();
	public abstract void setEndpoint(String value);

	public abstract String getResName();
	public abstract void setResName(String value);
	
	public String getGroupId();
	public void setGroupId(String value);

	public Number getAssignType();
	public void setAssignType(Number assignType);
	
	public String getAssignParam1();
	public void setAssignParam1(String assignParam1);
	
	public String getDispatchParam1();
	public void setDispatchParam1(String DispatchParam1);
	
	public Number getDispatchOption();
	public void setDispatchOption(Number DispatchOption);
	

}
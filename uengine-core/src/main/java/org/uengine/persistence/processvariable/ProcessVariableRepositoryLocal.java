package org.uengine.persistence.processvariable;

import java.util.Date;


//import java.rmi.RemoteException;

/**
 * @author Jinyoung Jang
 */


public interface ProcessVariableRepositoryLocal extends javax.ejb.EJBLocalObject{

	public abstract Long getVarId();
	public abstract void setVarId(Long id);

	public abstract Long getInstId();
	public abstract void setInstId(Long id);

	public abstract String getValueString();
	public abstract void setValueString(String value);

	public abstract long getValueLong();
	public abstract void setValueLong(long value);

	public abstract java.util.Date getValueDate();
	public abstract void setValueDate(java.util.Date value);

	public abstract boolean getValueBoolean();
	public abstract void setValueBoolean(boolean value);
	
	public abstract int getDataType();
	public abstract void setDataType(int type);

	public abstract Number getVarIndex();
	public abstract void setVarIndex(Number index);

	public abstract String getTrcTag();
	public abstract void setTrcTag(String tracingTag);
	
	public abstract Boolean getIsProperty();
	public abstract void setIsProperty(Boolean isProperty);
	
	public abstract Date getModDate();
	public abstract void setModDate(Date modifiedDate);
	
	public abstract String getKeyName();
	public abstract void setKeyName(String key);

	public abstract String getKeyString();
	public abstract void setKeyString(String key);
	
	public abstract String getFileContent();
	public abstract void setFileContent(String fileContent);
	
	public abstract String getHtmlFilePath();
	public abstract void setHtmlFilePath(String htmlFilePath);

}
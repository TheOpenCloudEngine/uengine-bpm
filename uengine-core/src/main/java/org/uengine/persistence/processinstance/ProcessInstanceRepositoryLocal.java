package org.uengine.persistence.processinstance;


//import java.rmi.RemoteException;

/**
 * @author Jinyoung Jang
 */


public interface ProcessInstanceRepositoryLocal extends javax.ejb.EJBLocalObject{

	public Long getInstId();
	public void setInstId(Long id);

	public String getDefVerId();
	public void setDefVerId(String defVerId);

	public String getDefId();
	public void setDefId(String defId);

	public String getDefPath();
	public void setDefPath(String DefPath);

	public String getDefName();
	public void setDefName(String defName);

	public java.util.Date getStartedDate();
	public void setStartedDate(java.util.Date when);

	public abstract java.util.Date getFinishedDate();
	public abstract void setFinishedDate(java.util.Date when);

	public abstract java.util.Date getDueDate();
	public abstract void setDueDate(java.util.Date when);

	public abstract java.util.Date getDefModDate();
	public abstract void setDefModDate(java.util.Date when);

	public abstract java.util.Date getModDate();
	public abstract void setModDate(java.util.Date when);

	public String getStatus();
	public void setStatus(String status);

	public String getInfo();
	public void setInfo(String info);

	public abstract String getName();
	public abstract void setName(String name);
	
	public abstract boolean isDeleted();
	public abstract void setDeleted(boolean deleted);
	
	public abstract boolean isAdhoc();
	public abstract void setAdhoc(boolean adhoc);

	public abstract boolean isSubProcess();
	public abstract void setSubProcess(boolean subProcess);
	
	public abstract boolean isEventHandler();
	public abstract void setEventHandler(boolean eventHandler);
	
	public abstract Long getRootInstId();
	public abstract void setRootInstId(Long instanceId);
	
	public abstract Long getMainInstId();
	public abstract void setMainInstId(Long instanceId);
	
	public abstract String getMainActTrcTag();
	public abstract void setMainActTrcTag(String value);	

	public abstract String getMainExecScope();
	public abstract void setMainExecScope(String value);	

	public abstract Long getMainDefVerId();
	public abstract void setMainDefVerId(Long defVerId);
	
	public boolean isArchive();
	public void setArchive(boolean archive);
	
	public abstract String getAbsTrcPath();
	public abstract void setAbsTrcPath(String value);

	public boolean isDontReturn();
	public void setDontReturn(boolean dontReturn);
	
	public String getExt1();
	public void setExt1(String ext1);
	
	public String getExt2();
	public void setExt2(String ext2);
	
	public String getExt3();
	public void setExt3(String ext3);
	
	public String getExt4();
	public void setExt4(String ext4);
	
	public String getExt5();
	public void setExt5(String ext5);
	
	public String getExt6();
	public void setExt6(String ext6);
	
	public String getExt7();
	public void setExt7(String ext7);
	
	public String getExt8();
	public void setExt8(String ext8);
	
	public String getExt9();
	public void setExt9(String ext9);
	
	public String getExt10();
	public void setExt10(String ext10);
	
	public Number getStrategyId();
	public void setStrategyId(Number strategyId);

	public String getInitComCd();
	public void setInitComCd(String initComCd);
}
package org.uengine.processmanager;

import java.util.Calendar;

import org.uengine.kernel.ProcessInstance;

import org.uengine.persistence.processinstance.ProcessInstanceRepositoryLocal;

/**
 * This class is only for listing brief information on process instances. 
 * So this class doesn't hold entire part of the instance data.
 * Use ProcessInstance or DefaultProcessInstance directly if full data copy is needed.
 * @author Jinyoung Jang
 */

public class ProcessInstanceRemote implements java.io.Serializable{

	String id;
	String status;
	String info;
	String definition;
	String name;
	Calendar startedDate;
	
	public ProcessInstanceRemote(){
	}
	
	public ProcessInstanceRemote(ProcessInstance ai){
		setId(ai.getInstanceId());
		
		try{
			setStatus(ai.getStatus(""));
			setInfo(ai.getInfo());
			setDefinition(ai.getProcessDefinition().getId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ProcessInstanceRemote(ProcessInstanceRepositoryLocal pirl){		
		setId(pirl.getInstId().toString());
		setStatus(pirl.getStatus());
		setInfo(pirl.getInfo());
		setDefinition(pirl.getDefVerId().toString());
		setName(pirl.getName());
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(pirl.getStartedDate());
		setStartedDate(cal);		
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setId(String string) {
		id = string;
	}

	public void setStatus(String string) {
		status = string;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String string) {
		info = string;
	}

	public String getDefinition() {
		return definition;
	}

	public Calendar getStartedDate() {
		return startedDate;
	}

	public void setDefinition(String string) {
		definition = string;
	}

	public void setStartedDate(Calendar calendar) {
		startedDate = calendar;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}

}
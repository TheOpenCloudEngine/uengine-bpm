package org.uengine.processmanager;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.uengine.contexts.TextContext;
import org.uengine.kernel.ActivityReference;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;
import org.uengine.persistence.processdefinition.ProcessDefinitionRepositoryLocal;
import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionRepositoryLocal;

/**
 * This class is only for listing brief information on a process definition. 
 * So this class doesn't hold the process definition data at all (except several declarations like variables and roles).
 * Use ProcessDefinition directly for such purpose if necessary.
 * @author Jinyoung Jang
 */

public class ProcessDefinitionRemote implements java.io.Serializable{
	
	Role[] roles;

	ProcessVariable[] processVariables;

	TextContext name;
	TextContext description;
	boolean isFolder;
	boolean production;
	boolean isAdhoc;
	String parentFolder;
	boolean initiateByFirstWorkitem = false;
	String initiatorHumanActivityTracingTag;
	String id; //defid
	int version;
	String objType;
	String belongingDefinitionId; //defverid
	String alias;
	boolean isVisible;
	Date modifiedDate;
	
	public ProcessDefinitionRemote(){}
	
	public ProcessDefinitionRemote(ProcessDefinition pd, ProcessTransactionContext ptc){
		this(pd, ptc, null);
	}

	public ProcessDefinitionRemote(ProcessDefinition pd, ProcessTransactionContext ptc, ProcessDefinitionVersionRepositoryLocal pdvrl){
		roles = pd.getRoles();
		name.setText(pd.getName());		
		description.setText(pd.getDescription());
		processVariables = pd.getProcessVariables();
		isAdhoc = pd.isAdhoc();
		belongingDefinitionId = pd.getBelongingDefinitionId();
		id = pd.getId();
		version = pdvrl!=null ? pdvrl.getVer().intValue():pd.getVersion();
		alias = pd.getAlias();
		modifiedDate = pd.getModifiedDate().getTime();

		setInitiateByFirstWorkitem(pd.isInitiateByFirstWorkitem());

		if(ptc!=null && isInitiateByFirstWorkitem()){
			ActivityReference initiatorActRef = pd.getInitiatorHumanActivityReference(ptc);
			HumanActivity initiatorHumanActivity = null;

			if(initiatorActRef!=null)
				initiatorHumanActivity = (HumanActivity)initiatorActRef.getActivity();

			if(initiatorHumanActivity!=null)
				//throw new RuntimeException(new UEngineException("Although this definition is set to be initiated by the first workitem, there's no initiating human activity."));
				setInitiatorHumanActivityTracingTag(initiatorHumanActivity.getTracingTag());
		}
	}
	
	public String getStrModifiedDate() {
		String strDate = "";
		if (modifiedDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			strDate = formatter.format(modifiedDate);
		}
		
		return strDate;
	}
	
	public ProcessDefinitionRemote(ProcessDefinitionRepositoryLocal pdDAO, ProcessDefinitionVersionRepositoryLocal pdvrl){
		name = TextContext.createInstance();
		name.setText(pdDAO.getName());
		
		description = TextContext.createInstance();
		description.setText(pdDAO.getDescription());
		
		isFolder = pdDAO.getIsFolder();
		
		if(!(pdDAO.getProdVerId() == null))
			setProduction(true);
		
		if(pdvrl!=null) {
			version = pdvrl.getVer().intValue();
			modifiedDate = pdvrl.getModDate();
		}
			
		alias = pdDAO.getAlias();
		
		parentFolder = (pdDAO.getParentFolder() != null ? pdDAO.getParentFolder().toString() : null);
		belongingDefinitionId = (pdDAO.getDefId() != null ? pdDAO.getDefId().toString() : null);
		if(pdvrl != null){
			id = (pdvrl.getDefVerId() != null ? pdvrl.getDefVerId().toString() : null);
		}
	
		objType = pdDAO.getObjType();
		if(!org.uengine.util.UEngineUtil.isNotEmpty(objType))
			objType = "process";
	}
	
	public Role[] getRoles(){
		return roles;
	}
	
	public TextContext getName(){
		return name;
	}
	
	protected void setName(TextContext name){
		this.name = name; 
	}
	
	public TextContext getDescription(){
		return description;
	}
	
	protected void setDescription(TextContext description){
		this.description = description;
	}
	
	public ProcessVariable[] getProcessVariableDescriptors(){
		return processVariables;
	}
	
	public boolean isFolder() {
		return isFolder;
	}

	public String getParentFolder() {
		return parentFolder;
	}

	protected void setFolder(boolean b) {
		isFolder = b;
	}

	protected void setParentFolder(String id) {
		parentFolder = id;
	}
	
	public boolean isProduction(){
		return production;
	}
	
	public void setProduction(boolean production){
		this.production = production;
	}

	public boolean isAdhoc() {
		return isAdhoc;
	}

	public void setAdhoc(boolean b) {
		isAdhoc = b;
	}
	

	public boolean isInitiateByFirstWorkitem() {
		return initiateByFirstWorkitem;
	}

	public void setInitiateByFirstWorkitem(boolean b) {
		initiateByFirstWorkitem = b;
	}

	public String getInitiatorHumanActivityTracingTag() {
		return initiatorHumanActivityTracingTag;
	}

	public void setInitiatorHumanActivityTracingTag(String string) {
		initiatorHumanActivityTracingTag = string;
	}

	public String getId() { 
		return id;
	}

	public void setId(String l) {
		id = l;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int i) {
		version = i;
	}

	public String getBelongingDefinitionId() {
		return belongingDefinitionId;
	}

	public void setBelongingDefinitionId(String l) {
		belongingDefinitionId = l;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
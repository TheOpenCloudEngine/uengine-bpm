package org.uengine.kernel;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.uengine.contexts.MappingContext;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.bpmn.StartEvent;
import org.uengine.persistence.dao.DAOFactory;
import org.uengine.persistence.worklist.WorklistDAOType;
import org.uengine.util.ActivityForLoop;
import org.uengine.util.UEngineUtil;
import org.uengine.webservices.worklist.DefaultWorkList;
import org.uengine.webservices.worklist.SimulatorWorkList;
import org.uengine.webservices.worklist.WorkList;
import org.uengine.webservices.worklist.WorkListServiceLocator;

/**
 * @author Jinyoung Jang
 */

//@Face(ejsPath="dwr/metaworks/genericfaces/FormFace.ejs")
public class HumanActivity extends ReceiveActivity {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static final String GENERICCONTEXT_CURR_LOGGED_ROLEMAPPING = "currentLoggedRoleMapping";

	
	public static String PVKEY_DUEDATE="_due date";
	public static String PVKEY_DURATION="_duration";
	public static String PVKEY_TASKID="_task id";
	public static String PVKEY_OPENTIME = "_openTime";
	public static String WORKLIST_SERVICE="Worklist Service";
	public static String PAYLOADKEY_TASKID = "TASKID";
	public static String PVKEY_PREVIOUS_ACTIVITY_TRACINGTAG = "_previous";
	public static String COMPLETED_ROLEMAPPING="_completed rolemapping";
	public static final String SNAPSHOT_DIRECTORY = GlobalContext.getPropertyString("filesystem.path", ProcessDefinitionFactory.DEFINITION_ROOT) + "history" + File.separatorChar; 
	
	
	protected Role role;
	@Hidden
		public Role getRole() {
			
			if(role!=null && role.getName()!=null && getProcessDefinition()!=null){
				//TODO: sometimes object reference can't reflect the definition's change
				Role roleInDefinition = getProcessDefinition().getRole(role.getName());
				if(roleInDefinition!=null && roleInDefinition != role){
					role = roleInDefinition;
				}
			}
			
			return role;
		}
		public void setRole(Role value) {
			role = value;
		}
		
	Role referenceRole;
	@Hidden
		public Role getReferenceRole() {
			return referenceRole;
		}
		public void setReferenceRole(Role referenceRole) {
			this.referenceRole = referenceRole;
		}
	
	String tool;
		public String getTool() {
			return tool;
		}
		public void setTool(String value) {
			tool = value;
		}

	boolean isSendEmailWorkitem=true;
	@Hidden
		public boolean isSendEmailWorkitem() {
			return isSendEmailWorkitem;
		}
		public void setSendEmailWorkitem(boolean isSendEmailWorkitem) {
			this.isSendEmailWorkitem = isSendEmailWorkitem;
		}
		
	boolean isNotificationWorkitem=false;
	@Hidden
		public boolean isNotificationWorkitem() {
			return isNotificationWorkitem;
		}
		public void setNotificationWorkitem(boolean isNotificationWorkitem) {
			this.isNotificationWorkitem = isNotificationWorkitem;
		}
		
	boolean isAllowAnonymous=true;
	@Hidden
		public boolean isAllowAnonymous() {
			return isAllowAnonymous;
		}
		public void setAllowAnonymous(boolean isAllowAnonymous) {
			this.isAllowAnonymous = isAllowAnonymous;
		}		
	String id;
		@Hidden
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
	int duration;
	@Hidden
		public int getDuration() {
			return duration;
		}
		public int getDuration(ProcessInstance instance) throws Exception{
			Integer durationInt = null;
			if( instance != null ){
				durationInt = (Integer)(instance.getProperty(getTracingTag(), PVKEY_DURATION));
			}
			
			if(durationInt==null){
				return getDuration();
			}
			
			return durationInt.intValue();
		}
		public void setDuration(int i) {
			duration = i;
		}
		
		public void setDuration(ProcessInstance instance, Integer i) throws Exception {
			setDuration(instance, i.intValue());
		}
		public void setDuration(ProcessInstance instance, int duration) throws Exception{
			if( instance != null ){
				instance.setProperty(getTracingTag(), PVKEY_DURATION, new Integer(duration));

				String[] taskIds = getTaskIds(instance);
				
				setDueDate(instance, instance.calculateDueDate(GlobalContext.getNow(instance.getProcessTransactionContext()), duration), true, false);
				
			}else{
				setDuration(duration);
			}
		}


	public Calendar getDueDate(ProcessInstance instance) throws Exception{
		Calendar dueDate = (Calendar)instance.getProperty(getTracingTag(), PVKEY_DUEDATE);
		
			return dueDate;
		}
	
	public void setDueDate(ProcessInstance instance, Calendar dueDate, boolean updateWorkList) throws Exception{
		setDueDate(instance, dueDate, updateWorkList, true);
	}

	public void setDueDate(ProcessInstance instance, Calendar dueDate, boolean updateWorkList, boolean refreshDuration) throws Exception{
		instance.setProperty(getTracingTag(), PVKEY_DUEDATE, dueDate);
		
		if(refreshDuration)
			refreshDuration(instance);
		
		if(updateWorkList){
			//reflect the duedate changes to the worklist where the taskid matches.
			String[] taskIds = getTaskIds(instance);
			
//			update the duedates of the associated workitems
			WorklistDAOType worklistDF = WorklistDAOType.getInstance(instance.getProcessTransactionContext());
			Date dueDateInDate = (dueDate == null ? null:dueDate.getTime());
			worklistDF.updateDueDateByTaskId(taskIds, dueDateInDate);
		}
		
		firePropertyChangeEventToActivityFilters(instance, PVKEY_DUEDATE, dueDate);
	}
	
	public void setDueDate(ProcessInstance instance, Calendar dueDate) throws Exception{
		setDueDate(instance, dueDate, true);
	}
	
	public void setDueDate(ProcessInstance instance, Date dueDate) throws Exception{
		
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(dueDate);
		
		setDueDate(instance, calDate);
	}
	
	public void setDueDate(ProcessInstance instance, GregorianCalendar dueDate) throws Exception{
		setDueDate(instance, (Calendar)dueDate);
	}
	
	private void setCompletedRoleMapping(ProcessInstance instance, RoleMapping completedRoleMapping) throws Exception{
		instance.setProperty(getTracingTag(), COMPLETED_ROLEMAPPING, completedRoleMapping);
	}

	private RoleMapping getCompletedRoleMapping(ProcessInstance instance) throws Exception{
		return (RoleMapping)instance.getProperty(getTracingTag(), COMPLETED_ROLEMAPPING);
	}
	
	int co2Emission;
	@Hidden
		public int getCo2Emission() {
			return co2Emission;
		}
		public void setCo2Emission(int co2Emission) {
			this.co2Emission = co2Emission;
		}

	int workload;
	@Hidden
		public int getWorkload() {
			return workload;
		}
		public void setWorkload(int i) {
			workload = i;
		}

	int priority;
	@Hidden
		public int getPriority() {
			return priority;
		}
		public void setPriority(int i) {
			priority = i;
		}

	ProcessVariable input;
		@Hidden
		public ProcessVariable getInput() {
			return input;
		}
		public void setInput(ProcessVariable value) {
			input = value;
		}
		
	String extValue1;
	@Hidden
		public String getExtValue1() {
			return extValue1;
		}
		public void setExtValue1(String extValue1) {
			this.extValue1 = extValue1;
		}
		
	String extValue2;
	@Hidden
		public String getExtValue2() {
			return extValue2;
		}
		public void setExtValue2(String extValue2) {
			this.extValue2 = extValue2;
		}
		
	String extValue3;
	@Hidden
		public String getExtValue3() {
			return extValue3;
		}
		public void setExtValue3(String extValue3) {
			this.extValue3 = extValue3;
		}
	
    String extValue4;
    @Hidden
		public String getExtValue4() {
			return extValue4;
		}
		public void setExtValue4(String extValue4) {
			this.extValue4 = extValue4;
		}

	String extValue5;
	@Hidden
		public String getExtValue5() {
			return extValue5;
		}
		public void setExtValue5(String extValue5) {
			this.extValue5 = extValue5;
		}
		
	String extValue6;
		@Hidden
		public String getExtValue6() {
			return extValue6;
		}
		public void setExtValue6(String extValue6) {
			this.extValue6 = extValue6;
		}
			
	String extValue7;
	@Hidden
		public String getExtValue7() {
			return extValue7;
		}
		public void setExtValue7(String extValue7) {
			this.extValue7 = extValue7;
		}
			
	String extValue8;
	@Hidden
		public String getExtValue8() {
			return extValue8;
		}
		public void setExtValue8(String extValue8) {
			this.extValue8 = extValue8;
		}
		
	String extValue9;
	@Hidden
		public String getExtValue9() {
			return extValue9;
		}
		public void setExtValue9(String extValue9) {
			this.extValue9 = extValue9;
		}
		
	String extValue10;
	@Hidden
		public String getExtValue10() {
			return extValue10;
		}
		public void setExtValue10(String extValue10) {
			this.extValue10 = extValue10;
		}				

/*	protected String getWIHSysAddress(){
		return GlobalContext.getProperties().getProperty("workitemhandler.address", "localhost:8082");
	}
*/
	TextContext instruction = TextContext.createInstance();
	@Hidden
	@Face(displayName="$instruction")
		public TextContext getInstruction() {
			return instruction;
		}
		public void setInstruction(TextContext string) {
			instruction = string;
		}
		
	TextContext keyword = TextContext.createInstance();
	@Hidden
	@Face(displayName="$keyword")
		public TextContext getKeyword() {
			return keyword;
		}
		public void setKeyword(TextContext string) {
			keyword = string;
		}
	
	public void setOpenTime(ProcessInstance instance, Calendar theTime) throws Exception{
		instance.setProperty(getTracingTag(), PVKEY_OPENTIME, theTime);
		
		firePropertyChangeEventToActivityFilters(instance, PVKEY_OPENTIME, theTime);
	}
	
	public Calendar getOpenTime(ProcessInstance instance){
		try{
			return (Calendar)instance.getProperty(getTracingTag(), PVKEY_OPENTIME);
		}catch(Exception e){
			return null;
		}
	}
		
	public HumanActivity(){
		super();
		setName("");
		setDescription("");
		setDuration(5);
		setTool("defaultHandler");
	}
	
	public Map createParameter(final ProcessInstance instance) throws Exception{
		
		Properties kpv = new Properties()/*{
			public Object put(Object key, Object val){
				if(val==null){
System.out.println("=========================== HARD-TO-FIND : HumanActivity.createParameter:put(" + key + "," + val + ") pi="+ instance.getInstanceId() + "  tracingtag=" + getTracingTag());
					return null;
				}else
					return super.put(key, val); 
			}
		}*/;
		
		if(instance!=null){
			kpv.setProperty(KeyedParameter.INSTRUCTION, getExtraMessage(instance));
			kpv.setProperty(KeyedParameter.INSTANCEID, instance.getInstanceId());
			kpv.setProperty(KeyedParameter.ROOTINSTANCEID, instance.getRootProcessInstanceId());
			kpv.setProperty(KeyedParameter.PROCESSDEFINITION, instance.getProcessDefinition().getId());
			kpv.setProperty(KeyedParameter.KEYWORD, evaluateContent(instance, getKeyword()!=null ? getKeyword().getText(GlobalContext.DEFAULT_LOCALE) : null).toString());
			kpv.setProperty("instanceName", instance.getName() != null ? instance.getName() : instance.getInstanceId());
			kpv.setProperty(KeyedParameter.DISPATCHINGOPTION, ""+getRole().getDispatchingOption());
			kpv.setProperty("startedTime", ""+getStartedTime(instance).getTimeInMillis());
			
			if(instance.getExecutionScopeContext()!=null 
					&& 
					(instance.getExecutionScopeContext().getRootActivityInTheScope() == this 
							|| instance.getExecutionScopeContext().getRootActivityInTheScope().isAncestorOf(this))){
				kpv.setProperty("executionScope", instance.getExecutionScopeContext().getExecutionScope());
			}
			
			Calendar dueDate = getDueDate(instance);
			if(dueDate!=null)
				kpv.setProperty(KeyedParameter.DUEDATE, ""+dueDate.getTimeInMillis());

		}
		
		kpv.setProperty("disOption", ""+getRole().getDispatchingOption());

		
		kpv.setProperty(KeyedParameter.TOOL, getTool());
		kpv.setProperty(KeyedParameter.TRACINGTAG, getTracingTag());
		kpv.setProperty(KeyedParameter.MESSAGE, getMessage() != null ? getMessage() : "");
		String title = getName(GlobalContext.DEFAULT_LOCALE);
//		if( getDescription() != null && getDescription().getText() != null ){
//			title = getDescription().getText();
//		}
		kpv.setProperty(KeyedParameter.TITLE, title);
		kpv.setProperty(KeyedParameter.DURATION, ""+getDuration());
		kpv.setProperty(KeyedParameter.PROCESSDEFINITIONNAME, getProcessDefinition().getName(GlobalContext.DEFAULT_LOCALE));
		kpv.setProperty(KeyedParameter.CO2EMISSION, ""+getCo2Emission());
		
		if(getExtValue1()!=null)
			kpv.setProperty("extValue1", evaluateContent(instance, getExtValue1()).toString());
		
		if(getExtValue2()!=null)
			kpv.setProperty("extValue2", evaluateContent(instance, getExtValue2()).toString());
		
		if(getExtValue3()!=null)
			kpv.setProperty("extValue3", evaluateContent(instance, getExtValue3()).toString());
		
		if(getExtValue4()!=null)
			kpv.setProperty("extValue4", evaluateContent(instance, getExtValue4()).toString());
		
		if(getExtValue5()!=null)
			kpv.setProperty("extValue5", evaluateContent(instance, getExtValue5()).toString());
		
		if(getExtValue6()!=null)
			kpv.setProperty("extValue6", evaluateContent(instance, getExtValue6()).toString());
		
		if(getExtValue7()!=null)
			kpv.setProperty("extValue7", evaluateContent(instance, getExtValue7()).toString());
		
		if(getExtValue8()!=null)
			kpv.setProperty("extValue8", evaluateContent(instance, getExtValue8()).toString());
		
		if(getExtValue9()!=null)
			kpv.setProperty("extValue9", evaluateContent(instance, getExtValue9()).toString());
		
		if(getExtValue10()!=null)
			kpv.setProperty("extValue10", evaluateContent(instance, getExtValue10()).toString());
		
		return (Map)kpv;
	}

//	protected RoleMapping getActualMapping(ProcessInstance instance) throws Exception{
//		return getRole().getMapping(instance, getTracingTag());
//	}
	
	public RoleMapping getActualMapping(ProcessInstance instance) throws Exception{	
		
		RoleMapping roleMapping = null;

		if(getRole()==null) throw new IllegalStateException("Role is not set for HumanActivity ["+ getName() + "]");
		
		try{
			if (Activity.STATUS_COMPLETED.equals(instance.getStatus(getTracingTag()))) {
				roleMapping = getCompletedRoleMapping(instance);
			}
			
			if (roleMapping == null) {
				roleMapping = getRole().getMapping(instance, getTracingTag());
			}
			
//			if(roleMapping == null && instance.isNew() && instance.isSubProcess())
//				throw new NullPointerException();
		}catch(Exception e){
			throw new UEngineException("Couldn't get the actual user for the role [" + getRole().getName() + "] since: \n" + e.getMessage(), e);
		}
		if(instance.isNew()){ //only when the instance is newly initiating now.
			//FIXME: may occur a decrease of performance 
			ActivityReference actRef = getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext());
			boolean thisIsInitiationActivity = (actRef.getActivity() == this);
			if( actRef.getActivity() instanceof StartEvent){
				thisIsInitiationActivity = true;
			}
			//if the activity is initiator, put the role mapping with the login user.
			if(thisIsInitiationActivity && getProcessDefinition().isInitiateByFirstWorkitem()){
				RoleMapping currentLogin = null;
				try{
					if( instance.isSubProcess() ){
						currentLogin = instance.getRootProcessInstance().getRoleMapping("Initiator");
					}else{
						currentLogin = (RoleMapping)instance.getProcessTransactionContext().getProcessManager().getGenericContext().get(GENERICCONTEXT_CURR_LOGGED_ROLEMAPPING);
					}
				}catch(Exception e){	
				}
				if(roleMapping==null){
	
					if(currentLogin==null){
						throw new UEngineException("Couldn't get the actual user for the role [" + getName() + "]. The reason maybe among these: 1. you didn't assign any role for this activity. 2. you didn't give login information for the current login user");
					}
					
					roleMapping = currentLogin;
					
					instance.putRoleMapping(getRole().getName(), roleMapping);
					
				}else{
					try{
						if(!instance.isSubProcess() && getRole().getRoleResolutionContext()!=null && !getRole().containsMapping(instance, currentLogin)){
							UEngineException ue = new UEngineException("You ("+currentLogin+") are not permitted to initiate this process. The initiator group is '"+getRole().getRoleResolutionContext()+"'.");
							ue.setErrorLevel(UEngineException.MESSAGE_TO_USER);
							
							throw ue;
						}
						
					}catch(Exception e){
						if(e instanceof UEngineException) throw e;
						throw new UEngineException("Error when to check permission for process initiation. Remove the first workitem's Role Resolution Context or set the \"Initiate by first workitem\" option to \"off\" if it is an undesired permission checking.", e);
					}
				}
			}
		}
		
		/***
		 * TODO: Initiator Dispatching Option :: 
		 *    Default -> Role.DISPATCHINGOPTION_AUTO; 
		 *    (change) Default -> ProcessDefinition in DISPATCHINGOPTION
		 */		
		//if(roleMapping!=null){
		//	int disPatchingOption = getProcessDefinition().getRole(roleMapping.getName()).getDispatchingOption();
		//	roleMapping.setDispatchingOption(disPatchingOption);
		//}
			
		return roleMapping;
	}

	protected void setCompletedHumanActivityInTransaction(ProcessInstance instance) throws Exception{
		List aicList= instance.getProcessTransactionContext().getExecutedActivityInstanceContextsInTransaction();
		if(aicList !=null && aicList.size() >0){
			for(int i=0 ; i < aicList.size() ; i++){
				ActivityInstanceContext aic= (ActivityInstanceContext)aicList.get(i);
				Activity previous = aic.getActivity();
				if(previous instanceof HumanActivity){
					instance.setProperty(getTracingTag(), PVKEY_PREVIOUS_ACTIVITY_TRACINGTAG,previous.getTracingTag());
				}
			}
		}
	}
	
	public HumanActivity getCompletedHumanActivityInTransaction(ProcessInstance instance) throws Exception{	
		try{
			String tt = (String)instance.getProperty(getTracingTag(), PVKEY_PREVIOUS_ACTIVITY_TRACINGTAG);
			return (HumanActivity)instance.getProcessDefinition().getActivity(tt);
		}catch(Exception e){
			return null;
		}
	}
		
	protected void executeActivity(ProcessInstance instance) throws Exception{
		addWorkitem(instance, null);
		setCompletedHumanActivityInTransaction(instance);
	}

	protected void addWorkitem(ProcessInstance instance, String defaultStatus) throws Exception{
		
		RoleMapping roleMapping = null;
		try{
			roleMapping = getActualMapping(instance);
			
			/*
			 * 2013/06/10
			 * role mapping 이 되어있지 않은 경우에 Exception 발생 안하게 변경 
			UEngineException actualWorkerNotBound = new UEngineException("Actual worker for '"+ getRole() +"' isn't bound yet"); 
			actualWorkerNotBound.setActivity(this);
			actualWorkerNotBound.setInstance(instance);
			if(roleMapping==null) 
				throw actualWorkerNotBound;
			if(roleMapping.getEndpoint()==null) 
				throw actualWorkerNotBound;
			if(roleMapping.getEndpoint().trim().length()==0) 
				throw actualWorkerNotBound;
			*/
			
		}catch(Exception e){
			
			//if(getRole().getRoleResolutionContext()==null)
			throw e;
		}
		
		WorkList worklist = instance.getWorkList();

		//TODO: change not to read again by the createParameter() if you don't want to cache processinstance
		Calendar dueDate = getDueDate(instance);
		if(dueDate==null)
			setDueDate(instance, instance.calculateDueDate(GlobalContext.getNow(instance.getProcessTransactionContext()), getDuration()), false);

		Map kpv = createParameter(instance);

		if(defaultStatus!=null){
			kpv.put(KeyedParameter.DEFAULT_STATUS, defaultStatus);
		}

		if(roleMapping!=null) {
			if (!kpv.get(KeyedParameter.DISPATCHINGOPTION).equals("7")) {
				int dispatchingOption = roleMapping.getDispatchingOption();
				
				if(dispatchingOption == Role.DISPATCHINGOPTION_AUTO)
					dispatchingOption = (roleMapping.size() > 1 ? Role.DISPATCHINGOPTION_RACING : Role.DISPATCHINGOPTION_ALL);
	
				kpv.put(KeyedParameter.DISPATCHINGOPTION, ""+dispatchingOption);
			}
			
			
			if(
					roleMapping.getResourceName().equals(roleMapping.getEndpoint())
					|| !UEngineUtil.isNotEmpty(roleMapping.getResourceName())
			)
				roleMapping.fill(instance);
			kpv.put("resourceName", roleMapping.getResourceName());

			String[] params = roleMapping.getDispatchingParameters();
			if(params!=null && params.length > 0){
				for(int i=0; i<params.length; i++){
					if(params[i]!=null){
						kpv.put("dispatchParam" + (i+1), params[i]);
					}
				}
			}
		}
		if( roleMapping==null || (roleMapping!=null && roleMapping.size() > 1)){
			/*
			 * 2013/06/10
			 * role mapping 이 되어있지 않은 경우에 경합모드로 변경
			 * 2014/03/12
			 * role mapping 이 여러사람에게 떨어질때 경합모드로 변경
			 */
			//kpv.put(KeyedParameter.DISPATCHINGOPTION, ""+getRole().getDispatchingOption());
			roleMapping = new RoleMapping();
			roleMapping.setEndpoint(GlobalContext.getPropertyString("codi.user.id","0"));
			roleMapping.setResourceName(GlobalContext.getPropertyString("codi.user.name","CODI"));
			
			kpv.put("resourceName", roleMapping.getResourceName());
			kpv.put(KeyedParameter.DISPATCHINGOPTION, ""+Role.DISPATCHINGOPTION_RACING);
		}
		
		if(getRole()!=null)
			kpv.put("roleName", getRole().getName());
		
		if(getReferenceRole()!=null){
			kpv.put("referenceRoleName", getReferenceRole().getName());
			
			RoleMapping referenceRoleMapping = getReferenceRole().getMapping(instance);
			if(referenceRoleMapping!=null/* && instance.getRoleMapping(getReferenceRole().getName())==null*/){
				instance.putRoleMapping(referenceRoleMapping);
			}
		}
		
//		if(getRole().getRoleResolutionContext()!=null){
//			String[] params = getRole().getRoleResolutionContext().getDispatchingParameters();
//			
//			if(params!=null && params.length > 0 && params[0]!=null)
//				kpv.put("dispatchParam1", params[0]);
//		}
		//kpv.put("dispatchParam1", getRole().getDispatchingOption());
		
		KeyedParameter[] parameters = new KeyedParameter[kpv.size()];
		
		int i=0;
		for(Iterator iter = kpv.keySet().iterator(); iter.hasNext(); ){
			String key = (String)iter.next();
			parameters[i] = new KeyedParameter();
			parameters[i].setKey(key);
			parameters[i].setValue(kpv.get(key));
			i++;
		}		

		String[] taskIds = getTaskIds(instance);
		
		String[] taskIds2 = new String[1];//roleMapping.size()];
		
		i=0;
// Dispatching option to all users is deprecated in 2.0
//		do{
		String tID = null;
		if(taskIds!=null && taskIds.length > i)
			tID = taskIds[i];
			
		if(tID!=null && (tID.equals("null") || tID.trim().length()==0)){
			tID = null;
		}
		
		if(tID==null){
			tID = worklist.addWorkItem(roleMapping != null ? roleMapping.getEndpoint() : null, parameters, instance.getProcessTransactionContext());
		}else if(Activity.STATUS_SUSPENDED.equals(getStatus(instance))){//if suspended
			ResultPayload rp = new ResultPayload();
			rp.setExtendedValue(new KeyedParameter(KeyedParameter.DEFAULT_STATUS, DefaultWorkList.WORKITEM_STATUS_CONFIRMED)); //or WORKITEM_STATUS_RESUMED
			
			instance.getWorkList().updateWorkItem(taskIds[0], null, rp.getExtendedValues(), instance.getProcessTransactionContext());
		}else{
			
			//check if this thread trigered by the method 'compensateToThis', clear the tID so that the workitem should be issued again. 
/*			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			
			String callerMethodName = stack[2].getMethodName();
			String callerClassName = stack[2].getClassName()+"";
			
			if(stack.length>2){
				for(int j=3; j<stack.length; j++){
					StackTraceElement stackElement = stack[j];
					callerMethodName = stack[j].getMethodName();
					if("compensateToThis".equals(callerMethodName)){
						tID = null;
						break;
					}
				}
			}*/
			
			worklist.addWorkItem(tID, roleMapping != null ? roleMapping.getEndpoint() : null, parameters, instance.getProcessTransactionContext());
			
		}
			
		taskIds2[i++] = tID;
//		}while(roleMapping.next());
		
		setTaskIds(instance, taskIds2);		
		
		super.executeActivity(instance);
	}

	public boolean onComplete(ProcessInstance instance, Object payload) throws Exception{
		String taskId;
		
		if(payload instanceof ResultPayload){
			ResultPayload resultPayload = ((ResultPayload)payload);
			taskId = (String)resultPayload.getExtendedValue(PAYLOADKEY_TASKID);
		}else{//for old-version, single role mapping
			taskId = (String)instance.getProperty(getTracingTag(), PVKEY_TASKID);
		}

		WorkList worklist = instance.getWorkList();
		
		if(worklist instanceof SimulatorWorkList) return true;

		String[] taskIds = getTaskIds(instance);

		if(!UEngineUtil.isNotEmpty(taskId) && taskIds.length==1){
			taskId = taskIds[0];
		}

		KeyedParameter[] parameters = new KeyedParameter[]{};		
		worklist.completeWorkItem(taskId, parameters, instance.getProcessTransactionContext());
		
		
		if(taskIds!=null && taskIds.length>1){		
			Map map = getTaskStatusMap(instance);
			map.put(taskId, Activity.STATUS_COMPLETED);
			setTaskStatusMap(instance, map);
			
			if(map.size()==taskIds.length)
				return true;
			else	
				return false;
		}
		
		return true;
	}

	protected void onReceive(ProcessInstance instance, Object payload) throws Exception {
		if(onComplete(instance, payload) && !isNotificationWorkitem()){//only when the completion logic is ok, fire completion message
			setCompletedRoleMapping(instance, getActualMapping(instance));
			super.onReceive(instance, payload);
		}
	}

	public void setTracingTag(String tag) {
		super.setTracingTag(tag);		
		setMessage("onHumanActivityResult" + getTracingTag());
	}
	
	/* (non-Javadoc)
	 * @see org.uengine.kernel.Activity#getStatus(org.uengine.kernel.ActivityInstance)
	 */
	public String getStatus(ProcessInstance instance) throws Exception{
		String status = super.getStatus(instance);
		
		if(status.equals(Activity.STATUS_RUNNING)){
			Calendar dueDateInCalendar = getDueDate(instance); 
			if(dueDateInCalendar!=null){
				Date dueDate = dueDateInCalendar.getTime();
				Date rightNow = GlobalContext.getNow(instance.getProcessTransactionContext()).getTime();
				if(rightNow.after(dueDate)){
					instance.setStatus(getTracingTag(), Activity.STATUS_TIMEOUT);
				}
			}
		}
		
		return status; 
	}
	
	protected String getExtraMessage(ProcessInstance instance){
		return evaluateContent(instance,getInstruction()!=null ? getInstruction().getText(GlobalContext.DEFAULT_LOCALE) : "").toString();
	}
	
	@Override
	public Map getActivityDetails(ProcessInstance inst, String locale)
			throws Exception {
		Map details = super.getActivityDetails(inst, locale);
		
		try{
			if(getRole()!=null){
				details.put(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.humanactivity.details.role", locale, "role"), getRole().getDisplayName());
				details.put(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.humanactivity.details.actualworker", locale,  "actual worker"), UEngineUtil.getSafeString(""+getActualMapping(inst), "&lt;not set&gt;")/*.getResourceName()*/);
			}
			
			details.put(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.humanactivity.details.duration", locale, "duration"), ""+getDuration(inst)+" day(s)");

		}catch(Exception e){}

		try{
			Calendar calDueDate = getDueDate(inst); 
			String strDueDate = "-";
			if ( calDueDate != null )
			{
				java.text.DateFormat df = new java.text.SimpleDateFormat(
						"yyyy-MM-dd" );
				strDueDate = df.format( calDueDate.getTime() );
			}
			details.put(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.humanactivity.details.duedate", locale, "due date"), strDueDate);
		}catch(Exception e){}
		
		try{
			String[] taskIds = getTaskIds(inst);
			StringBuffer taskIdStr = new StringBuffer();
			if (taskIds != null) {
				for (int i = 0; i < taskIds.length; i++) {
					if (taskIdStr.length() > 0) taskIdStr.append(", ");
					taskIdStr.append(taskIds[i]);
				}
			}
			details.put("task id(s)", taskIdStr.toString());
		}catch(Exception e){}
		
		return details;
	}
	
	protected void afterComplete(ProcessInstance instance) throws Exception {
		saveSnapshotHTML(instance);
		
		//UserManagerBean.addWorkload(getRole().getMapping(instance).getEndpoint(), (-1)*getWorkload());
		super.afterComplete(instance);
		dataMapping(instance);
		fireEventToActivityFilters(instance, "saveAnyway", null);		
	}

	protected void beforeExecute(ProcessInstance instance) throws Exception {
		//String userId = getRole().getMapping(instance).getEndpoint();
		//TODO: [serious] it's little wierd to directly invoke an EJB
		//UserManagerBean.addWorkload(userId, getWorkload());
			//review: task is added before the due date is set.
		//UserManagerBean.addTask(userId, instance.getInstanceId(), getTracingTag());
		//
		super.beforeExecute(instance);
	}

	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);
		
		if(getRole()==null)
			vc.add(getActivityLabel() + "Role is not specified");
		
		if(getTool()==null)
			vc.addWarning(getActivityLabel() + "Tool is not specified");
			
		return vc;
	}

	public void compensate(ProcessInstance instance) throws Exception {
		
		try{
			cancelWorkItem(instance);
		}catch(Exception e){
			instance.addDebugInfo("failed to cancel workitem in the middle of compensation since: "+e.getMessage());
		}

		saveSnapshotHTML(instance);
		super.compensate(instance);
		setTaskIds(instance, null);
	}

	public void skip(ProcessInstance instance) throws Exception {
		cancelWorkItem(instance);

		super.skip(instance);
	}

	public void suspend(ProcessInstance instance) throws Exception {
		//old logic resets all the reservations, worklists...
//		super.suspend(instance);
		
//		cancelWorkItem(instance);
//		setTaskIds(instance, null);
		//end
		
		//from 3.0 it only changes the worklist's status into 'SUSPENDED'
		String[] taskIds = getTaskIds(instance);
		
		if(taskIds!=null){
			ResultPayload rp = new ResultPayload();
			rp.setExtendedValue(new KeyedParameter(KeyedParameter.DEFAULT_STATUS, DefaultWorkList.WORKITEM_STATUS_SUSPENDED));
			
			instance.getWorkList().updateWorkItem(taskIds[0], null, rp.getExtendedValues(), instance.getProcessTransactionContext());
			//end
		}
		instance.setStatus(getTracingTag(), STATUS_SUSPENDED);		
	}
	
	public void reset(ProcessInstance instance) throws Exception{
		super.reset(instance);
		
		cancelWorkItem(instance);
		setDueDate(instance, (Calendar)null);
		setTaskIds(instance, null);
	}
	
	protected void cancelWorkItem(ProcessInstance instance) throws Exception{
		cancelWorkItem(instance, null);
	}
	
	protected void cancelWorkItem(ProcessInstance instance, String status) throws Exception{
		WorkList worklist = (new WorkListServiceLocator()).getWorkList();
		
		KeyedParameter[] parameters = new KeyedParameter[]{new KeyedParameter("status",status)};

		String[] taskIds = getTaskIds(instance);
		
		if(taskIds!=null)
			for(int i=0; i<taskIds.length; i++){
				String taskId = taskIds[i];
					worklist.cancelWorkItem(taskId, parameters, instance.getProcessTransactionContext());
			}
	}
	
	
	
	@Override
	public void reserveActivity(ProcessInstance instance) throws Exception {
		// TODO Auto-generated method stub
		super.reserveActivity(instance);
		reserveWorkItem(instance);
	}
	
	public String[] reserveWorkItem(ProcessInstance instance) throws Exception{
		//if(!getStatus(instance).equals(Activity.STATUS_READY)) return null;
		
		WorkList worklist = instance.getWorkList();
		
		Map kpv = createParameter(instance);
		KeyedParameter[] parameters = KeyedParameter.fromMap(kpv);		

		RoleMapping roleMapping = null;
		try{
			roleMapping = getActualMapping(instance);
			
			UEngineException actualWorkerNotBound = new UEngineException("Actual worker for '"+ getRole() +"' isn't bound yet"); 
			actualWorkerNotBound.setActivity(this);
			actualWorkerNotBound.setInstance(instance);
			if(roleMapping==null) 
				throw actualWorkerNotBound;
			if(roleMapping.getEndpoint()==null) 
				throw actualWorkerNotBound;
			if(roleMapping.getEndpoint().trim().length()==0) 
				throw actualWorkerNotBound;
		}catch(Exception e){
			
			//if(getRole().getRoleResolutionContext()==null)
			throw e;
		}

		String[] taskIds = new String[roleMapping.size()];
		int i=0;
		do{
			taskIds[i++] = worklist.reserveWorkItem(roleMapping.getEndpoint(), parameters, instance.getProcessTransactionContext());				
		}while(roleMapping.next());
		
		setTaskIds(instance, taskIds);

		return taskIds;
	}
	
	public String[] getTaskIds(ProcessInstance instance) throws Exception{
		if (instance == null ) return null; 
		String taskId = (String)instance.getProperty(getTracingTag(), PVKEY_TASKID);	
		String[] taskIds = null;
		if(taskId!=null && taskId.trim().length() > 0)
			taskIds = taskId.split(",");
		else
			taskIds = null;
		
		return taskIds;
	}

	protected void setTaskIds(ProcessInstance instance, String[] taskIds) throws Exception{
		StringBuffer taskId = new StringBuffer();
		if (taskIds != null) {
			for (int i = 0; i < taskIds.length; i++) {
				if (taskId.length() > 0) taskId.append(",");
				taskId.append(taskIds[i]);
			}
		}
		
		instance.setProperty(getTracingTag(), PVKEY_TASKID, taskId.toString());
	}
	
	protected Map getTaskStatusMap(ProcessInstance instance) throws Exception{
		Map statusMap = (Map)instance.getProperty(getTracingTag(), "TASK_STATUS_MAP");
		
		if(statusMap==null)
			return new HashMap();
			
		return statusMap;
	}

	protected void setTaskStatusMap(ProcessInstance instance, Map map) throws Exception{
		instance.setProperty(getTracingTag(), "TASK_STATUS_MAP", (java.io.Serializable)map);
	}
	
	/**
	 * for getting parameters (without instance info) from outside
	 */
	public Map getParameterMap() throws Exception{
		// TODO
		return null;
//		return createParameter(null);
	}

	public void setStartedTime(ProcessInstance instance, Calendar theTime) throws Exception {
		// TODO Auto-generated method stub
		super.setStartedTime(instance, theTime);
		refreshDuration(instance);
	}

	public void setDueDate(ProcessInstance instance, String theTime) throws Exception{
		String[] yearMonthDate = theTime.split("-");
		
		Calendar theCalendar = Calendar.getInstance();
		
		int year = Integer.parseInt(yearMonthDate[0]); 
		int month = Integer.parseInt(yearMonthDate[1]) - 1; 
		int date = Integer.parseInt(yearMonthDate[2]); 
				
		theCalendar.set(year, month, date);
		
		setDueDate(instance, theCalendar);
		
	}

	protected void refreshDuration(ProcessInstance instance) throws Exception{
    }
	
	public void stop(ProcessInstance instance) throws Exception {
		super.stop(instance);
		cancelWorkItem(instance);
	}
	
	public void stop(ProcessInstance instance, String status) throws Exception {
		super.stop(instance, status);
		cancelWorkItem(instance, status);		
	}
	
	public void delegate(ProcessInstance instance, RoleMapping roleMapping) throws Exception{
		delegate(instance, roleMapping, false);
	}
	
	public void delegate(ProcessInstance instance, RoleMapping roleMapping, boolean delegateOnlyForWorkitem) throws Exception{
		
		if(!delegateOnlyForWorkitem){
			Role role = getRole();
			
			roleMapping.setName(role.getName());
			instance.putRoleMapping(roleMapping);
		}
		
		saveSnapshotHTML(instance);
		
		String[] taskIds = getTaskIds(instance);
		
		WorkList wl = (new WorkListServiceLocator()).getWorkList();

		ResultPayload rp = new ResultPayload();
		rp.setExtendedValue(new KeyedParameter(KeyedParameter.DEFAULT_STATUS, DefaultWorkList.WORKITEM_STATUS_DELEGATED));
		rp.setExtendedValue(new KeyedParameter("endDate", DAOFactory.getInstance(instance.getProcessTransactionContext()).getNow().getTime()));
		rp.setExtendedValue(new KeyedParameter(KeyedParameter.DISPATCHINGOPTION, "" + Role.DISPATCHINGOPTION_ALL));
		rp.setExtendedValue(new KeyedParameter("dispatchParam1", ""));
		
		wl.updateWorkItem(taskIds[0], null, rp.getExtendedValues(), instance.getProcessTransactionContext());

		setTaskIds(instance, null);
		executeActivity(instance);

		firePropertyChangeEventToActivityFilters(instance, "roleMapping", roleMapping);
	}
	
	
	public void saveWorkItem(ProcessInstance instance, ResultPayload payload) throws Exception{
		
		savePayload(instance, payload);
		Date now = DAOFactory.getInstance(instance.getProcessTransactionContext()).getNow().getTime();
		
		String[] taskIds = getTaskIds(instance);
		if(taskIds == null || taskIds.length == 0){ 
			addWorkitem(instance, DefaultWorkList.WORKITEM_STATUS_DRAFT);
		}else{ //wl update : flag 'DRAFT'
			WorkList wl = (new WorkListServiceLocator()).getWorkList();

			ResultPayload rp = new ResultPayload();
			rp.setExtendedValue(new KeyedParameter(KeyedParameter.DEFAULT_STATUS, DefaultWorkList.WORKITEM_STATUS_DRAFT));
			
			rp.setExtendedValue(new KeyedParameter("saveDate", now));
			
			wl.updateWorkItem(taskIds[0], null, rp.getExtendedValues(), instance.getProcessTransactionContext());
		}
		
		firePropertyChangeEventToActivityFilters(instance, "saveDate", now);
		firePropertyChangeEventToActivityFilters(instance, "saveEndpoint", payload);
		fireEventToActivityFilters(instance, "saveWorkitem", payload);
		fireEventToActivityFilters(instance, "saveAnyway", payload);

	}

	public HumanActivity getPreviousHumanActivity(ProcessInstance instance) throws Exception{
		Vector actList = getPreviousActivities();
		if(actList ==null) return null;
		final ProcessInstance instanceTemp = instance;
		
		for(int i=0 ; i<actList.size() ; i++){
			Activity actTemp= (Activity)actList.get(i);
			
			ActivityForLoop findingLoop = new ActivityForLoop() {
				public void logic(Activity activity) {
					try {
						if (activity instanceof HumanActivity&&
							activity.STATUS_COMPLETED.equals(activity.getStatus(instanceTemp))) {
							setReturnValue(activity);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
					
			findingLoop.runBackward(actTemp);
			HumanActivity preAct =(HumanActivity)findingLoop.getReturnValue();
			if(preAct!=null) return preAct;
		}
			
		return null;
	}
	
	
	public void afterExecute(ProcessInstance instance) throws Exception {
		
		if (isNotificationWorkitem()) {
			onComplete(instance, new org.uengine.kernel.ResultPayload());
			fireComplete(instance);
		}
		
		super.afterExecute(instance);
	}
	
	protected String createSnapshotHtmlFilePath(ProcessInstance instance , HumanActivity humanActivity) throws Exception {
		ProcessInstance rootInstance = instance.getRootProcessInstance();
		int	year = rootInstance.getProcessDefinition().getStartedTime(rootInstance).get(Calendar.YEAR);

		String filePath = SNAPSHOT_DIRECTORY		+ year
			+ File.separatorChar 					+ rootInstance.getInstanceId()
			+ File.separatorChar 					+  getTaskIds(instance)[0] + ".html";
		
		return filePath;
	}
	
	protected void saveSnapshotHTML(ProcessInstance instance) throws Exception {
		
		if(!"true".equals(GlobalContext.getPropertyString("humanactivity.save.snapshot.html", "false"))) return;

		
		if("defaultHandler".equals(getTool()) || "codiReplyHandler".equals(getTool())) {
			
			boolean wasIsJBoss = "JBOSS".equals(GlobalContext.getPropertyString("was.type", "TOMCAT"));
			HttpServletRequest request = (HttpServletRequest)instance.getProcessTransactionContext().getServletRequest();
			HttpServletResponse response = (HttpServletResponse)instance.getProcessTransactionContext().getServletResponse();
			
			/***********************
			 * Save Snapshot File 
			 **********************/
			request.setAttribute("instanceForSnapshot", instance);
			request.setAttribute("currActivityForSnapshot", this);
			
			RequestDispatcher dis = request.getRequestDispatcher(
					(wasIsJBoss ? GlobalContext.WEB_CONTEXT_ROOT : "") 
					+ "/wih/wihDefaultTemplate/snapshot_showInputForm.jsp");
			
			if("codiReplyHandler".equals(getTool())){
			    dis = request.getRequestDispatcher(
					(wasIsJBoss ? GlobalContext.WEB_CONTEXT_ROOT : "") 
					+ "/wih/codiReplyHandler/snapshot_showInputForm.jsp?isReply=true");
			}
			
			final StringWriter sw = new StringWriter();
			OutputStreamWriter osw = null;
			
			try {
				dis.include(request, new HttpServletResponseWrapper(response){
					public PrintWriter getWriter() throws IOException {	
						return new PrintWriter(sw);
					}
				});
				
				/**************************
				 * Append Tag Div
				 **************************/
				String Tags = request.getParameter("tags");
				if(UEngineUtil.isNotEmpty(Tags)){
					StringBuffer buff = sw.getBuffer();
					
					buff.append("<div id='tags'>");
					buff.append(Tags.replaceAll(";", ",").substring(0,Tags.length()-1));
					buff.append("</div>");
				}
				sw.flush();
				
				request.removeAttribute("instanceForSnapshot");
				request.removeAttribute("currActivityForSnapshot");
				
				File newFile = new File(createSnapshotHtmlFilePath(instance , this));
				File dir = newFile.getParentFile();
				if (!dir.exists()) {
					dir.mkdirs();
				}
				
				osw = new OutputStreamWriter(new FileOutputStream(newFile), GlobalContext.ENCODING);
				osw.write(sw.toString());
			} catch (Exception e) {
				throw e;
			} finally {
				if (osw != null) {
					osw.close();
				}
				if (sw != null) {
					sw.close();
				}
			}
		}
	}
	
	transient String parentEditorId;
	@Hidden
		public String getParentEditorId() {
			return parentEditorId;
		}
		public void setParentEditorId(String parentEditorId) {
			this.parentEditorId = parentEditorId;
		}
	
	MappingContext mappingContext;
	@Face(displayName="$dataMapping")
	@Hidden
		public MappingContext getMappingContext() {
			return mappingContext;
		}
		public void setMappingContext(MappingContext mappingContext) {
			this.mappingContext = mappingContext;
		}
	
	protected void dataMapping(ProcessInstance instance) throws Exception {
	}
	
}
package org.uengine.kernel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.uengine.modeling.resource.Describable;
import org.uengine.modeling.HasThumbnail;
import org.uengine.processmanager.ProcessManagerFactoryBean;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.processmanager.ProcessTransactionContext;
import org.uengine.util.ActivityFor;
import org.uengine.util.ActivityForLoop;
import org.uengine.util.UEngineUtil;

/**
 * @author Jinyoung Jang
 */

@Face(displayName="BPMN")
public class ProcessDefinition extends ScopeActivity implements Serializable, IDefinitionModel, Describable, HasThumbnail {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	private transient static ActivityFilter[] defaultActivityFilters;
	
	transient public final static int VERSIONSELECTOPTION_CURRENT_PROD_VER			=0; 
	transient public final static int VERSIONSELECTOPTION_PROD_VER_AT_INITIATED_TIME=2; 
	transient public final static int VERSIONSELECTOPTION_PROD_VER_AT_DESIGNED_TIME	=1; 

	transient public final static int VERSIONSELECTOPTION_JUST_SELECTED				=3; 
	

																	//|	 in-memory instance	 |  definition archive	
/*	public static final int PROCESS_TYPE_WORKFLOW = 1;				//|	 X					 |	O/X
	public static final int PROCESS_TYPE_INTEGRATION = 2;			//|	 O					 |	X
	public static final int PROCESS_TYPE_PAGEFLOW = 3;				//|	 X					 |	O/X*/	
	
	public static final String PVKEY_QUEUINGMECH = "_queuingmech";
//	static Hashtable messageListenerVectors = new Hashtable();
		protected static transient Role MESSENGER_SERVICE_ROLE;
		protected static transient Role EMAIL_SERVICE_ROLE;
//	protected static Role[] defaultRoles
//		=new Role[]{
//			EMAIL_SERVICE_ROLE = Role.forName(
//				EMailActivity.MAIL_SERVICE,
//				"http://localhost/axis/services/EMailServer"
//			),
//			MESSENGER_SERVICE_ROLE = Role.forName(
//				MessengerActivity.MESSENGER_SERVICE, 
//				"http://localhost/axis/services/MSNMessengerService"
//			)
//		};
	
//	protected static ServiceDefinition[] defaultServiceDefinitions
//	= new ServiceDefinition[defaultRoles.length];
//	static{
//		String stubPkgs[] = new String[]{ 
//			"org.uengine.webservices.emailserver",
//			"org.uengine.webservices.msnmessenger"
//		};
//		
//		for(int i=0; i<defaultRoles.length; i++){
//			Role theRole = defaultRoles[i];
//			theRole.setAskWhenInit(false);
//			ServiceDefinition sd = new ServiceDefinition();
//			
//			sd.setName(theRole.getName());
//			sd.setWsdlLocation(theRole.getDefaultEndpoint() + "?wsdl");
//			sd.setStubPackage(stubPkgs[i]);
//						
//			defaultServiceDefinitions[i] = sd;			
//			theRole.setServiceType(sd);	
//		}		
//	}
	
	protected static transient Role[] defaultRoles;
	protected static transient ServiceDefinition[] defaultServiceDefinitions;
	
	static{
		defaultRoles = new Role[]{};		
		defaultServiceDefinitions = new ServiceDefinition[]{};
	}		

	transient Hashtable wholeChildActivities/* = new Hashtable()*/;
	private String thumbnailURL;

	public Activity getActivity(String tracingTag){
			if("".equals(tracingTag)) return this;
			
			if(wholeChildActivities == null){
				registerToProcessDefinition(false, false);
			}
			
			if(!wholeChildActivities.containsKey(tracingTag))
				return null;//throw new RuntimeException( new UEngineException("No such child activity where tracing tag='" + tracingTag+"' in the process definition '" + getName() + "(" + getBelongingDefinitionId() + "@" + getId() + ")'"));
			
			return (Activity)wholeChildActivities.get(tracingTag);
		}
		/*public synchronized void addChildActivity(Activity child){
			super.addChildActivity(child);
			
			registerActivity(child);
		}*/

	ServiceDefinition[] serviceDefinitions;

		public ServiceDefinition[] getServiceDefinitions(){
			return serviceDefinitions;
		}
		
		public void setServiceDefinitions(ServiceDefinition[] sds){
			serviceDefinitions = sds;
			
			bXmlTypesNeedToBeRefreshed = true;
		}
		// this method will be seldom called
		public void addServiceDefinition(ServiceDefinition sd){
			ServiceDefinition[] sds = getServiceDefinitions();
			ServiceDefinition[] newSDs = new ServiceDefinition[sds.length+1];	
				
			for(int i=0; i<sds.length; i++){
				if(sds[i].equals(sd)) return;
				newSDs[i] = sds[i];
			}
				
			newSDs[sds.length] = sd;
			setServiceDefinitions(newSDs);			
		}

	boolean isGlobal;
	@Hidden
		public boolean isGlobal() {
			return isGlobal;
		}
		public void setGlobal(boolean isGlobal) {
			this.isGlobal = isGlobal;
		}


	String defaultFlowchartViewType = "swimlane";
		public String getDefaultFlowchartViewType() {
			return defaultFlowchartViewType;
		}
	
		public void setDefaultFlowchartViewType(String viewType) {
			this.defaultFlowchartViewType = viewType;
		}
	
	String defaultFlowchartViewOption = "vertical";
		public String getDefaultFlowchartOption() {
			return defaultFlowchartViewOption;
		}
	
		public void setDefaultFlowchartViewOption(String viewOption) {
			this.defaultFlowchartViewOption = viewOption;
		}

	boolean isAbstract;
		public boolean isAbstract() {
			return isAbstract;
		}
		public void setAbstract(boolean isAbstract) {
			this.isAbstract = isAbstract;
		}

	int duration = 10;
		public int getDuration() {
			return duration;
		}
		public void setDuration(int duration) {
			this.duration = duration;
		}
	
	int simulationInstanceCount = 10;
		public int getSimulationInstanceCount() {
			return simulationInstanceCount;
		}
		public void setSimulationInstanceCount(int simultationInstanceCount) {
			this.simulationInstanceCount = simultationInstanceCount;
		}

	int simulationInputFrequency = 10;
		public int getSimulationInputFrequency() {
			return simulationInputFrequency;
		}
		public void setSimulationInputFrequency(int simulationInputFrequency) {
			this.simulationInputFrequency = simulationInputFrequency;
		}
	
	int simulationInputFrequencyDeviation;
		public int getSimulationInputFrequencyDeviation() {
			return simulationInputFrequencyDeviation;
		}
		public void setSimulationInputFrequencyDeviation(
				int simulationInputFrequencyDeviation) {
			this.simulationInputFrequencyDeviation = simulationInputFrequencyDeviation;
		}
		
	SimulationResource[] simulationResources;
		public SimulationResource[] getSimulationResources() {
			return simulationResources;
		}
		public void setSimulationResources(SimulationResource[] simulationResources) {
			this.simulationResources = simulationResources;
		}

		
	transient boolean bXmlTypesNeedToBeRefreshed = true;


	MessageDefinition[] messageDefinitions;
		public MessageDefinition[] getMessageDefinitions() {
			return messageDefinitions;
		}
	
		public void setMessageDefinitions(MessageDefinition[] definitions) {
			messageDefinitions = definitions;
		}
		
	ArrayList revisionInfoList;	
	
		public ArrayList getRevisionInfoList() {
			return revisionInfoList;
		}
		@Deprecated
		public void setRevisionInfoList(ArrayList revisionInfoList) {
			this.revisionInfoList = revisionInfoList;
		}
		@Deprecated
		public void addRevisionInfo(RevisionInfo revisionInfo){
			if(revisionInfoList==null) revisionInfoList = new ArrayList();
			revisionInfoList.add(revisionInfo);
		}
		
		public void clearWithOutLastRevisionInfo() {
			if (revisionInfoList != null) {
				RevisionInfo revisionInfo = (RevisionInfo) revisionInfoList.get(revisionInfoList.size() - 1);
				revisionInfoList.clear();
				revisionInfoList.add(revisionInfo);
			}
		}

	String wsdlLocation;
		public String getWsdlLocation() {
			return wsdlLocation;
		}
		public void setWsdlLocation(String string) {
			wsdlLocation = string;
		}

	String endpoint;
		public String getEndpoint() {
			return endpoint;
		}
		public void setEndpoint(String string) {
			endpoint = string;
		}

	boolean archive = true;
		public boolean isArchive() {
			return archive;
		}
		public void setArchive(boolean boolean1) {
			archive = boolean1;
		}

	int uEngineVersion; //for backward compatibility
		public int getUEngineVersion() {
			return uEngineVersion;
		}
		public void setUEngineVersion(int i) {
			uEngineVersion = i;
		}
		
	String id;
		public String getId() {
			return id;
		}
		public void setId(String string) {
			id = string;
		}
		
	int version;
		public int getVersion() {
			return version;
		}
		public void setVersion(int i) {
			version = i;
		}
	
	String belongingDefinitionId;
		public String getBelongingDefinitionId() {
			return belongingDefinitionId;
		}
		public void setBelongingDefinitionId(String string) {
			belongingDefinitionId = string;
		}

	ActivityFilter[] activityFilters;
		public ActivityFilter[] getActivityFilters() {
			
			if(!GlobalContext.isDesignTime() && defaultActivityFilters == null){
				String defaultActivityFilterClsNames = GlobalContext.getPropertyString("defaultactivityfilters","org.uengine.components.activityfilters.InstanceDataAppendingActivityFilter");
				
				if(org.uengine.util.UEngineUtil.isNotEmpty(defaultActivityFilterClsNames)){
					String [] filterClsNames = defaultActivityFilterClsNames.split(",");
					defaultActivityFilters = new ActivityFilter[filterClsNames.length];
					
					int j=0;
					for(int i=0; i<filterClsNames.length; i++){
						String filterClsName = filterClsNames[i].trim();
						
						try{
//							defaultActivityFilters[j] = (ActivityFilter)Thread.currentThread().getContextClassLoader().loadClass(filterClsName).newInstance();
							defaultActivityFilters[j] = (ActivityFilter)GlobalContext.loadClass(filterClsName).newInstance();
							j++;
						}catch(Exception e){
							new RuntimeException("Failed to load Activity Filter ["+filterClsName+"]: ", e);
						}
					}
					
				}else{
					defaultActivityFilters = new ActivityFilter[]{};
				}
				
			}
			
			if(defaultActivityFilters==null) defaultActivityFilters = new ActivityFilter[]{};
			if(activityFilters==null) return defaultActivityFilters;
			
			ActivityFilter[] result = new ActivityFilter[defaultActivityFilters.length + activityFilters.length];
			System.arraycopy(defaultActivityFilters, 0, result, 0, defaultActivityFilters.length);
			System.arraycopy(activityFilters, 0, result, defaultActivityFilters.length, activityFilters.length);
			
			return result;
			
			//return activityFilters;
		}
		public void setActivityFilters(ActivityFilter[] filters) {
			activityFilters = filters;
		}

	/*tring title;
		public String getTitle() {
			if(title==null)
				setTitle(getName());
			
			return title;
		}
		public void setTitle(String string) {
			title = string;
		}
*/
	boolean isAdhoc;
		public boolean isAdhoc() {
			return isAdhoc;
		}	
		public void setAdhoc(boolean b) {
			isAdhoc = b;
		}
		
	String shortDescription;
		public String getShortDescription() {
			return shortDescription;
		}
		public void setShortDescription(String string) {
			shortDescription = string;
		}

	boolean initiateByFirstWorkitem = false;
		public boolean isInitiateByFirstWorkitem() {
			return initiateByFirstWorkitem;
		}
		public void setInitiateByFirstWorkitem(boolean b) {
			initiateByFirstWorkitem = b;
		}
		
//	ListField [] worklistFields;
//		public ListField[] getWorklistFields() {
//			return worklistFields;
//		}
//		public void setWorklistFields(ListField[] worklistFields) {
//			this.worklistFields = worklistFields;
//		}

	transient HumanActivity initiatorHumanActivity;
	/*public HumanActivity getInitiatorHumanActivity(){
		if(initiatorHumanActivity!=null)
			return initiatorHumanActivity;
		
		if(!isInitiateByFirstWorkitem())
			throw new RuntimeException("this process definition is not allowed to be initiated by the first workitem.");
		
		final Vector willCache = new Vector();
		
		ActivityFor findingLoop = new ActivityFor(){
			public void logic(Activity activity){
				if(activity instanceof HumanActivity){
					stop(activity);
				}else if(activity instanceof SubProcessActivity){
					

					if(getProcessTransactionContext()==null) stop(null); 
					
					SubProcessActivity spAct = (SubProcessActivity)activity;
					
					try{
						ProcessManager pm = ProcessManagerFactory.create(getProcessTransactionContext());					
						String versionId = pm.getProductionVersionId(spAct.getDefinitionId());		
						ProcessDefinition spDef = ProcessDefinitionFactory.getDefinitionWithVersionId(versionId, getProcessTransactionContext());
						
						if(spDef.isInitiateByFirstWorkitem()){
							willCache.add("no");
							stop(spDef.getInitiatorHumanActivity());
						}
					}catch(Exception e){
						throw new RuntimeException(e);
					}	
				}
			}
		};
		
		findingLoop.run(this);

		if(willCache.size()==0)
			this.initiatorHumanActivity = (HumanActivity)findingLoop.getReturnValue(); 
		
		return initiatorHumanActivity;
	}*/
	
	ListField instanceListFields[];
		public ListField[] getInstanceListFields() {
			return instanceListFields;
		}
		public void setInstanceListFields(ListField[] instanceListFields) {
			this.instanceListFields = instanceListFields;
		}
	
	ListField workListFields[];
		public ListField[] getWorkListFields() {
			return workListFields;
		}
		public void setWorkListFields(ListField[] workListFields) {
			this.workListFields = workListFields;
		}
	
	transient String currentLocale;
		public String getCurrentLocale() {
			return currentLocale;
		}
		public void setCurrentLocale(String currentLocale) {
			this.currentLocale = currentLocale;
		}
		
	boolean isVolatile;
		public boolean isVolatile() {
			return isVolatile;
		}
		public void setVolatile(boolean isVolatile) {
			this.isVolatile = isVolatile;
		}

	public ActivityReference getInitiatorHumanActivityReference(final ProcessTransactionContext ptc){
		if(initiatorHumanActivity!=null){
			ActivityReference ref = new ActivityReference();
			ref.setActivity(initiatorHumanActivity);
			ref.setAbsoluteTracingTag(initiatorHumanActivity.getTracingTag());
			
			return ref;
		}
	
		ActivityReference activityReference = super.getInitiatorHumanActivityReference(ptc);

		//this.initiatorHumanActivity = (activityReference==null ? null : (HumanActivity)activityReference.getActivity());

		return activityReference;
	}
		
	Calendar modifiedDate;
		public Calendar getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Calendar calendar) {
			modifiedDate = calendar;
		}
		
/*	int processType;
		public int getProcessType() {
			return processType;
		}
		public void setProcessType(int i) {
			processType = i;
		}*/
		
	long activitySequence;
		public long getNextActivitySequence() {
			return (++activitySequence);
		}
		public long getActivitySequence() {
			return activitySequence;
		}
		public void setActivitySequence(long l) {
			activitySequence = l;
		}

	public Calendar getDueDate(ProcessInstance instance) throws Exception{
		//Calendar dueDate = (Calendar)instance.get(getTracingTag(), HumanActivity.PVKEY_DUEDATE);
		return instance.getDueDate();
	}
	
	public void setDueDate(ProcessInstance instance, Calendar dueDate) throws Exception{
		//instance.set(getTracingTag(), HumanActivity.PVKEY_DUEDATE, dueDate);
		instance.setDueDate(dueDate);
	}
	

////////////////////////////

	public ProcessDefinition(){
		super();
//		setName("noname");
		
//		setRoles(defaultRoles);
		setServiceDefinitions(defaultServiceDefinitions);
		
		
		setName(GlobalContext.getLocalizedMessage("activitytypes.org.uengine.kernel.processdefinition.untitled", "Noname"));
		
		setDuration(10);

		setInitiateByFirstWorkitem(true);
		setAdhoc(true);
		
		//role		
		if(GlobalContext.isDesignTime()){
			Role drafter = new Role();
			drafter.setName("Initiator");
			drafter.setAskWhenInit(false);
	
			setRoles(new Role[]{drafter});
		}
		
		//filter		
//		DefaultActivityFilter filter = new DefaultActivityFilter();

//		filter.setUseScript(false);
//		filter.setFilterClass("hanwha.bpm.activityfilters.AlarmActivityFilter");
//		setActivityFilters(new ActivityFilter[]{filter});		
		
	}
	
	public ProcessInstance createInstance(String name, Map options) throws Exception{		
		ProcessInstance activityInstance = AbstractProcessInstance.create(this, name, options);
		//activityInstance.setProcessDefinition(this);
		//activityInstance.create();
				
		createInstance(activityInstance);
		
		return activityInstance;
	}

//-------------- private method -----------------------

	protected boolean registerActivity(Activity act, boolean autoTagging){
		return registerActivity(act, autoTagging, false);
	}
	
	protected boolean registerActivity(Activity act, boolean autoTagging, boolean checkCollision){
		
		if(wholeChildActivities==null) wholeChildActivities = new Hashtable();
		
		String tracingTagOfAct = act.getTracingTag();
		if(tracingTagOfAct==null){
			if(autoTagging){
				act.setTracingTag(""+getNextActivitySequence());
			}else
				throw new RuntimeException(new UEngineException("This definition is corrupt. One of child activity's tracingtag is null."));
		}
		
		if(checkCollision && wholeChildActivities.containsKey(act.getTracingTag())){
			return false;
		}
		
		wholeChildActivities.put(act.getTracingTag(), act);
		//TODO: tracingtag
//		if(act instanceof ComplexActivity){
//			int i=0;
//			for(Enumeration enum = ((ComplexActivity)act).getChildActivities().elements(); enum.hasMoreElements(); ){
//				Activity childAct = (Activity)enum.nextElement();
//					
//				/*if(autoTagging)
//					childAct.setTracingTag(act.getTracingTag() + "_" + (i++));*/
//				
//				//TODO: removable
//				childAct.setParentActivity(act);
//					
//				boolean ok = registerActivity(childAct, autoTagging, checkCollision);
//				if(checkCollision && !ok) return false;
//			}
//		}
		
		return true;
	}
	  
	protected void registerActivity(Activity act){
		registerActivity(act, true);
	}
	
	static public ProcessDefinition getDefinition(String defVerId, ProcessTransactionContext ptc) throws Exception{
		
		//if not cached, find in files
/*		if(!processDefinitions.containsKey(processDefinition)){
			try{
				java.io.FileInputStream fi = new java.io.FileInputStream(processDefinition + ".Bean" );				
				Serializer ser = new org.uengine.components.serializers.BeanSerializer();							     
				ProcessDefinition pd = (ProcessDefinition)ser.deserialize(fi);
				
				processDefinitions.put(processDefinition, pd);
System.out.println("found and deserialized. process definition will be used is from a file.");

System.out.println("length of childs : " + pd.getChildActivities().size());
			}catch(Exception e){System.out.println("Although tried to find process in files, failed");}
		}
		
		return (ProcessDefinition)processDefinitions.get(processDefinition);*/
				
		return ProcessDefinitionFactory.getInstance(ptc).getDefinition(defVerId);
	}
	
	public static String[] getDefinitionNames() throws Exception{
		Vector nameVector = new Vector();
		
		File f = new File(".");			
		File[] fs = f.listFiles();
		
		if(fs!=null)
		for( int i=0; i< fs.length; i++){			
		
			//The filename itself is the name of process
			
			if( fs[i].getName().endsWith(".Bean")){
				nameVector.add(
					fs[i].getName().substring(0, fs[i].getName().indexOf("."))
				);
			}
		}
		
		String[] names = new String[nameVector.size()];
		nameVector.toArray(names);
		
		return names;
	}

	public void addMessageListener(ProcessInstance instance, MessageListener ml) throws Exception{
		ml.beforeRegistered(instance);

		addMessageListener(ml.getMessage(), instance, ml.getTracingTag());

		ml.afterRegistered(instance);
	}

	protected void addMessageListener(String message, ProcessInstance instance, String scope) throws Exception{
		//review: change to JMS subscriber
		
//		instance.set("", VAR_PENDING_MESSAGE, new Boolean(false));
		instance.addMessageListener(message, scope);
/*		if (!messageListenerVectors.containsKey(message))
			messageListenerVectors.put(message, new Vector());
		else
			removeMessageListener(message, instance, scope); //void duplicated subscription
			
System.out.println("ProcessDefinition::addMessageListener.message = " + message);
		
		Vector listeners = (Vector)messageListenerVectors.get(message);
		
		listeners.add(new Object[]{instance, scope});*/
		
	}
	
	protected void removeMessageListener(String message, ProcessInstance instance, String scope) throws Exception{
		/*		if (!messageListenerVectors.containsKey(message)) return;
					
				Vector listeners = (Vector)messageListenerVectors.get(message);
				
				Vector removeTargets = new Vector();
				for(Enumeration enum = listeners.elements(); enum.hasMoreElements(); ){
					Object[] context = (Object[]) enum.nextElement();
					ActivityInstance subscriber = (ActivityInstance)context[0];						
					String contextScope = (String)context[1];
					
					if(subscriber.getInstanceId().equals(instance.getInstanceId()) && contextScope.equals(scope)){
						removeTargets.add(context);
		System.out.println("found duplicated message subscription!: message=" +  message +", instanceid=" + instance.getInstanceId() + ", scope=" + scope);
					}
				}
				
				listeners.removeAll(removeTargets);*/
				
		instance.removeMessageListener(message, scope);
	}
	
	public void removeMessageListener(ProcessInstance instance, MessageListener ml) throws Exception{
		removeMessageListener(ml.getMessage(), instance, ml.getTracingTag());

		ml.afterUnregistered(instance);
	}
	
	//TODO: hotspot
	protected Object fireMessage(String message, final ProcessInstance instance, Object payload, boolean payloadIsXML) throws Exception{
		//review: use JMS or wake-up mechanism in turn
		boolean ack = false;
//		for(int i=0; !ack && i<10; i++){
			System.out.println("ProcessDefinition::fireMessage.message = " + message);
//			if (!messageListenerVectors.containsKey(message)) return;
			Vector subscribedScopes = (Vector)instance.getMessageListeners(message);
			
			if(subscribedScopes!=null)
			for(Enumeration enumeration = subscribedScopes.elements(); enumeration.hasMoreElements(); ){
				String scope = (String) enumeration.nextElement();
				System.out.println(" 	distribute message to " + scope);			
	/*			if(payloadIsXML){
					Activity receiveAct = getActivity(scope);
		
					if(receiveAct instanceof ReceiveActivity){
						ProcessVariable outPV = ((ReceiveActivity)receiveAct).getOut();					
						ProcessVariableDescriptor pvd = getProcessVariableDescriptor(outPV.getName());					
						QName qname = pvd.getQName();
			
						Serializer ser = GlobalContext.getSerializer(qname);					
						Object objPayload = ser.deserialize(new java.io.StringBufferInputStream((String)payload));
						payload = objPayload;
					}
				}*/

				Activity targetActivity = getActivity(scope);
				if(!(targetActivity instanceof MessageListener)) continue;
				
				MessageListener activityAsMessageListener = (MessageListener)targetActivity;
//				if( message == null ){
//					ack = true;
//				}
				//if(message == null || message != null && message.equals(activityAsMessageListener.getMessage())){
				if(message == null || message != null && message.equals(activityAsMessageListener.getMessage())){
					if( payload instanceof EventMessagePayload){
						if(targetActivity.getName().equals(((EventMessagePayload) payload).getEventName())){
							try{
								if(activityAsMessageListener.onMessage(instance, payload))
									ack = true;
							}catch(Exception e){
								
								targetActivity.fireFault(instance, e);
								
								//TODO: what's happening?
								throw e;
							}
						}
					}else{
						if(activityAsMessageListener.onMessage(instance, payload))
							ack = true;
					}
				}
			}
			
/*			if(!ack)
				Thread.sleep(1000*i);		
		}*/

		/*if(!ack){
			fireMessage(message, instance, payload, payloadIsXML);
		}*/
//		if(!ack)
//			throw new UEngineException("There is no subscribed listener for the message", "Try again or check your message");
			
		//review: if there is a synchronized receiver, it should return a value.
		return null;
	}
	
	public Object fireMessage(String message, ProcessInstance instance, Object payload) throws Exception{
		return fireMessage(message, instance, payload, false);
	}
	
	public Object fireMessageXML(String message, ProcessInstance instance, String payload) throws Exception{
		return fireMessage(message, instance, payload, true);
	}
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
		if(command.equals(ACTIVITY_DONE) || command.equals(ACTIVITY_SKIPPED)){
			returnToMainProcess(instance, command.equals(ACTIVITY_SKIPPED));
		}else if(command.equals(CHILD_FAULT)){
			if(!isFaultTolerant())
				stop(instance); // stop the process instance there

			reportFaultToMainProcess(instance, (FaultContext) payload);
		}
		
		super.onEvent(command, instance, payload);
	}

	protected void reportFaultToMainProcess(ProcessInstance instance, FaultContext e) throws Exception {
		//if this process is subprocess, notify that it is completed to the main process.
		String parentProcess = (String) instance.getMainProcessInstanceId();
		if (parentProcess != null && !parentProcess.equals("null")) {

			String returningTracingTag = (String) instance.getMainActivityTracingTag();

			Hashtable options = new Hashtable();
			options.put("ptc", instance.getProcessTransactionContext());

			ProcessInstance parentInstance = AbstractProcessInstance.create().getInstance(parentProcess, options);
			ProcessDefinition parentDefinition = parentInstance.getProcessDefinition();

			Activity callActivity = parentDefinition.getActivity(returningTracingTag);

			callActivity.fireFault(parentInstance, e);
		}
	}

	public void returnToMainProcess(ProcessInstance instance) throws Exception{
		returnToMainProcess(instance, false);
	}
	
	
	protected void returnToMainProcess(ProcessInstance instance, boolean skipped) throws Exception{
		//if this process is subprocess, notify that it is completed to the main process.
		String returningProcess = (String)instance.getMainProcessInstanceId();
		if(returningProcess!=null && !returningProcess.equals("null")){
			
			String returningTracingTag = (String)instance.getMainActivityTracingTag();
			
			Hashtable options = new Hashtable();
			options.put("ptc", instance.getProcessTransactionContext());
			
			ProcessInstance returningInstance = AbstractProcessInstance.create().getInstance(returningProcess, options);
			ProcessDefinition returningDefinition = returningInstance.getProcessDefinition();
							
			SubProcessActivity returningActivity = (SubProcessActivity)returningDefinition.getActivity(returningTracingTag);
			if(returningActivity == null)
				throw new UEngineException("subprocess activity is not found. maybe event handling subprocess is set having to returned.");
				
			returningActivity.onSubProcessReturn(returningInstance, instance, skipped);
			
		}
	}
		
	public void afterDeserialization(){
		
//			EventHandler[] eventHandlers = super.getEventHandlers();    //anyhow now there's no process definition having its event handlers since the process designer isn't allowed to add event handlers.

		setActivityFilters(null);

		super.afterDeserialization();	
		//TODO: tuning point : does it need to load the hashmap again?	
		registerToProcessDefinition(false, false);

		//healTracingTagCollision();

		//TODO: tempororaly disabled
		/*
		for(int i=0; i<defaultRoles.length; i++)
			addRole(defaultRoles[i]);		
		for(int i=0; i<defaultServiceDefinitions.length; i++)
			addServiceDefinition(defaultServiceDefinitions[i]);*/		

/*		for(int i=0; i<processVariableDescriptors.length; i++)
			ProcessVariable.declareProcessVariable(processVariableDescriptors[i]);*/		
		
	}
	
	public void flowControl(String command, ProcessInstance instance, String tracingTag) throws Exception{
		String status = instance.getStatus(tracingTag);
		
		//TODO: dirty read might be possible
		if(command.equals("resume")){
			if(isResumable(status))
				getActivity(tracingTag).resume(instance);
			else System.out.println("resume cancelled...");
		}
		else if(command.equals("suspend")){
			if(isSuspendable(status))
				getActivity(tracingTag).suspend(instance);
			else System.out.println("suspend cancelled...");
		}
		else if(command.equals("skip")){
			if(isSkippable(status))
				getActivity(tracingTag).skip(instance);
			else System.out.println("skip cancelled...");
		}
		else if(command.equals("compensate")){
			if(isCompensatable(status))
				getActivity(tracingTag).compensate(instance);	
			else System.out.println("compensate cancelled...");
		}
		else if(command.equals("compensateTo")){
			if(status.equals(STATUS_COMPLETED) || status.equals(STATUS_SKIPPED))
				getActivity(tracingTag).compensateToThis(instance);	
			else System.out.println("compensateTo cancelled...");
		}
	}

	public String getTracingTag() {
		return ""; //a process definition should be the root
	}
	
	public void beforeSerialization() {
		super.beforeSerialization();
		
		if(GlobalContext.isDesignTime()){
			//cache the initiator human activity
			if(isInitiateByFirstWorkitem()){
				getInitiatorHumanActivityReference(null);
			}
		
			registerToProcessDefinition(true, false);
		}

		setActivityFilters(null);
	}
	


	public ValidationContext validate(Map options) {
		final ValidationContext ctx = super.validate(options);
		
		/*if(isInitiateByFirstWorkitem()){
			HumanActivity initiatorHumanActivity = getInitiatorHumanActivity();
			if(initiatorHumanActivity==null)
				ctx.add("Although this definition is set to be initiated by the first workitem, there's no initiating human activity.");
		}*/
		
		if(wholeChildActivities!=null)
			wholeChildActivities.clear();
		
		if(!registerActivity(this, true, true)){
			ctx.add("Activity id (tracingtag) collision is detected.");
		}
		
		final HashMap tracingTagMap = new HashMap();
		ActivityForLoop findingLoopForTracingTagCollision = new ActivityForLoop(){

			public void logic(Activity activity) {
				if(tracingTagMap.containsKey(activity.getTracingTag())){
					Activity originalTTUser = (Activity)tracingTagMap.get(activity.getTracingTag());
					ctx.add(activity.getActivityLabel() + " TracingTag collision with " + originalTTUser.getActivityLabel() + ". You should choose another tag number for this activity (Change the definition source directly code in the 'XPD' Tab).");
				}else 
					tracingTagMap.put(activity.getTracingTag(), activity);
			}			
			
		};
		
		findingLoopForTracingTagCollision.run(this);
		
		
		if(getRoles()!=null) {
			Role[] roles = getRoles();
			for(int i=0; i<roles.length; i++){
				if(!UEngineUtil.isNotEmpty(roles[i].getName())){
					ctx.add("Role Id for ["+ roles[i] + "] should be provided.");
				}
			}
		}
		
		if(processVariableDescriptors!=null)
		for(int i=0; i<processVariableDescriptors.length; i++){
			if(!UEngineUtil.isNotEmpty(processVariableDescriptors[i].getName())){
				ctx.add("Process Variable Id for ["+ processVariableDescriptors[i] + "] should be provided.");
			}
		}

		return ctx;
	}


	public void reset(ProcessInstance instance) throws Exception {
		super.reset(instance);
		instance.setStatus(Activity.STATUS_READY);
	}

	public void stop(ProcessInstance instance) throws Exception {
		stop(instance, Activity.STATUS_STOPPED);
	}
	
	public void stop(ProcessInstance instance,String status) throws Exception {
		super.stop(instance,status);
	}
	
	public static ProcessDefinition create(){ 
		try{
		
			return (ProcessDefinition) Thread.currentThread().getContextClassLoader().loadClass(GlobalContext.getPropertyString("processdefinition.class", "org.uengine.kernel.ProcessDefinition")).newInstance();
			
		}catch(Exception e){
			return new ProcessDefinition();
		}
	}
	
//	public String getQueuingMechanism(ProcessInstance instance){
//		try{
//			return (String)instance.get("", PVKEY_QUEUINGMECH);
//		}catch(Exception e){
//			return null;
//		}
//	}
//
//	public void setQueuingMechanism(ProcessInstance instance, String val){
//		try{
//			instance.set("", PVKEY_QUEUINGMECH, val);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}	
	
	public String getQueuingMechanism(ProcessInstance instance) {
		return QUEUINGMECH_SYNC;
	}

	public void setQueuingMechanism(ProcessInstance instance, String val) {
	}
	
	private String alias;
		public String getAlias() {
			return alias;
		}
		public void setAlias(String value) {
			this.alias = value;
		}

	public static String[] splitDefinitionAndVersionId(String fullDefinitionId){
		String versionId = null;
		String definitionId = null;
		if(fullDefinitionId!=null && fullDefinitionId.indexOf("@") > -1){
			String [] defIdAndVersionId = fullDefinitionId.split("@");
			definitionId = defIdAndVersionId[0];
			versionId = defIdAndVersionId[1];
		}else{
			definitionId = fullDefinitionId;
		}
		
		return new String[]{definitionId, versionId};
	}

	public static String getDefinitionVersionId(ProcessInstance instance, String definitionId, int versionSelectionOption, ProcessDefinition definition) throws Exception{
		ProcessManagerRemote pm = null;

		if(instance!=null)
			pm = instance.getProcessTransactionContext().getProcessManager();
		else
			pm = (new ProcessManagerFactoryBean()).getProcessManagerForReadOnly();

		return getDefinitionVersionId(instance, pm, definitionId, versionSelectionOption, definition);
	}

	public static String getDefinitionVersionId(ProcessManagerRemote pm, String definitionId, int versionSelectionOption, ProcessDefinition definition) throws Exception{
		return getDefinitionVersionId(null, pm, definitionId, versionSelectionOption, definition);
	}

	public static String getDefinitionVersionId(ProcessInstance instance, ProcessManagerRemote pm, String definitionId, int versionSelectionOption, ProcessDefinition definition) throws Exception{
		String versionId = null;

		String [] defIdAndVersionId = splitDefinitionAndVersionId(definitionId);
		definitionId = defIdAndVersionId[0];
		versionId = defIdAndVersionId[1];
		
		try{
			//need re-resolution for definition id if the definition id is an alias
			if(definitionId.startsWith("[") && definitionId.endsWith("]")){
				String definitionAlias = definitionId.substring(1, definitionId.length()-1);
				definitionId = pm.getProcessDefinitionIdByAlias(definitionAlias);
			}

			switch(versionSelectionOption){
			case VERSIONSELECTOPTION_CURRENT_PROD_VER:
				versionId = pm.getProcessDefinitionProductionVersion(definitionId);
				break;
			case VERSIONSELECTOPTION_PROD_VER_AT_DESIGNED_TIME:
				//if(instance==null) throw new UEngineException("")
				versionId = pm.getProductionVersionIdAtThatTime(definitionId, instance.getRootProcessInstance().getProcessDefinition().getModifiedDate().getTime());
				break;
			case VERSIONSELECTOPTION_PROD_VER_AT_INITIATED_TIME:
				versionId = pm.getProductionVersionIdAtThatTime(definitionId, definition.getStartedTime(instance).getTime());
				break;
			case VERSIONSELECTOPTION_JUST_SELECTED:
				if(versionId==null)
					throw new UEngineException("Even though the sub process activity is set to use the sub processes' definition version with the just selected one, the version is not explicitly set.");
				break;
			}

			if(versionId == null){
				versionId = pm.getProcessDefinitionProductionVersion(definitionId);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
			try{
				versionId = pm.getFirstProductionVersionId(definitionId);
			}catch(Exception ex){
				ex.printStackTrace();
				versionId = pm.getProcessDefinitionProductionVersion(definitionId);
			}
		}finally{
			try{
				if(instance==null) pm.remove();
			}catch(Exception e){};
		}
		
		return versionId;
	}

	protected void beforeExecute(ProcessInstance instance) throws Exception {
		super.beforeExecute(instance);
		Calendar dueDate = getDueDate(instance);
		if(dueDate==null)
			setDueDate(instance, instance.calculateDueDate(GlobalContext.getNow(instance.getProcessTransactionContext()), getDuration()));
	
	}

	String processDesignerInstanceId;
		public String getProcessDesignerInstanceId() {
			return processDesignerInstanceId;
		}
		public void setProcessDesignerInstanceId(String processDesignerInstanceId) {
			this.processDesignerInstanceId = processDesignerInstanceId;
		}
	Object GraphicInfo;
		public Object getGraphicInfo() {
			return GraphicInfo;
		}
		public void setGraphicInfo(Object graphicInfo) {
			GraphicInfo = graphicInfo;
		}
	Object poolInfo;
		public Object getPoolInfo() {
			return poolInfo;
		}
		public void setPoolInfo(Object poolInfo) {
			this.poolInfo = poolInfo;
		}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}


	public void updateActivitySequence(){
		ActivityFor loop = new ActivityFor() {
			public long maxTT = 0;

			@Override
			public void logic(Activity activity) {
				try {
					long tt = Long.parseLong(activity.getTracingTag());
					if (tt > maxTT) {
						maxTT = tt;
						setReturnValue(maxTT);
					}
				} catch (Exception e) {

				}
			}
		};

		loop.run(this);

		if (loop.getReturnValue() != null) {
			setActivitySequence((long) loop.getReturnValue());
		}
	}

	protected void healTracingTagCollision(){
		updateActivitySequence();

		final Map<String, String> existTT = new HashMap<String, String>();

		new ActivityFor() {
			@Override
			public void logic(Activity activity) {
				if(existTT.containsKey(activity.getTracingTag())){
					activity.setTracingTag(String.valueOf(++ activitySequence));
					existTT.put(activity.getTracingTag(), activity.getTracingTag());
				}
			}
		}.run(this);

		setActivitySequence(activitySequence);

	}

}


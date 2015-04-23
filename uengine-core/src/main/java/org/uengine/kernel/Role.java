package org.uengine.kernel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.metaworks.ContextAware;
import org.metaworks.FieldDescriptor;
import org.metaworks.MetaworksContext;
import org.metaworks.Remover;
import org.metaworks.ServiceMethodContext;
import org.metaworks.Type;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
import org.metaworks.annotation.Order;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.inputter.RadioInput;
import org.metaworks.validator.NotNullValid;
import org.metaworks.validator.Validator;
import org.metaworks.widget.ModalWindow;
import org.uengine.contexts.TextContext;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.util.UEngineUtil;

/**
 * @author Jinyoung Jang
 */
@Face(ejsPath = "genericfaces/ActivityFace.ejs",
ejsPathMappingByContext = {
		"{how: 'menu', face: 'dwr/metaworks/org/uengine/kernel/Role.ejs'}"
		}, options={"fieldOrder"},values={"name,displayName"})
public class Role implements IElement, java.io.Serializable, Cloneable, ContextAware {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static final int DISPATCHINGOPTION_AUTO				= -1; // ����
	public static final int DISPATCHINGOPTION_RACING			= 1; // ����
	public static final int DISPATCHINGOPTION_LOADBALANCED 		= 2; // �ε� ���
	public static final int DISPATCHINGOPTION_ALL 				= 0; // ���
	public static final int DISPATCHINGOPTION_SETBYRIGHTPERSON 	= 4; // ����ڿ� ���� ��d
	public static final int DISPATCHINGOPTION_REFERENCE 		= 5; //����ڿ� ���� ��d
	public static final int DISPATCHINGOPTION_RECEIVE 			= 6; //����ڿ� ���� ��d
	public static final int DISPATCHINGOPTION_ANNOUNCEMENT 		= 7; //Notice Option
	
	public static final int ASSIGNTYPE_USER		 	= 0;	
	public static final int ASSIGNTYPE_DEPT 		= 2;	
	public static final int ASSIGNTYPE_GROUP 		= 3;	
	public static final int ASSIGNTYPE_ROLE 		= 4;	

	
	public static void metaworksCallback_changeMetadata(Type type){
		
		FieldDescriptor fd;
		
		//table.removeFieldDescriptor("AskWhenInit");
		//table.removeFieldDescriptor("DispatchingOption");
		//table.removeFieldDescriptor("Identifier");
		//table.removeFieldDescriptor("HumanWorker");
		//table.removeFieldDescriptor("ServiceType");

		String advancedOptions = GlobalContext.getLocalizedMessage("role.advancedoptions.label", "Advanced Options");
		
		//type.setName(GlobalContext.getLocalizedMessage("role.displayname", "Role"));
				
		fd = type.getFieldDescriptor("RoleResolutionContext");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.roleresolutioncontext.displayname", "Role"));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
//		fd = table.getFieldDescriptor("AskWhenInit");		
//		fd.setAttribute("hidden", new Boolean(true));
//		//fd.setDisplayName(GlobalContext.getLocalizedMessage("role.askwheninit.displayname"));
//		
		fd = type.getFieldDescriptor("DispatchingOption");
		//fd.setAttribute("hidden", new Boolean(true));
		fd.setInputter(new RadioInput(
			new String[]{
				GlobalContext.getLocalizedMessage("role.dispatchingoption.racing.displayname", "Racing"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.loadbalanced.displayname", "Load-balanced"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.setbyrightperson.displayname", "Set by right-person"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.notice.displayname", "Notice"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.all.displayname", "All"),
			},
			new Object[]{
				new Integer(DISPATCHINGOPTION_RACING), 
				new Integer(DISPATCHINGOPTION_LOADBALANCED), 
				new Integer(DISPATCHINGOPTION_SETBYRIGHTPERSON), 
				new Integer(DISPATCHINGOPTION_ANNOUNCEMENT),
				new Integer(DISPATCHINGOPTION_ALL),
			}
		));	

		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.dispatchingoption.displayname", ""));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
//
//		fd = table.getFieldDescriptor("Identifier");
//		fd.setAttribute("hidden", new Boolean(true));
//
//		fd = table.getFieldDescriptor("HumanWorker");
//		fd.setAttribute("hidden", new Boolean(true));
//		
//		fd = table.getFieldDescriptor("ServiceType");
//		fd.setAttribute("hidden", new Boolean(true));

		fd = type.getFieldDescriptor("Name");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.name.displayname", "Role Name (id)"));
		fd.setValidators(new Validator[]{new NotNullValid()}); //it's mandatory

		fd = type.getFieldDescriptor("DisplayName");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.displayname.displayname", "Display Name"));

		fd = type.getFieldDescriptor("DefaultEndpoint");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.defaultendpoint.displayname", "DefaultEndpoint"));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
		fd = type.getFieldDescriptor("DontPersistResolutionResult");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.dontpersistresolutionresult.displayname", "Do not persist the resolution result"));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
		fd = type.getFieldDescriptor("RoleResolutionContext");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd = type.getFieldDescriptor("ServiceType");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd = type.getFieldDescriptor("HumanWorker");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd = type.getFieldDescriptor("Identifier");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
		fd = type.getFieldDescriptor("AskWhenInit");		
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd.setAttribute("collapseGroup", true);
		
	}
	
	transient MetaworksContext metaworksContext;
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}

	private java.lang.String name;
	@Face(displayName="역할 이름")
	@Order(1)
		public String getName() {
			return name;
		}
		public void setName(String value) {
			name = value;
		}

	private RoleResolutionContext roleResolutionContext = null;
	@Face(displayName="역할 선택")
	@Order(3)
		public RoleResolutionContext getRoleResolutionContext() {
			if (roleResolutionContext instanceof DirectRoleResolutionContext) {
				DirectRoleResolutionContext drrc = (DirectRoleResolutionContext) roleResolutionContext;
				if(!UEngineUtil.isNotEmpty(drrc.getEndpoint())) {
					roleResolutionContext = null;
				}
			}
			if( roleResolutionContext == null ){
				String roleresolutioncontexts = GlobalContext.getPropertyString("roleresolutioncontexts", null);
				if( roleresolutioncontexts != null ){
				}
			}
			
			return roleResolutionContext;
		}
		public void setRoleResolutionContext(RoleResolutionContext context) {
			roleResolutionContext = context;

			if(context!=null){
				setAskWhenInit(false);
			}
		}
	
	int dispatchingOption = DISPATCHINGOPTION_ALL;
	@Hidden
		public int getDispatchingOption() {
			return dispatchingOption;
		}
	
		public void setDispatchingOption(int i) {
			dispatchingOption = i;
		}
	private ServiceDefinition serviceType;
	@Hidden
		public ServiceDefinition getServiceType() {
			return serviceType;
		}
		public void setServiceType(ServiceDefinition definition) {
			serviceType = definition;
		}
	
	private boolean isHumanWorker;
	@Hidden
		public boolean isHumanWorker() {
			return isHumanWorker;
		}	
		public void setHumanWorker(boolean b) {
			isHumanWorker = b;
		}
		
	private boolean askWhenInit = true;
	@Hidden
		public boolean isAskWhenInit() {
			return askWhenInit;
		}
		public void setAskWhenInit(boolean b) {
			askWhenInit = b;
		}

	private ProcessVariable identifier;
		@Hidden
		public ProcessVariable getIdentifier() {
			return identifier;
		}
		public void setIdentifier(ProcessVariable variable) {
			identifier = variable;
		}

	private String defaultEndpoint;
	@Hidden
		public String getDefaultEndpoint() {
			return defaultEndpoint;
		}
		public void setDefaultEndpoint(String string) {
			defaultEndpoint = string;
		}
		
	private TextContext displayName = TextContext.createInstance();
	@Name
	@Face(displayName="역할 설명")
	@Order(2)
		public TextContext getDisplayName() {
			if(displayName==null){
				displayName = TextContext.createInstance();
			}
			
			/*
			if(displayName.getText()==null)
				displayName.setText(getName());
			*/
			
			return displayName;
		}
		public void setDisplayName(TextContext string) {
			displayName = string;
		}
		public void setDisplayName(String string) {
			displayName.setText(string);
		}

	boolean dontPersistResolutionResult;
	@Hidden
		public boolean isDontPersistResolutionResult() {
			return dontPersistResolutionResult;
		}
		public void setDontPersistResolutionResult(boolean dontPersistResolutionResult) {
			this.dontPersistResolutionResult = dontPersistResolutionResult;
		}
	public Role(){
		setMetaworksContext(new MetaworksContext());
		this.setDisplayName("");
	}

	public Role(String name){
		this. name = name;
	}
	
	public RoleMapping getMapping(ProcessInstance inst) throws Exception{
		return getMapping(inst, (String)null);
	}

	public RoleMapping getMapping(ProcessInstance inst, Activity activity) throws Exception{
		return getMapping(inst, activity.getTracingTag());
	}
		
	public RoleMapping getMapping(ProcessInstance inst, String tracingTag) throws Exception{
		RoleMapping mapping = null;
		Role role = null;
		ProcessDefinition definition = null;
	
		if(inst!=null) {
			mapping = inst.getRoleMapping(getName());
			
			if (inst.getProcessDefinition()!=null) {
				definition = inst.getProcessDefinition();
				role = definition.getRole(getName());
			}
		}
		
		if(role==null)
			role = this;
			
		//clean up the existing resolution result when isDontPersistResolutionResult() option is true
		if(mapping!=null && isDontPersistResolutionResult()){
			mapping=null;
		}
		
		//TODO: is it hard-code?
		if(mapping==null) {
			//try to use role resolution context		
			Exception resolutionException = null;
			if (role.getRoleResolutionContext()!=null) {
				try{
					mapping = role.getRoleResolutionContext().getActualMapping(definition, inst, tracingTag, new java.util.Hashtable()); // danger roop with DefaultCompanyRoleResolutionContext.java (line 64) 
				}catch(Exception e){
					resolutionException = e;
					e.printStackTrace();
				}
				
				//set the rolemapping if resolution succeed. 
				//this should work for the case 'Racing' work-distribution option for future reference of the resolved mapping.
				if(!isDontPersistResolutionResult() && mapping!=null && inst != null ){
					inst.putRoleMapping(getName(), mapping);
				}
			}
			
			//try to use default finally
			if(mapping==null && ( role.getDefaultEndpoint()!=null && !"".equals(role.getDefaultEndpoint())) ){
				try{
					mapping = RoleMapping.create();
					mapping.setName(role.getName());
					mapping.setEndpoint(role.getDefaultEndpoint());
					
					if(role.isHumanWorker())
						mapping.fill(inst);
				}catch(Exception e){
					
					throw new UEngineException("Can't find user where the id ["+ role.getDefaultEndpoint()+ "] since: " + e.getMessage() + "\n Please contact to the process administrator.");
				}
			}else if(resolutionException!=null){
				throw resolutionException;
			}
		}
		
		if(mapping!=null){
			mapping.setCursor(0);
			
			do {
				RoleMapping rm = mapping.getCurrentRoleMapping();
				rm.setDispatchingOption(role.getDispatchingOption());
				mapping.replaceCurrentRoleMapping(rm);
				
			} while (mapping.next());
			
			mapping.setCursor(0);
		}
		
		return mapping;
	}
	
	public static Role forName(String name){
		return forName(name, null);
	}
	
	public static Role forName(String name, String defaultEP){	
		//review: fly-weight pattern needed
		Role role = new Role(name);
		role.setDefaultEndpoint(defaultEP);
		
		return role;
	}
	
	public boolean equals(Object obj){
		try{
			return getName().equals(((Role)obj).getName());
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean containsMapping(ProcessInstance instance, RoleMapping testingRoleMapping) throws Exception{
		RoleMapping thisRoleMapping = getMapping(instance);
		
		//���� �� ������ �μ��� ���� �μ��� ���ٸ� ����8�� ��		
		if(thisRoleMapping.getAssignType() == ASSIGNTYPE_DEPT){
			if(thisRoleMapping.getGroupId().equals(testingRoleMapping.getGroupId()))
				return true;
			else
				return false;
		}

		do{
			if(testingRoleMapping.getEndpoint().equals(thisRoleMapping.getEndpoint())){
				return true;
			}
		}while(thisRoleMapping.next());
		
		return false;
	}
	
	

//end

	public String toString() {
		String dispName = getDisplayName().toString(); 
		
		if(dispName!=null)
			return dispName;
		
		return super.toString();
	}
	
	public Object clone(){
		//TODO [tuning point]: Object cloning with serialization. it will be called by ProcessManagerBean.getProcessDefintionXX method.
		try{
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream ow = new ObjectOutputStream(bao);
			ow.writeObject(this);
			ByteArrayInputStream bio = new ByteArrayInputStream(bao.toByteArray());			
			ObjectInputStream oi = new ObjectInputStream(bio);
			
			Role clonedOne = (Role) oi.readObject();
			clonedOne.setIdentifier(null);
			clonedOne.setServiceType(null);
			clonedOne.setRoleResolutionContext(null);
			
			return clonedOne;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	transient String currentEditorId;
	@Hidden
		public String getCurrentEditorId() {
			return currentEditorId;
		}
		public void setCurrentEditorId(String currentEditorId) {
			this.currentEditorId = currentEditorId;
		}

	/**
	 * 나중에 apply 버튼은 ActivityWindow 로 빼야한다... 지금은 텝에 버튼이 보이질 않아서 임시로 달아놓음
	 * @return
	 */
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] apply(){
		return null;
	}

	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_APPEND)
	public Object[] cancel(){
		ModalWindow modalWindow = new ModalWindow();
		return new Object[]{new Remover(modalWindow , true)};
		
	}
	

	ElementView elementView;
	
	public ElementView createView(){
		ElementView elementView = (ElementView) UEngineUtil.getComponentByEscalation(getClass(), "view");
		elementView.setElement(this);

		return elementView;
	}
	public ElementView getElementView() {
		return this.elementView;
	}
	public void setElementView(ElementView elementView) {
		this.elementView = elementView;
	}
	
	public void createDocument() {
		// TODO Auto-generated method stub
	}
	@Override
	public String getDescription() {
		return this.displayName.getText();
	}
}

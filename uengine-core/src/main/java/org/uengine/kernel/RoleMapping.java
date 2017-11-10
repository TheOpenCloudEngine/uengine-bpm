package org.uengine.kernel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.metaworks.annotation.Hidden;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * @author Jinyoung Jang
 * @author Sungsoo Park
 */

public class RoleMapping implements java.io.Serializable, Cloneable, CommandVariableValue{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static Class USE_CLASS = null;
	
	protected String name;//this is roleName. don't be confused with resource name
	protected String resourceName;
	protected String emailAddress;
	protected String instanceMessengerId;
	protected String endpoint;		// userId
	protected String companyId;
	protected String nickName;
	protected String groupId;
	protected String groupName;
	protected String userPortrait;
	protected String userLastName;
	protected String userMiddleName;
	protected String userFirstName;
	protected String title;
	protected int assignType = 0;	
	protected String assignParam1;
	private int dispatchingOption = org.uengine.kernel.Role.DISPATCHINGOPTION_AUTO;
	protected boolean isMale = true;
	protected Date birthday;
	protected boolean isGroup = false;
	protected String locale;
	protected String isReferencer;
	protected String dispatchParam1;

    private java.util.Properties extendedProperties;
    int cursor;
    boolean isSingle = true;
    
    //for multiple role binding
	@JsonIgnore
	private ArrayList multipleMappings = null;

    protected RoleMapping() {
    	this.isSingle = false;    	
    }
    
    protected RoleMapping(boolean isSingle){
    	this.isSingle = isSingle;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return getCurrentRoleMapping().emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		getCurrentRoleMapping().emailAddress = emailAddress;
	}

	public String getInstanceMessengerId() {
		return getCurrentRoleMapping().instanceMessengerId;
	}

	public void setInstanceMessengerId(String instanceMessengerId) {
		getCurrentRoleMapping().instanceMessengerId = instanceMessengerId;
	}

	public String getResourceName() {
		String resourceName = getCurrentRoleMapping().resourceName;
		if(resourceName==null)
			resourceName = getEndpoint();
		
		if ( getAssignType()==Role.ASSIGNTYPE_DEPT  && getGroupName()!=null) {
			resourceName = getGroupName();
		}
		
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		getCurrentRoleMapping().resourceName = resourceName;
	}

	public String getUserPortrait() {
		return getCurrentRoleMapping().userPortrait;
	}

	public void setUserPortrait(String userPortrait) {
		getCurrentRoleMapping().userPortrait = userPortrait;
	}

	public String getUserLastName() {
		return getCurrentRoleMapping().userLastName;
	}

	public void setUserLastName(String userLastName) {
		getCurrentRoleMapping().userLastName = userLastName;
	}
	
	public String getUserFirstName() {
		return getCurrentRoleMapping().userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		getCurrentRoleMapping().userFirstName = userFirstName;
	}

	public String getUserMidddleName() {
		return getCurrentRoleMapping().userMiddleName;
	}

	public void setUserMiddleName(String userMiddleName) {
		getCurrentRoleMapping().userMiddleName = userMiddleName;
	}

	public String getEndpoint() {
		return getCurrentRoleMapping().endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		getCurrentRoleMapping().endpoint = endpoint;
	}
	
	public String getGroupName() {
		return getCurrentRoleMapping().groupName;
	}
	
	public void setGroupName(String deptName) {
		getCurrentRoleMapping().groupName = deptName;
	}
	
	public String getGroupId() {
		return getCurrentRoleMapping().groupId;
	}
	
	public void setGroupId(String id) {
		getCurrentRoleMapping().groupId = id;
	}
	
	public String getCompanyId() {
		return getCurrentRoleMapping().companyId;
	}

	public void setCompanyId(String compId) {
		getCurrentRoleMapping().companyId = compId;
	}
	
	public String getNickName(){
		return getCurrentRoleMapping().nickName;
	}
	public void setNickName(String nickName) {
		getCurrentRoleMapping().nickName = nickName;
	}	

	public String getTitle() {		
		return getCurrentRoleMapping().title;
	}
	public void setTitle(String title) {
		getCurrentRoleMapping().title = title;
	}
	
	public int getAssignType() {
		return getCurrentRoleMapping().assignType;
	}
	public void setAssignType(int assignType) {
		getCurrentRoleMapping().assignType = assignType;
	}		

	public Date getBirthday() {
		return getCurrentRoleMapping().birthday;
	}

	public void setBirthday(Date birthDay) {
		getCurrentRoleMapping().birthday = birthDay;
	}

	public boolean isMale() {
		return getCurrentRoleMapping().isMale;
	}

	public void setMale(boolean isMale) {
		getCurrentRoleMapping().isMale = isMale;
	}
	
	public boolean isGroup() {
		return getCurrentRoleMapping().isGroup;
	}

	public void setGroup(boolean isGroup) {
		getCurrentRoleMapping().isGroup = isGroup;
	}
	
	public String toString() {
		if (getResourceName() != null) {
			beforeFirst();

			StringBuilder names = new StringBuilder();
			do {
				if (names.length() > 0) names.append(", ");
				names.append(this.getResourceName()).append("/").append(this.getEndpoint());
			} while (next());

			beforeFirst();

			return names.toString();
		} else {
			return super.toString();
		}
	}
	
//	public String getResourceName() {
//		return getUserName();
//	}	
	
	public String getExtendedProperty(String key){
		RoleMapping currentRoleMapping = getCurrentRoleMapping();
		if(currentRoleMapping.extendedProperties!=null)
			return currentRoleMapping.extendedProperties.getProperty(key);
			
		return null;
	}
	
	

	public void setExtendedProperty(String key, String value){
		RoleMapping currentRoleMapping = getCurrentRoleMapping();
		if(currentRoleMapping.extendedProperties==null)
			currentRoleMapping.extendedProperties = new Properties();
			
		if(value!=null)
			currentRoleMapping.extendedProperties.setProperty(key, value);
		else
			currentRoleMapping.extendedProperties.remove(key);			
	}
    
//    public RoleMapping findRelative(String relationName){
//    	try{
//			return RelationFactory.getRelation(relationName).findRelative(this);
//    	}catch(Exception e){
//    		return null;
//    	}
//    }
    
    public synchronized boolean next(){
    	if(multipleMappings==null)
    		return false;
    	else if(cursor >= (multipleMappings.size()-1))
    		return false;	
    	    	
    	cursor++;
    	
    	return true;
    }
    
    public synchronized void moveToAdd(){
       	if(isSingle) return;
    	
    	if(multipleMappings==null){
			multipleMappings = new ArrayList();
			multipleMappings.add(this);
    	}
    		
/*    	RoleMapping newRoleMapping = create();
    	newRoleMapping.isSingle = true;
    	multipleMappings.add(newRoleMapping);
    	
*/		cursor = multipleMappings.size();
	}
    
    public synchronized int size(){
    	return (multipleMappings != null ? multipleMappings.size() : 1);
    }
    
	public synchronized int getCursor(){
		return cursor;
	}
	
	public synchronized void setCursor(int c){
		if(isSingle){
			cursor = 0;
			return;
		}

		if(c>=0 && c<size())
			cursor = c;
	}

	public synchronized void remove() {
		if(multipleMappings==null) return;
		
		multipleMappings.remove(getCursor());
		
		if(getCursor() >= size())
			setCursor(size());
		
		if(getCursor() == 0) //means we have only one mapping
			multipleMappings = null;
	}

	@JsonIgnore
	public synchronized RoleMapping getCurrentRoleMapping(){
		//TODO: currently disabled for XMLDecoder
		/*if(isSingle)
			throw new RuntimeException("Single rolemapping is not allowed to be bound multiple binding");*/
		
		RoleMapping targetMapping;
		
		if(multipleMappings==null)
			targetMapping = this;
		else{
			
			boolean a = false;
			if(multipleMappings.size() <= cursor){
		    	RoleMapping newRoleMapping = create();
		    	newRoleMapping.isSingle = true;
		    	newRoleMapping.setDispatchingOption(this.getDispatchingOption());
		    	newRoleMapping.setDispatchingParameters(this.getDispatchingParameters());
		    	multipleMappings.add(newRoleMapping);
			}	    	

			targetMapping = (RoleMapping)multipleMappings.get(cursor);
		}
		
		/*if(targetMapping == this && size() > 0){
			RoleMapping clonedOne = (RoleMapping)targetMapping.clone();
			targetMapping = clonedOne;
		}*/
		
		return targetMapping;
	}
	
	public synchronized void replaceCurrentRoleMapping(RoleMapping mapping){
		if(isSingle){
			System.out.println( new RuntimeException("Single rolemapping is not allowed to be bound multiple binding"));
			Thread.dumpStack();
		}


		if(multipleMappings==null){
			multipleMappings = new ArrayList();
			multipleMappings.add(mapping);
		}else{
			
			if(multipleMappings.size() <= cursor){
		    	multipleMappings.add(mapping);
			}else
				multipleMappings.set(getCursor(), mapping);			
		}
	}
	
	public static RoleMapping create(){
		if(USE_CLASS==null){
			try{
				USE_CLASS = GlobalContext.loadClass(GlobalContext.getPropertyString("rolemapping.class","com.defaultcompany.organization.DefaultCompanyRoleMapping"));
			}catch(Exception e){
				throw new RuntimeException("Couldn't find 'rolemapping.class' in uengine.properties has been set or couldn't initializes it.", e);
				//USE_CLASS = Liferay44RoleMapping.class;
			}
		}
		
		try {
			return (RoleMapping) USE_CLASS.newInstance();
		} catch (Exception e) {
			return null;
		}
	}

    public static void main(String[] args) throws Exception{
    	
    	RoleMapping rm = new RoleMapping();
    	
    	rm.setEmailAddress("pongsor2@hotmail.com");
    	rm.setEndpoint("pongsor");
    	
    	rm.moveToAdd();    	
    	rm.setEmailAddress("abc@abc.com");
    	rm.setEndpoint("abc");
    	
    	rm.moveToAdd();

    	rm.setEmailAddress("abc@abc.com");
    	rm.setEndpoint("abc");


    	System.out.println(rm.size());
    	
    	rm.beforeFirst();
    	
    	RoleMapping newRM = RoleMapping.create();
    	java.util.HashMap endpoints = new java.util.HashMap();
    	do{
    		if(!endpoints.containsKey(rm.getEndpoint())){
    			newRM.replaceCurrentRoleMapping(rm.getCurrentRoleMapping().makeSingle());
    			newRM.moveToAdd();

        		endpoints.put(rm.getEndpoint(), rm.getEndpoint());
    		}
    	}while(rm.next());
    	
    	GlobalContext.serialize(newRM, System.out, String.class);
    	System.out.flush();
    	GlobalContext.serialize(rm, System.out, String.class);
    }
    	

// for serialization by XMLEncoder/Decoder. don't use it directly.
	@Hidden //to prevent a recursive face rendering at the client. - https://github.com/TheOpenCloudEngine/essencia/issues/34
	public ArrayList getMultipleMappings() {
		if(isSingle) return null;
		return multipleMappings;
	}

	public void setMultipleMappings(ArrayList list) {
/*		if(list==null){
			isSingle = true;			
		}else*/
		multipleMappings = list;
	}
//

//	public void fill(){
//		if(ProcessDesigner.getInstance()!=null) return;
//		
//		DefaultTransactionContext dtc = null;
//		try {
//			dtc = new DefaultTransactionContext();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		try {
//			dtc.setConnectective(true);
//			OrgDAOFacade orgDF = OrgDAOFacade.getInstance(dtc);
//			UserDAO userDAO = orgDF.findUserById(getEndpoint());
//			
//			setEmailAddress(userDAO.getEmailAddress());
//			setLoginName(userDAO.getLoginName());
//			setCompId(userDAO.getCompanyId());
//			setNickName(userDAO.getNickname());
//			setDeptId(userDAO.getDeptId());
//			setDeptName(userDAO.getDeptName());
//			setUserName(userDAO.getName());
//			setTitle(userDAO.getJobPositionName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				dtc.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}		
//	}
	
	
	public void fill(ProcessInstance instance) throws Exception {
		if(GlobalContext.isDesignTime()) return;
	}
	
	public void beforeFirst(){
		setCursor(0);
	}

	public RoleMapping makeSingle(){
		isSingle = true;
		
		RoleMapping clonedOne = (RoleMapping)clone();
		clonedOne.setMultipleMappings(null);
		return clonedOne;
	}
		
	public java.util.Properties getExtendedProperties() {
		return extendedProperties;
	}

	public void setExtendedProperties(java.util.Properties properties) {
		extendedProperties = properties;
	}

	public Object clone(){
		try {
			RoleMapping clonedOne = (RoleMapping)super.clone();
			
			if(multipleMappings!=null){
				ArrayList newList = new ArrayList();
				newList.addAll(multipleMappings);
				clonedOne.multipleMappings = newList;
			}

			return clonedOne;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return Returns the dispatchingOption.
	 */
	public int getDispatchingOption() {
		return dispatchingOption;
	}

	/**
	 * @param dispatchingOption The dispatchingOption to set.
	 */
	public void setDispatchingOption(int dispatchingOption) {
		this.dispatchingOption = dispatchingOption;
	}
	
	String[] dispatchingParams;
		public String[] getDispatchingParameters() {
			return dispatchingParams;
		}
		public void setDispatchingParameters(String[] dispatchingParams) {
			this.dispatchingParams = dispatchingParams;
		}
	
	public String getAssignParam1() {
		return getCurrentRoleMapping().assignParam1;
	}

	public void setAssignParam1(String assignParam1) {
		getCurrentRoleMapping().assignParam1 = assignParam1;
	}

	public boolean equals(Object obj) {
		if(obj instanceof RoleMapping){
			RoleMapping comparatee = (RoleMapping)obj;
			if(getEndpoint()==null) return super.equals(obj);
			return getEndpoint().equals(comparatee.getEndpoint());
		}else
			return super.equals(obj);
	}

	public boolean doCommand(ProcessInstance instance, String variableKey) throws Exception {
		if(instance.getProcessDefinition().getRole(variableKey)!=null){
			setName(variableKey);
			instance.putRoleMapping(this);
			return true;
		}else
			return false;
	}


	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		getCurrentRoleMapping().locale = locale;
	}
	
	public String getIsReferencer() {
		return isReferencer;
	}

	public void setIsReferencer(String isReferencer) {
		this.isReferencer = isReferencer;
	}
	
	public String getDispatchParam1() {
		return dispatchParam1;
	}

	public void setDispatchParam1(String dispatchParam1) {
		this.dispatchParam1 = dispatchParam1;
	}
}

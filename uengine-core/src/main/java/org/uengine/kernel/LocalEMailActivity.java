package org.uengine.kernel;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.metaworks.Type;
import org.uengine.util.UEngineUtil;
import org.uengine.webservices.emailserver.impl.EMailServerSoapBindingImpl;

/**
 * @author Jinyoung Jang
 */
public class LocalEMailActivity extends DefaultActivity{
	
	protected static final String MAIL_SERVICE = "mailServer";
	
	public static void metaworksCallback_changeMetadata(Type type){
		//FieldDescriptor fd = type.getFieldDescriptor("AttachFiles");

		//org_uengine_kernel_ProcessVariableArrayInput varArrinputter = (org_uengine_kernel_ProcessVariableArrayInput) fd.getInputter();
		//varArrinputter.setFilter(FileContext.class.getName());
	}
	
	String contents;
		public String getContents() {
			return contents;
		}
		public void setContents(String value) {
			contents = value;
		}

	String title;
		public String getTitle() {
			return title;
		}
		public void setTitle(String value) {
			title = value;
		}

	String to;
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		
	Role toRole;
		public Role getToRole() {
			return toRole;
		}
		public void setToRole(Role value) {
			toRole = value;
		}

	String from;
		public String getFrom() {
			return from;
		}
		public void setFrom(String string) {
			from = string;
		}
		
	Role fromRole;
		public Role getFromRole() {
			return fromRole;
		}
		public void setFromRole(Role fromRole) {
			this.fromRole = fromRole;
		}
		
	String characterSet;
		public String getCharacterSet() {
			return characterSet;
		}
		public void setCharacterSet(String characterSet) {
			this.characterSet = characterSet;
		}		

	boolean isAppendTrackingLink=false;
		public boolean isAppendTrackingLink() {
			return isAppendTrackingLink;
		}
		
		public void setAppendTrackingLink(boolean isAppendTrackingLink) {
			this.isAppendTrackingLink = isAppendTrackingLink;
		}
			
	/**
	 * @deprecated
	 */
//	ProcessVariable attachFile;
//	
//	ProcessVariable[] attachFiles;
//		public ProcessVariable[] getAttachFiles() {
//			if(attachFile!=null) return new ProcessVariable[]{attachFile};
//			return attachFiles;
//		}
//		public void setAttachFiles(ProcessVariable[] attachFiles) {
//			if(attachFile!=null) attachFile = null;
//			this.attachFiles = attachFiles;
//		}
		
	
	
	public LocalEMailActivity(){
		setName("Email Activity");
		setCharacterSet("UTF-8");
	}

	public void executeActivity(ProcessInstance instance) throws Exception{
		String actualContent = evaluateContent(instance, getContents()).toString();
		
		if(isAppendTrackingLink()) {
			InetAddress thisIp = InetAddress.getLocalHost();

			final String host = GlobalContext.getPropertyString("alarmactivityfilter.wih.host", thisIp.getHostAddress());
			final String port = GlobalContext.getPropertyString("alarmactivityfilter.wih.port", "8080");
			actualContent += "<br><a href=\"http://" + host + ":" + port
					+ GlobalContext.WEB_CONTEXT_ROOT
					+ "/processparticipant/viewProcessInformation.jsp?omitHeader=yes&instanceId="
					+ instance.getInstanceId()
					+"\" > If you want to track this process click here </a>";
		}
		
		String actualFrom = evaluateContent(instance, getFrom()).toString();
		String actualFromName = null;
		if(getFromRole()!=null){
			RoleMapping fromUser = getFromRole().getMapping(instance);
			
			if(fromUser!=null){
				actualFrom = fromUser.getEmailAddress();			
				if(!UEngineUtil.isNotEmpty(actualFrom)){
					fromUser.fill(instance);
					actualFrom = fromUser.getEmailAddress();
					if (UEngineUtil.isNotEmpty(actualFrom)) {
						actualFromName = fromUser.getResourceName();
					}
				}
				
//				if(UEngineUtil.isNotEmpty(actualFrom)){
//					actualFrom = fromUser.getResourceName() + "<" + actualFrom + ">";
//				}
			}else{
				throw new UEngineException("The actual user for mail sender [" + getFromRole() + "] is not bound yet.");
			}
		}
		if (!UEngineUtil.isNotEmpty(actualFrom)) {
			actualFrom = evaluateContent(instance, getFrom()).toString();
		}

		String actualTitle = evaluateContent(instance, getTitle()).toString();
		String to = evaluateContent(instance, getTo()).toString();
		
		if(getToRole()==null && !UEngineUtil.isNotEmpty(to))
			throw new UEngineException("Receiver is not set.");
			
		RoleMapping roleMapping = getToRole().getMapping(instance, getTracingTag());
		
		if(roleMapping==null && !UEngineUtil.isNotEmpty(to))
			throw new UEngineException("Actual target receiver is not set yet.");

		// creates receiver mail addresses
		//---------------------------------------------------------------
		
		String mailAddrs = null;
		
		if(roleMapping!=null){			
			roleMapping.beforeFirst();
			String sep = "";				
			do{
				if(mailAddrs==null){
					mailAddrs = ""; 
				}
				
				if(!UEngineUtil.isNotEmpty(roleMapping.getEmailAddress())) roleMapping.fill(instance);

				if(roleMapping.getEmailAddress()==null){ //if there's no email address though filling up, use endpoint instead.
					roleMapping.setEmailAddress(roleMapping.getEndpoint());
				}
				
				String emailAddress = roleMapping.getEmailAddress();
				if (UEngineUtil.isNotEmpty(emailAddress) && UEngineUtil.isValidEmailAddress(emailAddress) == true) {
					mailAddrs += emailAddress + ",";
				}
				
				/*
				if(!(mailAddrs.indexOf(roleMapping.getEmailAddress()) >= 0)){
					mailAddrs = mailAddrs + sep + roleMapping.getResourceName() +"<"+roleMapping.getEmailAddress()+">";
					sep = ",";
				}
				*/
				
			}while(roleMapping.next());
		}
		
		if (!UEngineUtil.isNotEmpty(mailAddrs)) {
			mailAddrs = to;
		}
		
		if (UEngineUtil.isNotEmpty(mailAddrs)) {
			
			// prepare attach files 
			//--------------------------------------------------------------
			
			List filePaths = new ArrayList();
//			if(getAttachFiles()!=null){
//				for(int i=0; i<getAttachFiles().length; i++){
//					ProcessVariable theAttachFile = getAttachFiles()[i];
//					if(theAttachFile!=null){
//						Serializable value = theAttachFile.get(instance, "");
//						if(value!=null && value instanceof FileContext){
//							FileContext fileContext = (FileContext)value;
//							
//							//TODO temporal code. it should be cached to local file to be sent via SMTP
//							String realFilePath = fileContext.getPath();
//							if(realFilePath!=null && fileContext.getPath().startsWith("ftp://")){
//								String[] elements = realFilePath.replace('/','@').split("@");
//								realFilePath = elements[elements.length-1];
//							}
//							
//							filePaths.add(realFilePath);
//						}
//					}
//				}
//			}
			
			
			// send mail actually 
			//--------------------------------------------------------------
			String[] tempMailAddrs = mailAddrs.split(",");
			for (int i = 0; i < tempMailAddrs.length; i++) {
				mailAddrs = tempMailAddrs[i];
				
				(new EMailServerSoapBindingImpl()).sendMail(
						actualFrom, 
						actualFromName,
						mailAddrs, 
						actualTitle, 
						actualContent, 
						filePaths, 
						null,
						UEngineUtil.isNotEmpty(getCharacterSet()) ? getCharacterSet() : "UTF-8"
				);
			}
		}
		
		fireComplete(instance);
	}


}
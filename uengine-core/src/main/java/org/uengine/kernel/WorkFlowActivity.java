package org.uengine.kernel;


import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.uengine.kernel.HumanActivity;


public class WorkFlowActivity extends HumanActivity{
	
	public WorkFlowActivity(){
		super();
		setName("WorkFlowActivity");
	}
	
	public static void metaworksCallback_changeMetadata(Type type){
		
		FieldDescriptor fd;
		
		type.removeFieldDescriptor("AllowAnonymous");
		type.removeFieldDescriptor("NotificationWorkitem");
		type.removeFieldDescriptor("SendEmailWorkitem");
		type.removeFieldDescriptor("Keyword");
		type.removeFieldDescriptor("Tool");
		type.removeFieldDescriptor("Workload");
		type.removeFieldDescriptor("Priority");
		type.removeFieldDescriptor("ReferenceRole");	
		type.removeFieldDescriptor("Cost");		
		type.removeFieldDescriptor("StatusCode");		
		type.removeFieldDescriptor("ActivityIcon");
		type.removeFieldDescriptor("Hidden");
		type.removeFieldDescriptor("QueuingEnabled");		
		type.removeFieldDescriptor("FaultTolerant");		
		type.removeFieldDescriptor("RetryDelay");			
		type.removeFieldDescriptor("RetryLimit");
		type.removeFieldDescriptor("DynamicChangeAllowed");	
		type.removeFieldDescriptor("Instruction");	
		type.removeFieldDescriptor("Input");	
		
		fd = type.getFieldDescriptor("ExtValue1");	
		fd.setDisplayName("FormId");
		fd = type.getFieldDescriptor("ExtValue2");
		fd.setDisplayName("SubCtrlId1");
		fd = type.getFieldDescriptor("ExtValue3");
		fd.setDisplayName("SubCtrlId2");
		type.removeFieldDescriptor("ExtValue4");
		type.removeFieldDescriptor("ExtValue5");
		type.removeFieldDescriptor("ExtValue6");
		type.removeFieldDescriptor("ExtValue7");
		type.removeFieldDescriptor("ExtValue8");
		type.removeFieldDescriptor("ExtValue9");
		type.removeFieldDescriptor("ExtValue10");
	}
	
}

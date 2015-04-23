package org.uengine.kernel;

import java.awt.Component;
import java.io.Serializable;

import javax.swing.JLabel;

import org.metaworks.FieldDescriptor;
import org.metaworks.ServiceMethodContext;
import org.metaworks.Type;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.inputter.AbstractComponentInputter;
import org.metaworks.inputter.SelectInput;
import org.uengine.contexts.TextContext;

public class EventHandler implements Serializable{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	public final static int TRIGGERING_BY_EVENTBUTTON = 1;
	public final static int TRIGGERING_BY_API = 2;
	public final static int TRIGGERING_BY_WEBSERVICE = 3;
	public final static int TRIGGERING_BY_DELEGATION = 10;
	public final static int TRIGGERING_BY_COMPENSATION = 11;
	public final static int TRIGGERING_BY_FAULT = 12;

	//activity event
	public final static int TRIGGERING_BY_AFTER_CHILD_COMPLETED = 21;
	public final static int TRIGGERING_BY_AFTER_CHILD_SAVED = 22;
	public final static int TRIGGERING_BY_AFTER_CHILD_SAVED_OR_COMPLETED = 23;

	//approval
	public final static int TRIGGERING_BY_DRAFTED = 101;
	public final static int TRIGGERING_BY_APPROVED = 102;
	public final static int TRIGGERING_BY_REJECTED = 103;
	public final static int TRIGGERING_BY_ARBITRARYFINISHED = 104;
		
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd = type.getFieldDescriptor("HandlerActivity");
		//fd.setAttribute("hidden", new Boolean(true));
		
		fd.setInputter(new AbstractComponentInputter(){
			private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
			
			JLabel valueLabel;
			Activity value;
			
			public Object getValue() {
				// TODO Auto-generated method stub
				return value;
			}

			public void setValue(Object data) {
				// TODO Auto-generated method stub
				valueLabel.setText(((Activity)data).getName());
				value = (Activity)data;
			}

			public Component getNewComponent() {
				// TODO Auto-generated method stub
				return (valueLabel = new JLabel("not set"));
			}
			
		});
		
		fd = type.getFieldDescriptor("TriggeringMethod");
		fd.setInputter(new SelectInput(
				new String[]{
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.byeventbutton", "By Event Button (in web)"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.byapi", "By API invocation"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.whendelegation", "When a participant is delegated"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.whencompensation", "When the scope is compensated"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.whenfault", "When one of child is in fault"),
						//activity event
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.activityevent.aftercompleted", "After one of child is completed"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.activityevent.aftersaved", "After one of child is saved only"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.activityevent.aftersaved.or.completed", "After one of child is saved or completed"),
						//approval
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.approval.drafted", "Approval / Drafted"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.approval.approved", "Approval / Approved"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.approval.rejected", "Approval / Rejected"),
						GlobalContext.getLocalizedMessage("eventhandler.triggeringmethods.approval.arbitraryapproved", "Approval / Arbitrary Approved"),
				}, new Object[]{
						new Integer(TRIGGERING_BY_EVENTBUTTON),
						new Integer(TRIGGERING_BY_API),						
						new Integer(TRIGGERING_BY_DELEGATION),						
						new Integer(TRIGGERING_BY_COMPENSATION),						
						new Integer(TRIGGERING_BY_FAULT),
						//activity events
						new Integer(TRIGGERING_BY_AFTER_CHILD_COMPLETED),
						new Integer(TRIGGERING_BY_AFTER_CHILD_SAVED),
						new Integer(TRIGGERING_BY_AFTER_CHILD_SAVED_OR_COMPLETED),
						//approval
						new Integer(TRIGGERING_BY_DRAFTED),						
						new Integer(TRIGGERING_BY_APPROVED),						
						new Integer(TRIGGERING_BY_REJECTED),						
						new Integer(TRIGGERING_BY_ARBITRARYFINISHED),						
				}
			)
		);
	}	
	
	
	String name;
	TextContext displayName = TextContext.createInstance();
	Activity handlerActivity;
	Role openRoles;
	int triggeringMethod = TRIGGERING_BY_EVENTBUTTON;
	
	@Hidden
	public Activity getHandlerActivity() {
		return handlerActivity;
	}
	public void setHandlerActivity(Activity handlerActivity) {
		this.handlerActivity = handlerActivity;
	}
	@Id
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TextContext getDisplayName() {
		if(displayName.getText()==null){
			TextContext result = TextContext.createInstance();
			result.setText(getName());
			return result;
		}
		
		return displayName;
	}
	public void setDisplayName(TextContext displayName) {
		this.displayName = displayName;
	}
	public Role getOpenRoles() {
		return openRoles;
	}
	public void setOpenRoles(Role openRoles) {
		this.openRoles = openRoles;
	}
	public int getTriggeringMethod() {
		return triggeringMethod;
	}
	public void setTriggeringMethod(int triggeringMethod) {
		this.triggeringMethod = triggeringMethod;
	}
	
	@ServiceMethod(target=ServiceMethodContext.TARGET_POPUP)
	public Object addEventHandler() throws Exception{
		
		return null;
	}
		
}

package org.uengine.kernel;

import java.io.Serializable;
import java.util.Map;

import org.metaworks.FieldDescriptor;
import org.metaworks.ObjectType;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */
public class RoleParameterContext implements Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public static final String DIRECTION_IN = "in".intern();
	public static final String DIRECTION_OUT = "out".intern();
	public static final String DIRECTION_INOUT = "in-out".intern();
	
	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
				
		fd = type.getFieldDescriptor("Direction");		
		fd.setInputter(new RadioInput(
			new String[]{
				GlobalContext.getLocalizedMessage("parametercontext.direction.in.displayname", "in"),
				GlobalContext.getLocalizedMessage("parametercontext.direction.out.displayname", "out"),
				GlobalContext.getLocalizedMessage("parametercontext.direction.inout.displayname", "in-out")
			},
			new Object[]{
				DIRECTION_IN, 
				DIRECTION_OUT, 
				DIRECTION_INOUT, 
			}
		));
	}
	
	String argument;
		public String getArgument() {
			return argument;
		}
		public void setArgument(String string) {
			argument = string;
		}

	Role role;
		public Role getRole() {
			return role;
		}	
		public void setRole(Role role) {
			this.role = role;
		}

	String direction;
		public String getDirection() {
			return direction;
		}
		public void setDirection(String direction) {
			this.direction = direction;
		}
		
	boolean split;
		public boolean isSplit() {
			return split;
		}
		public void setSplit(boolean split) {
			this.split = split;
		}
	
}

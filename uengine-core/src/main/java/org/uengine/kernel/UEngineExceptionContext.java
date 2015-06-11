package org.uengine.kernel;

import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 */

public class UEngineExceptionContext implements java.io.Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	String message;
		public String getMessage() {
			return message;
		}
		public void setMessage(String value) {
			message = value;
		}

	String details;
		public String getDetails() {
			return details;
		}
		public void setDetails(String value) {
			details = value;
		}
	
	String resolution;

		/**
		 * 
		 * @uml.property name="resolution"
		 */
		public String getResolution() {
			return resolution;
		}

		/**
		 * 
		 * @uml.property name="resolution"
		 */
		public void setResolution(String value) {
			resolution = value;
		}

		
	public UEngineException createException(){
		UEngineException ue = new UEngineException(getMessage());
		ue.setDetails(getDetails());
		ue.setResolution(getResolution());
		
		return ue;
	}
}
package org.uengine.kernel;

import javax.xml.namespace.*;

import org.uengine.kernel.GlobalContext;

/**
 * @author Jinyoung Jang
 * @deprecated This class is merged with the class 'ProcessVariable'. use ProcessVariable
 */

public class ProcessVariableDescriptor implements java.io.Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	String name;
		public String getName() {
			return name;
		}
		public void setName(String value) {
			name = value;
		}
	
	String displayName;	
		public String getDisplayName(){
			return displayName;
		}	
		public void setDisplayName(String value){
			displayName = value;
		}
	
	Class type;
		public Class getType() {
			if (type == null) {
				if (getXmlBindingClassName() != null) {
					try {
						type =
							GlobalContext.getComponentClass(
								getXmlBindingClassName());
					} catch (Exception e) {
						System.out.println(
							"Warning: Binding class not found at design time.");
					}
				}
				//The other case need to be coded: dynamic compilation of XML binding classes based on the QName
			}

			return type;
		}

		public void setType(Class type) {
			this.type = type;
		}

	Role[] openRoles;
		public Role[] getOpenRoles() {
			return openRoles;
		}
		public void setOpenRoles(Role[] value) {
			openRoles = value;
		}
	
	String xmlBindingClsName;
		public String getXmlBindingClassName(){
			if(getQName()!=null && xmlBindingClsName==null)
				xmlBindingClsName = org.uengine.util.UEngineUtil.QName2ClassName(getQName());
								
			return xmlBindingClsName;
		}
			
		public void setXmlBindingClassName(String value){
			xmlBindingClsName = value;
		}

	QName qname;
		public QName getQName(){
			return qname;
		}
		public void setQName(QName value){
			qname = value;
		}
	
/*	String bindingClassName;
	
		public String getBindingClassName(){
			if(getQName()!=null && bindingClassName==null)
				bindingClassName = getQName().getLocalPart();
				
			return bindingClassName;
		}
			
		public void setBindingClassName(String value){
			bindingClassName = value;
		}
*/
	public boolean equals(Object obj){
		return getName().equals(((ProcessVariableDescriptor)obj).getName());
	}
	
	public ProcessVariableDescriptor(Object[] settings){
		org.uengine.util.UEngineUtil.initializeProperties(this, settings);
	}

	public String toString(){
		if(getName()!=null) return getName();		
		return super.toString();
	}
	
	public ProcessVariableDescriptor(){}

}
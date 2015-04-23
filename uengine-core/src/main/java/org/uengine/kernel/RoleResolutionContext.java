package org.uengine.kernel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.metaworks.ServiceMethodContext;
import org.metaworks.Type;
import org.metaworks.annotation.ServiceMethod;

/**
 * @author Jinyoung Jang
 */
public abstract class RoleResolutionContext implements java.io.Serializable, Transferable {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public static void metaworksCallback_changeMetadata(Type type){
		type.removeFieldDescriptor("Name");
	}
	
	public RoleResolutionContext(){
		
	}
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String string) {
		name = string;
	}
	
	abstract public RoleMapping getActualMapping(
		ProcessDefinition pd,
		ProcessInstance instance,
		String tracingTag,
		Map options) throws Exception;
		
	abstract public String getDisplayName();
	
	/**
	 * @deprecated
	 */
	public String[] getDispatchingParameters(){
		return null;
	}
	
	/**
	 * @deprecated
	 */
	public int getDispatchingOption(){
		return Role.DISPATCHINGOPTION_AUTO;//means auto
	}

	public Object getTransferData(DataFlavor flavor)
		throws UnsupportedFlavorException, IOException {
		List list = new ArrayList();
		list.add(this);
		return list;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return null;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return false;
	}

	public String toString() {
		return getDisplayName();
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_POPUP)
	public Object openPicker() throws Exception{
		return null;
	}
}

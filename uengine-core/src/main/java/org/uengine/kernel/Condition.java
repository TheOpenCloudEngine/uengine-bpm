package org.uengine.kernel;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.*;
import org.uengine.contexts.TextContext;

/**
 * @author Jinyoung Jang
 */

public abstract class Condition implements Validatable, Serializable, ContextAware{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	public abstract boolean isMet(ProcessInstance instance, String scope) throws Exception;
	public ValidationContext validate(Map options){
		return new ValidationContext();  
	}

	TextContext description = org.uengine.contexts.TextContext.createInstance();
		public TextContext getDescription() {
			return description;
		}
		public void setDescription(TextContext string) {
			description = string;
		}
		
	public String toString(){
		if(getDescription().getText()!=null)
			return getDescription().getText();
		else
			return super.toString();
	}

	@ServiceMethod(inContextMenu = true, callByContent = true)
	@Hidden(where="removed")
	public void remove(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhere("removed");
	}

	@ServiceMethod(inContextMenu = true, callByContent = true)
	@Available(where="removed")
	public void revert(){
		setMetaworksContext(new MetaworksContext());
		getMetaworksContext().setWhere(null);
	}

	MetaworksContext metaworksContext;
		@Override
		public MetaworksContext getMetaworksContext() {
			return metaworksContext;
		}

		@Override
		public void setMetaworksContext(MetaworksContext metaworksContext) {
			this.metaworksContext = metaworksContext;
		}


	@Name  //only stands for metaworks tree
	@NonEditable
	public String getName(){
		if(getMetaworksContext()!=null && "removed".equals(getMetaworksContext().getWhere()))
			return "<strike> "+toString()+" </strike>";

		return toString();
	}
	public void setName(String name){}

}
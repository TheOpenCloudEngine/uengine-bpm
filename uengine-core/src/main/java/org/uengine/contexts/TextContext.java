package org.uengine.contexts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;

/**
 * This class holds descriptive string in multi-lingual support. 
 * It contains each language-specific string in the localedTexts hashmap with key as the locale.
 * When it is requested a string by caller without certain locale information (in case of being-called by the method 'getText()'),
 * it references the process definition object to get the currently set locale setting in a static-way.
 *  
 * @author Jinyoung Jang
 */

public class TextContext implements Serializable{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static TextContext createInstance(ProcessDefinition definition){
		TextContext newTC = new TextContext();
		newTC.setProcessDefinition(definition);
		return newTC;
	}
	
	public static TextContext createInstance(){
		// TODO: locale 
		return new TextContext();
	}
	
	String text;
		@Name
		public String getText() {
			if(!GlobalContext.isDesignTime()){
				String defaultLocale = GlobalContext.getDefaultLocale();
				
				if(defaultLocale!=null) return getText(defaultLocale);
			}
			
			if(getProcessDefinition()==null) return text;
			return getText(getProcessDefinition().getCurrentLocale());
		}

		public String getText(String locale) {
			String textInSelectedLocale = text;
			if(/*getProcessDefinition()!=null && */locale!=null){
				
				if(localedTexts==null)
					localedTexts = new HashMap();
					
				if(localedTexts.containsKey(locale))
					textInSelectedLocale = (String)localedTexts.get(locale);
				
			}
			
			return textInSelectedLocale;
		}
		
		public void setText(String value) {
			if(text==null || (getProcessDefinition()!=null && getProcessDefinition().getCurrentLocale()==null)){
				text = value;
				return;
			}
			
			if(getProcessDefinition()!=null)
				setText(getProcessDefinition().getCurrentLocale(), value);
			else
				text = value;
		}
		public void setText(String locale, String value){
			if(localedTexts==null)
				localedTexts = new HashMap();
			
			localedTexts.put(locale, value);
		}
		
	Map localedTexts;
		@Hidden
		public Map getLocaledTexts() {
			return localedTexts;
		}
		public void setLocaledTexts(Map localedTexts) {
			this.localedTexts = localedTexts;
		}
	
	transient ProcessDefinition definition;
		@Hidden
		public ProcessDefinition getProcessDefinition() {
			// TODO: locale
			return this.definition;
		}
		public void setProcessDefinition(ProcessDefinition definition) {
			this.definition = definition;
		}
		
	public String parse(Activity activity, ProcessInstance instance){
		return activity.evaluateContent(instance, getText()).toString();
	}

	public String toString() {
		// TODO Auto-generated method stub
		return getText();
	}
}
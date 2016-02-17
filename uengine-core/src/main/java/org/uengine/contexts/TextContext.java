package org.uengine.contexts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
import org.metaworks.dwr.MetaworksRemoteService;
import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.modeling.LanguageSelector;

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
	
//	public static TextContext createInstance(ProcessDefinition definition){
//		TextContext newTC = new TextContext();
////		newTC.setProcessDefinition(definition);
//		return newTC;
//	}
	
	public static TextContext createInstance(){
		// TODO: locale 
		return new TextContext();
	}
	
	String text;
		@Name
		public String getText() {

			if(MetaworksRemoteService.metaworksCall()) {
				MetaworksRemoteService.autowire(this);
				if (languageSelector != null && languageSelector.getLanguage() != null) {
					return getText(languageSelector.getLanguage());
				}
			}

			return text;
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
//			if(text==null || (getProcessDefinition()!=null && getProcessDefinition().getCurrentLocale()==null)){
//				text = value;
//				return;
//			}
//
//			if(getProcessDefinition()!=null)
//				setText(getProcessDefinition().getCurrentLocale(), value);
//			else


			//TODO enable for some time ?
//			if(MetaworksRemoteService.metaworksCall()) {
//				MetaworksRemoteService.autowire(this);
//				if (languageSelector != null && languageSelector.getLanguage() != null) {
//					setText(languageSelector.getLanguage(), value);
//
//					return;
//				}
//			}

			text = value;
		}

		public void setText(String locale, String value){
			if(localedTexts==null)
				localedTexts = new HashMap();
			
			localedTexts.put(locale, value);
		}

	@AutowiredFromClient
	transient public LanguageSelector languageSelector;
		
	Map localedTexts;
		@Hidden
		public Map getLocaledTexts() {
			return localedTexts;
		}
		public void setLocaledTexts(Map localedTexts) {
			this.localedTexts = localedTexts;
		}
	
//	transient ProcessDefinition definition;
//		@Hidden
//		public ProcessDefinition getProcessDefinition() {
//			// TODO: locale
//			return this.definition;
//		}
//		public void setProcessDefinition(ProcessDefinition definition) {
//			this.definition = definition;
//		}
		
	public String parse(Activity activity, ProcessInstance instance){
		return activity.evaluateContent(instance, getText()).toString();
	}

	public String toString() {
		// TODO Auto-generated method stub
		return getText();
	}
}
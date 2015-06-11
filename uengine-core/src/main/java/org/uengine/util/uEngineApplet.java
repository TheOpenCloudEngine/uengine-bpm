package org.uengine.util;
import java.applet.Applet;
//import java.io.ByteArrayInputStream;
import java.util.Hashtable;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;

public class uEngineApplet extends Applet {

	public void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	public String flowChartTransform(String definitionXml) throws Exception{
		//try{
			Hashtable options = new Hashtable();
			ProcessDefinition definition = (ProcessDefinition) GlobalContext.deserialize(definitionXml, ProcessDefinition.class);

			return null;
			//return ProcessDefinitionViewer.getInstance().render(getDefinition(processDefinition), null, options).toString();
		//}catch(Exception e){
//			ByteArrayOutputStream bao = new ByteArrayOutputStream();
//			return "반영되고있니???:"+e.getMessage();
//		}
		

	}
}

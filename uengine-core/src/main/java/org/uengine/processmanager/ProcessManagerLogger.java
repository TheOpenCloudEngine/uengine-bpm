package org.uengine.processmanager;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;

import org.uengine.kernel.GlobalContext;

public class ProcessManagerLogger {
	//for debug
	static long firstRequestedTime=0L;
	static boolean DEBUG = GlobalContext.getPropertyString(
			"server.invocationlog",
			"false"
		).equalsIgnoreCase("true");
		
	static boolean DEBUG_PARAMETER_OUT_WITH_XML = false;
	static String INVOCATION_LOG_FILE = "ue_invoc";
	
	static void log(String methodName, String messageStr, String sourceCodes){
		if(!DEBUG) return;
		StringBuffer message = new StringBuffer();
		StringBuffer src = new StringBuffer();
		
		message.append("\n\n#- ProcessManagerBean invocation log ------------------\n");

		long now = System.currentTimeMillis();
		if(firstRequestedTime==0L){
					
			firstRequestedTime = now;
			
		}
		
		message.append("Time: " + (now-firstRequestedTime) + "\n");
		message.append("Method: " + methodName + "\n");
		message.append("Arguments: \n");
		
		src.append("waitUntil(" + (now-firstRequestedTime) + ");\n");
		src.append("pmCall(\"" + methodName + "\", new String[]{\n");
		src.append(sourceCodes+"\n});\n");

		message.append(messageStr + "\n");		
		message.append("Invocation Code: \n");
		message.append(src);
		message.append("------------------------------------------------------#\n\n");
		
		try{
			FileWriter fw = new FileWriter(INVOCATION_LOG_FILE + firstRequestedTime + ".log", true);
			fw.write(src.toString()+"\n");
			fw.close();
		}catch(Exception e){
		}

		//System.out.println(message.toString());
	}

	static void logInst(String methodName, Object[] parameters){
		parameters[0] = "<instanceId>";
		log(methodName, parameters);
	}
	
	static void log(String methodName, Object[] parameters){
		StringBuffer message = new StringBuffer();
		StringBuffer src = new StringBuffer();
		String sep="";
		
		for(int i=0; i<parameters.length; i++){
			String xml = null;
			if(parameters[i]!=null){
				if(parameters[i].equals("<instanceId>")){
					xml = "<instanceId>";
				}else{
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					try{
						GlobalContext.serialize(parameters[i], bao, String.class);	
						xml = bao.toString(GlobalContext.DATABASE_ENCODING);
					}catch(Exception e){					
					}
				}
			}
			
			message.append(sep + xml);
			
			if(xml!=null){
				xml = xml
				.replaceAll("\n", "\\\\n")
				.replaceAll("\r", "\\\\r")
				.replaceAll("\"", "\\\\\"");
			}
				
			src.append(sep + (xml!=null ? ("\"" + xml + "\"") : "null"));
			sep=", \n";
		}
		
		log(methodName, message.toString(), src.toString());
	}
}

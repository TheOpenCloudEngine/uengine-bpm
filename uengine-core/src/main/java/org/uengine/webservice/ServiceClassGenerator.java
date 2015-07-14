package org.uengine.webservice;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.MessageDefinition;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ReceiveActivity;
import org.uengine.kernel.UEngineException;


import java.util.*;
import java.io.*;

/**
 * @author Jinyoung Jang
 */

public class ServiceClassGenerator{

	public static String generateSource(ProcessDefinition proc, Map options, PrintStream out) throws Exception{		
//		Vector receiveActivities = new Vector();		
//		gatherReceiveActivities(proc, receiveActivities);
		
		String procName;
		boolean isInterface = options.containsKey("isInterface");
		
		if(options.containsKey("className"))
			procName = options.get("className").toString();
		else{
			procName = proc.getName().replace(' ', '_');
	
			if(!isInterface)
				procName = procName + "SoapBindingImpl";
		}
				
		if(options.containsKey("packageName") && options.get("packageName").toString().trim().length()>0){
			out.println("package " + options.get("packageName").toString() + ";");
			out.println();
		}
		
		out.println("import org.uengine.uengine.*;");
		out.println();
		out.println("public " + (isInterface ? "interface ":"class ") + procName + (isInterface ? "{":" extends org.uengine.uengine.ProcessWebService implements "+ options.get("interface") +"{"));
		out.println();
		if(!isInterface){
			out.println("	public "+ procName +"(){}");
			out.println();
		}
		
		MessageDefinition starterMessage = getStarterMessage(proc); 
		if(starterMessage == null){
			out.print("	public String start() throws java.rmi.RemoteException");
		
			if(isInterface) 
				out.println(";");
			else{
				out.println("{");
				out.println("		try{");
				out.println("			return _start(\"" + proc.getBelongingDefinitionId() + "\");");
				out.println("		}catch(Exception e){");
				out.println("			throw new java.rmi.RemoteException(\"uengine exception\", e);");
				out.println("		}");
				out.println("	}");
			}
			out.println();
		}
		
		MessageDefinition[] messageDefinitions = proc.getMessageDefinitions();
/*		for(Enumeration enum = receiveActivities.elements(); enum.hasMoreElements();){
			ReceiveActivity act = (ReceiveActivity)enum.nextElement();
			ParameterContext[] vars = act.getParameters();
			ProcessVariable var = act.getOutput();

			if(act.getMessage()==null) continue; 
			String messageName = act.getMessage().replace(' ', '_');
*/
		for(int j=0; j<messageDefinitions.length; j++){
			
			ParameterContext[] vars = messageDefinitions[j].getParameters();
			ProcessVariable var = null;
			String messageName = messageDefinitions[j].getName();			
			
			boolean bCustomizedStarter = false;
			if(starterMessage!=null && messageDefinitions[j].equals(starterMessage)){
				bCustomizedStarter = true;
			}
			
			out.print("	public " + (bCustomizedStarter ? "String on" + messageName + "(" : "void on" + messageName + "(String instanceId" ));

			if(vars!=null){
				String sep = (bCustomizedStarter ? "":", ");
				for(int i=0; i<vars.length; i++){
					try{
						ProcessVariable pvd = vars[i].getVariable();
						Class type = null;
						if(pvd!=null){
							type = pvd.getType();
						}else{
							type = (Class)vars[i].getType();
						}
						
						//review: messagedefinition need a field, which contains parameter type in Class
						out.print( sep + getActualTypeName(type) + " " + vars[i].getArgument());
						sep = ", ";
					}catch(Exception e){
						throw new UEngineException("Can't resolve type information of message '" + messageName + "', property '" + vars[i].getArgument()+"'");
					}
				}
			}else if(var!=null){				
				out.print(", " + getActualTypeName(var.getType()) + " " + var.getName());
			}
			
			out.print(") throws java.rmi.RemoteException");
			
			if(isInterface){
				out.println(";");
			}else{
				out.println("{");
				out.println("		try{");
				
				if(bCustomizedStarter ){
					out.println("			String instanceId = _startAndSendMessage(new Object[]{\"" + proc.getName() + "\", \"" + messageName +"\"");
				}else
					out.print  ("			_sendMessage(new Object[]{instanceId, \"" + messageName +"\"");
				
				if(vars!=null){
					for(int i=0; i<vars.length; i++){
						try{
							ProcessVariable pvd = vars[i].getVariable();
							Class type = null;
							if(pvd!=null){
								type = pvd.getType();
							}else{
								type = (Class)vars[i].getType();
							}
						
							out.print(", " + convert2Object(type, vars[i].getArgument().getText()));
							
						}catch(Exception e){
							throw new UEngineException("Can't resolve type information of message '" + messageName + "', property '" + vars[i].getArgument()+"'");
						}
					}
				}else{
					if(var!=null)
						out.print(", " + convert2Object(var.getType(), var.getName()));	
				}
				
				out.println("});");
				
				if(bCustomizedStarter ){
					out.println("			return instanceId;");
				}

				out.println("		}catch(Exception e){");
				out.println("			throw new java.rmi.RemoteException(\"uengine exception\", e);");
				out.println("		}");
				out.println("	}");
			}
		}		
		
		out.println("}");
		
		return procName;
	}
	
	private static MessageDefinition getStarterMessage(ProcessDefinition proc){
		Activity act = (Activity)proc.getChildActivities().get(0);
		if(act instanceof ReceiveActivity){
			ReceiveActivity rcvAct = (ReceiveActivity)act;
			return rcvAct.getMessageDefinition();
		}		
		return null;
	}
	
	private static void gatherReceiveActivities(Activity act, Vector receiveActivities){
		if(act instanceof ReceiveActivity){
			receiveActivities.add(act);
		}else
		if(act instanceof ComplexActivity){		
			for(Activity activity : ((ComplexActivity)act).getChildActivities()){
				
				gatherReceiveActivities(activity, receiveActivities);
			}
		}
	}
	
	public static String getActualTypeName(Class type){
		if(type == Integer.class) return "int";
		if(type == Long.class) return "long";
		if(type == Float.class) return "float";
		if(type == Double.class) return "double";
		if(type == Boolean.class) return "boolean";

		String typeName = type.getName();
		if(type.isArray()){
			typeName = typeName.substring(2, typeName.length()-1) + "[]";
		}
		
		return typeName;
	}
	
	public static String convert2Object(Class type, String variable){
		if(type == Integer.class) return "new Integer(" + variable + ")";
		if(type == Long.class) return "new Long(" + variable + ")";
		if(type == Float.class) return "new Float(" + variable + ")";
		if(type == Double.class) return "new Double(" + variable + ")";
		if(type == Boolean.class) return "new Boolean(" + variable + ")";
		
		return variable;		
	}
		
}
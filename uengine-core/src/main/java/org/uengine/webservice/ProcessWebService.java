package org.uengine.webservice;

import org.uengine.processmanager.*;

import javax.naming.InitialContext;
import java.io.*;
import java.util.Vector;

/**
 * @author Jinyoung Jang
 */

public class ProcessWebService{

	public Object getProcessVariable(String k){
		return null;
	}
	
	protected String _start(String definitionName) throws Exception{
		InitialContext jndiCtx = new InitialContext();
		
		ProcessManagerHomeRemote pmh = (ProcessManagerHomeRemote)jndiCtx.lookup("ProcessManagerHomeRemote");
		ProcessManagerRemote pm = pmh.create();	
		
		definitionName = pm.getProcessDefinitionProductionVersionByName(definitionName); //use production version
		String instId = pm.initializeProcess(definitionName);
		pm.executeProcess(instId);
		pm.applyChanges();
		
		return instId;
	}

	protected static Serializable _sendMessage(Object[] context) throws Exception{
		String instanceId = null;
		String message = null;
		Serializable data = null; 
		
		try{
			instanceId = (String)context[0];
			message = (String)context[1];

//old version is depricated on 2004.9.20.
/*			if(context.length <= 3){ //single argument
				data = (Serializable)context[2];
			}else{*/
			Vector arguments = new Vector();
			
			for(int i=2; i<context.length; i++)
				arguments.add(context[i]);
				
			data = arguments;
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(message==null || instanceId==null){
System.out.println("ProcessWebService::_sendMessage: instanceId or message is null");
			 return null;
		}
					
		InitialContext jndiCtx = new InitialContext();
		
		ProcessManagerHomeRemote pmh = (ProcessManagerHomeRemote)jndiCtx.lookup("ProcessManagerHomeRemote");
		ProcessManagerRemote pm = pmh.create();
		
		Serializable result = pm.sendMessage(instanceId, message, data);
		
		pm.applyChanges();
		
		return result;
	}
	
	protected static String _startAndSendMessage(Object[] context) throws Exception{
		String message = null;
		String definitionName = null;
		Serializable data = null; 
		
		try{
			definitionName = (String)context[0];
			message = (String)context[1];

//old version is depricated on 2004.9.20.
/*			if(context.length <= 3){ //single argument
				data = (Serializable)context[2];
			}else{*/
			Vector arguments = new Vector();
			
			for(int i=2; i<context.length; i++)
				arguments.add(context[i]);
				
			data = arguments;
//			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(message==null || definitionName==null){
System.out.println("ProcessWebService::_sendMessage: instanceId or message is null");
			 return null;
		}
					
		InitialContext jndiCtx = new InitialContext();
		ProcessManagerHomeRemote pmh = (ProcessManagerHomeRemote)jndiCtx.lookup("ProcessManagerHomeRemote");
		ProcessManagerRemote pm = pmh.create();
		
		
		definitionName = pm.getProcessDefinitionProductionVersionByName(definitionName); //use production version
		String instId = pm.initializeProcess(definitionName);
		pm.executeProcess(instId);


		Serializable result = pm.sendMessage(instId, message, data);
		
		pm.applyChanges();
		
		return instId;
	}
}
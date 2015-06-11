/*
 * Created on 2004-05-31
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.webservice;

import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessDefinitionFactory;
import org.uengine.kernel.ServiceDefinition;


import java.util.*;
import java.io.*;

/**
 * @author Jinyoung Jang
 *
 * This tool will generates a 'properties' file containing sequences for the axis stub generation command
 * the properties file will be used for interfacing with ant tool
 * the corresponding script in 'build.xml' will parse and interpret this command.
 */
public class StubGenerationCommandGenerator {
	final static String COMMAND_FILE = "command.properties";
	
	public static void generateStubCommand(ProcessDefinition definition) throws Exception{		
		Properties command = new Properties();
		command.setProperty("wsdlLocations.length", ""+definition.getServiceDefinitions().length);
		
		for(int i=0; i<definition.getServiceDefinitions().length; i++){
			ServiceDefinition sd = definition.getServiceDefinitions()[i];
			command.setProperty("wsdlLocation" + i, sd.getWsdlLocation());
			command.setProperty("name" + i, ""+sd.getName());
		}
		
		command.store(new FileOutputStream(COMMAND_FILE), "");
	}
	
	public static void main(String[] args) throws Exception{
		ProcessDefinition definition = ProcessDefinitionFactory.loadDefinitionFromFile(args[0]);
		generateStubCommand(definition);
	}
}

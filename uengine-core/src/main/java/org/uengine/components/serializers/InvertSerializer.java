package org.uengine.components.serializers;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Serializer;


/**
 * @author Jinyoung Jang
 */

public class InvertSerializer implements Serializer{

	public boolean isSerializable(Class cls){
		return (ProcessDefinition.class == cls);
	}
	
	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{
//		Adapter apt = new ProcessDefinitionAdapter();
//		ProcessDefinition proc = (ProcessDefinition)apt.convert(sourceObj, extendedContext);
//		GlobalContext.serialize(proc, os, String.class);
		//GlobalContext.serialize(proc, new PrintStream(System.out), String.class);
	}
	
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
		return null;
	}
}
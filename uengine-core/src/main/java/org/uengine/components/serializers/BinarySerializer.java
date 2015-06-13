package org.uengine.components.serializers;

import org.uengine.kernel.Serializer;

import java.io.*;
import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class BinarySerializer implements Serializer{

	public boolean isSerializable(Class srcCls){
		return true;
	}
	
	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{

		ObjectOutputStream objectOutPut = new ObjectOutputStream( os);
	
		objectOutPut.writeObject( sourceObj);
	
		objectOutPut.flush();
		objectOutPut.close();
		os.close();
	}
	
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
	
		ObjectInputStream input = new ObjectInputStream(is);
	
		Object obj = input.readObject();
		
		input.close();
		is.close();
		
		return obj;
	}
}
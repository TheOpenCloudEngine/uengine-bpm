package org.uengine.kernel;

import java.io.*;
import java.util.Hashtable;
//import javax.xml.namespace.*;

/**
 * @author Jinyoung Jang
 */

public interface Serializer{

	public boolean isSerializable(Class cls);
	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception;
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception;
	
}
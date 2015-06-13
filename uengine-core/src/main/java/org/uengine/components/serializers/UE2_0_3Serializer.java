package org.uengine.components.serializers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import org.uengine.kernel.ActivityRepository;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.Serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * @author Jinyoung Jang
 */

public class UE2_0_3Serializer implements Serializer{
	XStream xstream;// = new XStream(/*new DomDriver()*/);

	public UE2_0_3Serializer(){
		xstream = new XStream(/*new DomDriver()*/);
	}
	
	public boolean isSerializable(Class cls){
		return true;
	}
	
	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{

		if(sourceObj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)sourceObj).beforeSerialization();

		try{
			xstream.toXML(sourceObj, new OutputStreamWriter(os, "UTF-8"));
			//
		}catch(Exception ex){
			throw ex;
		}finally{
			try{os.close();}catch(Exception exx){};
		}
	}
	
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
		Object obj = null;

		try{
			obj = xstream.fromXML(new InputStreamReader(is, "UTF-8"));
		}catch(Exception e){
			throw e;
		}finally{
			try{is.close();}catch(Exception exx){};
		}
		
		if(obj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)obj).afterDeserialization();
		
		return obj;
	}
	

	
}


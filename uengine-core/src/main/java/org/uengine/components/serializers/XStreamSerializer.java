package org.uengine.components.serializers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

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

public class XStreamSerializer implements Serializer{
	public XStream xstream;// = new XStream(/*new DomDriver()*/);

	public XStreamSerializer(){
		xstream = new XStream(/*new DomDriver()*/);
		
//		if("true".equals(GlobalContext.getPropertyString("xstream.compatibility.for.jdk1.4.1", "true"))){
//			xstream.registerConverter(new ActivityRepositoryConverter(xstream.getClassMapper()));
//		}
	}
	
	public boolean isSerializable(Class cls){
		return true;
	}
	
	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{


		if(sourceObj instanceof Collection){
			for(Object value : ((Collection)sourceObj)){

				if(value instanceof NeedArrangementToSerialize)
					((NeedArrangementToSerialize)value).beforeSerialization();

			}
		}else

		if(sourceObj instanceof Map){
			for(Object value : ((Map)sourceObj).values()){
				if(value instanceof NeedArrangementToSerialize)
					((NeedArrangementToSerialize)value).beforeSerialization();

			}
		}else

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
			//xstream.setClassLoader(getClass().getClassLoader().loadClass("com.acme.TestClass"));
			//GlobalContext.class.getClassLoader().loadClass("com.acme.TestClass");
			
			obj = xstream.fromXML(new InputStreamReader(is, "UTF-8"));
		}catch(Exception e){
			throw e;
		}finally{
			try{is.close();}catch(Exception exx){};
		}

		if(obj instanceof Collection){
			for(Object value : ((Collection)obj)){

				if(value instanceof NeedArrangementToSerialize)
					((NeedArrangementToSerialize)value).afterDeserialization();

			}
		}else

		if(obj instanceof Map){
			for(Object value : ((Map)obj).values()){
				if(value instanceof NeedArrangementToSerialize)
					((NeedArrangementToSerialize)value).afterDeserialization();

			}
		}else

		if(obj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)obj).afterDeserialization();
		
		return obj;
	}
	
	public static void main(String args[]) throws Exception{
/*		Object test = GlobalContext.deserialize(new FileInputStream("Y:\\src\\servlet_home\\webapps\\uengine-web\\WEB-INF\\classes\\procdef\\629.upd"), "XStream");
		
		System.out.println(test);
*/		
		
//		ProcessDefinition proc = new ProcessDefinition();
//		proc.addChildActivity(new DefaultActivity("test"));
//		
//		FileOutputStream fos = new FileOutputStream(new File("c:\\test.xml"));
//		GlobalContext.serialize(proc, fos, String.class);
//		fos.close();
//		Object test = GlobalContext.deserialize(new FileInputStream("c:\\test.xml"), "XStream");
		
//		System.out.println(test);
	
		(new XStreamSerializer()).find(args[0]);
//		(new XStreamSerializer()).convert("c:\\temp\\procdef\\491.upd");
	}
	
	
	protected static void convert(String src, Serializer sourceSerialzer, Serializer targetSerializer) throws Exception{
		String srcXMLPath = src;
		String targetXMLPath = srcXMLPath.split("\\.")[0] + ".xpd";
		
		try{
			//System.out.println(srcXMLPath + " to " + targetXMLPath + ".");

			Object srcObj = sourceSerialzer.deserialize(new java.io.FileInputStream(srcXMLPath), null);
			
			ProcessDefinition def = (ProcessDefinition)srcObj;				
				
			
			System.out.println(src+"="+def.getName());
			//targetSerializer.serialize(srcObj, new java.io.FileOutputStream(targetXMLPath), null);
		}catch(Exception e){
			//e.printStackTrace();
		}

	}
	
    protected static void find(String s) throws Exception
    {
    	XStreamSerializer sourceSerializer = new XStreamSerializer();
    	sourceSerializer.xstream = new XStream();
    	XStreamSerializer targetSerializer = new XStreamSerializer();
    	
        File file = new File(s);
        File afile[] = file.listFiles();
        for(int i = 0; i < afile.length; i++){
            if(afile[i].getName().endsWith(".upd")){
                convert(afile[i].getPath(), sourceSerializer, targetSerializer);
            }else
            if(afile[i].isDirectory())
                find(afile[i].getPath());
        }
    }
	
}



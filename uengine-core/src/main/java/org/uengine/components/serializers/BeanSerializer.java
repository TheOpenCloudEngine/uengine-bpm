package org.uengine.components.serializers;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.NeedArrangementToSerialize;
import org.uengine.kernel.Serializer;

import java.io.*;
import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.beans.XMLDecoder;
import java.util.Hashtable;

/**
 * @author Jinyoung Jang
 */

public class BeanSerializer implements Serializer{

	public boolean isSerializable(Class cls){
		return true;
	}
	
	//TODO ����ս� ��f... 
	public synchronized void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{

		try{
			final ClassLoader urlClassLoader = GlobalContext.getComponentClassLoader();
			/* this will replace the default system class loader with the new custom classloader, so that XMLEncoder will use the new custom classloader to lookup a class */
			Thread.currentThread().setContextClassLoader(urlClassLoader);
		} catch (Exception e) {
System.out.println("can't replace classloader");
		}

		if(sourceObj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)sourceObj).beforeSerialization();


		//final boolean bErrorOut = sourceObj!=null && (sourceObj instanceof java.util.Vector);
		final boolean bErrorOut = true;
		XMLEncoder e = null;
		
		try{
			e = new XMLEncoder(new BufferedOutputStream(os));
			e.setExceptionListener(new ExceptionListener(){
				public void exceptionThrown(Exception e) {
					//do nothing				
				
					if(bErrorOut){
						//e.printStackTrace();
					}
				}
			});

//			Class encClass=null;
	/*		try{
	System.out.println("BeanSerializer::serialize :  using external jar");
				//encClass = sourceObj.getClass().getClassLoader().loadClass("java.beans.XMLEncoder");
				encClass = GlobalContext.getComponentClass("java.beans.XMLEncoder");	
			}catch(Exception e){
	System.out.println("BeanSerializer::serialize :  using internal classloader");
				encClass = Thread.currentThread().getContextClassLoader().loadClass("java.beans.XMLEncoder");
			}
		
			XMLEncoder e = (XMLEncoder)encClass.getConstructor(new Class[]{OutputStream.class})
					.newInstance(new Object[]{new BufferedOutputStream(os)});
	*/		
			if(sourceObj!=null){
				synchronized(sourceObj){//TODO [performance]: this may effect performance. if it effects too much, get rid of this synchronization block and make a copy of the target object to avoid the synchronization handling.  
					e.writeObject( sourceObj);
				}
			}else{
				e.writeObject(sourceObj);			
			}
		}catch(Exception ex){
			throw ex;
		}finally{
			try{e.close();}catch(Exception exx){};
			try{os.close();}catch(Exception exx){};
		}
	}
	
	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
		Object obj = null;
		XMLDecoder d = null;

		try{
			d = new XMLDecoder( new BufferedInputStream( is));		
			obj = d.readObject();
		}catch(Exception e){
			throw e;
		}finally{
			try{d.close();}catch(Exception exx){};
			try{is.close();}catch(Exception exx){};
		}
		
		if(obj instanceof NeedArrangementToSerialize)
			((NeedArrangementToSerialize)obj).afterDeserialization();
		
		return obj;
	}
}
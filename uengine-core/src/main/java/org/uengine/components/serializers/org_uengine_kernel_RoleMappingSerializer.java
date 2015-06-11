/*
 * Created on 2005. 3. 10.
 */
package org.uengine.components.serializers;

import java.io.*;
import java.util.*;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.RoleMapping;
import org.uengine.kernel.Serializer;
import org.uengine.util.UEngineUtil;

/**
 * @author Jinyoung Jang
 */
public class org_uengine_kernel_RoleMappingSerializer implements Serializer{

	public boolean isSerializable(Class cls){
		return (RoleMapping.class == cls);
	}
	
	public void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{
	//public synchronized void serialize(Object sourceObj, OutputStream os, Hashtable extendedContext) throws Exception{
		RoleMapping roleMapping = (RoleMapping)sourceObj;
		Properties props = roleMapping.getExtendedProperties();
		
		Enumeration keys = null;
		
		if(props!=null)
			keys = props.keys();
		//ByteArrayOutputStream bao = new ByteArrayOutputStream();
		
		PrintWriter pw = null;
		
		try{
			pw = new PrintWriter(os);
		
			String sep = ",";
			String endpoint = roleMapping.getEndpoint();
			String emailAddress = roleMapping.getEmailAddress();
			String name = roleMapping.getName();
			String resourceName = roleMapping.getResourceName();
		
			if(endpoint!=null){			
				pw.print("Endpoint=" + endpoint);
			}

			if(name!=null){			
				pw.print(sep + "Name=" + name);
			}				
				
			if(emailAddress!=null)			
				pw.print(sep + "EmailAddress=" + emailAddress);
				
			if(resourceName!=null)			
				pw.print(sep + "ResourceName=" + resourceName);
			
			if(keys!=null)
				while(keys.hasMoreElements()){
					String key = (String)keys.nextElement();
					pw.print(sep + "ext_"+ key + "=" + props.getProperty(key, ""));
				}
			
		}catch(Exception e){
			throw e;
		}finally{
			pw.close();
			os.close();		
		}
	}

	public Object deserialize(InputStream is, Hashtable extendedContext) throws Exception{
		try{
			byte[] bytes = new byte[1024]; 
			int c; 
			int total_bytes=0; 
				
			ByteArrayOutputStream bao = new ByteArrayOutputStream(); 
				
			while((c=is.read(bytes)) !=-1) 
			{ 
					total_bytes +=c; 
					bao.write(bytes,0,c); 
			} 
				
			is.close();
			
			String[] keysAndValues = bao.toString(GlobalContext.DATABASE_ENCODING).split(",");
			if(keysAndValues.length==0)
				return null;
			
			RoleMapping roleMapping = RoleMapping.create();
			
			for(int i=0; i<keysAndValues.length; i++){
				String[] keyAndValue = keysAndValues[i].split("=");
				
				if(keyAndValue==null || keyAndValue.length < 2){
					continue;
				}
				
				String key = keyAndValue[0];
				String value = keyAndValue[1];
				
				if(!UEngineUtil.isNotEmpty(key) || !UEngineUtil.isNotEmpty(key)){
					continue;
				}
				
				if(key.startsWith("ext_")){
					key = key.substring("ext_".length()); //TODO: [performance]
					roleMapping.setExtendedProperty(key, value); 
				}else{
					if(key.equals("Endpoint")){
						roleMapping.setEndpoint(value);
					}else if(key.equals("EmailAddress")){
						roleMapping.setEmailAddress(value);
					}else if(key.equals("Name")){
						roleMapping.setName(value);
					}else if(key.equals("ResourceName")){
						roleMapping.setResourceName(value);
					}
				}			
			}
			
			return roleMapping;
			
		}catch(Exception e){
			throw e;
		}finally{
			is.close();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		RoleMapping rm = RoleMapping.create();
		
//		rm.setName("drafter");
//		rm.setEndpoint("aaa");
//		rm.setEmailAddress("aaa@abc.com");
//		rm.setResourceName("");
//		rm.setExtendedProperty("loginName", "uengine");
//		rm.setExtendedProperty("deptId", "");
		
		GlobalContext.serialize(rm, System.out, RoleMapping.class);
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		GlobalContext.serialize(rm, bao, RoleMapping.class);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bao.toString().getBytes(GlobalContext.DATABASE_ENCODING));
		RoleMapping rm2 = (RoleMapping)GlobalContext.deserialize(bis, RoleMapping.class);
		
	}
	
}

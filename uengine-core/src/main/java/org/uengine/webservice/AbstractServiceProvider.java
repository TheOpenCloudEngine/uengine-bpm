package org.uengine.webservice;

import java.lang.reflect.*;

import org.uengine.kernel.UEngineException;

/**
 * @author Jinyoung Jang
 */

public abstract class AbstractServiceProvider implements ServiceProvider{
	Object stub;

	abstract public Object getStub(String endpoint) throws Exception;

	public Object invokeService(String endpoint, String operationName, Object [] parameters) throws Exception{
		
/*		Vector clsList = new Vector();
		for(int i=0; i<parameters.length; i++){
			clsList.add(parameters[i].getClass());
		}
					
		Class[] arrClsList = new Class[clsList.size()];
		clsList.toArray(arrClsList);
//System.out.println("WebServiceActivity: sp="+sp+"finding method: "+operationName);

for(int i=0; i<arrClsList.length; i++){
System.out.println("arrClsList is "+arrClsList[i]);
}*/
		Object stub = getStub(endpoint);
		Method method = null;// = stub.getClass().getMethod(operationName, arrClsList);
		if(method==null){
			Method methods[] = stub.getClass().getMethods();
			for(int i=0; i<methods.length; i++){
				if(methods[i].getName().equals(operationName)){
					if(methods[i].getParameterTypes().length == parameters.length){
						method = methods[i];
						break;
					}
				}
			}
		}
		
		if(method == null)
			throw new UEngineException("No such operation ["+ operationName +"] or mismatch with the parameters to Web Service invoke.");
		
		Object[] actualParameters = new Object[method.getParameterTypes().length];		
		for(int i=0; i<actualParameters.length; i++){
			if(parameters!=null && parameters.length > i){
System.out.println("	parameter[" + i + "] = " + parameters[i]);
				actualParameters[i] = parameters[i];
			}else{
System.out.println("	parameter[" + i + "] = null");
				actualParameters[i] = null;
			}
		}

System.out.println("	stub = " + stub + "  method = " + method);
		Object res = method.invoke(stub, actualParameters);
		
		return res;
	}
}
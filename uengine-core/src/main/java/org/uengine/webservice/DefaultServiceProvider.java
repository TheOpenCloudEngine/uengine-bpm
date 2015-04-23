package org.uengine.webservice;

import org.uengine.kernel.ServiceDefinition;
import org.uengine.kernel.UEngineException;


import java.lang.reflect.*;

public class DefaultServiceProvider extends org.uengine.webservice.AbstractServiceProvider{

	/**
	 * 
	 * @uml.property name="serviceDefinition"
	 * @uml.associationEnd 
	 * @uml.property name="serviceDefinition" multiplicity="(1 1)"
	 */
	ServiceDefinition serviceDefinition;

	String portType;

	public DefaultServiceProvider(ServiceDefinition serviceDefinition, String portType){
		this.serviceDefinition = serviceDefinition;
		this.portType = portType;
	}
	
	public Object getStub(String endpoint) throws Exception{
		String stubPkgName = serviceDefinition.getStubPackage();
		String clsName = stubPkgName + "." + portType + "ServiceLocator";

		try{
			Class svcLocatorCls = Thread.currentThread().getContextClassLoader().loadClass(clsName);
			Object svcLocator = svcLocatorCls.newInstance();
			Method stubGetterMethod = svcLocatorCls.getMethod("get" + portType, new Class[]{java.net.URL.class});
			Object stub = stubGetterMethod.invoke(svcLocator, new Object[]{new java.net.URL(endpoint)});
		
			return stub;
		}catch(java.net.MalformedURLException e){
			throw new UEngineException("Malformed endpoint URI:" + endpoint, e);
		}catch(ClassNotFoundException cne){
			throw new UEngineException("There's no stub class:" + clsName, cne);
		}catch(NoSuchMethodException nsme){
			throw new UEngineException("Wrong portType:" + portType, nsme);
		}catch(Exception ex){
			throw new UEngineException("Exception occurred during ws-invocation", ex);
		}
	}
}

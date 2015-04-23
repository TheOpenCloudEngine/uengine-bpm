package org.uengine.processdesigner.mapper.transformers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.uengine.kernel.DefaultProcessInstance;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;
import org.uengine.processdesigner.mapper.Transformer;

public class BeanValueTransformer extends Transformer{
	
	String className;
		public String getClassName() {
			return className;
		}
		public void setClassName(String className) {
			this.className = className;
			
			try {
				theClass = GlobalContext.loadClass(className);
				
				getterMethods = new HashMap();
				Vector outputNamesVt = new Vector();
				
				Method [] allMethods = theClass.getMethods();
				for(int i=0; i<allMethods.length; i++){
					Method thisMethod = allMethods[i];
					if(thisMethod.getName().startsWith("get") && thisMethod.getParameterTypes().length==0){
						String propName = thisMethod.getName().substring(3);
						getterMethods.put(propName, thisMethod);
						outputNamesVt.add(propName);
					}
				}

				outputNames = new String[outputNamesVt.size()];
				outputNamesVt.toArray(outputNames);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	@Override
	public Object transform(ProcessInstance instance, Map parameterMap, Map options) {
		
		Object result = parameterMap.get(getInputArguments()[0]);
		
		Object tempRole = null;

		if(getterMethods==null){
			setClassName(getClassName());
		}				

		if(RoleMapping.class.isAssignableFrom(theClass) && result instanceof String){
			String endpoint = (String)result;
			RoleMapping rm = RoleMapping.create();
			rm.setEndpoint(endpoint);
			result = rm;			
		}
		
		boolean isRoleMappingClass = false;
		if(result instanceof RoleMapping){
			isRoleMappingClass = true;
			tempRole = result;
		}
		
		if(instance.getProcessTransactionContext().getSharedContext("resultForTransformers")==null){
			instance.getProcessTransactionContext().setSharedContext("resultForTransformers", new HashMap());
		}
		HashMap resultsForTransformers = (HashMap) instance.getProcessTransactionContext().getSharedContext("resultForTransformers");
		
		if(!resultsForTransformers.containsKey(this)){
			resultsForTransformers.put(this, result);
			
			if(result instanceof RoleMapping){
				RoleMapping rm = (RoleMapping)result;
				try {
					rm.beforeFirst();
					rm.fill(instance);
					String endpoint = rm.getEndpoint();
					result = rm;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}
		
		if(getterMethods==null){
			setClassName(getClassName());
		}	
		
		String outputArgumentName = (String)options.get(OPTION_KEY_OUTPUT_ARGUMENT);
		
		Method getter = (Method) getterMethods.get(outputArgumentName);
		if(getter!=null){
			try {
				result = getter.invoke(result, new Object[]{});
				if(result == null && isRoleMappingClass){
					RoleMapping rm = (RoleMapping)tempRole;
					rm.beforeFirst();
					rm.fill(instance);
					result = getter.invoke(rm, new Object[]{});
				}
				
				return result;
			} catch (ClassCastException cce) {
				throw new RuntimeException("The value [" + result + "(" + (result != null ? result.getClass() : null) + ")] is not compatible with target class [" + theClass + "]", cce);
			} catch (Exception e){
				throw new RuntimeException("Error when to resolve value in BeanValueTransformer", e);				
			}
		}
		
		throw new RuntimeException("Can't resolve such resolver: "+outputArgumentName);
		
	}

	@Override
	public String[] getInputArguments() {
		return new String[]{"in"};
	}

	@Override
	public String[] getOutputArguments() {
		return outputNames;
	}


	transient Class theClass;
	transient HashMap getterMethods;
	String[] outputNames = new String[]{};
	

	public static void main(String args[]) throws Exception{
		RoleMapping rm = RoleMapping.create();
		rm.setResourceName("jjy");
		rm.moveToAdd();
		rm.setResourceName("kbs");
		
		BeanValueTransformer bvt = new BeanValueTransformer();
		bvt.setClassName("org.uengine.kernel.RoleMapping");
		bvt.getArgumentSourceMap().put("in", "rolemapping");
		
		ProcessInstance.USE_CLASS = DefaultProcessInstance.class;
		
		ProcessInstance pi = DefaultProcessInstance.create();
		pi.set("", "rolemapping", rm);
		
		Object output = bvt.letTransform(pi, "ResourceName");
		
		System.out.println(output);
		
	}
	
}

package org.uengine.persistence.processvariable;

import java.util.Iterator;

public class DB2ProcessVariableDAOType extends ProcessVariableDAOType{

	public void deleteValue(String instanceId, Iterator keyStrings) throws Exception {
		
		if(keyStrings!=null){
			while(keyStrings.hasNext()){
				ProcessVariableDAO processVariableToDelete = createDAOImpl(deleteValue_SQL);
	
				processVariableToDelete.setKeyString((String)keyStrings.next());
				processVariableToDelete.setInstId(new Long(instanceId));

				processVariableToDelete.update();				
			}
		}
	}
	

}

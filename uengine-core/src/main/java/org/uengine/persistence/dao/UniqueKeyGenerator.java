/*
 * Created on 2005. 1. 25.
 */
package org.uengine.persistence.dao;

import org.uengine.util.dao.ConnectionFactory;

/**
 * @author Jinyoung Jang
 */
public class UniqueKeyGenerator {

	public static Long issueKey(String forWhat, ConnectionFactory cf) throws Exception{
		KeyGeneratorDAO kg = DAOFactory.getInstance(cf).createKeyGenerator(forWhat, null);
		kg.select();
		kg.next();
				
		Number number = kg.getKeyNumber();
		
		if(number!=null){
			return new Long(number.longValue());
		}else		
			return null;		
	}
	
	public static Long issueProcessDefinitionKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcDef", cf);
	}

	public static Long issueProcessDefinitionVersionKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcDefVer", cf);
	}
	
	public static Long issueProcessInstanceKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcInst", cf);
	}

	public static Long issueProcessVariableKey(ConnectionFactory cf) throws Exception{
		return issueKey("ProcVar", cf);
	}

	public static Long issueRoleMappingKey(ConnectionFactory cf) throws Exception{
		return issueKey("RoleMapping", cf);
	}
	
	public static Long issueWorkItemKey(ConnectionFactory cf) throws Exception{
		return issueKey("WorkItem", cf);
	}
}

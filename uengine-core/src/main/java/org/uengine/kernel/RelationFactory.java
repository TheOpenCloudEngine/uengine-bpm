/*
 * Created on 2004-04-02
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.kernel;

import java.util.*;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class RelationFactory {
	static Hashtable relations = new Hashtable();
	
	public static Relation getRelation(String relationName) throws UEngineException{
		try{
			if(!relations.containsKey(relationName)){
				Class cls = GlobalContext.getComponentClass("org.uengine.components.relations." + relationName + "Relation");
		
				relations.put(relationName, (Relation)cls.newInstance());
			}
			
			Relation relation = (Relation)relations.get(relationName);

			return relation;			
		}catch(Exception e){
			throw new UEngineException("relation '"+relationName+"' was not found");
		}		
	}
}

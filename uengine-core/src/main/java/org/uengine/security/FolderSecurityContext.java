/*
 * Created on 2004. 10. 29.
 */
package org.uengine.security;

/**
 * @author Jinyoung Jang
 */

import java.util.*;
import java.security.acl.*;
import java.security.Principal;


public class FolderSecurityContext{

	String name;
	Collection authorizedUserGroup = new ArrayList();
	public Collection getAuthorizedUserGroup() {
		return authorizedUserGroup;
	}
	public String getName() {
		return name;
	}
	public void setAuthorizedUserGroup(Collection collection) {
		authorizedUserGroup = collection;
	}
	public void setName(String string) {
		name = string;
	}

	
	public Permission permissionTo(Principal principal){
		for(Iterator iter = getAuthorizedUserGroup().iterator(); iter.hasNext();){
			AuthorizedUserGroup au = (AuthorizedUserGroup)iter.next();
			if(au.isMember(principal))
				return au.getPermission();
		}
		
		return null;
	}

}

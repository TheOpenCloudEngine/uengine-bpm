/*
 * Created on 2004. 10. 29.
 */
package org.uengine.security;

/**
 * @author Jinyoung Jang
 */

import java.util.*;
import java.security.Principal;
import java.security.acl.*;

public class AuthorizedUserGroup implements Group{

	public boolean isMember(Principal principal){
		//production implementation should be based on sql-query.
		return true;
	}
		
	Permission permission;
		public Permission getPermission() {
			return permission;
		}
		public void setPermission(Permission permission) {
			this.permission = permission;
		}

	String name;
		public String getName() {
			return name;
		}
		public void setName(String string) {
			name = string;
		}

	Collection userIdentifiers = new ArrayList();
		public Collection getUserIdentifiers() {
			return userIdentifiers;
		}
		public void setUserIdentifiers(Collection list) {
			userIdentifiers = list;
		}
	
	public void addUserIdentifier(String userIdentifier){
		userIdentifiers.add(userIdentifier);
	}

	public boolean addMember(Principal arg0) {
		return false;
	}

	public Enumeration members() {
		return null;
	}

	public boolean removeMember(Principal arg0) {
		return false;
	}
}

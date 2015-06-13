package org.uengine.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.uengine.util.dao.ConnectionFactory;
import org.uengine.util.dao.DefaultConnectionFactory;

public class NoneAclManager extends AclManager {

	@Override
	public HashMap<Character, ArrayList<Character>> getDefaultPermission(int defId) {
		HashMap<Character, ArrayList<Character>> permission = new HashMap<Character, ArrayList<Character>>();
		
		Connection conn	= null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;
		
		try {
			ConnectionFactory dcf = getTransactionContext();
			
			conn = dcf.getConnection();
			sql = " SELECT DEFAULTUSER, PERMISSION FROM BPM_ACLTABLE WHERE DEFID = ? AND DEFAULTUSER IS NOT NULL ";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, defId);
			
			rs = stmt.executeQuery();
			
			ArrayList<Character> listA = new ArrayList<Character>();
			ArrayList<Character> listM = new ArrayList<Character>();
			while (rs.next()) {
				if (PERMISSION_DEFAULT_ANONYMOUS == rs.getString("DEFAULTUSER").charAt(0)) {
					listA.add(rs.getString("PERMISSION").charAt(0));
				} else {
					listM.add(rs.getString("PERMISSION").charAt(0));
				}
			}
			
			permission.put(PERMISSION_DEFAULT_ANONYMOUS, listA);
			permission.put(PERMISSION_DEFAULT_MEMBERS, listM);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (Exception e2) {}
			if (stmt != null) try { stmt.close(); } catch (Exception e2) {}
			
			if (isReleaseContext) {
				try {
					transactionContext.releaseResources();
				} catch (Exception e) {
				}
			}
		}
		
		return permission;
	}
	
	@Override
    public String getPermission(String empCode) {
        return null;
    }
	
	@Override
	public HashMap<Character, Boolean> getPermission(int defId, String empCode) {
		
		HashMap<Character, Boolean> permission = new HashMap<Character, Boolean>();
		
		permission.put(PERMISSION_VIEW, true);
		permission.put(PERMISSION_INITIATE, true);
		permission.put(PERMISSION_EDIT, true);
		permission.put(PERMISSION_MANAGEMENT, true);
		
		return permission;
	}
	
	@Override
    public HashMap<Character, Boolean> getPermission(int defId, String tracingTag, String empCode) {
        // TODO Auto-generated method stub
	    HashMap<Character, Boolean> permission = new HashMap<Character, Boolean>();
        
        permission.put(PERMISSION_VIEW, true);
        permission.put(PERMISSION_INITIATE, true);
        permission.put(PERMISSION_EDIT, true);
        permission.put(PERMISSION_MANAGEMENT, true);
        
        return permission;
    }
    
    @Override
    public HashMap<Character, Boolean> getPermission(int defId, String empCode, String comCode, String parentfolder) {
        // TODO Auto-generated method stub
        HashMap<Character, Boolean> permission = new HashMap<Character, Boolean>();
        
        permission.put(PERMISSION_VIEW, true);
        permission.put(PERMISSION_INITIATE, true);
        permission.put(PERMISSION_EDIT, true);
        permission.put(PERMISSION_MANAGEMENT, true);
        
        return permission;
    }
    
    @Override
    public HashMap<Character, Boolean> getPermission(int defId, String empCode, boolean dummy) {
        HashMap<Character, Boolean> permission = new HashMap<Character, Boolean>();
        
        permission.put(PERMISSION_VIEW, true);
        permission.put(PERMISSION_INITIATE, true);
        permission.put(PERMISSION_EDIT, true);
        permission.put(PERMISSION_MANAGEMENT, true);
        
        return permission;
    }
    
	@Override
	public boolean isPermission(int defId, String empCode, String permission) {
		return true;
	}
	
	@Override
	public boolean hasPermissionEdit(String empCode) {
		return true;
	}

	@Override
	public String getGroupSqlString(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserSqlString(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermission(int defId, List<Authority> authorities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPermission(int defId, String tracingTag, String userType, String userCode, String[] permissions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePermission(int iAclId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removePermissionForDefinistion(int iDefId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDefaultPermission(int defId, String[] permissionA, String[] permissionM) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInherite(String defId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public char getTopPermission(String userId) {
		return PERMISSION_MANAGEMENT;
	}

}

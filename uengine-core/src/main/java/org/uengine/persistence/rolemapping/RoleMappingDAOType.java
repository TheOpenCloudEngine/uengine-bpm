package org.uengine.persistence.rolemapping;

import java.util.Iterator;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.RoleMapping;
import org.uengine.persistence.AbstractDAOType;
import org.uengine.processmanager.TransactionContext;
import org.uengine.util.dao.ConnectiveDAO;

public class RoleMappingDAOType extends AbstractDAOType{
	
	public static RoleMappingDAOType getInstance(TransactionContext ptc) {
		return (RoleMappingDAOType)getInstance(RoleMappingDAOType.class, ptc);
	}
	
	protected RoleMappingDAO createDAOImpl(String sql) throws Exception {
		RoleMappingDAO roleMapping = (RoleMappingDAO)ConnectiveDAO.createDAOImpl(getTransactionContext(), sql, RoleMappingDAO.class);
		return roleMapping;
	}
	
	
	public String findByRootInstanceIdAndEndpoint_SQL =
		"SELECT InstId, AssignType, DispatchOption, DispatchParam1, RoleName, Endpoint, ResName " +
		"FROM BPM_ROLEMAPPING " +
		"WHERE RootInstId = ?RootInstId " +
		"AND endpoint = ?endpoint";

	public String findByInstanceIdAndRoleName_SQL =
		"SELECT InstId, AssignType, DispatchOption, DispatchParam1, RoleName, Endpoint, ResName " +
		"FROM BPM_ROLEMAPPING " +
		"WHERE InstId = ?InstId " +
		"AND RoleName = ?RoleName";
		
	public String removeRoleMapping_SQL = 
		"DELETE FROM BPM_ROLEMAPPING " + 
		"WHERE InstId=?InstId and RoleName=?RoleName";	

	public String insertRoleMapping_SQL = GlobalContext.getSQL("rolemapping.insert");		

	
	public RoleMappingDAO findByInstanceIdAndRoleName (Long instanceId, String roleName) throws Exception {
		RoleMappingDAO roleMapping = createDAOImpl(findByInstanceIdAndRoleName_SQL);
		roleMapping.setInstId(instanceId);
		roleMapping.setRoleName(roleName);
		roleMapping.select();
		if ( roleMapping.size() > 0 ) roleMapping.next();
		
		return roleMapping;
	}
	
	public void removeRoleMapping (String instanceId, String roleName) throws Exception {
		RoleMappingDAO roleMapping = createDAOImpl(removeRoleMapping_SQL);
		roleMapping.setInstId(new Long(instanceId));
		roleMapping.setRoleName(roleName);
		roleMapping.update();
	}
	
	public void removeRoleMappings (String instanceId, Iterator keyStrings) throws Exception {
		RoleMappingDAO roleMapping = createDAOImpl(removeRoleMapping_SQL);

		if(keyStrings!=null){
			boolean addOnce = keyStrings.hasNext();
			while(keyStrings.hasNext()){
				roleMapping.setRoleName((String)keyStrings.next());
				roleMapping.setInstId(new Long(instanceId));
				roleMapping.addBatch();
			}
			
			if(addOnce)
				roleMapping.updateBatch();				
		}
	}
	
	public RoleMappingDAO createDAOForInsertRoleMappingBatch () throws Exception {
		return createDAOImpl(insertRoleMapping_SQL);
	}
	
	public void insertRoleMappingBatch (RoleMappingDAO roleMappingDAO, Long instanceId, Long rootInstanceId, String roleName, RoleMapping roleMapping) throws Exception {
		roleMappingDAO.setInstId(instanceId);
		roleMappingDAO.setRootInstId(rootInstanceId);
		roleMappingDAO.setAssignType(new Long(roleMapping.getAssignType()));
		roleMappingDAO.setDispatchOption(new Long(roleMapping.getDispatchingOption()));
		roleMappingDAO.setRoleName(roleName);
		roleMappingDAO.setEndpoint(roleMapping.getEndpoint());
		roleMappingDAO.setResName(roleMapping.getResourceName());
		roleMappingDAO.setGroupId(roleMapping.getGroupId());
		roleMappingDAO.addBatch();
	}	
	
	public RoleMappingDAO findByRootInstanceIdAndEndpoint(Long rootInstanceId, String endpoint) throws Exception{
		RoleMappingDAO roleMapping = createDAOImpl(findByRootInstanceIdAndEndpoint_SQL);
		roleMapping.setRootInstId(rootInstanceId);
		roleMapping.setEndpoint(endpoint);
		roleMapping.select();
		
		return roleMapping;
	}
	
	public RoleMapping createRoleMapping(RoleMappingDAO roleMappingDAO) {
		RoleMapping roleMapping = RoleMapping.create();
		roleMapping.setEndpoint(roleMappingDAO.getEndpoint());
		roleMapping.setResourceName(roleMappingDAO.getResName());
		roleMapping.setAssignType(roleMappingDAO.getAssignType().intValue());
		roleMapping.setDispatchingOption(roleMappingDAO.getDispatchOption().intValue());
		
		String dispParam1 = roleMappingDAO.getDispatchParam1();
		if(dispParam1!=null){
			roleMapping.setDispatchingParameters(new String[]{dispParam1});
		}
		
		return roleMapping;
	}	
}

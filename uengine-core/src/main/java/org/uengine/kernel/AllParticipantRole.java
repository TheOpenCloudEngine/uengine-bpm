package org.uengine.kernel;

import org.apache.log4j.Logger;

public class AllParticipantRole extends Role {
	
	static AllParticipantRole instance = new AllParticipantRole();
	
	private AllParticipantRole(){
		setName(GlobalContext.getLocalizedMessage("org.uengine.kernel.allparticipantrole.name", "Whole Participants"));		
	}

	public boolean containsMapping(ProcessInstance instance, RoleMapping testingRoleMapping) throws Exception {
		return instance.isParticipant(testingRoleMapping); 
	}
	
	public static AllParticipantRole getInstance(){
		return instance;
	}
	
}

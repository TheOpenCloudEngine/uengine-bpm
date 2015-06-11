package org.uengine.persistence.processdefinition;

import org.uengine.persistence.processdefinitionversion.ProcessDefinitionVersionDAO;

public interface ProductionDefinitionDAO extends ProcessDefinitionDAO, ProcessDefinitionVersionDAO {
	public Number setDefinitionIdForMax(Number definitionId); 
}

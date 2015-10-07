package org.uengine.processadmin;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.modeling.resource.EditorPanel;
import org.uengine.modeling.resource.ResourceNavigator;

@Component
@Scope("prototype")
public class ProcessAdminEditorPanel extends EditorPanel{

	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true)
	public void save(
			@AutowiredFromClient Session session,
			@AutowiredFromClient ProcessVariablePanel processVariablePanel,
			@AutowiredFromClient RolePanel rolePanel,
			@AutowiredFromClient ResourceNavigator resourceNavigator
	) throws Exception {
		super.save(resourceNavigator);
	}

}

package org.uengine.processadmin;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.EditorPanel;

@Component
@Scope("prototype")
public class ProcessAdminEditorPanel extends EditorPanel{

	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true)
	public void save(@AutowiredFromClient Session session) throws Exception {
		super.save();
	}

}

package org.uengine.processadmin;

import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.widget.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.ResourceNavigator;

import javax.annotation.PostConstruct;

@Component
@Scope("prototype")
public class ProcessAdminAddProcessMapPanel extends org.uengine.codi.mw3.model.AddProcessMapPanel{

//	ResourceFile processDefinitions;
//		@Face(displayName="$FileResource")
//		public ResourceFile getProcessDefinitions() {
//			return processDefinitions;
//		}
//		public void setProcessDefinitions(ResourceFile processDefinitions) {
//			this.processDefinitions = processDefinitions;
//		}


	@Override
	@Hidden
	public Label getBpmsNotSupportedLabel() {
		return super.getBpmsNotSupportedLabel();
	}


	ProcessAdminResourceNavigator processAdminResourceNavigator;

		public ProcessAdminResourceNavigator getProcessAdminResourceNavigator() {
			return processAdminResourceNavigator;
		}

		public void setProcessAdminResourceNavigator(ProcessAdminResourceNavigator processAdminResourceNavigator) {
			this.processAdminResourceNavigator = processAdminResourceNavigator;
		}

	@AutowiredFromClient
	public Session session;


	public void load() {

		setProcessAdminResourceNavigator(new ProcessAdminResourceNavigator());
		getProcessAdminResourceNavigator().setProcessAdminResourceControlDelegate(new ResourceControlDelegateForAddingProcessMap());


	}
	
}

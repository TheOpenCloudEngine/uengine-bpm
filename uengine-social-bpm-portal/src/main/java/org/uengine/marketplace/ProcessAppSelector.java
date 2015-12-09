package org.uengine.marketplace;

import org.metaworks.Face;
import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Hidden;
import org.metaworks.widget.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.model.IProcessMap;
import org.uengine.codi.mw3.model.ProcessMapList;
import org.uengine.codi.mw3.model.Session;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.IResource;
import org.uengine.modeling.resource.resources.ClassResource;
import org.uengine.modeling.resource.resources.UrlappResource;
import org.uengine.processadmin.ProcessAdminResourceNavigator;
import org.uengine.processadmin.ResourceControlDelegateForAddingProcessMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProcessAppSelector implements Face<String> {



	ProcessAdminResourceNavigator processAdminResourceNavigator;

		public ProcessAdminResourceNavigator getProcessAdminResourceNavigator() {
			return processAdminResourceNavigator;
		}

		public void setProcessAdminResourceNavigator(ProcessAdminResourceNavigator processAdminResourceNavigator) {
			this.processAdminResourceNavigator = processAdminResourceNavigator;
		}

	@AutowiredFromClient
	public Session session;


	public ProcessAppSelector() {

		setProcessAdminResourceNavigator(new ProcessAdminResourceNavigator());
		getProcessAdminResourceNavigator().setResourceControlDelegate(new ResourceControlDelegateForAddingProcessMap());

		getProcessAdminResourceNavigator().getRoot().filterResources(ClassResource.class);
		getProcessAdminResourceNavigator().getRoot().filterResources(UrlappResource.class);

		try {
			ProcessMapList processMapList = new ProcessMapList();
			processMapList.load(session);
			IProcessMap processMap = processMapList.getProcessMapList();

			List<IResource> resourceList = new ArrayList<IResource>();

			String navigatorRootPath = getProcessAdminResourceNavigator().getRoot().getPath();

			if(processMap.size() > 0){
				while(processMap.next()){
					IResource resource = DefaultResource.createResource(navigatorRootPath + "/"
							+ processMap.getDefId());

					resourceList.add(resource);
				}

				getProcessAdminResourceNavigator().getRoot().filterResources(resourceList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		MetaworksContext metaworksContext = new MetaworksContext();
		metaworksContext.setWhen("addProcess");

		getProcessAdminResourceNavigator().getRoot().initMetaworksContext(metaworksContext);
	}

	@Override
	public void setValueToFace(String value) {

	}

	@Override
	public String createValueFromFace() {
		return null;
	}
}

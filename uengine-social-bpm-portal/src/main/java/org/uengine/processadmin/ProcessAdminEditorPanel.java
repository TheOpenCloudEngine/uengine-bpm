package org.uengine.processadmin;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.StartCodi;
import org.uengine.codi.mw3.model.Employee;
import org.uengine.codi.mw3.model.IEmployee;
import org.uengine.codi.mw3.model.Session;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.modeling.resource.DefaultResource;
import org.uengine.modeling.resource.EditorPanel;
import org.uengine.modeling.resource.ResourceManager;
import org.uengine.modeling.resource.ResourceNavigator;

@Component
@Scope("prototype")
public class ProcessAdminEditorPanel extends EditorPanel{

	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true, when = MetaworksContext.WHEN_EDIT)
	public void save(
			@AutowiredFromClient Session session,
			@AutowiredFromClient ProcessVariablePanel processVariablePanel,
			@AutowiredFromClient RolePanel rolePanel,
			@AutowiredFromClient ResourceNavigator resourceNavigator
	) throws Exception {
		super.save(resourceNavigator);
	}

	@ServiceMethod(callByContent = true)
	public void load() throws Exception {

		if(session.getUser().getEmail()!=null){

			Employee emp = new Employee();
			emp.setEmail(session.getUser().getEmail());
			IEmployee findEmp = emp.findByEmail();

			if (findEmp == null)
				throw new Exception("<font color=blue>Wrong User or Password! forgot?</font>");

			session.setEmployee(findEmp);
			session.fillSession();

			new TenantContext(session.getCompany().getComCode());
		}

		DefaultResource defaultResource = new DefaultResource();
		defaultResource.setPath(getResourcePath());
		MetaworksRemoteService.autowire(defaultResource);

		defaultResource.reopen();
	}

	private Session session;
		public Session getSession() {
			return session;
		}
		public void setSession(Session session) {
			this.session = session;
		}


}

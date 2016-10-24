package org.uengine.processadmin;

import org.metaworks.MetaworksContext;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.Payload;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dao.Database;
import org.metaworks.dao.TransactionContext;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Download;
import org.metaworks.widget.ModalWindow;
import org.oce.garuda.multitenancy.TenantContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.uengine.codi.mw3.StartCodi;
import org.uengine.codi.mw3.model.*;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.modeling.resource.*;

import java.io.FileNotFoundException;
import java.io.IOException;

@Component
@Scope("prototype")
@Order(10)
public class ProcessAdminEditorPanel extends EditorPanel{


	@AutowiredFromClient public Session session;

	@ServiceMethod(keyBinding="Ctrl+S", callByContent = true, when = MetaworksContext.WHEN_EDIT)
	public void save(
			@AutowiredFromClient ProcessVariablePanel processVariablePanel,
			@AutowiredFromClient RolePanel rolePanel,
			@AutowiredFromClient ResourceNavigator resourceNavigator
	) throws Exception {
		super.save();

	}

	String accessToken;
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}



	@ServiceMethod(callByContent = true)
	public void load() throws Exception {

		if(getAccessToken()!=null){

			Session session = new Session();
			session.setUser(new User());
			session.getUser().setEmail(getAccessToken());

			Employee emp = new Employee();
			emp.setEmail(session.getUser().getEmail());
			IEmployee findEmp = emp.findByEmail();

			if (findEmp == null)
				throw new Exception("<font color=blue>Wrong User or Password! forgot?</font>");

			session.setEmployee(findEmp);
			session.fillSession();

			if(GlobalContext.multiTenant)
				new TenantContext(session.getEmployee().getGlobalCom());
		}

		DefaultResource defaultResource = new DefaultResource();
		defaultResource.setPath(getResourcePath());
		MetaworksRemoteService.autowire(defaultResource);

		defaultResource.reopen();
	}


	@ServiceMethod(target=ServiceMethod.TARGET_POPUP)
	public void history() throws Exception {

		IInstance instance = (IInstance) Database.sql(IInstance.class, "select * from bpm_procinst where topicId= ?topicId");
		instance.set("topicId", getResourcePath());
		instance.select();

		if(!instance.next()){
			throw new Exception("No history instance");
		}

		InstanceView instanceView = new InstanceView();

		MetaworksRemoteService.autowire(instanceView);
		instanceView.load(instance);

		MetaworksRemoteService.wrapReturn(new ModalWindow(instanceView, 600, 600, "Feed on " + getResourceName()));

	}


	@Override
	@ServiceMethod(payload = {"resourcePath"}, target=ServiceMethod.TARGET_STICK)
	@org.metaworks.annotation.Order(5)
	public Download download() throws FileNotFoundException, IOException, Exception {

		//if(getResourcePath().endsWith(".method") || getResourcePath().endsWith(".process")){

		if(MetaworksRemoteService.metaworksCall()) {
			Popup popup = new Popup(new ProcessExporter());
			MetaworksRemoteService.wrapReturn(popup);

			return null;
		}
		//}

		//return super.download();

		return super.download();
	}



}

package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.face.RolePanelButton;
import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.kernel.bpmn.view.*;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;

public class SimplePalette extends Palette {
	RolePanel rolePanel = new RolePanel();
		public RolePanel getRolePanel() {
			return rolePanel;
		}

		public void setRolePanel(RolePanel rolePanel) {
			this.rolePanel = rolePanel;
		}

	ProcessVariablePanel processVariablePanel = new ProcessVariablePanel();
		public ProcessVariablePanel getProcessVariablePanel() {
			return processVariablePanel;
		}

		public void setProcessVariablePanel(ProcessVariablePanel processVariablePanel) {
			this.processVariablePanel = processVariablePanel;
		}

	public SimplePalette(){
		
	}

	public SimplePalette(String type){
		this.setName("BPMN Diagram");
		setType(type);

		this.getSymbolList().add((new StartEventView()).createSymbol());
		this.getSymbolList().add((new EndEventView()).createSymbol());
		//this.getSymbolList().add((new DefaultActivityView()).createSymbol());
		this.getSymbolList().add((new GatewayView()).createSymbol());
		this.getSymbolList().add((new InclusiveGatewayView()).createSymbol());
		this.getSymbolList().add((new ParallelGatewayView()).createSymbol());
		this.getSymbolList().add((new HumanActivityView()).createSymbol());
		this.getSymbolList().add((new SubProcessView()).createSymbol());
		//this.getSymbolList().add((new MessageEventView()).createSymbol());
		this.getSymbolList().add((new EscalationEventView()).createSymbol());
		this.getSymbolList().add((new TimerEventView()).createSymbol());

	}
}

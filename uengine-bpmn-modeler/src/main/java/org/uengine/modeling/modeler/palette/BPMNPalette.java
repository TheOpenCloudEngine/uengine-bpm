package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.face.ProcessVariablePanel;
import org.uengine.kernel.bpmn.face.RolePanel;
import org.uengine.kernel.bpmn.view.*;
import org.uengine.kernel.view.*;
import org.uengine.modeling.CompositePalette;
import org.uengine.modeling.Palette;

public class BPMNPalette extends CompositePalette {
	public BPMNPalette(){
		this(null);
	}

	public BPMNPalette(String type){
		super();
		setType(type);

//		this.getSymbolList().add(new PoolView().createSymbol());
//		this.getSymbolList().add((new StartEventView()).createSymbol());
//		this.getSymbolList().add((new EndEventView()).createSymbol());
//		this.getSymbolList().add((new GatewayView()).createSymbol());
//		this.getSymbolList().add((new InclusiveGatewayView()).createSymbol());
//		this.getSymbolList().add((new ParallelGatewayView()).createSymbol());
//		this.getSymbolList().add((new HumanActivityView()).createSymbol());
//		this.getSymbolList().add((new RestWebServiceActivityView()).createSymbol());
//		this.getSymbolList().add((new SubProcessView()).createSymbol());
//		this.getSymbolList().add((new EscalationEventView()).createSymbol());
//		this.getSymbolList().add((new TimerEventView()).createSymbol());
//		this.getSymbolList().add((new ReceiveRestMessageEventActivityView()).createSymbol());

		Palette taskPallet = new TaskPalette();
		Palette eventPallet = new EventPalette();
		Palette gatewayPalette = new GatewayPalette();
//		rolePalette = new RolePalette();
		 processVariablePalette = new ProcessVariablePalette();

		addPalette(eventPallet);
		addPalette(taskPallet);
		addPalette(gatewayPalette);
//		addPalette(rolePalette);
		addPalette(processVariablePalette);

	}

//	RolePalette rolePalette;
//		public RolePalette getRolePalette(){
//			return rolePalette;
//		}
//		public void setRolePalette(RolePalette rolePalette) {
//			this.rolePalette = rolePalette;
//		}

	ProcessVariablePalette processVariablePalette;
		public ProcessVariablePalette getProcessVariablePalette() {
			return processVariablePalette;
		}
		public void setProcessVariablePalette(ProcessVariablePalette processVariablePalette) {
			this.processVariablePalette = processVariablePalette;
		}



}

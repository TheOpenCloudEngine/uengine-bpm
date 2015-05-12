package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.ParallelGatewayView;
import org.uengine.kernel.bpmn.view.GatewayView;
import org.uengine.kernel.bpmn.view.InclusiveGatewayView;
import org.uengine.modeling.Palette;
import org.uengine.modeling.SymbolFactory;

public class GatewayPalette extends Palette {

	public GatewayPalette(){
		this.setName("Gateway");
		initPallet();
	}
	
	@Override
	protected void initPallet() {
		this.getSymbolList().add(SymbolFactory.create(GatewayView.class));
		this.getSymbolList().add(SymbolFactory.create(ParallelGatewayView.class));
		this.getSymbolList().add(SymbolFactory.create(InclusiveGatewayView.class));
	}

}

package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.ParallelGatewayView;
import org.uengine.kernel.bpmn.view.GatewayView;
import org.uengine.kernel.bpmn.view.InclusiveGatewayView;
import org.uengine.modeling.Palette;

public class GatewayPalette extends Palette {

	public GatewayPalette(){
		this.setName("Gateway");

		this.getSymbolList().add((new GatewayView()).createSymbol());
		this.getSymbolList().add((new InclusiveGatewayView()).createSymbol());
		this.getSymbolList().add((new ParallelGatewayView()).createSymbol());
	}
	

}

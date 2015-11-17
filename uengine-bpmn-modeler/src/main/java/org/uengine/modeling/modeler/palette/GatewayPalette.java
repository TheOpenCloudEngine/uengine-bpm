package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.ParallelGatewayView;
import org.uengine.kernel.bpmn.view.GatewayView;
import org.uengine.kernel.bpmn.view.InclusiveGatewayView;
import org.uengine.modeling.Palette;
import org.uengine.modeling.Symbol;

public class GatewayPalette extends Palette {

	public GatewayPalette(){
		this.setName("Gateways");

		addSymbol((new GatewayView()).createSymbol());
		addSymbol((new InclusiveGatewayView()).createSymbol());
		addSymbol((new ParallelGatewayView()).createSymbol());
	}


	@Override
	public void addSymbol(Symbol symbol) {
		symbol.setIconResizing(true);

		super.addSymbol(symbol);
	}
}

package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.view.AndGatewayActivityView;
import org.uengine.kernel.view.GatewayActivityView;
import org.uengine.kernel.view.OrGatewayActivityView;
import org.uengine.modeling.Palette;
import org.uengine.modeling.SymbolFactory;

public class GatewayPalette extends Palette {

	public GatewayPalette(){
		this.setName("Gateway");
		initPallet();
	}
	
	@Override
	protected void initPallet() {
		this.getSymbolList().add(SymbolFactory.create(GatewayActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(AndGatewayActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(OrGatewayActivityView.class));
	}

}

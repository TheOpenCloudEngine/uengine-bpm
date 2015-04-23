package org.uengine.modeling.modeler.symbol;

import org.metaworks.EventContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;
import org.uengine.modeling.Palette;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.modeler.palette.GatewayPalette;

public class GatewaySymbol extends Symbol {
	@ServiceMethod(callByContent=true, eventBinding=EventContext.EVENT_CLICK, target=ServiceMethodContext.TARGET_STICK)
	public Object open(){
		Palette palette = new GatewayPalette();
		return palette;
	}
}

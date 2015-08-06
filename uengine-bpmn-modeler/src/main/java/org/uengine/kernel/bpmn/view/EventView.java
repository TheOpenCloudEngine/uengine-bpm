package org.uengine.kernel.bpmn.view;

import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Symbol;


public class EventView extends ActivityView {

	@Override
	public Symbol createSymbol() {
		Symbol symbol = super.createSymbol();
		symbol.setHeight(30);
		symbol.setWidth(30);

		return symbol;
	}

	@Override
	public String getLabel() {
		return null;
	}
}

package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.*;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;
import org.uengine.modeling.PaletteWindow;
import org.uengine.modeling.Symbol;

public class EventPalette extends PaletteWindow {

	public EventPalette(){
		this.setName("Events");

		addSymbol(new StartEventView().createSymbol());
		addSymbol((new EndEventView()).createSymbol());
		addSymbol((new EscalationEventView()).createSymbol());
		addSymbol((new TimerEventView()).createSymbol());
		addSymbol((new ReceiveRestMessageEventActivityView()).createSymbol());
		addSymbol((new MessageEventView()).createSymbol());
	}


	@Override
	public void addSymbol(Symbol symbol) {
		symbol.setIconResizing(true);

		super.addSymbol(symbol);
	}
}

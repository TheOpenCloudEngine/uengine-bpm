package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.*;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;

public class EventPalette extends Palette {

	public EventPalette(){
		this.setName("Events");

		this.getSymbolList().add((new StartEventView()).createSymbol());
		this.getSymbolList().add((new EndEventView()).createSymbol());
		this.getSymbolList().add((new EscalationEventView()).createSymbol());
		this.getSymbolList().add((new TimerEventView()).createSymbol());
		this.getSymbolList().add((new ReceiveRestMessageEventActivityView()).createSymbol());
		this.getSymbolList().add((new MessageEventView()).createSymbol());
	}

}

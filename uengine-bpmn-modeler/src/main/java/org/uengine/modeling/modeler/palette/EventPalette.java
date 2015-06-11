package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.*;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;

public class EventPalette extends Palette {

	public EventPalette(){
		this.setName("Event");

		addSymbol(StartEventView.class);
		addSymbol(EndEventView.class);
		addSymbol(TerminateActivityView.class);

	}

}

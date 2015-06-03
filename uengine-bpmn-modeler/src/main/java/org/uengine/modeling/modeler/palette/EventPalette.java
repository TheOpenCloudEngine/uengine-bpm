package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.*;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;

public class EventPalette extends Palette {

	public EventPalette(){
		this.setName("Event");

		addSymbol(StartActivityView.class);
		addSymbol(IntermediateActivityView.class);
		addSymbol(EndActivityView.class);
		addSymbol(StartMessageActivityView.class);
		addSymbol(IntermediateMessageActivityView.class);
		addSymbol(IntermediateFilledMessageActivityView.class);
		addSymbol(EndMessageActivityView.class);
		addSymbol(StartTimerActivityView.class);
		addSymbol(IntermediateTimerActivityView.class);
		addSymbol(StartConditionalActivityView.class);
		addSymbol(IntermediateConditionalActivityView.class);
		addSymbol(TerminateActivityView.class);

	}

}

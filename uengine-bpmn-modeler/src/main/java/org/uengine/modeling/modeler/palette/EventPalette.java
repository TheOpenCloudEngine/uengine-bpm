package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;
import org.uengine.modeling.SymbolFactory;

public class EventPalette extends Palette {

	public EventPalette(){
		this.setName("Event");
		initPallet();
	}
	
	@Override
	protected void initPallet() {
		this.getSymbolList().add(SymbolFactory.create(StartActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(IntermediateActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(EndActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(StartMessageActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(IntermediateMessageActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(IntermediateFilledMessageActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(EndMessageActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(StartTimerActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(IntermediateTimerActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(StartConditionalActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(IntermediateConditionalActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(TerminateActivityView.class));
	}
}

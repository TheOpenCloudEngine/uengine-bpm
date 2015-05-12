package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.GatewayView;
import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;
import org.uengine.modeling.Symbol;
import org.uengine.modeling.SymbolFactory;
import org.uengine.modeling.modeler.symbol.EventSymbol;
import org.uengine.modeling.modeler.symbol.GatewaySymbol;
import org.uengine.modeling.modeler.symbol.TaskSymbol;

public class SimplePalette extends Palette {

	public SimplePalette(){
		
	}
	
	
	public SimplePalette(String type){
		this.setName("BPMN Diagram");
		setType(type);
		initPallet();
	}
	
	@Override
	protected void initPallet() {
		
		Symbol symbol=SymbolFactory.create(StartActivityView.class, EventSymbol.class);
		symbol.setName("Event");
		this.getSymbolList().add(symbol);
		
		symbol=SymbolFactory.create(DefaultActivityView.class, getType(), TaskSymbol.class);
		symbol.setName("Default");
		this.getSymbolList().add(symbol);
		
		symbol=SymbolFactory.create(GatewayView.class, GatewaySymbol.class);
		symbol.setName("Gateway");
		this.getSymbolList().add(symbol);

		this.getSymbolList().add(SymbolFactory.create(LoopActivityView.class));
		this.getSymbolList().add(SymbolFactory.create(HumanActivityView.class));

//		this.getSymbolList().add(SymbolFactory.create(PoolView.class));
		this.getSymbolList().add(SymbolFactory.create(RoleView.class));
//		this.getSymbolList().add(SymbolFactory.create(DataObjectActivityView.class));
//		this.getSymbolList().add(SymbolFactory.create(DataStoreActivityView.class));
//		this.getSymbolList().add(SymbolFactory.create(AnnotationActivityView.class));
//		this.getSymbolList().add(SymbolFactory.create(ScopeActivityView.class));
		
		this.getSymbolList().add(SequenceFlowView.createSymbol());
		
	}
}

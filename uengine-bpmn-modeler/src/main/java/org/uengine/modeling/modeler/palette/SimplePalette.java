package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.GatewayView;
import org.uengine.kernel.bpmn.view.StartEventView;
import org.uengine.kernel.bpmn.view.SubProcessView;
import org.uengine.kernel.view.*;
import org.uengine.modeling.Palette;

public class SimplePalette extends Palette {

	public SimplePalette(){
		
	}
	
	
	public SimplePalette(String type){
		this.setName("BPMN Diagram");
		setType(type);

//		this.getSymbolList().add((new StartEventView()).createSymbol());
//		this.getSymbolList().add((new DefaultActivityView()).createSymbol());
//		this.getSymbolList().add((new GatewayView()).createSymbol());
//		this.getSymbolList().add((new HumanActivityView()).createSymbol());
//		this.getSymbolList().add((new SubProcessView()).createSymbol());

	}
}

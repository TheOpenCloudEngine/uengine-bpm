package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.graph.view.TransitionView;
import org.uengine.kernel.view.AssociationTransitionView;
import org.uengine.kernel.view.DataAssociationTransitionView;
import org.uengine.kernel.view.MessageTransitionView;
import org.uengine.modeling.Palette;

public class ConnectorPalette extends Palette {

	public ConnectorPalette(){
		this.setName("Connector!");
		initPallet();
	}
	
	@Override
	protected void initPallet() {
		this.getSymbolList().add(TransitionView.createSymbol());
		this.getSymbolList().add(MessageTransitionView.createSymbol());
		this.getSymbolList().add(DataAssociationTransitionView.createSymbol());
		this.getSymbolList().add(AssociationTransitionView.createSymbol());
	}

	
}

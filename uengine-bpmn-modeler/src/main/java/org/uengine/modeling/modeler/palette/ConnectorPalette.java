package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.SequenceFlowView;
import org.uengine.kernel.bpmn.view.AssociationTransitionView;
import org.uengine.kernel.bpmn.view.DataAssociationTransitionView;
import org.uengine.kernel.bpmn.view.MessageFlowView;
import org.uengine.modeling.Palette;

public class ConnectorPalette extends Palette {

	public ConnectorPalette(){
		this.setName("Connector!");
		this.getSymbolList().add(SequenceFlowView.createSymbol());
		this.getSymbolList().add(MessageFlowView.createSymbol());
		this.getSymbolList().add(DataAssociationTransitionView.createSymbol());
		this.getSymbolList().add(AssociationTransitionView.createSymbol());
	}

	
}

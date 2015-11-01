package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.PoolView;
import org.uengine.kernel.bpmn.view.SubProcessView;
import org.uengine.kernel.view.HumanActivityView;
import org.uengine.kernel.view.RestWebServiceActivityView;
import org.uengine.modeling.Palette;

public class TaskPalette extends Palette {

	public TaskPalette() {
		this.setName("Tasks");

		this.getSymbolList().add(new PoolView().createSymbol());
		this.getSymbolList().add((new HumanActivityView()).createSymbol());
		this.getSymbolList().add((new RestWebServiceActivityView()).createSymbol());
		this.getSymbolList().add((new SubProcessView()).createSymbol());
	}

	public TaskPalette(String type) {
		this();
		setType(type);
	}
}

package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.bpmn.view.PoolView;
import org.uengine.kernel.bpmn.view.SubProcessView;
import org.uengine.kernel.view.HumanActivityView;
import org.uengine.kernel.view.RestWebServiceActivityView;
import org.uengine.kernel.view.RoleView;
import org.uengine.modeling.Palette;
import org.uengine.modeling.PaletteWindow;
import org.uengine.modeling.Symbol;

public class TaskPalette extends Palette {

	public TaskPalette() {
		this.setName("Tasks");

		addSymbol(new RoleView().createSymbol());
		addSymbol(new PoolView().createSymbol());
		addSymbol((new HumanActivityView()).createSymbol());
		addSymbol((new RestWebServiceActivityView()).createSymbol());
		addSymbol((new SubProcessView()).createSymbol());
	}

	public TaskPalette(String type) {
		this();
		setType(type);
	}

	@Override
	public void addSymbol(Symbol symbol) {
		symbol.setIconResizing(true);

		super.addSymbol(symbol);
	}
}

package org.uengine.modeling.modeler.palette;

import org.uengine.kernel.view.DefaultActivityView;
import org.uengine.kernel.view.HumanActivityView;
import org.uengine.kernel.view.ManualActivityView;
import org.uengine.kernel.view.WebServiceActivityView;
import org.uengine.modeling.Palette;

public class TaskPalette extends Palette {

	public TaskPalette() {
		this.setName("TaskPalette");
	}
	public TaskPalette(String type) {
		this();
		setType(type);


		addSymbol(DefaultActivityView.class);
		addSymbol(HumanActivityView.class);
		addSymbol(WebServiceActivityView.class);
		addSymbol(ManualActivityView.class);
		
	}

}

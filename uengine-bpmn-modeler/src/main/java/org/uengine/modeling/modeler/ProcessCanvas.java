package org.uengine.modeling.modeler;

import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.Clipboard;
import org.uengine.modeling.*;

public class ProcessCanvas extends Canvas {

	public ProcessCanvas(){super();}

	public ProcessCanvas(String modelerType) {
		super(modelerType);
	}


	@Override
	public void drop() {
		super.drop();
	}
}

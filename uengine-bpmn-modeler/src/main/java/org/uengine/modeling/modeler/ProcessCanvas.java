package org.uengine.modeling.modeler;

import org.metaworks.*;
import org.metaworks.annotation.AutowiredFromClient;
import org.metaworks.annotation.Available;
import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.MetaworksRemoteService;
import org.metaworks.widget.Clipboard;
import org.metaworks.widget.Label;
import org.metaworks.widget.ModalWindow;
import org.metaworks.widget.Popup;
import org.uengine.modeling.*;

public class ProcessCanvas extends Canvas {

	public ProcessCanvas(){super();}

	public ProcessCanvas(String modelerType) {
		super(modelerType);
	}


}

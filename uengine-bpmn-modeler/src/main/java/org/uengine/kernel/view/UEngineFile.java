package org.uengine.kernel.view;

import org.metaworks.widget.AbstractMetaworksFile;
import org.uengine.kernel.GlobalContext;

public class UEngineFile extends AbstractMetaworksFile {

	@Override
	public String overrideUploadPathPrefix() {
		return GlobalContext.getPropertyString("codebase", "codebase");
	}

}

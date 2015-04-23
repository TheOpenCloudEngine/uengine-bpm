package org.uengine.modeling;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;

/**
 * @author jyj
 */
public abstract class Modeler implements ContextAware {

	protected IModel model;
	private Canvas canvas;
	private Palette palette;
	private String type;
	private MetaworksContext metaworksContext;
	
	public Modeler() {
		this.metaworksContext = new MetaworksContext();
		this.getMetaworksContext().setWhen(MetaworksContext.WHEN_VIEW);
	}
	
	public IModel getModel() throws Exception {
		return model;
	}
	public void setModel(IModel model) throws Exception {
		this.model = model;
	}

	public Canvas getCanvas() {
		return canvas;
	}
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public Palette getPalette() {
		return palette;
	}
	public void setPalette(Palette palette) {
		this.palette = palette;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public MetaworksContext getMetaworksContext() {
		return metaworksContext;
	}
	public void setMetaworksContext(MetaworksContext metaworksContext) {
		this.metaworksContext = metaworksContext;
	}
}

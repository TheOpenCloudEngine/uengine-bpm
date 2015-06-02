package org.uengine.modeling;

import java.util.ArrayList;
import java.util.List;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;

/**
 * @author jyj
 */
public abstract class Palette implements ContextAware {
	
	transient MetaworksContext metaworksContext;
	public MetaworksContext getMetaworksContext() {
		return metaworksContext;
	}
	public void setMetaworksContext(MetaworksContext metaworksContext) {
		this.metaworksContext = metaworksContext;
	}
	
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
	String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}

	List<Symbol> symbolList;
		public List<Symbol> getSymbolList() {
			return symbolList;
		}
		public void setSymbolList(List<Symbol> symbolList) {
			this.symbolList = symbolList;
		}
	
	public Palette(){
		setMetaworksContext(new MetaworksContext());
		this.symbolList = new ArrayList<Symbol>();
	}

	public void addSymbol(Class <? extends ElementView> elementForSymbol){
		try {
			this.getSymbolList().add(elementForSymbol.newInstance().createSymbol());
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

	}
}

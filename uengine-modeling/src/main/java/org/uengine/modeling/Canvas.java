package org.uengine.modeling;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jyj
 */
public class Canvas {
	
	List<ElementView> elementViewList;
		public List<ElementView> getElementViewList() {
			return elementViewList;
		}
		public void setElementViewList(List<ElementView> elementViewList) {
			this.elementViewList = elementViewList;
		}

	List<RelationView> relationViewList;
		public List<RelationView> getRelationViewList() {
			return relationViewList;
		}
		public void setRelationViewList(List<RelationView> relationViewList) {
			this.relationViewList = relationViewList;
		}
			
	public Canvas(){
		this.elementViewList = new ArrayList<ElementView>();
		this.relationViewList = new ArrayList<RelationView>();
	}	
}

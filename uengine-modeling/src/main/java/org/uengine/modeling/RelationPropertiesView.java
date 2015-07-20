package org.uengine.modeling;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.widget.ModalWindow;

public class RelationPropertiesView extends ModalWindow{

	RelationView relationView;
		public RelationView getRelationView() {
			return relationView;
		}
		public void setRelationView(RelationView relationView) {
			this.relationView = relationView;
		}

	public RelationPropertiesView(RelationView relationView){
		this.setRelationView(relationView);
        setPanel(this.getRelationView());
	}

	@ServiceMethod(callByContent=true)
	public void apply(){
		
	}
}

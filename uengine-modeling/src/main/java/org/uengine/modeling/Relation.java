package org.uengine.modeling;

import org.metaworks.annotation.Hidden;

import java.io.Serializable;


/**
 * @author jyj
 */
public abstract class Relation implements Serializable, IRelation{

	private static final long serialVersionUID = 1234L;

	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}


	RelationView relationView;
	@Hidden
		public RelationView getRelationView() {
			return relationView;
		}
		public void setRelationView(RelationView relationView) {
			this.relationView = relationView;
		}



	/**
	 * sourceElement
	 */
	transient IElement sourceElement;
		@Override
		@Hidden
		public IElement getSourceElement() {
			return this.sourceElement;
		}
		public void setSourceElement(IElement sourceElement) {
			this.sourceElement = sourceElement;
		}

	/**
	 * targetElement
	 */
	transient IElement targetElement;
		@Override
		@Hidden
		public IElement getTargetElement() {
			return this.targetElement;
		}
		public void setTargetElement(IElement targetElement) {
			this.targetElement = targetElement;
		}

	@Override
	public RelationView createView() {
		RelationView relationView = null;
		try {
			relationView = (RelationView)Thread.currentThread().getContextClassLoader().loadClass("org.uengine.modeling.RelationView").newInstance();
			relationView.setRelation(this);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relationView;
	}
	
	@Override
	public boolean equals(Object obj) {
		IElement element = (IElement)obj;
		
		return getRelationView().getFrom().substring(0, getRelationView().getTo().indexOf("T") -1).equals(element.getElementView().getId());
	}

	@Override
	public RelationView asView() {
		RelationView view = getRelationView();
		setRelationView(null);
		view.setRelation(this);
		return view;
	}
}

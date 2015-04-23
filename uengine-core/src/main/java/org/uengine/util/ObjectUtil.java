package org.uengine.util;

import org.uengine.modeling.ElementView;
import org.uengine.modeling.IElement;
import org.uengine.modeling.Relation;
import org.uengine.modeling.RelationView;

public class ObjectUtil {
	
	/**
	 * element가 view를 갖고 있는 구조로 전환
	 * @param view
	 * @return
	 */
	public static IElement convertToElementHasView(ElementView view){
		IElement element = view.getElement();
		view.setElement(null);
		element.setElementView(view);
		return element;
	}
	
	/**
	 * view가 element를 갖는 구조로 변환
	 * @param element
	 * @return
	 */
	public static ElementView convertToViewHasElement(IElement element){
		ElementView view = element.getElementView();
		element.setElementView(null);
		view.setElement(element);
		return view;
	}
	
	/**
	 * relation가 view를 갖고 있는 구조로 전환
	 * @param view
	 * @return
	 */
	public static Relation convertToRelationHasView(RelationView view){
		Relation relation = (Relation)view.getRelation();
		view.setRelation(null);
		relation.setRelationView(view);
		return relation;
	}
	
	/**
	 * viewr가 relation을 갖는 구조로 변환
	 * @param relation
	 * @return
	 */
	public static RelationView convertToViewHasRelation(Relation relation){
		RelationView view = relation.getRelationView();
		relation.setRelationView(null);
		view.setRelation(relation);
		return view;
	}
	
}

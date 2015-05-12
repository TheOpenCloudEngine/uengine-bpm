package org.uengine.util;

import org.apache.commons.lang.UnhandledException;
import org.uengine.modeling.*;

public class ObjectUtil {
	
	/**
	 * element가 view를 갖고 있는 구조로 전환
	 * @param view
	 * @return
	 */
	public static IElement convertToElementHasView(ElementView view){
		ElementView tempView = null;
		try{
			tempView = (ElementView)view.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		IElement element = tempView.getElement();
		tempView.setElement(null);
		element.setElementView(tempView);

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
		RelationView tempView = null;
		try{
			tempView = (RelationView)view.clone();
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		Relation relation = (Relation)tempView.getRelation();
		tempView.setRelation(null);
		relation.setRelationView(tempView);

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

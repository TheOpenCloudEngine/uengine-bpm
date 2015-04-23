package org.uengine.modeling;

/**
 * @author jyj
 */
public interface IRelation {
	
	IElement getSourceElement();
	IElement getTargetElement();
	RelationView createView();
//	String getFromTerminalId();
//	String getToTerminalId();
	
	
}

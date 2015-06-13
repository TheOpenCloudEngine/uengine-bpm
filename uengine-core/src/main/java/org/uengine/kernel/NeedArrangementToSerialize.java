package org.uengine.kernel;

/**
 * @author Jinyoung Jang
 */

public interface NeedArrangementToSerialize{

	public void beforeSerialization();
	
	public void afterDeserialization();
}
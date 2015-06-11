package org.uengine.kernel;

import java.util.Map;
/**
 * @author Jinyoung Jang
 */
public interface Validatable {
	
	public ValidationContext validate(Map options);

}
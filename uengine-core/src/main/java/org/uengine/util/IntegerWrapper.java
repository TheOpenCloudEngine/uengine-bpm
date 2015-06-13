/*
 * Created on 2005. 3. 24.
 */
package org.uengine.util;

/**
 * @author Jinyoung Jang
 */
public class IntegerWrapper {
	int value;
	

	public int getValue() {
		return value;
	}

	public void setValue(int i) {
		value = i;
	}
	
	public synchronized void increase(){
		value ++;
	}

	public synchronized void decrease(){
		value --;
	}

}

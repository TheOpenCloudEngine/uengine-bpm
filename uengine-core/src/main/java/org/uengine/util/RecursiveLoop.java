/*
 * Created on 2004. 10. 14.
 */
package org.uengine.util;

import java.util.*;

/**
 * @author Jinyoung Jang
 */
public abstract class RecursiveLoop {
	boolean stopSignaled = false;
	Object returnValue;

	public abstract boolean logic(Object tree);
	
	public abstract List getChildren(Object tree);
	
	public void run(Object tree, boolean backward){
		boolean breturn = logic(tree);
		
		if(breturn) return;
		
		List children = getChildren(tree);
		
		if(children!=null){			
			if(backward){
				for(int i=children.size()-1; i>-1 && !stopSignaled; i--){
					run(children.get(i), backward);
				}
			}else{
				for(int i=0; i<children.size() && !stopSignaled; i++){
					run(children.get(i), backward);
				}
			}
		}		
	}
	
	public void runBackward(Object tree){
		run(tree, true);
	}

	public void run(Object tree){
		run(tree, false);
	}
	
	public void stop(){
		stopSignaled = true;
	}

	public void stop(Object returnValue){
		stopSignaled = true;
		setReturnValue(returnValue);
	}
	
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	public static void main(String args[]){
		Vector firstChild = new Vector();
		Vector secondChild = new Vector();
		Vector tree = new Vector();
		
		secondChild.add("second data");
		firstChild.add("first data");
		firstChild.add(secondChild);
		
		tree.add(firstChild);
		
		RecursiveLoop recursiveLoop = new RecursiveLoop(){

			public boolean logic(Object tree) {
				System.out.println(tree);
				
				if(tree instanceof List && ((List)tree).size() == 1) return false;
				
				return true;
			}
			
			public List getChildren(Object tree) {
				if(tree instanceof List) return (List)tree;
				return null;
			}

			
		};
		
		recursiveLoop.run(tree);
		
	}
}

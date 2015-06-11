/*
 * Created on 2004. 10. 14.
 */
package org.uengine.util;

import java.util.*;
/**
 * @author Jinyoung Jang
 */
public abstract class ForLoop {
	boolean stopSignaled = false;
	
	public void run(Enumeration enumeration){
		while(enumeration.hasMoreElements() && !stopSignaled){
			logic(enumeration.nextElement());
		}		
	}
	
	public void run(Iterator iter){
		while(iter.hasNext() && !stopSignaled){
			logic(iter.next());
		}		
	}
	
	public void run(Collection list){
		run(list.iterator());		
	}

	public void run(ArrayList list){
		for(int i=0; i<list.size() && !stopSignaled; i++){
			logic(list.get(i));
		}
	}

	public void run(Vector list){
		for(int i=0; i<list.size() && !stopSignaled; i++){
			logic(list.elementAt(i));
		}
	}

	public void run(Map map) throws Exception{
		run(map.values().iterator());
	}
	
//// catridges ////

	abstract public void logic(Object target);

	public void onException(Throwable t){
		t.printStackTrace();
	}

	public void stop(){
		stopSignaled = true;
	}

///////////////////
	
	public static void main(String args[]) throws Exception{
		
		Vector vt = new Vector();
		vt.add("1");
		vt.add("2");
		vt.add("3");
		vt.add("4");
		vt.add("5");
		
		new ForLoop(){
			public void logic(Object target){
				System.out.println(target);
			}
		}.run(vt);
	}

}

	

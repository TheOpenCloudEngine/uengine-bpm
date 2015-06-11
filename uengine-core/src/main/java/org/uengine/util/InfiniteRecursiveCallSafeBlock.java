package org.uengine.util;

public abstract class InfiniteRecursiveCallSafeBlock {

	public void run() throws Exception{
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		
		String callerMethodName = stack[2].getMethodName();
		String callerClassName = stack[2].getClassName();
		int lineNumber = stack[2].getLineNumber();
		
		if(stack.length>2){
			for(int i=3; i<stack.length; i++){
				StackTraceElement stackElement = stack[i];
				if(lineNumber==stackElement.getLineNumber() && callerMethodName.equals(stackElement.getMethodName()) && callerClassName.equals(stackElement.getClassName())){
					return;  //don't do the logic if recursively called.
				}
			}
		}
		
		logic();
	}
	
	public abstract void logic() throws Exception;
	
}

package org.uengine.modeling;

public class SymbolFactory {
	
	public SymbolFactory(){
	  
	}
	
	public static Symbol create(Class<? extends ElementView> clsType){
		Symbol symbol = null;
		
		try {
			ElementView elementView = (ElementView)Thread.currentThread().getContextClassLoader().loadClass(clsType.getName()).newInstance();
			symbol = elementView.createSymbol();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return symbol;
	}
	
	public static Symbol create(Class<? extends ElementView> clsType, String modelerType){
		Symbol symbol = null;
		
		try {
			ElementView elementView = (ElementView)Thread.currentThread().getContextClassLoader().loadClass(clsType.getName()).newInstance();
			symbol = elementView.createSymbol(modelerType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return symbol;
	}
	
	public static Symbol create(Class<? extends ElementView> clsType, Class<? extends Symbol> symbolType){
		Symbol symbol = null;
		
		try {
			ElementView elementView = (ElementView)Thread.currentThread().getContextClassLoader().loadClass(clsType.getName()).newInstance();
			symbol = elementView.createSymbol(symbolType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return symbol;
	}
	
	public static Symbol create(Class<? extends ElementView> clsType, String modelerType, Class<? extends Symbol> symbolType){
		Symbol symbol = null;
		
		try {
			ElementView elementView = (ElementView)Thread.currentThread().getContextClassLoader().loadClass(clsType.getName()).newInstance();
			symbol = elementView.createSymbol(modelerType, symbolType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return symbol;
	}
 
}
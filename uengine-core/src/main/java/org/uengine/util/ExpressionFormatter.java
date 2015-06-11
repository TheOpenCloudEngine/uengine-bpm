package org.uengine.util;

import org.uengine.kernel.Activity;

public abstract class ExpressionFormatter {

	public abstract Object formatExpression(String expression);
	
	public String format(String src){
		StringBuffer generating = new StringBuffer();
		
		if(src==null) return src;
//		System.out.println("EMailActivity:: parseContent:2");
				
				int pos=0, endpos=0, oldpos=0;
				String key;
				String starter = "<%", ending="%>";	
				
				while((pos = src.indexOf(starter, oldpos)) > -1){
					pos += starter.length();
					endpos = src.indexOf(ending, pos);
//		System.out.println("oldpos="+oldpos +"; pos = "+pos);
					if(endpos > pos){
						generating.append(src.substring(oldpos, pos - starter.length()));
						key = src.substring(pos, endpos);
						if(key.startsWith("=")) key = key.substring(1, key.length());
						if(key.startsWith("*")) key = key.substring(1, key.length());
						if(key.startsWith("+")) key = key.substring(1, key.length());
						
						key = key.trim();
						
						Object val = formatExpression(key);

						if(val!=null)
							generating.append("" + val);
					}
					oldpos = endpos + ending.length();
				}
				generating.append(src.substring(oldpos));
				//end
				
				return generating.toString();		
	}
	
}

package org.uengine.util;

import java.io.*;

public class SystemLauncher{

	int completing=0;

	public void run(String command){
		try{
			Process proc = 	(Runtime.getRuntime()).exec(/*"command.com /c "+*/command);
						
			if(proc==null) throw new Exception("can't create process");
			
			final InputStream stream = proc.getErrorStream();
			final InputStream stream2 = proc.getInputStream();
			
			final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			final BufferedReader reader2 = new BufferedReader(new InputStreamReader(stream2));	
			
			Thread pourer1 = new Thread(new Runnable(){
				public void run(){
//					System.out.println("running...");
					
					try{
						String line;
						while((line = reader.readLine())!=null){
							write(line);
						}
						
						reader.close();
						
						/*int c;
						while((c=stream.read())!=-1)
							write(c);
					
						stream.close();*/
						
					}catch(Exception e){
						e.printStackTrace();
					}
					
					pourCompleted(this);
				}
				
			});
			pourer1.start();
			
			Thread pourer2 = new Thread(new Runnable(){
				public void run(){
					try{
						String line;
						while((line = reader2.readLine())!=null){
							write(line);
						}
						
						reader2.close();
						
						/*int c;
						while((c=stream2.read())!=-1)
							write(c);
						
						stream2.close();*/
					}catch(Exception e){
						e.printStackTrace();
					}
					
					pourCompleted(this);
				}
			});
			pourer2.start();			
			
						
								
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void pourCompleted(Object some){
		
//System.out.println("pourCompleted");
		
		try{
			if((++completing)>1){
				completed();
				completing=0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void completed(){}
	public void write(String out){}
	public void write(int c){System.out.print(c);}
	
	
}
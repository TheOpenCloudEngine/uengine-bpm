package org.uengine.util;

import org.apache.log4j.Logger;
import org.uengine.kernel.ProcessInstance;

public class MeasuringContext {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MeasuringContext.class);
	
	long startTime;
	long lastTime;
	int count;
	String name;
	
	public MeasuringContext(){
		this("elapsed time ");
	}
	
	public MeasuringContext(String name){
		setStartTime(System.currentTimeMillis());
		setLastTime(getStartTime());
		setName(name);
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public long issueElapsedTime(){
		count ++;
		long now = System.currentTimeMillis();
		long elapsed = now - getLastTime();
		setLastTime(now);
		
		return elapsed;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void printElapsedTime(){
		System.out.println(getName() + " - ["+ getCount() +"] = " + issueElapsedTime());
	}
	
	public void printElapsedTime(ProcessInstance instance){
		instance.addDebugInfo(getName() + " - ["+ getCount() +"] = " + issueElapsedTime());
	}
	
	
	static public void  main(String[] args) throws Exception{
		MeasuringContext mc = new MeasuringContext();
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
		Thread.sleep(50);
		if (logger.isInfoEnabled()) {
			logger.info("main(String[]) - " + mc.issueElapsedTime()); //$NON-NLS-1$
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

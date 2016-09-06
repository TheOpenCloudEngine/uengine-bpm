package org.uengine.kernel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * @author Jinyoung Jang
 */

public class LeveledException extends Exception{
	private static final long serialVersionUID = 1234L;

	public final static int WARNING = 0;
	public final static int ERROR = 1;
	public final static int FATAL = 2;
	public final static int MESSAGE_TO_USER = 3;
	
	int errorLevel = ERROR;
		public int getErrorLevel() {
			return errorLevel;
		}
		public void setErrorLevel(int value) {
			errorLevel = value;
		}


	//just for making it as a property in DWR.
	public void setDetailMessage(String message){

	}


	//Some exception types are difficult to be serialized to XML. So uEngine Exception doesn't serialize the cause exception.
	transient Throwable cause;

		public Throwable getCauseException(){
			return cause;
		}

		public void setCauseException(Throwable value){
			cause = value;
		}
		
	String details;
		public String getDetails() {
			if (details == null) {
				try {
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					printStackTrace(new PrintStream(bao));

					setDetails(bao.toString());
				} catch (Exception e) {
					/*System.out.println(
						"[UEngineException::getDetails] An non-fatal error : "
							+ e.getMessage());*/
				}
			}

			return details;
		}
		public void setDetails(String value) {
			//TODO disabled by Oracle database's field size
			/*if(value.startsWith("null")){
				System.out.println("=======================> "+value);
			}*/
//System.out.println("UEngineException.setDetails('" + value + "')");
			//details = value;
		}
		
	String resolution;
		public String getResolution() {
			return resolution;
		}
		public void setResolution(String value) {
			resolution = value;
		}

	public LeveledException(String errorMsg, String resolution, Throwable cause){
		super(NPEifNull(errorMsg), cause);
		setCauseException(cause);
		setResolution(resolution);

		if(cause instanceof LeveledException){
			setErrorLevel(((LeveledException) cause).getErrorLevel());
		}
	}


	protected static String NPEifNull(String errorMsg){
		return (errorMsg != null ? errorMsg : "Null Pointer Exception");
	}
	

	public LeveledException(String errorMsg, Throwable cause){
		this(errorMsg, null, cause);
	}	
	
	public LeveledException(String errorMsg, String resolution){
		this(errorMsg, resolution, null);
	}	

	public LeveledException(String errorMsg){
		this(errorMsg, null, null);
	}
	
	public LeveledException(){
		this(null, null, null);
	}
	
	public String toString(){
		return getMessage();
	}
	



}


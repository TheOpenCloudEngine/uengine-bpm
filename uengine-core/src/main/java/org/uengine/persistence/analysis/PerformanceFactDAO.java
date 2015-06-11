/*
 * Created on 2005. 8. 8.
 */
package org.uengine.persistence.analysis;

import java.util.Date;

import org.uengine.util.dao.IDAO;

/**
 * @author Jinyoung Jang
 */
public interface PerformanceFactDAO extends IDAO{

	Number getINST_ID();
	Number getROOTINST_ID();
	Number getTIME_ID();
	String getACT_ID();
	String getACT_NAME();
	Number getDEF_ID();
	String getDEF_NAME();
	Number getPRSNGTIME();
	Number getTRNRNDTIME();
	Number getIDLETIME();
	Number getCHOVRTIME();
	Number getSSPNDTIME();
	Number getCOST();
	Number getSLA_STFTRY();

	void setINST_ID(Number value);
	void setROOTINST_ID(Number value);
	void setTIME_ID(Number value);
	void setACT_ID(String value);
	void setACT_NAME(String value);
	void setDEF_ID(Number value);
	void setDEF_NAME(String value);
	void setPRSNGTIME(Number value);
	void setTRNRNDTIME(Number value);
	void setIDLETIME(Number value);
	void setCHOVRTIME(Number value);
	void setSSPNDTIME(Number value);
	void setCOST(Number value);
	void setSLA_STFTRY(Number value);

	String getRsrc_Id();
	void setRsrc_Id(String resourceId);
	
	Date getMODTIME();
	void setMODTIME(Date modDate);
	
	Number getDEADLNHT();
	void setDEADLNHT(Number value);
	  
}


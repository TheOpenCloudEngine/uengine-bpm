/*
 * Created on 2005. 8. 23.
 */
package org.uengine.persistence.processinstance;

/**
 * @author Jinyoung Jang
 */
public interface ProcessArchiveDAO extends ProcessInstanceDAO {
	java.util.Date 	getFinishedDate();
	void setFinishedDate(java.util.Date when);
	
	String getFilePath();
	void setFilePath(String path);

}

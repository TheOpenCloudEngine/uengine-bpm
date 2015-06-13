/*
 * Created on 2004. 11. 3.
 */
package org.uengine.util.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jinyoung Jang
 */
public interface IDAO extends Serializable {
	
//	public int getRow() throws Exception;
//	public boolean absolute(int row) throws Exception;
	
	public void select() throws Exception;
	public int insert() throws Exception;
	public int update() throws Exception;
	public int update(String stmt) throws Exception;
	public int call() throws Exception;
	public void addBatch() throws Exception;
	public int[] updateBatch() throws Exception;
	
	public void beforeFirst() throws Exception;
	public boolean previous() throws Exception;
	public boolean next() throws Exception;
	public boolean first() throws Exception;
	public void afterLast() throws Exception;
	public boolean last() throws Exception;
	public boolean absolute(int pos) throws Exception;
	
	public int size();
	
//	public void setStatement(String stmt) throws Exception;
	
	public Object get(String key) throws Exception;
	public Object set(String key, Object value) throws Exception;
	
	public String getString(String key) throws Exception; 
	public Integer getInt(String key) throws Exception;
	public Long getLong(String key) throws Exception;
	public Boolean getBoolean(String key) throws Exception;
	public Date getDate(String key) throws Exception;
	
	public AbstractGenericDAO getImplementationObject();
	public void releaseResource() throws Exception;

}

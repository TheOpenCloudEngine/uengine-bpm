/*
 * Created on 2005. 8. 8.
 */
package org.uengine.persistence.analysis;

import java.util.Date;

import org.uengine.util.dao.IDAO;

/**
 * @author Jinyoung Jang
 */
public interface ResourceDimensionDAO extends IDAO{
	  String getRsrc_Id();
	  void setRsrc_Id(String resourceId);
	  
	  String getRsrc_Name();
	  void setRsrc_Name(String name);
	  
	  String getDept_Name();
	  void setDept_Name(String deptName);
	  
	  String getDept_Id();
	  void setDept_Id(String deptId);
	  
	  String getTitle();
	  void setTitle(String title);
	  
	  boolean getGender();
	  void setGender(boolean isMail);
	  
	  Number getBirthDay();
	  void setBirthDay(Number birthDay);
}


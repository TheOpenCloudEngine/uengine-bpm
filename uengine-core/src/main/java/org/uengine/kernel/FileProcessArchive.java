/*
 * Created on 2005. 8. 19.
 */
package org.uengine.kernel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.uengine.persistence.dao.*;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;

import org.uengine.persistence.processinstance.*;
import org.uengine.processmanager.ProcessTransactionContext;

/**
 * @author Jinyoung Jang
 */
public class FileProcessArchive extends DefaultProcessInstance {
	
	static String ARCHIVE_ROOT
		= GlobalContext.getPropertyString(
			"server.processarchive.path",
			"uengine/processarchive/"
		);

	public FileProcessArchive() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static FileProcessArchive load(String instanceId, ProcessTransactionContext tc) throws Exception {
		ProcessInstanceDAO processArchive = (ProcessInstanceDAO)ConnectiveDAO.createDAOImpl(
			tc,
			"select defPath from bpm_procinst where instid=?instid",
			ProcessInstanceDAO.class
		);
		
		processArchive.setInstId(new Long(instanceId));
		processArchive.select();
		
		if(processArchive.next()){
			String fileForArchivedPI = ARCHIVE_ROOT + processArchive.getDefPath();
			FileInputStream fis = new FileInputStream(fileForArchivedPI);
			
			FileProcessArchive inst = (FileProcessArchive)GlobalContext.deserialize(fis, String.class);
			return inst;
		}else
			throw new UEngineException("No such process archive(instance):" + instanceId);
	}

	public String save(ProcessTransactionContext tc) throws Exception{
		String relativePath = UEngineUtil.getCalendarDir();
		
		File dirToCreate = new File(ARCHIVE_ROOT + relativePath);
		dirToCreate.mkdirs();
		
		relativePath = relativePath + "/" + getInstanceId() + ".archive";
		String fileForArchivedPI = ARCHIVE_ROOT + relativePath;
		
		FileOutputStream fos = new FileOutputStream(fileForArchivedPI);			
		GlobalContext.serialize(this, fos, String.class);					
		fos.close();
		
		return relativePath;

	}
	
}

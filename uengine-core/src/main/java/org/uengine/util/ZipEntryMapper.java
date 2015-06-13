package org.uengine.util;

import java.io.InputStream;

/**
 * @author Heebyung Lee
 */

public class ZipEntryMapper {
	
	public final static String TYPE_FOLDER = "folder";
	
	public final static String ENTRY_SEPARATOR = ".";
	
	String entryName;
	
	String entryId;
	
	String entryAlias;
	
	String entryType;
	
	int version;
	
	InputStream stream;
	
	public ZipEntryMapper(String entryName,String entryId,String entryAlias, String entryType,int version, InputStream stream) {
		this.entryName = entryName;
		this.entryAlias = entryAlias;
		this.entryType = entryType;
		this.version = version;
		this.stream = stream;
		this.entryId = entryId;
	}
	
	public String getEntryName() {
		return entryName;
	}
	
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	
	public InputStream getStream() {
		return stream;
	}
	
	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
	public String getEntryAlias() {
		return entryAlias;
	}
	
	public void setEntryAlias(String entryAlias) {
		this.entryAlias = entryAlias;
	}
	
	public String getEntryType() {
		return entryType;
	}
	
	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	
}

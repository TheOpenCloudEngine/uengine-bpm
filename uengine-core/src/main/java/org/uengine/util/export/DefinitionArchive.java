package org.uengine.util.export;

import java.io.Serializable;

public class DefinitionArchive implements Serializable{
	String id;
	String name;
	String alias;
	String belongingId;
	String versionId;
	String objectType;
	String archiveFileName;
	String description;
	String parentFolder;
	boolean isRoot;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getArchiveFileName() {
		return archiveFileName;
	}
	public void setArchiveFileName(String archiveFileName) {
		this.archiveFileName = archiveFileName;
	}
	public String getBelongingId() {
		return belongingId;
	}
	public void setBelongingId(String belongingId) {
		this.belongingId = belongingId;
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getParentFolder() {
		return parentFolder;
	}
	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}
	
	

}

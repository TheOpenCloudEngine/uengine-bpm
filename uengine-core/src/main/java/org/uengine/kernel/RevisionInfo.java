package org.uengine.kernel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.TextAreaInput;
import org.metaworks.validator.NotNullValid;
import org.metaworks.validator.Validator;

public class RevisionInfo implements Serializable{
	
	
	public static void metaworksCallback_changeMetadata(Type type){
		type.removeFieldDescriptor("AuthorId");
		type.removeFieldDescriptor("ChangeTime");
		type.removeFieldDescriptor("Version");
		type.setFieldOrder(new String[]{"AuthorName", "AuthorEmailAddress", "AuthorCompany", "ChangeDescription"});
		type.getFieldDescriptor("ChangeDescription").setInputter(new TextAreaInput(30,5));
	}
	
	
	String authorName;
	String authorId;
	String authorEmailAddress;
	String authorCompany;
	String changeDescription;
	Calendar changeTime;
	int version;

	public String getAuthorCompany() {
		return authorCompany;
	}
	public void setAuthorCompany(String authorCompany) {
		this.authorCompany = authorCompany;
	}
	public String getAuthorEmailAddress() {
		return authorEmailAddress;
	}
	public void setAuthorEmailAddress(String authorEMailAddress) {
		this.authorEmailAddress = authorEMailAddress;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public Calendar getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Calendar changeDate) {
		this.changeTime = changeDate;
	}
	public String getChangeDescription() {
		return changeDescription;
	}
	public void setChangeDescription(String changeDescription) {
		this.changeDescription = changeDescription;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public Object clone() throws CloneNotSupportedException {
		try{
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream ow = new ObjectOutputStream(bao);
			ow.writeObject(this);
			ByteArrayInputStream bio = new ByteArrayInputStream(bao.toByteArray());			
			ObjectInputStream oi = new ObjectInputStream(bio);
			
			return oi.readObject();			
		}catch(Exception e){
			throw new RuntimeException(e);
		}

	}
	
}

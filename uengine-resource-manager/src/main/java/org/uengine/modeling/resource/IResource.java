package org.uengine.modeling.resource;

import org.metaworks.ContextAware;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;
import org.metaworks.annotation.Name;
//import org.metaworks.widget.Download;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;

@Face(ejsPath="dwr/metaworks/genericfaces/TreeFace.ejs")
public interface IResource extends ContextAware{
	
	public static String TYPE_FOLDER = "folder";

	public static String HOW_TREE = "tree";
	public static String HOW_DELETE = "delete";
	
	public static String WHERE_NAVIGATOR = "navigator";

	/**
	 * path is the most important field in the class.
	 * path is relative path from codebase
	 * for example, 
	 * <p>folder : practices</p>
	 * <p>file : practices\\scurm.practice</p>
	 * name, displayName, type are based on the path thus if you want to modify resource 
	 * setPath() is the best way
	 *
	 * @return      relative path from codebase
	 */
	public String getPath();
	
	 /**
	 * displayname is provided from path by parsing
	 * the file's name is provided except filename extension
	 * * for example, 
	 * <p>folder : practices</p>
	 * <p>file : scurm</p>
	 *
	 * @return      only file name will be returned
	 */
	 @Name
	public String getDisplayName();
	
	/**
	 * name is provided from path by parsing
	 * * for example, 
	 * <p>folder : practices</p>
	 * <p>file : scurm.practice</p>
	 *
	 * @return      file name includ filename extension except folder 
	 */

	public String getName();
	
	/**
	 * type is filename extension 
	 * if the file is folder String "folder" will be returned
	 * * for example, 
	 * <p>folder : folder</p>
	 * <p>file : practice</p>
	 *
	 * @return      filename extension 
	 */
	public String getType();
	 /**
	 * change the file or folder's name
	 * for example, 
	 * if "method" is recieved as a newName parameter
	 * <p>folder : practices -> method</p>
	 * if "UX" is recieved as a newName parameter
	 * <p>file : scurm -> UX</p>
	 *
	 * @param newName 
	 * @return      only file name will be returned
	 */
	public void rename(String newName);
	public IContainer getParent();
	public void setParent(IContainer iContainer);
	
	public void accept(IResourceVisitor visitor);
	public void accept(IResourceVisitor visitor, boolean admin);
	
	@Hidden
	public boolean isContainer();

	public void setPath(String path);

	void save(Object editingObject) throws Exception;

//	void delete();
//
//	Download download(String fileName, String mimeType) throws Exception;
//
//	void copy(String desPath) throws Exception;
//
//	void upload(InputStream is);
//
//	void move(IContainer container) throws IOException;
//
//	void newOpen() throws Exception;
//
//	void reopen() throws Exception;
}

package org.uengine.util;

import java.io.*;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;


/**
 * 
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version $Id: LocalFileSystem.java,v 1.1 2012/02/13 05:29:10 sleepphoenix4 Exp $
 */
public class LocalFileSystem implements FileSystem {
	
	private static Logger logger = Logger.getLogger(LocalFileSystem.class);
	
	public LocalFileSystem() {
	}

	public String getClassesDirectory(Object object) {
		return getClassesFile(object).getParent();
	}

	public File getClassesFile(Object object) {
		String name = object.getClass().getName().replace('.',
				File.separatorChar);
		name = String.valueOf(name).concat(".class");

		return getClassesFile(name);
	}

	public File getClassesFile(String fileName) {
		String path = System.getProperty("java.class.path");

		for (StringTokenizer tokenizer = new StringTokenizer(path,
					File.pathSeparator); tokenizer.hasMoreTokens();) {
			path = tokenizer.nextToken();

			File file = new File(String.valueOf((new StringBuffer(
							String.valueOf(path))).append(File.separator)
												 .append(fileName)));

			if (file.exists()) {
				return file;
			}
		}

		return null;
	}

	public File newFile(String path) {
		return new File(path);
	}

	public int listContains(String[] baseList, String name) {
		for (int i = 0; i < baseList.length; i++) {
			if (baseList[i].equalsIgnoreCase(name)) {
				return i;
			}
		}

		return -1;
	}

	public String addDir(String path, String dir) {
		File file = newFile(path);

		return String.valueOf((new StringBuffer(String.valueOf(file.getParent()))).append(
				File.separator).append(dir).append(File.separator).append(file.getName()));
	}

	public boolean makeDir(String path) {
		File dir = newFile(path);
		dir.mkdir();

		return dir.isDirectory();
	}

	public boolean makeDirs(String path) {
		File dir = newFile(path);
		dir.mkdirs();

		return dir.isDirectory();
	}

	public Properties loadProperties(String path) throws IOException {
		Properties properties = new Properties();
		BufferedInputFile stream = new BufferedInputFile(path);
		properties.load(stream);
		stream.close();

		return properties;
	}

	public boolean traverse(TraverseAction act, File dir)
		throws Exception {
		if (!dir.isAbsolute()) {
			return false;
		}

		String[] fileList = dir.list();

		if (fileList == null) {
			return false;
		}

		for (int i = 0; i < fileList.length; i++) {
			File curFile = newFile(createFilePath(dir.getAbsolutePath(),
						fileList[i]));

			if (curFile.isFile()) {
				if (act.onFile(curFile) == 3) {
					return false;
				}

				continue;
			}

			if (curFile.isDirectory() && (act.onDirEntry(curFile) == 1) &&
					(!traverse(act, newFile(curFile.getAbsolutePath())) ||
					(act.onDirExit(curFile, fileList) != 2))) {
				return false;
			}
		}

		return true;
	}

	public void deleteDirectoryTree(String s) throws Exception {
	}

	public boolean compareFiles(String path1, String path2)
		throws IOException {
		long total = newFile(path1).length();

		if (total != newFile(path2).length()) {
			return false;
		}

		FileInputStream input1 = new FileInputStream(path1);
		FileInputStream input2 = new FileInputStream(path2);
		byte[] buffer1 = new byte[32768];
		byte[] buffer2 = new byte[32768];
		long totalRead;
		int bytesRead;

		for (totalRead = 0L;
				(bytesRead = input1.read(buffer1, 0, buffer1.length)) > 0;
				totalRead += bytesRead) {
			if (input2.read(buffer2, 0, buffer2.length) != bytesRead) {
				return false;
			}

			if (!compareBytes(buffer1, buffer2, bytesRead)) {
				return false;
			}
		}

		input1.close();
		input2.close();

		return total == totalRead;
	}

	public boolean compareBytes(byte[] buf1, byte[] buf2, int count) {
		for (int i = 0; i < count; i++) {
			if (buf1[i] != buf2[i]) {
				return false;
			}
		}

		return true;
	}

	public void copyFile(String src, String dest, String header)
		throws IOException {
			
		File descFile = new File(dest);
		if ( !descFile.getParentFile().exists() ) descFile.getParentFile().mkdirs();
		FileInputStream input = new FileInputStream(src);
		FileOutputStream output = new FileOutputStream(dest);

		// header ������� ��=
//		if (header != null) {
//			int headerLength = header.length();
//			byte[] headerBytes = new byte[headerLength];
//			header.getBytes(0, headerLength, headerBytes, 0);
//			output.write(headerBytes, 0, headerLength);
//		}
		int bufferSize = 32768;
		
//		try {
//			if ( FileConstants.BUFFER_SIZE > 0 ) bufferSize = FileConstants.BUFFER_SIZE; 
//		} catch (Exception e) {}
		
		byte[] buffer = new byte[bufferSize];
		int bytesRead;

		while ((bytesRead = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, bytesRead);
		}

		input.close();
		output.close();
	}

	public void copyFile(String src, String dest) throws IOException {
		copyFile(src, dest, null);
	}


	/**
	   * ������(fSource); �ٸ� ����(fDestionation)�� �����Ѵ�.
	   * @param     source ������
	   * @param     destination ������ ����
	   * @exception
	   * @return    �����ϸ� 1, �����ϸ� -1; ����
	   */
	public int copyFile(File fSource, File fDestination)
	{
		//System.out.println(fSource.getPath() + " >>>>> " + fDestination.getPath());
		
		if (!fSource.exists()) {
			return -1;
		}

		try {
			copyFile(fSource.getAbsolutePath(), fDestination.getAbsolutePath());
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String createFilePath(String path, String file) {
		return String.valueOf((new StringBuffer(String.valueOf(path))).append(
				File.separator).append(file));
	}
	

	/**
	 * ������ ���� �����Ѵ�.
	 * @return boolean true�� ���强��
	 */
	public boolean saveDoc(File docFile, String htmlContents)
		throws Exception {
		FileWriter fw = new FileWriter(docFile);

		try {
			logger.info("Return saveDoc() Contens path " + docFile);
			logger.debug("htmlContents " + htmlContents);
			fw.write(htmlContents);
			fw.close();

			return true;
		} catch (Exception e) {
			logger.error("saveDoc() ERROR " + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e) {
			}
		}
	}	
}
/*
 * $Log: LocalFileSystem.java,v $
 * Revision 1.1  2012/02/13 05:29:10  sleepphoenix4
 * initial commit uEngine package, since 2012
 *
 * Revision 1.3  2008/04/10 08:44:36  curonide
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/05 02:31:29  curonide
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/04 07:34:43  bpm
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/04 05:25:43  bpm
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/09 02:31:27  pongsor
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/16 01:03:56  mahler
 * Add package file.io, Append Constants Variable
 *
 * Revision 1.1  2005/09/06 06:59:37  ghbpark
 * xcommons 2.0 start
 *
 * Revision 1.1  2005/04/11 10:24:19  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/04/03 01:36:47  ghbpark
 * *** empty log message ***
 *
 * Revision 1.5  2005/02/28 11:32:20  ghbpark
 * *** empty log message ***
 *
 * Revision 1.4  2004/04/14 11:29:30  ghbpark
 * *** empty log message ***
 *
 * Revision 1.3  2004/03/31 07:12:28  ghbpark
 * ���� ī�� �� �� �޼ҵ� �߰�
 *
 * Revision 1.2  2004/03/31 02:36:35  ghbpark
 * Constants d��
 *
 * Revision 1.1  2004/03/26 10:29:43  ghbpark
 * FILE I/O ���̺귯�� �߰�
 *
 * Revision 1.1  2004/03/26 03:16:11  ghbpark
 * *** empty log message ***
 *
 */

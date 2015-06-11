package org.uengine.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.uengine.kernel.GlobalContext;
import org.uengine.util.export.UEngineArchive;

import net.sf.jazzlib.ZipEntry;
import net.sf.jazzlib.ZipOutputStream;

public class CompressZip {
	private static final int COMPRESSION_LEVEL = 8;
	private static final int BUFFER_SIZE = 1024 * 2;
	private static final char FS = File.separatorChar;

	public CompressZip() {
		
	}
	
	public void zip(String sourcePath, String fileName) throws IOException {
		zip(sourcePath, fileName, null);
	}
	

	public void zip(String sourcePath, String fileName, UEngineArchive ua) throws IOException {

		File sourceFile = new File(sourcePath);
		String strTarget = null;
		if (sourceFile.isDirectory()) {
			strTarget = sourceFile.getParent();
		} else {
			strTarget = sourcePath.substring(0, sourcePath.lastIndexOf(FS));
		}
		zip(sourcePath, fileName, strTarget, ua);
	}

	public void zip(String sourcePath, String fileName, String targetDir, UEngineArchive ua) throws IOException {

		File sourceFile = new File(sourcePath);
		if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
			System.out.println("Not Found a Source File or Directory.....");
			return;
		}

		String fileNm = null;
		if (fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".zip")) {
			fileNm = fileName;
		} else {
			System.out.println("You gave a non-zip file name... please check your parameters...");
			return;
		}

		sourcePath = sourcePath.replace("\\", String.valueOf(File.separatorChar));
		sourcePath = sourcePath.replace("/", String.valueOf(File.separatorChar));
		targetDir = targetDir.replace("\\", String.valueOf(File.separatorChar));
		targetDir = targetDir.replace("/", String.valueOf(File.separatorChar));
		targetDir += FS;

		BufferedOutputStream outputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(targetDir + FS + fileNm));
			zipOutputStream = new ZipOutputStream(outputStream);
			zipOutputStream.setLevel(COMPRESSION_LEVEL);

			zipEntry(sourceFile, sourcePath, targetDir, zipOutputStream);
			
			if (ua != null) {
				zipOutputStream.putNextEntry(new net.sf.jazzlib.ZipEntry("META-INF" + File.separatorChar));
				zipOutputStream.closeEntry();
				
				zipOutputStream.putNextEntry(new net.sf.jazzlib.ZipEntry("META-INF" + File.separatorChar+ "manifest.xml"));
				try {
					GlobalContext.serialize(ua, zipOutputStream, String.class);
					zipOutputStream.closeEntry();
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
			
			
			zipOutputStream.finish();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zipOutputStream != null) {
				zipOutputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}

	}

	private void zipEntry(File sourceFile, String sourcePath,
			String targetDir, ZipOutputStream zipOutputStream) throws Exception {

		if (sourceFile.isDirectory()) {
			if (sourceFile.getName().equalsIgnoreCase(".metadata")) {
				return;
			}
			
			File[] fileArray = sourceFile.listFiles();
			for (int i = 0; i < fileArray.length; i++) {
				zipEntry(fileArray[i], sourcePath, targetDir, zipOutputStream);
			}
		} else {
			BufferedInputStream inputStream = null;
			byte[] buffer = new byte[BUFFER_SIZE];
			try {
				if (sourceFile.getAbsolutePath().equalsIgnoreCase(targetDir)) {
					return;
				}
				if (sourceFile.getName().indexOf(".zip") > -1) {
					return;
				}
				
				String strAbsPath = sourceFile.getPath();
				String strZipEntryName = strAbsPath.substring(sourcePath.length(), strAbsPath.length());
				
				inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
				ZipEntry zentry = new ZipEntry(strZipEntryName);
				zentry.setTime(sourceFile.lastModified());
				zipOutputStream.putNextEntry(zentry);

				int cnt = 0;
				while ((cnt = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zipOutputStream.write(buffer, 0, cnt);
				}
				zipOutputStream.closeEntry();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
	}

	public void zip(List fileLists, String fileName, String targetDirPath) throws IOException {

		String fileNm = null;
		if (fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".zip")) {
			fileNm = fileName;
		} else {
			System.out.println("You gave a non-zip file name... please check your parameters...");
			return;
		}

		BufferedOutputStream outputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(targetDirPath + FS + fileNm));
			zipOutputStream = new ZipOutputStream(outputStream);
			zipOutputStream.setLevel(COMPRESSION_LEVEL);

			for (int i = 0; i < fileLists.size(); i++) {
				int indexNum = ((File) fileLists.get(i)).getAbsolutePath().lastIndexOf(FS);
				System.out.println("indexNum ::: " + indexNum);
				String sourcePath = ((File) fileLists.get(i)).getAbsolutePath().substring(0, indexNum);
				System.out.println("sourcePath :::: " + sourcePath);
				zipEntry((File) fileLists.get(i), sourcePath, targetDirPath, zipOutputStream);
			}
			zipOutputStream.finish();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zipOutputStream != null) {
				zipOutputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
}

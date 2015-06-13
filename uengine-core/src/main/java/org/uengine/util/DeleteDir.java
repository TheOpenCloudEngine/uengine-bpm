package org.uengine.util;

import java.io.File;

public class DeleteDir {
	public static void deleteDir(String path) {
		deleteDir(new File(path));
	}

	public static void deleteDir(File file) {
		if (!file.exists())
			return;

		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDir(files[i]);
			} else {
				files[i].delete();
			}
		}
		file.delete();
	} 
}

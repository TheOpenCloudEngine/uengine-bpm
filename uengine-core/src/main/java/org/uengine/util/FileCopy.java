package org.uengine.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {
	private DataInputStream in;
	private DataOutputStream out;
	
	public FileCopy() {
		
	}
	
	public FileCopy(String source, String target) {
		try {
			in = new DataInputStream(new FileInputStream(source));
			out = new DataOutputStream(new FileOutputStream(target));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setSource(String source) {
		try {
			in = new DataInputStream(new FileInputStream(source));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void setTarget(String target) {
		try {
			out = new DataOutputStream(new FileOutputStream(target));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		byte[] buff = new byte[1024];
		int size;
		
		try {
			while ((size = in.read(buff)) > -1)
				out.write(buff, 0, size);
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		FileCopy copy = new FileCopy(args[0], args[1]);
	}
}
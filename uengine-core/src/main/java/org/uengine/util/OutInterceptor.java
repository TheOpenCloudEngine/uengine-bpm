/*
 * System out 콘솔 출력 후킹
 */
package org.uengine.util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class OutInterceptor {

	private PipedInputStream pipedInputStream;
	private PrintStream originalPrint;

	public OutInterceptor() {
		originalPrint = System.out;
		this.pipedInputStream = new PipedInputStream();
	}

	public String getMessages() throws IOException {
		byte[] messages = new byte[pipedInputStream.available()];
		pipedInputStream.read(messages, 0, messages.length);
		return new String(messages);
	}

	public void active() throws IOException {
		final PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
		PrintStream saveStream = new PrintStream(pipedOutputStream) {
			@Override
			public void print(String x) {
				try {
					pipedOutputStream.write(x.getBytes());
				} catch (IOException e) {
					System.out.println(" OutInterceptor Error ");
				}
				originalPrint.println(x);
			}
		};
		System.setOut(saveStream);
	}
	
	public static void main(String args[]) {
		OutInterceptor oi = new OutInterceptor();
		
		try {
			oi.active();
			System.out.println("123");
			System.out.print("999");
			System.out.println(1);
			System.out.println(true);
			System.out.println("456");
			
			System.out.println(oi.getMessages());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
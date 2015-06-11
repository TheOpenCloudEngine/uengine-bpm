package org.uengine.util;

import java.io.*;


/**
 * 
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version $Id: BufferedInputFile.java,v 1.1 2012/02/13 05:29:10 sleepphoenix4 Exp $
 */
public class BufferedInputFile extends BufferedInputStream {
	StringBuffer buffer;
	FileInputStream f;

	public BufferedInputFile(String path) throws FileNotFoundException {
		super(new FileInputStream(path));
		f = (FileInputStream) super.in;
		buffer = new StringBuffer(128);
	}

	public final String readLine() throws IOException {
		buffer.setLength(0);

		do {
			int ch;

			if ((ch = read()) == -1) {
				break;
			}

			if (ch == 13) {
				continue;
			}

			if (ch == 10) {
				break;
			}

			buffer.append((char) ch);
		} while (true);

		if (buffer.length() > 0) {
			return buffer.toString();
		} else {
			return null;
		}
	}

	public final void close() throws IOException {
		f.close();
	}
}
/*
 * $Log: BufferedInputFile.java,v $
 * Revision 1.1  2012/02/13 05:29:10  sleepphoenix4
 * initial commit uEngine package, since 2012
 *
 * Revision 1.3  2008/09/30 05:11:43  curonide
 * *** empty log message ***
 *
 * Revision 1.2  2007/12/05 02:31:29  curonide
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/04 07:34:42  bpm
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/04 05:25:42  bpm
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/09 02:31:27  pongsor
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/16 01:03:56  mahler
 * Add package file.io, Append Constants Variable
 *
 * Revision 1.1  2005/09/06 06:59:36  ghbpark
 * xcommons 2.0 start
 *
 * Revision 1.1  2005/04/11 10:24:19  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/04/03 01:36:47  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2004/03/26 10:29:43  ghbpark
 * FILE I/O 
 *
 * Revision 1.1  2004/03/26 03:16:11  ghbpark
 * *** empty log message ***
 *
 */

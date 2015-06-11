/*
 * Copyright 2001-2004 by HANWHA S&C Corp.,
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HANWHA S&C Corp. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with HANWHA S&C Corp.
 */
package org.uengine.util;

import java.io.*;


/**
 * InputStrea; 
 *
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 * @version $Id: StreamUtils.java,v 1.1 2012/02/13 05:29:10 sleepphoenix4 Exp $
 */
public class StreamUtils {
	private static final int BUFFER_SIZE = 1024;

	public static String getString(InputStream inputStream)
		throws IOException {
		if (inputStream == null) {
			return null;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"), BUFFER_SIZE);
		int charsRead;
		char[] copyBuffer = new char[BUFFER_SIZE];
		StringBuffer sb = new StringBuffer();

		while ((charsRead = in.read(copyBuffer, 0, BUFFER_SIZE)) != -1) {
			sb.append(copyBuffer, 0, charsRead);
		}

		in.close();

		return sb.toString();
	}

	public static String getString(Reader reader)
		throws IOException {
		if (reader == null) {
			return null;
		}

		BufferedReader in = new BufferedReader(reader, BUFFER_SIZE);
		int charsRead;
		char[] copyBuffer = new char[BUFFER_SIZE];
		StringBuffer sb = new StringBuffer();

		while ((charsRead = in.read(copyBuffer, 0, BUFFER_SIZE)) != -1) {
			sb.append(copyBuffer, 0, charsRead);
		}

		in.close();

		return sb.toString();
	}
}
/*
 * $Log: StreamUtils.java,v $
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
 * Revision 1.3  2007/12/04 05:25:43  bpm
 * *** empty log message ***
 *
 * Revision 1.1  2007/07/02 01:41:02  pongsor
 * Form management support
 *
 * Revision 1.1  2007/01/26 10:54:42  mahler
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/26 10:41:15  mahler
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/06 06:59:33  ghbpark
 * xcommons 2.0 start
 *
 * Revision 1.1  2005/04/11 10:24:06  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2005/04/03 01:36:51  ghbpark
 * *** empty log message ***
 *
 * Revision 1.2  2005/01/27 07:37:58  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2004/04/12 11:44:04  ghbpark
 * InputStream or Reader -> Stream
 *
 */

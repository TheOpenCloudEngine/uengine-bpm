package org.uengine.util;

import java.io.File;


/**
 * 
 * 
 * @author <a href="mailto:bigmahler@users.sourceforge.net">Jong-Uk Jeong</a>
 * @version $Id: TraverseAction.java,v 1.1 2012/02/13 05:29:10 sleepphoenix4 Exp $
 */
public interface TraverseAction {
	public static final int ABORT_TRAVERSE = 3;
	public static final int CONTINUE_TRAVERSE = 2;
	public static final int DIVE_TRAVERSE = 1;

	public abstract int onDirExit(File file, String[] as)
		throws Exception;

	public abstract int onDirEntry(File file) throws Exception;

	public abstract int onFile(File file) throws Exception;
}
/*
 * $Log: TraverseAction.java,v $
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
 * Revision 1.1  2007/07/09 02:31:31  pongsor
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
 * Revision 1.1  2004/03/26 10:29:43  ghbpark
 * FILE I/O 
 *
 * Revision 1.1  2004/03/26 03:16:11  ghbpark
 * *** empty log message ***
 *
 */

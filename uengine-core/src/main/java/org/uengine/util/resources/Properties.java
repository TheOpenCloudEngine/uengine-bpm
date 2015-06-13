package	org.uengine.util.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.*;

/**
 * 
 * @version	$Id: Properties.java,v 1.1 2012/02/13 05:29:50 sleepphoenix4 Exp $
 * @author  <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 */
 
public class Properties extends java.util.Properties {
	
	private static final String keyValueSeparators 			= "=: \t\r\n\f";
	private static final String strictKeyValueSeparators 	= "=:";
	private static final String specialSaveChars 			= "=: \t\r\n\f#!";
	private static final String whiteSpaceChars 			= " \t\r\n\f";
	
    private static Properties p = null;
	
	protected Properties defaults;

	public Properties() {
		this(null);
	}
	
	public Properties(Properties defaults) {
		this.defaults = defaults;
	}
	
	public synchronized void load(InputStream inStream) throws IOException {
		load(inStream,"UnicodeLittle");
	}
	
	
	public synchronized void load(InputStream inStream, String enc) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(inStream, enc));
		while (true) {
			String line = in.readLine();
			//System.out.println("line : " + line);
			if(line == null)
				return;
			
			if (line.length() > 0) {
				char firstChar = line.charAt(0);
				if ((firstChar != '#') && (firstChar != '!')) {
					while (continueLine(line)) {
						String nextLine = in.readLine();
						if(nextLine == null)
							nextLine = new String("");
						String loppedLine = line.substring(0, line.length()-1);
						int startIndex=0;
						for(startIndex=0; startIndex<nextLine.length(); startIndex++) {
							if (whiteSpaceChars.indexOf(nextLine.charAt(startIndex)) == -1)
								break;
						}
						nextLine = nextLine.substring(startIndex,nextLine.length());
						line = new String(loppedLine+nextLine);
					}
                    int len = line.length();
                    int keyStart;
                    for(keyStart=0; keyStart<len; keyStart++) {
                        if(whiteSpaceChars.indexOf(line.charAt(keyStart)) == -1)
                            break;
                    }
                    int separatorIndex;
                    for(separatorIndex=keyStart; separatorIndex<len; separatorIndex++) {
                        char currentChar = line.charAt(separatorIndex);
                        if (currentChar == '\\')
                            separatorIndex++;
                        else if(keyValueSeparators.indexOf(currentChar) != -1)
                            break;
                    }
                    int valueIndex;
                    for (valueIndex=separatorIndex+1; valueIndex<len; valueIndex++)
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;
                    if (valueIndex < len)
                        if (strictKeyValueSeparators.indexOf(line.charAt(valueIndex)) != -1)
                            valueIndex++;
                    while (valueIndex < len) {
                        if (whiteSpaceChars.indexOf(line.charAt(valueIndex)) == -1)
                            break;
                        valueIndex++;
                    }
                    String key = line.substring(keyStart, separatorIndex);
                    String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";
                    key = loadConvert(key);
                    value = expandMacro(loadConvert(value));
                    // Find Macro Key
                    if ( key.startsWith("$") ) {
                    	p.put(key.substring(1,key.length()), value);
                    } else {
                    	put(key, toEncode(value, enc));
                    }
                }
            }
	    }
    }
    
    public String toEncode( String str, String enc )
        throws java.io.UnsupportedEncodingException
    {
        if ( str == null )
            return null;
    	if ( enc.equals("UnicodeLittle") )
    		return str;
        return new String( str.getBytes("8859_1"), "KSC5601" );
    }  
    
    private boolean continueLine (String line) {
        int slashCount = 0;
        int index = line.length() - 1;
        while((index >= 0) && (line.charAt(index--) == '\\'))
            slashCount++;
        return (slashCount % 2 == 1);
    }

    private String loadConvert (String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for(int x=0; x<len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if(aChar == 'u') {
                    // Read the xxxx
                    int value=0;
		    for (int i=0; i<4; i++) {
		        aChar = theString.charAt(x++);
		        switch (aChar) {
		          case '0': case '1': case '2': case '3': case '4':
		          case '5': case '6': case '7': case '8': case '9':
		             value = (value << 4) + aChar - '0';
			     break;
			  case 'a': case 'b': case 'c':
                          case 'd': case 'e': case 'f':
			     value = (value << 4) + 10 + aChar - 'a';
			     break;
			  case 'A': case 'B': case 'C':
                          case 'D': case 'E': case 'F':
			     value = (value << 4) + 10 + aChar - 'A';
			     break;
			  default:
                              throw new IllegalArgumentException(
                                           "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char)value);
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private String saveConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len*2);

        for(int x=0; x<len; ) {
            aChar = theString.charAt(x++);
            switch(aChar) {
                case '\\':outBuffer.append('\\'); outBuffer.append('\\');
                          continue;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                          continue;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                          continue;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                          continue;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                          continue;
                default:
                    if ((aChar < 20) || (aChar > 127)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >> 8) & 0xF));
                        outBuffer.append(toHex((aChar >> 4) & 0xF));
                        outBuffer.append(toHex((aChar >> 0) & 0xF));
                    }
                    else {
                        if (specialSaveChars.indexOf(aChar) != -1)
                            outBuffer.append('\\');
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }

    public synchronized void store(OutputStream out,String header) throws IOException
    {
        store(out,header,"UnicodeLittle");
    }

    public synchronized void store(OutputStream out, String header, String enc) throws IOException
    {
        BufferedWriter awriter;
        awriter = new BufferedWriter(new OutputStreamWriter(out, enc));
        if (header != null)
            writeln(awriter, "#" + header);
        writeln(awriter, "#" + new Date().toString());

        Enumeration e=keys();
        Vector v=new Vector();
        while(e.hasMoreElements())
        {
            v.addElement(e.nextElement());
        }
        Collections.sort(v);
        for(int i=0;i<v.size();i++)
        {
            String key=(String)v.elementAt(i);
            String val=(String)get(key);
            key = saveConvert(key, true);
            val = saveConvert(val, false);
            writeln(awriter,key+"="+val);
        }

        awriter.flush();
    }

    private String saveConvert(String theString, boolean escapeSpace) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len*2);

        for(int x=0; x<len; x++)
        {
            char aChar = theString.charAt(x);
            switch(aChar)
            {
		        case ' ':
                    if (x == 0 || escapeSpace)
			            outBuffer.append('\\');
                    outBuffer.append(' ');
		            break;
                case '\\':
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                default:
              /*
                    if ((aChar < 0x0020) || (aChar > 0x007e))
                    {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >>  8) & 0xF));
                        outBuffer.append(toHex((aChar >>  4) & 0xF));
                        outBuffer.append(toHex( aChar        & 0xF));
                    }
                    else
                    {
                        if(specialSaveChars.indexOf(aChar) != -1)
                            outBuffer.append('\\');
                        outBuffer.append(aChar);
                    }
              */
                    if(specialSaveChars.indexOf(aChar) != -1)
                            outBuffer.append('\\');
                    outBuffer.append(aChar);

            }
        }
        return outBuffer.toString();
    }


    private static void writeln(BufferedWriter bw, String s) throws IOException
    {
        bw.write(s);
        bw.newLine();
    }

    private synchronized void enumerate(Hashtable h)
    {
	    if (defaults != null)
        {
	        defaults.enumerate(h);
	    }
	    for (Enumeration e = keys() ; e.hasMoreElements() ;)
        {
	        String key = (String)e.nextElement();
	        h.put(key, get(key));
	    }
    }

    private static char toHex(int nibble)
    {
	    return hexDigit[(nibble & 0xF)];
    }

    private static final char[] hexDigit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};


	private String expandMacro(String orgStr) {
		if (p == null) {
             p = new Properties();
        }

		char []orgChars;
		StringBuffer expStrBuf = new StringBuffer();
		int stIdx = 0;
		int edIdx = 0;

		if (orgStr == null) {
			return null;
        } else {
			orgChars = orgStr.toCharArray();
        }
		// if blank or '!' is first character, return immediately.
		if (orgStr.length()==0) {
            return orgStr;
        }
		if (orgChars[0] == '!') {
            return orgStr;
        }

		while (true) {
			// Find @@ Mark !
			if ((stIdx = orgStr.indexOf("${", edIdx)) >= edIdx) {
				expStrBuf.append(orgStr.substring(edIdx, stIdx));
				if ((edIdx = orgStr.indexOf("}", stIdx + 2)) >= stIdx + 2) {
					String macroStr = orgStr.substring(stIdx + 2, edIdx);
					String expandStr = p.getProperty(macroStr);
					if (expandStr != null) {
						expStrBuf.append(expandStr);
					} else {
						expStrBuf.append(orgStr.substring(stIdx, edIdx + 2));
					}
					
					edIdx += 1;
					stIdx = edIdx;
                } else {
					expStrBuf.append(orgStr.substring(edIdx, orgStr.length()));
					break;
				}
			} else {
				expStrBuf.append(orgStr.substring(edIdx, orgStr.length()));
				break;
			}
		}
		return expStrBuf.toString();
	}

}

/*
 * $Log: Properties.java,v $
 * Revision 1.1  2012/02/13 05:29:50  sleepphoenix4
 * initial commit uEngine package, since 2012
 *
 * Revision 1.4  2008/09/30 05:12:00  curonide
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/05 02:31:38  curonide
 * *** empty log message ***
 *
 * Revision 1.5  2007/12/04 07:34:45  bpm
 * *** empty log message ***
 *
 * Revision 1.3  2007/12/04 05:25:48  bpm
 * *** empty log message ***
 *
 * Revision 1.2  2006/06/17 07:51:44  pongsor
 * uEngine 2.0
 *
 * Revision 1.1  2005/09/06 07:08:18  ghbpark
 * EagleBPM 2.0 start
 *
 * Revision 1.3  2005/04/11 10:45:18  uengine
 * *** empty log message ***
 *
 * Revision 1.1  2004/12/28 04:01:43  ghbpark
 * *** empty log message ***
 *
 * Revision 1.1  2003/03/21 09:54:02  mslee
 * 2003-03-21 : CVS server repaired
 *
 * Revision 1.1  2002/09/23 00:45:43  ghbpark
 * first release
 *
 */
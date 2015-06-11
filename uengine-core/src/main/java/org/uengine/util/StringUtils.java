//StringUtils
package org.uengine.util;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 *
 * @author <a href="mailto:kighie@hanwha.co.kr">Kwon, Ik Chan</a>
 * @since 2003.07.21
 * @version 1.0
 */
public class StringUtils
{
	private StringUtils () {}
	
	/**
	* @param tobetested
	*/
	public final static boolean isEmpty ( final String tobetested )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			return false;
		}
		return true;
	}
	

	public final static String getString ( final Object tobetested , final String emptyString )
	{
		if ( tobetested != null )
		{
			return tobetested.toString().intern();
		}
		return emptyString;
	}
	
	
	public final static String getString ( final String tobetested , final String emptyString )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			return tobetested.intern();
		}
		return emptyString;
	}
	
	public final static int parseInt ( final String tobetested , final int defValue )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			try
			{
				return Integer.parseInt (tobetested);
			}
			catch ( Exception e )
			{
				// DO NOTHING return defValue
			}
		}
		return defValue;
	}
	
	public final static boolean parseBoolean ( final String tobetested , final boolean defValue )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			String str = tobetested.trim().toLowerCase();
			if ( str.equals ( "true" ) 
				|| str.equals ( "yes" ) )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		return defValue;
	}
	
	public final static long parseLong ( final String tobetested , final long defValue )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			try
			{
				return Long.parseLong (tobetested);
			}
			catch ( Exception e )
			{
				// DO NOTHING return defValue
			}
		}
		return defValue;
	}
	
	
	public final static float parseFloat ( final String tobetested , final float defValue )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			try
			{
				return Float.parseFloat (tobetested);
			}
			catch ( Exception e )
			{
				// DO NOTHING return defValue
			}
		}
		return defValue;
	}
	
	public final static short parseShort ( final String tobetested , final short defValue )
	{
		if ( ( tobetested != null )
			&& ( !tobetested.trim().equals("") ) )
		{
			try
			{
				return Short.parseShort (tobetested);
			}
			catch ( Exception e )
			{
				// DO NOTHING return defValue
			}
		}
		return defValue;
	}
	
	public static StringTokenizer tokenizeString ( String str, String del )
	{
		if ( isEmpty ( str ) ) return null;
		
		StringTokenizer tokenizer = new StringTokenizer ( str , del );
		
		return tokenizer;
	}
	
	public static Vector parseVector ( String str, String del )
	{
		StringTokenizer tokenizer = tokenizeString ( str , del );
		
		Vector vector = new Vector();
		
		if ( tokenizer != null )
		{
			while ( tokenizer.hasMoreElements() )
			{
				vector.add ( tokenizer.nextElement() );
			}
		}
		
		return vector;
	}
	
	/**
	 *
	 */
	public static final String[] devide ( String text , String delimiter )
	{
		String[] tokens = new String[2];
		
		int index = text.indexOf ( delimiter );
		
		if ( index > 0 )
		{
			String name = text.substring ( 0 , index );
			String value;
			
			if ( index < text.length()-1 )
			{
				value = text.substring ( index + 1 );
			}
			else
			{
				value = "";
			}
			
			tokens[0] = name;
			tokens[1] = value;
		}
		else if ( index == 0 )
		{
			tokens[0] = "";
			tokens[1] = text.substring(1);
		}
		else
		{
			tokens[0] = text;
			tokens[1] = "";
		}
		
		return tokens;
	}
	
	/**
	 *
	 */
	public static final String encode ( String text , String srcEnc , String targetEnc )
									throws java.io.UnsupportedEncodingException
	{
		if ( text == null )
		{
			return null;
		}
		
		return new String ( text.getBytes(srcEnc) , targetEnc );
	}
	
	/**
	 *
	 */
	public static final String encodeFrom ( String text , String srcEnc )
									throws java.io.UnsupportedEncodingException
	{
		if ( text == null )
		{
			return null;
		}
		
		return new String ( text.getBytes(srcEnc) );
	}
	
	/**
	 *
	 */
	public static final String encodeFromISO8859 ( String text )
									throws java.io.UnsupportedEncodingException
	{
		if ( text == null )
		{
			return null;
		}
		
		return encodeFrom ( text ,  "ISO-8859-1" );
	}
	
	/**
	 *
	 */
	public static final String encodeTo ( String text , String targetEnc )
									throws java.io.UnsupportedEncodingException
	{
		if ( text == null )
		{
			return null;
		}
		
		return new String ( text.getBytes() , targetEnc );
	}
	
	
	/*

	public static Iterator tokenizeString (String str, String del )
	{
		if ( isEmpty ( str ) ) return null;
		
		String[] array = str.split ( del );
		
		return new StringIterator(array);
	}
	
	/**
	* <Pre>
	*/
	public static String[] tokenizeString2Array ( String str, String del )
	{
		if ( isEmpty ( str ) ) return new String[0];
		
		int lastIndex = 0;
		int index = str.indexOf ( del , lastIndex );
		
		int endIndex = str.length() -1;
		
		int size = 0;
		String[] tempArray = new String[10];
		
		while ( ( index >= 0 ) 
			|| ( lastIndex < endIndex ) )
		{
			if ( tempArray.length < size + 1 )
			{
				String [] newArray = new String[tempArray.length + 10];
				System.arraycopy ( tempArray , 0 , newArray , 0 , tempArray.length );
				tempArray = newArray;
			}
			
			String token = "";
			
			if ( index >= 0 ) 
			{
				token = str.substring ( lastIndex , index );
				lastIndex = index + 1;
			}
			else if ( lastIndex < endIndex )
			{
				token = str.substring ( lastIndex );
				lastIndex = endIndex;
			}
			
			if ( token.length() > 0 )
			{
				tempArray[size] = token;
				size++;
			}
			
			index = str.indexOf ( del , lastIndex );
		}
		
		String [] strArray = new String[size];
		
		System.arraycopy ( tempArray , 0 , strArray , 0 , size );
		return strArray;
	}
	
	/**
	* <Pre>
	*/
	public static String[] tokenizeString2Array (String str, char del )
	{
		if ( isEmpty ( str ) ) return new String[0];
		
		String[] arrayToken = null;
		
		int l = str.length();
		StringBuffer sb = new StringBuffer(str.length());
		
		char[] charArr = str.toCharArray();
		
		char c;
		
		for(int i=0; i<l ; i++)
		{
			c=charArr[i];
			
			if(c==del)
			{
				if ( !sb.toString().trim().equals ("") )
				{
					arrayToken = expandStringArray	( arrayToken , 1 );
					String part = sb.toString();
					arrayToken [ arrayToken.length - 1 ] = part;
					sb.delete ( 0, part.length()-1 );
				}				
			}
			else if(i==l-1)
			{
				sb.append(c);
				arrayToken = expandStringArray	( arrayToken , 1 );
				arrayToken [ arrayToken.length - 1 ] = sb.toString();
			}
			else
			{
				sb.append(c);
			}
		}
		
		return arrayToken;
	}
	
	/**
	*/
	public static String[] expandStringArray ( String[] sourceArray , int expandLen )
	{
		String[] array = null;
		
		if ( sourceArray != null )
		{
			int tokenLen = sourceArray.length;
			
			array = new String [ tokenLen + expandLen ];
			
			System.arraycopy ( sourceArray , 0 , array , 0 , sourceArray.length );
		}
		else
		{
			array = new String [expandLen];
		}
		
		
		return array;
	}
	
		
	/**
	*/
	public static String[] expandStringArray ( String[] sourceArray , String string2Add )
	{
		String[] newArray = expandStringArray ( sourceArray , 1 );
		newArray [ newArray.length - 1 ] = string2Add;
		
		return newArray;
	}
	
		
	/**
	*/
	public static String[] concatStringArray ( String[] sourceArray , String[] array2Add )
	{
		if ( sourceArray == null )
		{
			return array2Add;
		}
		
		if ( array2Add == null )
		{
			return sourceArray;
		}
		
		String[] concatArray = new String [ sourceArray.length + array2Add.length ];
		
		System.arraycopy ( sourceArray , 0 , concatArray , 0 , sourceArray.length );
		
		System.arraycopy ( array2Add , 0 , concatArray , sourceArray.length , array2Add.length );
		
		return concatArray;
	}
	
		
	/**
	*/
	public static String replace( String in_str, String in_find, String in_rep )
	{
		StringBuffer sb = new StringBuffer( in_str.length( ) );

		int lenFind  = in_find.length( );

		int posFind;
		int pos = 0;

		while( true )
		{
			posFind	= in_str.indexOf( in_find, pos );
			if( -1 == posFind )
			{
				sb.append( in_str.substring( pos ) );
				break;
			}

			sb.append( in_str.substring( pos, posFind ) ).append( in_rep );
			pos = posFind + lenFind;
		}

		return	sb.toString( );
	}
	
	public static int count ( String source , char find )
	{
		char[] cahracters = source.toCharArray();
		int count = 0;
		
		for ( int i = cahracters.length-1;i>=0;i--)
		{
			if ( cahracters[i] == find )
			{
				count++;
			}
		}
		
		return count;
	}
	
	public static final String getClassShortName ( Object object )
	{
		String clzName = object.getClass().getName();
		int lastIndex = clzName.lastIndexOf (".");
		
		if ( lastIndex > -1 
			&& lastIndex < clzName.length() -1 )
		{
			clzName = clzName.substring ( lastIndex + 1 );
		}
		
		return clzName;
	}
	
	
	public static final short ALIGN_LEFT = 1;
	public static final short ALIGN_RIGHT = -1;
	
	public static final String formatClassName ( Object object , int len )
	{
		String clzName = object.getClass().getName();
		
		if ( clzName.length() > len )
		{
			return new String ( formatRightAlign ( clzName , len ) );
		}
		else
		{
			return new String ( format ( clzName , len ) );
		}
	}
	
	
	public static final String formatClassShortName ( Object object , int len )
	{
		String clzName = getClassShortName (object);
		
		if ( clzName.length() > len )
		{
			return new String ( formatRightAlign ( clzName , len ) );
		}
		else
		{
			return new String ( format ( clzName , len ) );
		}
	}
	
	
	public static final String formatStr ( String value , int len , short align , char fill )
	{
		return new String ( format ( value , len , align , fill ) );
	}
	
	public static final String formatMessage ( Object[] values , int[] lens , String delimiter )
	{
		StringBuffer buffer = new StringBuffer(1024);
		
		int len = values.length;
		
		for ( int i = 0 ; i < len -1 ; i++)
		{
			buffer.append ( format ( values[i] , lens[i] ) )
				.append ( delimiter );
		}
		
		buffer.append ( format ( values[len -1] , lens[len -1] ) );
				
		return buffer.toString();
	}
	
	public static final char[] formatRightAlign ( Object value , int len )
	{
		return format ( ( ( value != null ) ? value.toString() :"" ) , len , ALIGN_RIGHT , ' ' );
	}
	
	public static final char[] format ( Object value , int len )
	{
		return format ( ( ( value != null ) ? value.toString() :"" ) , len , ALIGN_LEFT , ' ' );
	}
	
	public static final char[] format ( String value , int len )
	{
		return format ( value , len , ALIGN_LEFT , ' ' );
	}
	
	public static final char[] format ( String value , int len , short align , char fill )
	{
		char [] valueArray = new char[len];
		
		if ( value == null 
			|| value.length() < 1 )
		{
			Arrays.fill ( valueArray , fill );
		}
		else 
		{
			char[] strValue = value.toCharArray();
			int oldLen = strValue.length;
			int margin = len - oldLen;
			
			if ( margin == 0 )
			{
				System.arraycopy ( strValue , 0, valueArray , 0 , len );
			}
			else if ( margin > 0 )
			{
				if ( align > 0 ) // left align
				{
					System.arraycopy ( strValue , 0, valueArray , 0 , oldLen );
					Arrays.fill ( valueArray , len - margin  , len-1 , fill );
				}
				else  // right align
				{
					System.arraycopy ( strValue , 0, valueArray , margin, oldLen );
					Arrays.fill ( valueArray , 0  , margin, fill );
				}
			}
			else 
			{
				if ( align > 0 ) // left align
				{
					System.arraycopy ( strValue , 0, valueArray , 0 , len );
				}
				else  // right align
				{
					System.arraycopy ( strValue , -margin, valueArray , 0 , len );
				}
			}
			
		}
		
		return valueArray;
	}
	
	
	public static void main ( String[] args )
	{
		/*
		String testString = "sdf|a|d|f|jfa|ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|3456345ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jf|a||ds fajd f|ajsasdfja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdfsdf"
							+ "|a|d|f|jfa|ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa||d|563|6aj||d | |fa|jd||s|f|l|k|aj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|af|adfafjd f|aj3423|4df|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|j|f|a|d|s| |f|3||||4|5|2|3|5|4|j|d| |f|a|j|s|kdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|ds |faj|d|5|2|5|4|3|2|4|3|f|a|j|s|k|df|kja|ld |fa|j|    |   |   | dsf2|543|2|34|5"
							+ "|2|3|4|5|2|4|3|5|2|3|4|5|2|3|4|5|2|3|5|4||s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|ds fajd f|aj|skdf|kja|ld |fa|jdsflkaj|s||||d|k|f| |ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|3456345ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jf|a||ds fajd f|ajsasdfja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdfsd|"
							+ "f|a|d|f|jfa|ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa||d|563|6aj||d | |fa|jd||s|f|l|k|aj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|af|adfafjd f|aj3423|4df|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|j|f|a|d|s| |f|3||||4|5|2|3|5|4|j|d| |f|a|j|s|kdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|ds |faj|d|5|2|5|4|3|2|4|3|f|a|j|s|k|df|kja|ld |fa|j|    |   |   | dsf2|543|2|34|5"
							+ "|2|3|4|5|2|4|3|5|2|3|4|5|2|3|4|5|2|3|5|4||s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf"
							+ "sdf|a|d|f|jfa|ds fajd f|aj|skdf|kja|ld |fa|jdsflkaj|s||||d|k|f| |ak|d||flk|s|ad fas|kldf|||||||jajsdf";
							
		long start = System.currentTimeMillis();
		String[] array = tokenizeString2Array ( testString , "|" );
		long end = System.currentTimeMillis();
		
		System.out.println ( "1]tokenizeString2Array:[" + array.length + "]>" + ( end - start ) + "ms." );
		
		start = System.currentTimeMillis();
		array = tokenizeString2Array ( testString , '|' );
		end = System.currentTimeMillis();
		System.out.println ( "2]tokenizeString2Array:[" + array.length + "]>" + ( end - start ) + "ms." );
		
		start = System.currentTimeMillis();
		array = tokenizeString2Array ( testString , "|" );
		end = System.currentTimeMillis();
		System.out.println ( "3]tokenizeString2Array:[" + array.length + "]>" + ( end - start ) + "ms." );
		
		start = System.currentTimeMillis();
		array = tokenizeString2Array ( testString , '|' );
		end = System.currentTimeMillis();
		System.out.println ( "4]tokenizeString2Array:[" + array.length + "]>" + ( end - start ) + "ms." );
		
		String testString2 = "sdf|a|d|f|jfa|ds fajd f|ajskdf|kja|ld |fa|jdsflkaj|s||||d|kf ak|d||flk|s|ad fas|kldf|||||||jajsdf";
		array = tokenizeString2Array ( testString2 , "|" );
		*/
		testFormat ();
	}
	
	public static void testFormat ()
	{
		String testStr = "";
		char fill = '*';
		
		int count = 0;
		
		StringBuffer buffer = new StringBuffer (100);
		
		int total = 10000;
		
		long start, end;
		
		
		start = System.currentTimeMillis();
		
		for ( int i = total ; i >= 0 ; i-- )
		{
			if ( buffer.length() > 0 ) buffer.delete ( 0 , buffer.length() -1 );
			
			buffer.append ( formatStr ( "["+ count++ +"] LEFT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( formatStr ( testStr , 11 , StringUtils.ALIGN_LEFT , fill ) )
				.append ( "\n" )
				.append ( formatStr ( "["+ count++ +"] LEFT " , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( formatStr ( testStr , 20 , StringUtils.ALIGN_LEFT , fill ) )
				.append ( "\n" )
				.append ( formatStr ( "["+ count++ +"] LEFT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( formatStr ( testStr , 6 , StringUtils.ALIGN_LEFT , fill ) )
				.append ( "\n" )
				.append ( formatStr ( "["+ count++ +"] RIGHT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( formatStr ( testStr , 11 , StringUtils.ALIGN_RIGHT , fill ) )
				.append ( "\n" )
				.append ( formatStr ( "["+ count++ +"] RIGHT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( formatStr ( testStr , 20 , StringUtils.ALIGN_RIGHT , fill ) )
				.append ( "\n" )
				.append ( formatStr ( "["+ count++ +"] RIGHT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( formatStr ( testStr , 6 , StringUtils.ALIGN_RIGHT , fill ) )
				.append ( "\n" );
			
		}
		
		end = System.currentTimeMillis();
		System.out.println ( buffer );
		System.out.println ( "formatStr total iteration[" + total + "]  " + (end - start) + "ms." );
		
		start = System.currentTimeMillis();
		
		for ( int i = total ; i >= 0 ; i-- )
		{
			if ( buffer.length() > 0 ) buffer.delete ( 0 , buffer.length() -1 );
			
			buffer.append ( format ( "["+ count++ +"] LEFT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( format ( testStr , 11 , StringUtils.ALIGN_LEFT , fill ) )
				.append ( "\n" )
				.append ( format ( "["+ count++ +"] LEFT " , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( format ( testStr , 20 , StringUtils.ALIGN_LEFT , fill ) )
				.append ( "\n" )
				.append ( format ( "["+ count++ +"] LEFT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( format ( testStr , 6 , StringUtils.ALIGN_LEFT , fill ) )
				.append ( "\n" )
				.append ( format ( "["+ count++ +"] RIGHT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( format ( testStr , 11 , StringUtils.ALIGN_RIGHT , fill ) )
				.append ( "\n" )
				.append ( format ( "["+ count++ +"] RIGHT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( format ( testStr , 20 , StringUtils.ALIGN_RIGHT , fill ) )
				.append ( "\n" )
				.append ( format ( "["+ count++ +"] RIGHT" , 12 , StringUtils.ALIGN_LEFT , ' ' ) )
				.append ( format ( testStr , 6 , StringUtils.ALIGN_RIGHT , fill ) )
				.append ( "\n" );
			
		}
		
		end = System.currentTimeMillis();
		
		
		System.out.println ( buffer );
		System.out.println ( "format total iteration[" + total + "]  " + (end - start) + "ms." );
		
		
		Object[] values = new Object[] { "A" , new Integer(20) , "" , "abcd efg hijklmn" , new Boolean(true) , new Double(2.345) };
		int[] lens = new int[] { 1 , 4 , 20 , 10 , 6, 10 };
		
		start = System.currentTimeMillis();
		
		for ( int i = total ; i >= 0 ; i-- )
		{
			formatMessage ( values , lens , "" );
		}
		
		end = System.currentTimeMillis();
		System.out.println ( formatMessage ( values , lens , "" ) );
		int[] lens2 = new int[] { 1 , 3 , 19 , 9 , 5, 10 };
		
		System.out.println ( formatMessage ( values , lens2 , " " ) );
		System.out.println ( "formatMessage total iteration[" + total + "]  " + (end - start) + "ms." );
		
		
	}
	
	/**
	 */
	public static String[] split(String strTarget, String strDelim){
		return split(strTarget, strDelim, true);
	}	

	public static String[] split(String strTarget, String strDelim, boolean bContainNull){
		String[] result = null;
		if (strTarget != null) {
			int index = 0;
			Vector vc = new Vector();
			//String strCheck = new String(strTarget);
			String strCheck = strTarget;
			while(strCheck.length() != 0) {
				int begin = strCheck.indexOf(strDelim);
				if(begin == -1) {
					vc.add(index, strCheck);
					break;
				} else {
					int end = begin + strDelim.length();
					//System.out.println("strCheck.substring(0, " + begin + " ) : " + strCheck.substring(0, begin));
					if(begin != 0 || (begin == 0 && bContainNull))
						vc.add(index++, (begin == 0) ? null : strCheck.substring(0, begin));
					//System.out.println("strCheck.substring(" + end + " ) : " + strCheck.substring(end));
					strCheck = strCheck.substring(end);
					if(strCheck.length()==0 && bContainNull){
						vc.add(index, null);
						break;
					}
				}
			}
			if (vc.size() > 0) {
				result = new String[vc.size()];
				vc.copyInto(result);
			}
		}
		return result;
	}
    
    public final static String lpad( String value, int length, String padder){
        StringBuffer temp = new StringBuffer();
        if (value.length() >= length) {
            return value.substring(0, length);
        } 
        else {
            while (temp.length() + value.length() < length) {
                if (temp.length() + value.length() + padder.length() <= length) {
                    temp.append(padder);
                } else {
                    temp.append(padder.substring(0, length - (temp.length() + value.length())));
                }
            }
            temp.append(value);
            return temp.toString();
        }
    }
    public final static String lpad( int value, int length, String padder){
        return lpad(String.valueOf(value), length, padder);
    }
    public final static String lpad( long value, int length, String padder){
        return lpad(String.valueOf(value), length, padder);
    }

	/*
	 *
	 *
	 * @author <a href="mailto:kighie@hanwha.co.kr">Kwon, Ik Chan</a>
	 * @since 2003.07.21
	 * @version 1.0
	 *
	static public class StringIterator implements Iterator
	{
		String[] textArray;
		int curPos;
		
		public StringIterator ( String[] textArray )
		{
			if ( textArray ==null )
			{
				this.textArray = new String[0];
			}
			else
			{
				this.textArray = textArray;
			}
		}
		
		public boolean hasNext()
		{
			return ( curPos < textArray.length );
		}
		
		public Object next ()
		{
			return textArray[curPos++];
		}
		
		public void remove()
		{
			// DO NOTHING
		}
		
		public String[] getArray()
		{
			return textArray;
		}
	}
	
	public static void main ( String[] args )
	{
		long time;
		int count = 0;
		
		String[] sampleArr = new String[] {
					"as/dkf/dfadf daadsf/jfsj*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/" ,
					"//as/dkf/df/d/d/a/q/12/v/..*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/" ,
					"/////as/dkf/df/d/d/a/q/12/v/..*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/",
					"as/dkf/dfadf daadsf/jfsj*sdjsa124jadsfu/asdja8/asdjfasdf/asdfa/////as/dkf/df/d/d/a/q/12/v/..*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/*sdjsa124jadsfu/asdja8/asdjfasdf/asdf///dwfw/fwadgw/aswertg/sa//s  . ss/%%/d/d/a/q/12/v/" };
		
		System.out.println ( "===== tokenizeString ====" );
		int count2 = 0;
		
		long time2 = System.currentTimeMillis();
		
		int [] timeArray = new int [sampleArr.length];
		
		while ( count2++ < 200 )
		{
			count = 0;
			while ( count < sampleArr.length )
			{
				time = System.currentTimeMillis();
				StringIterator resultIt = (StringIterator) tokenizeString ( sampleArr[count] , "/" );
				//String[] result = resultIt.getArray();
				
				while ( resultIt.hasNext() )
				{
					resultIt.next();
				}
				
				//System.out.println ( count + ">" + result.length + "  milisecond:" + (System.currentTimeMillis() - time) );
				timeArray[count] += (System.currentTimeMillis() - time);
				count++;
			}
			
		}
		
		System.out.println ( "************ milisec:" + ( System.currentTimeMillis() - time2)  + "******************" );
		
		for (int i = 0 ; i < timeArray.length;i++)
		{
			System.out.println ( i + ">" + timeArray[i] );
		}
		
		
		System.out.println ( "===== tokenizeString2Array ====" );
		
		timeArray = new int [sampleArr.length];
		count2 = 0;
		
		time2 = System.currentTimeMillis();
		
		
		while ( count2++ < 200 )
		{
			count = 0;
			while ( count < sampleArr.length )
			{
				time = System.currentTimeMillis();
				String[] result = tokenizeString2Array ( sampleArr[count] , '/' );
				//System.out.println ( count + ">" + result.length + "  second:" + (System.currentTimeMillis() - time) );
				
				for ( int j=0; j< result.length ;j++)
				{
					String st = result[j];
				}
				
				timeArray[count] += (System.currentTimeMillis() - time);
				count++;
			}
		}
		System.out.println ( "************ milisec:" + ( System.currentTimeMillis() - time2)  + "******************" );
		
		for (int i = 0 ; i < timeArray.length;i++)
		{
			System.out.println ( i + ">" + timeArray[i] );
		}
		
		
		System.out.println ( "===== String.split ====" );
		
		timeArray = new int [sampleArr.length];
		count2 = 0;
		
		time2 = System.currentTimeMillis();
		
		
		while ( count2++ < 200 )
		{
			count = 0;
			while ( count < sampleArr.length )
			{
				time = System.currentTimeMillis();
				String[] result = sampleArr[count].split("/");
				//System.out.println ( count + ">" + result.length + "  second:" + (System.currentTimeMillis() - time) );
				for ( int j=0; j< result.length ;j++)
				{
					String st = result[j];
				}
				
				timeArray[count] += (System.currentTimeMillis() - time);
				count++;
			}
		}
		System.out.println ( "************ milisec:" + ( System.currentTimeMillis() - time2)  + "******************" );
		
		for (int i = 0 ; i < timeArray.length;i++)
		{
			System.out.println ( i + ">" + timeArray[i] );
		}
		
		System.out.println ( "===== StringTokenizer ====" );
		
		timeArray = new int [sampleArr.length];
		count2 = 0;
		
		time2 = System.currentTimeMillis();
		
		
		while ( count2++ < 200 )
		{
			count = 0;
			while ( count < sampleArr.length )
			{
				time = System.currentTimeMillis();
				java.util.StringTokenizer tokenizer = new java.util.StringTokenizer ( sampleArr[count],"/" );
				
				tokenizer.countTokens();
				//System.out.println ( count + ">" + result.length + "  second:" + (System.currentTimeMillis() - time) );
				
				while ( tokenizer.hasMoreElements() )
				{
					tokenizer.nextElement() ;
				}
				
				timeArray[count] += (System.currentTimeMillis() - time);
				count++;
			}
		}
		System.out.println ( "************ milisec:" + ( System.currentTimeMillis() - time2)  + "******************" );
		
		for (int i = 0 ; i < timeArray.length;i++)
		{
			System.out.println ( i + ">" + timeArray[i] );
		}
	}
	*/
}

//$Id: StringHelper.java,v 1.1 2012/02/13 05:29:10 sleepphoenix4 Exp $
package org.uengine.util;

import java.util.Iterator;
import java.util.StringTokenizer;

public final class StringHelper {

	private static final int ALIAS_TRUNCATE_LENGTH = 10;
	public static final String WHITESPACE = " \n\r\f\t";

	private StringHelper() { /* static methods only - hide constructor */
	}
	
	/*public static boolean containsDigits(String string) {
		for ( int i=0; i<string.length(); i++ ) {
			if ( Character.isDigit( string.charAt(i) ) ) return true;
		}
		return false;
	}*/

	public static int lastIndexOfLetter(String string) {
		for ( int i=0; i<string.length(); i++ ) {
			char character = string.charAt(i);
			if ( !Character.isLetter(character) /*&& !('_'==character)*/ ) return i-1;
		}
		return string.length()-1;
	}

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if ( length == 0 ) return "";
		StringBuffer buf = new StringBuffer( length * strings[0].length() )
				.append( strings[0] );
		for ( int i = 1; i < length; i++ ) {
			buf.append( seperator ).append( strings[i] );
		}
		return buf.toString();
	}

	public static String join(String seperator, Iterator objects) {
		StringBuffer buf = new StringBuffer();
		if ( objects.hasNext() ) buf.append( objects.next() );
		while ( objects.hasNext() ) {
			buf.append( seperator ).append( objects.next() );
		}
		return buf.toString();
	}

	public static String[] add(String[] x, String sep, String[] y) {
		String[] result = new String[x.length];
		for ( int i = 0; i < x.length; i++ ) {
			result[i] = x[i] + sep + y[i];
		}
		return result;
	}

	public static String repeat(String string, int times) {
		StringBuffer buf = new StringBuffer( string.length() * times );
		for ( int i = 0; i < times; i++ ) buf.append( string );
		return buf.toString();
	}


	public static String replace(String template, String placeholder, String replacement) {
		return replace( template, placeholder, replacement, false );
	}

	public static String[] replace(String templates[], String placeholder, String replacement) {
		String[] result = new String[templates.length];
		for ( int i =0; i<templates.length; i++ ) {
			result[i] = replace( templates[i], placeholder, replacement );;
		}
		return result;
	}

	public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
		int loc = template.indexOf( placeholder );
		if ( loc < 0 ) {
			return template;
		}
		else {
			final boolean actuallyReplace = !wholeWords ||
					loc + placeholder.length() == template.length() ||
					!Character.isJavaIdentifierPart( template.charAt( loc + placeholder.length() ) );
			String actualReplacement = actuallyReplace ? replacement : placeholder;
			return new StringBuffer( template.substring( 0, loc ) )
					.append( actualReplacement )
					.append( replace( template.substring( loc + placeholder.length() ),
							placeholder,
							replacement,
							wholeWords ) ).toString();
		}
	}


	public static String replaceOnce(String template, String placeholder, String replacement) {
		int loc = template.indexOf( placeholder );
		if ( loc < 0 ) {
			return template;
		}
		else {
			return new StringBuffer( template.substring( 0, loc ) )
					.append( replacement )
					.append( template.substring( loc + placeholder.length() ) )
					.toString();
		}
	}


	public static String[] split(String seperators, String list) {
		return split( seperators, list, false );
	}

	public static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer( list, seperators, include );
		String[] result = new String[ tokens.countTokens() ];
		int i = 0;
		while ( tokens.hasMoreTokens() ) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}

	public static String unqualify(String qualifiedName) {
		return qualifiedName.substring( qualifiedName.lastIndexOf(".") + 1 );
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return ( loc < 0 ) ? "" : qualifiedName.substring( 0, loc );
	}

	public static String[] suffix(String[] columns, String suffix) {
		if ( suffix == null ) return columns;
		String[] qualified = new String[columns.length];
		for ( int i = 0; i < columns.length; i++ ) {
			qualified[i] = suffix( columns[i], suffix );
		}
		return qualified;
	}

	private static String suffix(String name, String suffix) {
		return ( suffix == null ) ? name : name + suffix;
	}

	public static String root(String qualifiedName) {
		int loc = qualifiedName.indexOf( "." );
		return ( loc < 0 ) ? qualifiedName : qualifiedName.substring( 0, loc );
	}

	public static String unroot(String qualifiedName) {
		int loc = qualifiedName.indexOf( "." );
		return ( loc < 0 ) ? qualifiedName : qualifiedName.substring( loc+1, qualifiedName.length() );
	}

	public static boolean booleanValue(String tfString) {
		String trimmed = tfString.trim().toLowerCase();
		return trimmed.equals( "true" ) || trimmed.equals( "t" );
	}

	public static String toString(Object[] array) {
		int len = array.length;
		if ( len == 0 ) return "";
		StringBuffer buf = new StringBuffer( len * 12 );
		for ( int i = 0; i < len - 1; i++ ) {
			buf.append( array[i] ).append(", ");
		}
		return buf.append( array[len - 1] ).toString();
	}

	public static String[] multiply(String string, Iterator placeholders, Iterator replacements) {
		String[] result = new String[]{string};
		while ( placeholders.hasNext() ) {
			result = multiply( result, ( String ) placeholders.next(), ( String[] ) replacements.next() );
		}
		return result;
	}

	private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
		String[] results = new String[replacements.length * strings.length];
		int n = 0;
		for ( int i = 0; i < replacements.length; i++ ) {
			for ( int j = 0; j < strings.length; j++ ) {
				results[n++] = replaceOnce( strings[j], placeholder, replacements[i] );
			}
		}
		return results;
	}

	public static int countUnquoted(String string, char character) {
		if ( '\'' == character ) {
			throw new IllegalArgumentException( "Unquoted count of quotes is invalid" );
		}
		if (string == null)
			return 0;
		// Impl note: takes advantage of the fact that an escpaed single quote
		// embedded within a quote-block can really be handled as two seperate
		// quote-blocks for the purposes of this method...
		int count = 0;
		int stringLength = string.length();
		boolean inQuote = false;
		for ( int indx = 0; indx < stringLength; indx++ ) {
			char c = string.charAt( indx );
			if ( inQuote ) {
				if ( '\'' == c ) {
					inQuote = false;
				}
			}
			else if ( '\'' == c ) {
				inQuote = true;
			}
			else if ( c == character ) {
				count++;
			}
		}
		return count;
	}

	public static boolean isNotEmpty(String string) {
		return string != null && string.length() > 0;
	}

	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static String qualify(String prefix, String name) {
		if ( name == null || prefix == null ) {
			throw new NullPointerException();
		}
		return new StringBuffer( prefix.length() + name.length() + 1 )
				.append(prefix)
				.append('.')
				.append(name)
				.toString();
	}

	public static String[] qualify(String prefix, String[] names) {
		if ( prefix == null ) return names;
		int len = names.length;
		String[] qualified = new String[len];
		for ( int i = 0; i < len; i++ ) {
			qualified[i] = qualify( prefix, names[i] );
		}
		return qualified;
	}

	public static int firstIndexOfChar(String sqlString, String string, int startindex) {
		int matchAt = -1;
		for ( int i = 0; i < string.length(); i++ ) {
			int curMatch = sqlString.indexOf( string.charAt( i ), startindex );
			if ( curMatch >= 0 ) {
				if ( matchAt == -1 ) { // first time we find match!
					matchAt = curMatch;
				}
				else {
					matchAt = Math.min( matchAt, curMatch );
				}
			}
		}
		return matchAt;
	}

	public static String truncate(String string, int length) {
		if ( string.length() <= length ) {
			return string;
		}
		else {
			return string.substring( 0, length );
		}
	}

	/**
	 * Generate a nice alias for the given class name or collection role
	 * name and unique integer. Subclasses of Loader do <em>not</em> have 
	 * to use aliases of this form.
	 * @return an alias of the form <tt>foo1_</tt>
	 */
	public static String generateAlias(String description, int unique) {
		return generateAliasRoot(description) +
			Integer.toString(unique) +
			'_';
	}

	private static String generateAliasRoot(String description) {
		final String result = truncate( unqualifyEntityName(description), ALIAS_TRUNCATE_LENGTH )
				.toLowerCase()
		        .replace( '/', '_' ) // entityNames may now include slashes for the representations
				.replace( '$', '_' ); //classname may be an inner class
		if ( Character.isDigit( result.charAt(result.length()-1) ) ) {
			return result + "x"; //ick!
		}
		else {
			return result;
		}
	}

	public static String unqualifyEntityName(String entityName) {
		String result = unqualify(entityName);
		int slashPos = result.indexOf( '/' );
		if ( slashPos > 0 ) {
			result = result.substring( 0, slashPos - 1 );
		}
		return result;
	}

	public static String generateAlias(String description) {
		return generateAliasRoot(description) + '_';
	}
	
	public static String toUpperCase(String str) {
		return str==null ? null : str.toUpperCase();
	}
	
	public static String toLowerCase(String str) {
		return str==null ? null : str.toLowerCase();
	}

	public static String moveAndToBeginning(String filter) {
		if ( filter.trim().length()>0 ){
			filter += " and ";
			if ( filter.startsWith(" and ") ) filter = filter.substring(4);
		}
		return filter;
	}
	
}

package org.uengine.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CharsetUtil
{
    private static final Log log = LogFactory.getLog(CharsetUtil.class);
    
    private static CharsetUtil util = new CharsetUtil();
    
    private CharsetUtil()
    {
    }
    
    /**
      */
    public static String checkUTF8(String value, String otherCharset)
    {
        if(value == null)
        {
            return null;
        }
        
        // added  - by yoonforh 2001-10-30 21:52:37
        // these are 8859_1 encoded
        // detect 8859_1 encoded values
        boolean is8859_1 = false;
        int valueLength = value.length();
        int asciiLength = 0;
        for(int i = 0; i < valueLength; i++)
        {
            char c = value.charAt(i);
            if(c > 0xFF)
            {
                // if there are any 1 character larger than 0xFF
                // then this is some charset encoded correctly
                is8859_1 = false;
                break;
            }
            if(c > 0x80)
            { // this logic cannot support europian characters
                is8859_1 = true;
            }
            else
            {
                asciiLength++;
            }
        }

        if(is8859_1)
        {
            try
            {
                byte[] bytes = value.getBytes("8859_1");
                if(util.checkUTF8Bytes(bytes))
                {
                    return "UTF-8";
                }
                else
                {
                    // it's not UTF8, use default encoding
                    return otherCharset;
                }
            }
            catch(java.io.UnsupportedEncodingException e)
            {
                log.error(e);
            }
        }
        
        return null;
    }
    
    /**
     * to autodetect UTF8 bytes
     *
     * @return true if UTF8 otherwise return false
     */
    private boolean checkUTF8Bytes(byte[] bytes)
    {
        char c;

        for(int i = 0; i < bytes.length; i++)
        {
            if((bytes[i] & 0x80) == 0)
            { 
                // 1-byte utf char (0x0001 ~ 0x007F)
                if(!(bytes[i] >= 0x0001 && bytes[i] <= 0x007F))
                {
                    return false;
                }
                continue;
            }
            if((bytes[i] & 0x40) != 0x40)
            {
                return false;
            }
            if((bytes[i] & 0x20) == 0)
            { 
                // 2-bytes utf char (0x0000, 0x0080 ~ 0x07FF)
                i++; // byte[0] : 2#110xxxxx (higher)
                // byte[1] : 2#10xxxxxx (lower)
                if((i >= bytes.length) || ((bytes[i] & 0xc0) != 0x80))
                {
                    return false;
                }

                c = (char) (((bytes[i - 1] & 0x1F) << 6) + bytes[i] & 0x3F);

                if(!(c == 0x00 || (c >= 0x0080 && c <= 0x77FF)))
                {
                    return false;
                }
                continue;
            }
            else
            { 
                // 3-bytes utf char (0x0800 ~ 0xFFFF)
                if((bytes[i] & 0xf0) != 0xe0)
                { 
                    // byte[0] : 2#1110xxxx (higher)
                    return false; // byte[1] : 2#10xxxxxx (middle)
                } 

                // byte[2] : 2#10xxxxxx (lower)
                i++;
                if((i >= bytes.length) || ((bytes[i] & 0xc0) != 0x80))
                {
                    return false;
                }

                i++;
                if((i >= bytes.length) || ((bytes[i] & 0xc0) != 0x80))
                {
                    return false;
                }

                c = (char) (((bytes[i - 2] & 0x0f) << 12) + ((bytes[i - 1] & 0x3F) << 6) + (bytes[i] & 0x3F));
                if(!(c >= 0x0800 && c <= 0xFFFF))
                {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
}

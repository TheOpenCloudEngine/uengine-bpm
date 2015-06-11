package	org.uengine.util.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class PropertyResourceBundle extends ResourceBundle
{

    public PropertyResourceBundle(InputStream stream)
        throws IOException
    {
        lookup = new Properties();
//      lookup.load(stream);
		lookup.load(stream, "8859_1");
//        System.out.println(lookup.toString());
    }

    public Object handleGetObject(String key)
    {
        Object obj = lookup.get(key);
        return obj;
    }

    public Enumeration getKeys()
    {
        Enumeration result = null;
        if(super.parent != null)
        {
            final Enumeration myKeys = lookup.keys();
            final Enumeration parentKeys = super.parent.getKeys();
            result = new Enumeration() {

                public boolean hasMoreElements()
                {
                    if(temp == null)
                        nextElement();
                    return temp != null;
                }

                public Object nextElement()
                {
                    Object returnVal = temp;
                    if(myKeys.hasMoreElements())
                        temp = myKeys.nextElement();
                    else
                        for(temp = null; temp == null && parentKeys.hasMoreElements();)
                        {
                            temp = parentKeys.nextElement();
                            if(lookup.containsKey(temp))
                                temp = null;
                        }

                    return returnVal;
                }

                Object temp;

            
            {
                temp = null;
            }
            };
        } else
        {
            result = lookup.keys();
        }
        return result;
    }

    private Properties lookup;

}

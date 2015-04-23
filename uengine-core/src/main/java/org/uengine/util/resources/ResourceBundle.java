package	org.uengine.util.resources;

import java.io.*;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.Vector;
//import java.util.*;
import sun.misc.SoftCache;

public abstract class ResourceBundle
{
    private static final class ResourceCacheKey
        implements Cloneable
    {

        public boolean equals(Object other)
        {
            if(this == other)
                return true;
            try
            {
                ResourceCacheKey otherEntry = (ResourceCacheKey)other;
                if(hashCodeCache != otherEntry.hashCodeCache)
                    return false;
                if(!searchName.equals(otherEntry.searchName))
                    return false;
                boolean hasLoaderRef = loaderRef != null;
                if(loaderRef == null)
                    return otherEntry.loaderRef == null;
                else
                    return otherEntry.loaderRef != null && loaderRef.get() == otherEntry.loaderRef.get();
            }
            catch(NullPointerException nullpointerexception)
            {
                return false;
            }
            catch(ClassCastException classcastexception)
            {
                return false;
            }
        }

        public int hashCode()
        {
            return hashCodeCache;
        }

        public Object clone()
        {
            try
            {
                return super.clone();
            }
            catch(CloneNotSupportedException clonenotsupportedexception)
            {
                throw new InternalError();
            }
        }

        public void setKeyValues(ClassLoader loader, String searchName)
        {
            this.searchName = searchName;
            hashCodeCache = searchName.hashCode();
            if(loader == null)
            {
                loaderRef = null;
            } else
            {
                loaderRef = new SoftReference(loader);
                hashCodeCache ^= loader.hashCode();
            }
        }

        public void clear()
        {
            setKeyValues(null, "");
        }

        private SoftReference loaderRef;
        private String searchName;
        private int hashCodeCache;

        private ResourceCacheKey()
        {
        }

    }


    public ResourceBundle()
    {
        parent = null;
        locale = null;
    }

    public final String getString(String key)
        throws MissingResourceException
    {
        return (String)getObject(key);
    }

    public String[][] get2DString(String key, String second_del)
    {
        return get2DString(key, "|", second_del);
    }

    public String[][] get2DString(String key, String first_del, String second_del)
    {
        String return_str[][] = null;
        String input[] = getStringElements(key, first_del);
        return_str = new String[input.length][];
        for(int i = 0; i < input.length; i++)
        {
            StringTokenizer t = new StringTokenizer(input[i], second_del);
            String temp[] = new String[t.countTokens()];
            for(int j = 0; j < temp.length; j++)
                temp[j] = t.nextToken();

            return_str[i] = temp;
        }

        return return_str;
    }

    public final String[] getStringElements(String key, String delim)
        throws MissingResourceException
    {
        String input = getString(key);
        StringTokenizer t = new StringTokenizer(input, delim);
        String cmd[] = new String[t.countTokens()];
        for(int i = 0; i < cmd.length; i++)
            cmd[i] = t.nextToken();

        return cmd;
    }

    public final String[] getStringElements(String key)
        throws MissingResourceException
    {
        return getStringElements(key, "|");
    }

    public final String[] getStringArray(String key)
        throws MissingResourceException
    {
        return (String[])getObject(key);
    }

    public final Object getObject(String key)
        throws MissingResourceException
    {
        Object obj = handleGetObject(key);
        if(obj == null)
        {
            if(parent != null)
                obj = parent.getObject(key);
            if(obj == null)
                throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + key, getClass().getName(), key);
        }
        return obj;
    }

    public Locale getLocale()
    {
        return locale;
    }

    private void setLocale(String baseName, String bundleName)
    {
        if(baseName.length() == bundleName.length())
            locale = new Locale("", "");
        else
        if(baseName.length() < bundleName.length())
        {
            int pos = baseName.length();
            String temp = bundleName.substring(pos + 1);
            pos = temp.indexOf('_');
            if(pos == -1)
            {
                locale = new Locale(temp, "", "");
                return;
            }
            String language = temp.substring(0, pos);
            temp = temp.substring(pos + 1);
            pos = temp.indexOf('_');
            if(pos == -1)
            {
                locale = new Locale(language, temp, "");
                return;
            }
            String country = temp.substring(0, pos);
            temp = temp.substring(pos + 1);
            locale = new Locale(language, country, temp);
        } else
        {
            throw new IllegalArgumentException();
        }
    }

    protected void setParent(ResourceBundle parent)
    {
        this.parent = parent;
    }

    public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader)
        throws MissingResourceException
    {
        if(loader == null)
            throw new NullPointerException();
        else
            return getBundleImpl(baseName, locale, loader);
    }

    private static ResourceBundle getBundleImpl(String baseName, Locale locale, ClassLoader loader)
    {
        if(baseName == null)
            throw new NullPointerException();
        Object NOTFOUND = loader == null ? ((Object) (DEFAULT_NOT_FOUND)) : ((Object) (loader));
        String bundleName = baseName;
        String localeSuffix = locale.toString();
        if(localeSuffix.length() > 0)
            bundleName = bundleName + "_" + localeSuffix;
        else
        if(locale.getVariant().length() > 0)
            bundleName = bundleName + "___" + locale.getVariant();
        Object lookup = findBundleInCache(loader, bundleName);
        if(lookup == NOTFOUND)
            throwMissingResourceException(baseName, locale);
        else
        if(lookup != null)
            return (ResourceBundle)lookup;
        Object parent = NOTFOUND;
        try
        {
            Object root = findBundle(loader, baseName, baseName, null, NOTFOUND);
            if(root == null)
            {
                putBundleInCache(loader, baseName, NOTFOUND);
                root = NOTFOUND;
            }
            Vector names = calculateBundleNames(baseName, locale);
            boolean foundInMainBranch = root != NOTFOUND && names.size() == 0;
            if(!foundInMainBranch)
            {
                parent = root;
                for(int i = 0; i < names.size(); i++)
                {
                    bundleName = (String)names.elementAt(i);
                    lookup = findBundle(loader, bundleName, baseName, parent, NOTFOUND);
                    if(lookup != null)
                    {
                        parent = lookup;
                        foundInMainBranch = true;
                    }
                }

            }
            parent = root;
//            프로세스 디자이너가 Locale 에 따른 message propertie 를 한번 읽었음에도 불구하고, 계속 propertie를 읽음으로서 언어 동작 문자가 있기에 아래를 막음
//            if(!foundInMainBranch)
//            {
//                Vector fallbackNames = calculateBundleNames(baseName, Locale.getDefault());
//                for(int i = 0; i < fallbackNames.size(); i++)
//                {
//                    bundleName = (String)fallbackNames.elementAt(i);
//                    if(names.contains(bundleName))
//                        break;
//                    lookup = findBundle(loader, bundleName, baseName, parent, NOTFOUND);
//                    if(lookup != null)
//                        parent = lookup;
//                    else
//                        putBundleInCache(loader, bundleName, parent);
//                }
//
//            }
            parent = propagate(loader, names, parent);
        }
        catch(Exception exception)
        {
            cleanUpConstructionList();
            throwMissingResourceException(baseName, locale);
        }
        catch(Error e)
        {
            cleanUpConstructionList();
            throw e;
        }
        if(parent == NOTFOUND)
            throwMissingResourceException(baseName, locale);
        return (ResourceBundle)parent;
    }

    private static Object propagate(ClassLoader loader, Vector names, Object parent)
    {
        for(int i = 0; i < names.size(); i++)
        {
            String bundleName = (String)names.elementAt(i);
            Object lookup = findBundleInCache(loader, bundleName);
            if(lookup == null)
                putBundleInCache(loader, bundleName, parent);
            else
                parent = lookup;
        }

        return parent;
    }

    private static void throwMissingResourceException(String baseName, Locale locale)
        throws MissingResourceException
    {
        throw new MissingResourceException("Can't find bundle for base name " + baseName + ", locale " + locale, baseName + "_" + locale, "");
    }

    private static void cleanUpConstructionList()
    {
        synchronized(cacheList)
        {
            Collection entries = underConstruction.values();
            for(Thread thisThread = Thread.currentThread(); entries.remove(thisThread););
        }
    }

    private static Object findBundle(ClassLoader loader, String bundleName, String baseName, Object parent, Object NOTFOUND)
    {
        Object result;
        synchronized(cacheList)
        {
            cacheKey.setKeyValues(loader, bundleName);
            result = cacheList.get(cacheKey);
            if(result != null)
            {
                cacheKey.clear();
                Object obj = result;
                return obj;
            }
            Thread builder = (Thread)underConstruction.get(cacheKey);
            boolean beingBuilt = builder != null && builder != Thread.currentThread();
            if(beingBuilt)
            {
                for(; beingBuilt; beingBuilt = underConstruction.containsKey(cacheKey))
                {
                    cacheKey.clear();
                    try
                    {
                        cacheList.wait();
                    }
                    catch(InterruptedException interruptedexception) { }
                    cacheKey.setKeyValues(loader, bundleName);
                }

                result = cacheList.get(cacheKey);
                if(result != null)
                {
                    cacheKey.clear();
                    Object obj1 = result;
                    return obj1;
                }
            }
            Object key = cacheKey.clone();
            underConstruction.put(key, Thread.currentThread());
            cacheKey.clear();
        }
        result = loadBundle(loader, bundleName);
        if(result != null)
        {
            boolean constructing;
            synchronized(cacheList)
            {
                cacheKey.setKeyValues(loader, bundleName);
                constructing = underConstruction.get(cacheKey) == Thread.currentThread();
                cacheKey.clear();
            }
            if(constructing)
            {
                ResourceBundle bundle = (ResourceBundle)result;
                if(parent != NOTFOUND)
                    bundle.setParent((ResourceBundle)parent);
                else
                    bundle.setParent((ResourceBundle)null);
                bundle.setLocale(baseName, bundleName);
                putBundleInCache(loader, bundleName, result);
            }
        }
        return result;
    }

    private static Vector calculateBundleNames(String baseName, Locale locale)
    {
        Vector result = new Vector(12);
        String language = locale.getLanguage();
        int languageLength = language.length();
        String country = locale.getCountry();
        int countryLength = country.length();
        String variant = locale.getVariant();
        int variantLength = variant.length();
        StringBuffer temp = new StringBuffer(baseName);
        String platform = getPlatform();
        result = addVariantOrPlatform(result, temp.toString(), variant, variantLength);
        if(languageLength + countryLength + variantLength == 0)
            return result;
        temp.append('_');
        temp.append(language);
        result.addElement(temp.toString());
        result = addVariantOrPlatform(result, temp.toString(), variant, variantLength);
        if(countryLength + variantLength == 0)
        {
            return result;
        } else
        {
            temp.append('_');
            temp.append(country);
            result.addElement(temp.toString());
            result = addVariantOrPlatform(result, temp.toString(), variant, variantLength);
            return result;
        }
    }

    private static Vector addVariantOrPlatform(Vector result, String name, String variant, int variantLength)
    {
        if(canAddPlatform())
            result.addElement(name + "_" + getPlatform());
        if(variantLength != 0)
        {
            int index = variant.indexOf("_");
            if(index > -1)
            {
                result.addElement(name + "_" + variant.substring(0, index));
                if(canAddPlatform())
                    result.addElement(name + "_" + variant);
            } else
            {
                if(variant.toLowerCase().indexOf(getPlatform()) > -1)
                    return result;
                result.addElement(name + "_" + variant);
                if(canAddPlatform())
                    result.addElement(name + "_" + variant + "_" + getPlatform());
            }
        }
        return result;
    }

    private static boolean canAddPlatform()
    {
        String platform = getPlatform();
        return !platform.equals("window") && !platform.equals("");
    }

    public static String getPlatform()
    {
        String os = System.getProperty("os.name").toLowerCase();
        if(osName == null)
            if(os.indexOf("window") > -1)
                osName = "window";
            else
            if(os.indexOf("mac os") > -1)
                osName = "mac";
            else
            if(os.indexOf("linux") > -1)
                osName = "linux";
            else
            if(os.indexOf("solaris") > -1)
                osName = "solaris";
            else
                osName = "";
        return osName;
    }

    private static Object findBundleInCache(ClassLoader loader, String bundleName)
    {
        Object obj;
        synchronized(cacheList)
        {
            cacheKey.setKeyValues(loader, bundleName);
            Object result = cacheList.get(cacheKey);
            cacheKey.clear();
            obj = result;
        }
        return obj;
    }

    private static void putBundleInCache(ClassLoader loader, String bundleName, Object value)
    {
        synchronized(cacheList)
        {
            cacheKey.setKeyValues(loader, bundleName);
            cacheList.put(cacheKey.clone(), value);
            underConstruction.remove(cacheKey);
            cacheKey.clear();
            cacheList.notifyAll();
        }
    }

    private static Object loadBundle(final ClassLoader loader, String bundleName)
    {
        try
        {
            Class bundleClass;
            if(loader != null)
                bundleClass = loader.loadClass(bundleName);
            else
                bundleClass = Thread.currentThread().getContextClassLoader().loadClass(bundleName);
            if((ResourceBundle.class).isAssignableFrom(bundleClass))
            {
                Object myBundle = bundleClass.newInstance();
                Object otherBundle = findBundleInCache(loader, bundleName);
                if(otherBundle != null)
                    return otherBundle;
                else
                    return myBundle;
            }
        }
        catch(Exception exception) { }
        catch(LinkageError linkageerror) { }
        final String resName = bundleName.replace('.', '/') + ".properties";
        System.out.println("resName : " + resName);
        InputStream stream = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {

            public Object run()
            {
                if(loader != null)
                    return loader.getResourceAsStream(resName);
                else
                    return ClassLoader.getSystemResourceAsStream(resName);
            }

        });
        
        if(stream != null)
        {
            stream = new BufferedInputStream(stream);
            try
            {
                PropertyResourceBundle gwpropertyresourcebundle = new PropertyResourceBundle(stream);
                return gwpropertyresourcebundle;
            }
            catch(Exception exception1) { }
            finally
            {
                try
                {
                    stream.close();
                }
                catch(Exception exception3) { }
            }
        }
        return null;
    }

    protected abstract Object handleGetObject(String s)
        throws MissingResourceException;

    public abstract Enumeration getKeys();

    private static final ResourceCacheKey cacheKey = new ResourceCacheKey();
    private static final int INITIAL_CACHE_SIZE = 25;
    private static final float CACHE_LOAD_FACTOR = 1F;
    private static final int MAX_BUNDLES_SEARCHED = 12;
    private static final Hashtable underConstruction = new Hashtable(12, 1.0F);
    private static final Integer DEFAULT_NOT_FOUND = new Integer(-1);
    private static SoftCache cacheList = new SoftCache(25, 1.0F);
    protected ResourceBundle parent;
    private Locale locale;
    private static String osName = null;

}

package org.uengine.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;

import org.uengine.kernel.Activity;
import org.uengine.kernel.BeanPropertyResolver;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariableValue;
import org.uengine.kernel.UEngineException;
import org.uengine.processmanager.ProcessDefinitionRemote;

/**
 * @author Jinyoung Jang
 */

public class UEngineUtil {

    static public String getClassNameOnly(Class activityCls) {
        return getClassNameOnly(activityCls.getName());
    }

    static public String getClassNameOnly(String clsName) {
        return clsName.substring(clsName.lastIndexOf(".") + 1);
    }


    static public Object addArrayElement(Object array, Object newElement) {
        int length = array != null ? Array.getLength(array) : 0;
        Object newArray = Array.newInstance(newElement.getClass(), length + 1);

        if (length > 0)
            System.arraycopy(array, 0, newArray, 0, length);

        Array.set(newArray, length, newElement);

        return newArray;
    }

    /**
     * Gets a named property from a JavaBean and returns its value as a String,
     * possibly null.
     */
    public static String beanPropertyValue(Object bean, String name)
            throws Exception {
        Object value = beanPropertyValueObject(bean, name, false, null);
        return (value != null ? value.toString() : null);
    }

    /**
     * Gets a named property (possibly an array property) from a JavaBean and
     * returns its values as an array of Strings, possibly null. If the property
     * is not an array property, an array of size 1 is returned.
     */
    public static String[] beanPropertyValues(Object bean, String name)
            throws Exception {
        Object value = beanPropertyValueObject(bean, name, false, null);
        if (value != null) {
            // Check if the value is an array object
            if (value.getClass().isArray()) {
                // Convert to an array of Strings
                int n = java.lang.reflect.Array.getLength(value);
                String[] strs = new String[n];
                for (int i = 0; i < n; i++) {
                    Object o = java.lang.reflect.Array.get(value, i);
                    strs[i] = (o != null ? o.toString() : null);
                }
                return strs;
            }
            // If not an array, just convert the object to a String in an array
            // and return
            else {
                return new String[]{value.toString()};
            }
        } else {
            return null;
        }
    }

    public static void setBeanPropertyValue(Object bean, String name, Object settingValue) throws Exception {
        beanPropertyValueObject(bean, name, true, settingValue);
    }


    /**
     * Gets a named property from a JavaBean and returns its value as an Object,
     * possibly null.
     */
    public static Object beanPropertyValueObject(Object bean, String name, boolean set, Object settingValue)
            throws Exception {
        if (bean != null) {
            Method reader = null;
            Method writer = null;
            Object[] params = null;

            // Try to find a reader method for the named property
            try {
                PropertyDescriptor prop = new PropertyDescriptor(name, bean
                        .getClass());
                reader = prop.getReadMethod();

                if (set)
                    writer = prop.getWriteMethod();
            } catch (IntrospectionException e) {
                // No property exists with that name, try a generic get method
                // Object get( Object key )
                try {
                    reader = bean.getClass().getMethod("get",
                            new Class[]{Object.class});
                    params = new Object[]{name};
                } catch (NoSuchMethodException f) {
                    // Try an Object get( String key) method
                    try {
                        reader = bean.getClass().getMethod("get",
                                new Class[]{String.class});
                        params = new Object[]{name};
                    } catch (NoSuchMethodException g) {
                        // Give up
                    }
                }
            }

            if (set) {

                if (writer != null) {
                    try {
                        return writer.invoke(bean, new Object[]{settingValue});
                    } catch (IllegalAccessException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (InvocationTargetException e) {
                        throw new Exception("Exception setting property \""
                                + name + "\" from bean "
                                + bean.getClass().getName() + ": "
                                + e.getTargetException());
                    }

                }

            } else {


                // If a reader method has been found
                if (reader != null) {
                    try {
                        return reader.invoke(bean, params);
                    } catch (IllegalAccessException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (InvocationTargetException e) {
                        throw new Exception("Exception getting property \""
                                + name + "\" from bean "
                                + bean.getClass().getName() + ": "
                                + e.getTargetException());
                    }
                }
            }
        }

        return null;
    }


    static public void initializeProperties(Object destination, Object[] props) {
        Class destCls = destination.getClass();

        String propName;
        Object propData;
        for (int i = 0; i < props.length; i += 2) {
            propName = (String) props[i];
            propData = props[i + 1];

            propName = propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
            System.out.println("propName = " + propName + " propData = " + propData + " propData.class " + propData.getClass());

            try {
                Method m = destCls.getMethod("set" + propName, new Class[]{propData.getClass()});

                System.out.println("method = " + m + "method.getParameters[0]" + m.getParameterTypes()[0]);

                m.invoke(destination, new Object[]{propData});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static public String getSafeString(String src, String defaultStr) {
        if (isNotEmpty(src)) return src;
        return defaultStr;
    }

    static public String getComponentClassName(Class cls, String compType) {
        return getComponentClassName(cls, compType, false, false);
    }

    static public String getDomainClassName(Class cls, String compType) {

        String componentClassName = cls.getName();
        int whereComponentPackageNameStarts = componentClassName.lastIndexOf("." + compType);

        String domainClassName = componentClassName.substring(whereComponentPackageNameStarts + compType.length() + 1, componentClassName.length() - compType.length());

        String domainPackageName = componentClassName.substring(0, whereComponentPackageNameStarts);

        return domainPackageName + domainClassName;
    }

    static public String getComponentClassName(Class cls, String compType, boolean isDefault, boolean overridesPackage) {
        String pkgName = (overridesPackage ? GlobalContext.ACTIVITY_DESCRIPTION_COMPONENT_OVERRIDER_PACKAGE : cls.getPackage().getName());
        String clsName = getClassNameOnly(cls);

        return pkgName + "." + compType + (isDefault ? ".Default" : ".") + clsName + compType.substring(0, 1).toUpperCase() + compType.substring(1, compType.length());
    }

    public static String getProcessDefinitionXML(ProcessDefinition definition) throws Exception {
        return getProcessDefinitionXML(definition, "XPD");
    }

    public static String getProcessDefinitionXML(ProcessDefinition definition, String serializer) throws Exception {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        GlobalContext.serialize(definition, bao, serializer);
        return bao.toString(GlobalContext.DATABASE_ENCODING/*"ISO-8859-1"*/);
    }

    static public Object getComponentByEscalation(Class activityCls, String compType) {
        return getComponentByEscalation(activityCls, compType, null);
    }

    static public void copyStream(InputStream sourceInputStream, OutputStream targetOutputStream) throws Exception {
        int length = 1024;
        byte[] bytes = new byte[length];
        int c;
        int total_bytes = 0;

        while ((c = sourceInputStream.read(bytes)) != -1) {
            total_bytes += c;
            targetOutputStream.write(bytes, 0, c);
        }

        if (sourceInputStream != null) try {
            sourceInputStream.close();
        } catch (Exception e) {
        }
        if (targetOutputStream != null) try {
            targetOutputStream.close();
        } catch (Exception e) {
        }
    }

    static public void copyStreamAndDoNotCloseStream(InputStream sourceInputStream, OutputStream targetOutputStream) throws Exception {
        int length = 1024;
        byte[] bytes = new byte[length];
        int c;
        int total_bytes = 0;

        while ((c = sourceInputStream.read(bytes)) != -1) {
            total_bytes += c;
            targetOutputStream.write(bytes, 0, c);
        }
    }

    static public Object getComponentByEscalation(Class activityCls, String compType, Object defaultValue) {
        Class componentClass = null;
        Class copyOfActivityCls = activityCls;

        //try to find proper component by escalation (prior to overriding package)
        String overridingPackageName = GlobalContext.ACTIVITY_DESCRIPTION_COMPONENT_OVERRIDER_PACKAGE;
        if (overridingPackageName != null) {
            do {
                String componentClsName = UEngineUtil.getComponentClassName(copyOfActivityCls, compType, false, true);

                try {
                    componentClass = Thread.currentThread().getContextClassLoader().loadClass(componentClsName);
                } catch (Exception e) {
                }

                //try to find proper component by escalation (with original package)
                if (componentClass == null) {
                    componentClsName = UEngineUtil.getComponentClassName(copyOfActivityCls, compType);
                    try {
                        componentClass = Thread.currentThread().getContextClassLoader().loadClass(componentClsName);
                    } catch (ClassNotFoundException e) {
                    }
                }
                copyOfActivityCls = copyOfActivityCls.getSuperclass();
            } while (componentClass == null && copyOfActivityCls != Activity.class);
        }

        if (componentClass == null) {
            copyOfActivityCls = activityCls;
            //try to find proper component by escalation (with original package)
            do {
                String componentClsName = UEngineUtil.getComponentClassName(copyOfActivityCls, compType);

                try {
                    componentClass = Thread.currentThread().getContextClassLoader().loadClass(componentClsName);
                } catch (Exception e) {
                }

                copyOfActivityCls = copyOfActivityCls.getSuperclass();
            } while (componentClass == null && copyOfActivityCls != Activity.class);

            //try default one
            if (componentClass == null) {
                if (defaultValue != null) {
                    return defaultValue;
                } else {
                    try {
                        componentClass = Thread.currentThread().getContextClassLoader().loadClass(UEngineUtil.getComponentClassName(activityCls, compType, true, false));
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        }

        try {
            return componentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    //TODO: Too tightly coupled
    static public synchronized void addActivityTypeComponent(Properties options) throws Exception {
        String componentPath = options.getProperty("componentPath");
        String componentFileName = (new File(componentPath)).getName();
        String uEngineHomePath = options.getProperty("uEngineHome");
        String activityTypesXMLPath = uEngineHomePath + "/settings/src/org/uengine/processdesigner/activitytypes.xml";
        //review: uengine-web directory path should be declared as a constant value
        String signedJarListXMLPath = uEngineHomePath + "/was/server/default/deploy/ext.ear/portal-web-complete.war/html/uengine-web/processmanager/jarlist.xml";
        String webImageDir = uEngineHomePath + "/was/server/default/deploy/uengine-web.war/processmanager/images";

        JarFile jarfile = new JarFile(componentPath);
        ZipEntry entry = jarfile.getEntry("META-INF/activitytypes.xml");
        InputStream is = jarfile.getInputStream(entry);
        ArrayList compActList = (ArrayList) GlobalContext.deserialize(is, String.class);
        is.close();

        //add activity types
        is = new FileInputStream(activityTypesXMLPath);
        ArrayList actList = (ArrayList) GlobalContext.deserialize(is, String.class);
        is.close();

        //TODO: ignore or overwrite if already contains
        actList.addAll(compActList);

        OutputStream os = new FileOutputStream(activityTypesXMLPath);
        GlobalContext.serialize(actList, os, String.class);
        os.close();

        //add jarfile location
        is = new FileInputStream(signedJarListXMLPath);
        ArrayList jarList = (ArrayList) GlobalContext.deserialize(is, String.class);
        is.close();

        jarList.add(componentFileName);

        os = new FileOutputStream(signedJarListXMLPath);
        GlobalContext.serialize(jarList, os, String.class);
    }

    public static String QName2ClassName(QName qName) {
        String clsName = "";
        clsName = QName2PackageName(qName.getNamespaceURI()) + "." + qName.getLocalPart();

        return clsName;
    }

    public static String QName2PackageName(String name) {
        String stubPkgName = "";
        {
            String targetNS = name;

            try {
                URL url = new URL(targetNS);
                String host = url.getHost();
                String path = url.getPath();
                String file = url.getFile();

                String fullName = host;

                boolean bFirst = true;
                StringTokenizer stk = new StringTokenizer(fullName, ".");
                while (stk.hasMoreTokens()) {
                    String token = stk.nextToken();
                    stubPkgName = token + (bFirst ? "" : ".") + stubPkgName;
                    bFirst = false;
                }

                //jws.Purchase_Order_Receiver_v2_WebService.axis.localhost

                stubPkgName = stubPkgName + path.replace('.', '_').replace('/', '.');

            } catch (Exception e) {
                //	e.printStackTrace();
            }
        }
        return stubPkgName;
    }

    public static boolean isNotEmpty(String value) {
        boolean blnIsData = false;
        if (value != null) {
            value = value.trim();
            if (value.length() > 0 && !"null".equals(value.toLowerCase())) {
                blnIsData = true;
            }
        }

        return blnIsData;
    }


    public static boolean isTrue(Object obj) {
        boolean isTrue = false;
        if (null != obj && isNotEmpty(obj.toString())) {
            String strTrue = obj.toString().trim().toLowerCase();
            isTrue = !(
                    "-1".equals(strTrue)
                            || "0".equals(strTrue)
                            || "false".equals(strTrue)
                            || "no".equals(strTrue)
                            || "n".equals(strTrue)
                            || "undefined".equals(strTrue)
            );
        }

        return isTrue;
    }

/*	public static Object[] getProcessNameAndVersion(String definitionName){
		String nameOnly=null; int version=-1;{
			int versionPos = definitionName.lastIndexOf("v");
			if(versionPos>-1){
				nameOnly = definitionName.substring(0, versionPos).trim();
				String strversion = definitionName.substring(versionPos+1, definitionName.length()).trim();
				try{
					version = Integer.parseInt(strversion);
				}catch(Exception e){
					version = -1;
				}
			}

			if(version==-1){
				nameOnly = definitionName.trim();
				version = 1;
			}
		}

		return new Object[]{nameOnly, new Integer(version)};
	}*/

    public static String getWebServerBaseURL() {
        String host = System.getProperty("uEngineWebServerBaseURL");
        if (host == null) {
            host = "http://localhost";
        }
        return host;
    }

    public static String sendViaHttpPost(String urlStr, String[] keyAndValues)
            throws Exception {
        try {
            // Construct data

            StringBuffer sb = new StringBuffer();
            String sep = "";

            for (int i = 0; i < keyAndValues.length; i += 2) {

                String key = keyAndValues[i];
                String value = keyAndValues[i + 1];

                if (value == null) continue;

                sb.append(sep)
                        .append(URLEncoder.encode(key, "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));

                sep = "&";
            }

            String data = sb.toString();

            // Send data
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            sb = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n");
            }

            rd.close();

            return sb.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public static InputStream getInputStreamFromHttpPost(String urlStr, String[] keyAndValues)
            throws Exception {
        try {
            // Construct data

            StringBuffer sb = new StringBuffer();
            String sep = "";

            for (int i = 0; i < keyAndValues.length; i += 2) {

                String key = keyAndValues[i];
                String value = keyAndValues[i + 1];

                if (value == null) continue;

                sb.append(sep)
                        .append(URLEncoder.encode(key, "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));

                sep = "&";
            }

            String data = sb.toString();

            // Send data
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            return conn.getInputStream();
        } catch (Exception e) {
            throw e;
        }
    }

    public static String createInstanceId(ProcessDefinitionRemote pd) {
        Calendar now = Calendar.getInstance();
        int y = now.get(Calendar.YEAR);
        int m = now.get(Calendar.MONTH) + 1;
        int d = now.get(Calendar.DATE);
        int h = now.get(Calendar.HOUR);
        int mi = now.get(Calendar.MINUTE);
        int s = now.get(Calendar.SECOND);

        return pd.getName().getText().replace(' ', '_') + "-" + y + (m < 10 ? "0" + m : "" + m) + (d < 10 ? "0" + d : "" + d) + (h < 10 ? "0" + h : "" + h) + (mi < 10 ? "0" + mi : "" + mi) + (s < 10 ? "0" + s : "" + s);
    }

    public static String createInstanceId(ProcessDefinition pd) {
        return createInstanceId(new ProcessDefinitionRemote(pd, null));
    }

    public static String toOnlyFirstCharacterUpper(String src) {
        if (src == null) return null;

        if (src.length() == 0) return "";

        String firstChar = src.substring(0, 1);
        String remainder = src.length() > 1 ? src.substring(1) : "";

        return firstChar.toUpperCase() + remainder;
    }


    public static void main(String[] args) throws Exception {
        if (args[0].equals("addActivityTypeComponent")) {
            Properties options = new Properties();
            options.setProperty("componentPath", args[1]);
            options.setProperty("uEngineHome", args[2]);

            addActivityTypeComponent(options);
        }
    }


    ////////////////


    /**
     * decimalformat
     */
    private static DecimalFormat subslotFormat = new DecimalFormat("0000");

    /**
     * ���� �߻�; '�� ����
     */
    public static int countForRandom = 100;

    /**
     * ��/��/��/ ���丮 ��v ��ȯ
     */
    public static String getCalendarDir() {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        return calendar.get(Calendar.YEAR) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getSubDir() {
        int frequency[] = new int[24];                // frequency�� 1�ð�; ��� ���� ���ΰ��� ���� d��
        frequency[0] = 0;
        frequency[12] = 3;        //
        frequency[1] = 0;
        frequency[13] = 5;        // ex)
        frequency[2] = 0;
        frequency[14] = 5;        // �ش� �ð��� frequency�� 0�� ���
        frequency[3] = 0;
        frequency[15] = 5;        // -> '9900' ����
        frequency[4] = 0;
        frequency[16] = 5;        //
        frequency[5] = 0;
        frequency[17] = 5;        // �ش� �ð��� 14���̰�, 14���� frequency�� 4�� ���
        frequency[6] = 1;
        frequency[18] = 4;        // -> 14��  0��~14�� : '1400' ����
        frequency[7] = 1;
        frequency[19] = 3;        //    14�� 15��~29�� : '1401' ����
        frequency[8] = 3;
        frequency[20] = 1;        //    14�� 30��~44�� : '1402' ����
        frequency[9] = 5;
        frequency[21] = 1;        //    14�� 45��~59�� : '1403' ����
        frequency[10] = 6;
        frequency[22] = 1;        //
        frequency[11] = 5;
        frequency[23] = 0;        // �� frequency�� 1~60 ���̷� ��d�� ��

        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int piece = 0;
        if (frequency[hour] == 0) {
            hour = 99;
        } else {
            piece = minute / (60 / frequency[hour]);
        }

        String zero = "0000";
        String strHour = zero + Integer.toString(hour);
        strHour = strHour.substring(strHour.length() - 2, strHour.length());
        String strPiece = zero + Integer.toString(piece);
        strPiece = strPiece.substring(strPiece.length() - 2, strPiece.length());

        return strHour + strPiece;
    }

    /**
     * ��/��/�� + UniqueFileName + ����Ȯ����
     */
    public static String getUniqueFile(String originalFileName) {
        return getUniqueFile(originalFileName, true);
    }


    public static String getUniqueFile(String originalFileName, boolean makeSubFolder) {
        if (makeSubFolder) {
            return getCalendarDir() + "/" + getSubDir() + "/" + getUniqID() + "." + getFileExt(originalFileName);
        } else {
            return getCalendarDir() + "/" + getUniqID() + "." + getFileExt(originalFileName);
        }
    }

    public static String getNamedExtFile(String namedFile, String ext) {
        return namedFile.substring(0, namedFile.lastIndexOf('.')) + "." + ext;
    }


    /**
     * String 배열의 내부에 값이 들어가 있는지 검사한다. 빈 값이 있다면 false 없다면 true 를 반환한다.
     *
     * @param values
     * @return
     */
    public static boolean isNotEmpty(String[] values) {
        boolean blnIsData = true;

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (!isNotEmpty(values[i])) {
                    blnIsData = false;
                    break;
                }
            }
        } else {
            blnIsData = false;
        }
        return blnIsData;
    }


    /**
     * Unique ���� �̸� ��
     */
    public static String getUniqID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS", Locale.KOREA);
        return sdf.format(new Date()) + "_" + getRandomStr();
    }

    public static String getRandomStr() {
        long randomNo = 0;

        for (int i = 0; i < 100; i++) {
            String temp = "";
            for (int j = 0; j < 5; j++) {
                temp = temp + Integer.toString(countForRandom);
                countForRandom++;
                if (countForRandom > 900) countForRandom = 100;
            }
            randomNo = randomNo + Long.parseLong(temp) + System.currentTimeMillis() + temp.hashCode();
        }

        String randomStr = Long.toString(randomNo);
        randomStr = randomStr.substring(randomStr.length() - 12, randomStr.length());

        return randomStr;
    }

    public static void saveContents(String path, String contents) throws IOException {
        saveContents(path, contents, "UTF-8");
    }

    /**
     * �����͸� properties �� ��d�� ���ڵ� ���·� ����
     */
    public static void saveContents(String path, String contents, String encoding) throws IOException {
        saveContents(new File(path), contents, encoding);
    }

    public static void saveContents(File file, String contents) throws IOException {
        saveContents(file, contents, "UTF-8");
    }

    public static void saveContents(File file, String contents, String encoding) throws IOException {
//		File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStreamWriter writer = null;
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            writer = new OutputStreamWriter(outputStream, encoding);
            writer.write(contents);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * CDATA ����
     *
     * @param data CDATA�� ����� XML ������
     * @return CDATA�� ��� ��Ʈ�� ������
     */
    public static String getCDataSections(String data) {
        return data.substring(data.indexOf("<![CDATA[") + 9, data.lastIndexOf("]]>"));
    }


    /**
     * ��Ʈ�� ��ũ������(�ε����� ��ū; ��ȯ�Ѵ�)
     *
     * @param strSrc �Է½�Ʈ��
     * @param nCount ��ū�ε���
     * @return ��ū��
     * @throws
     */
    public static String getTokenChar(String strSrc, int nCount)
            throws Exception {
        String strRet = "";
        if (strSrc == null) return "";
        try {
            if (strSrc.trim().equals("")) return strRet;

            StringTokenizer tokenTemp = new StringTokenizer(strSrc, 27 + "");

            for (int i = 1; tokenTemp.hasMoreTokens() && (i <= nCount); i++) {
                String strTemp = tokenTemp.nextToken();
                if (i == nCount) strRet = strTemp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strRet;
    }


    /**
     * ���ڿ�(strSrc)�߿��� ������(key)�� �̿��� �ε���(nCount)�� �ش��ϴ� ��ū; ��ȯ�Ѵ�.
     *
     * @param strSrc ���ڿ�
     * @param nCount �ε���
     * @param key    ������
     * @return �ش� ��ū
     * @throws
     */
    public static String getTokenString(String strSrc, int nCount, String key)
            throws Exception {
        String strRet = "";

        if (strSrc == null) return "";

        try {
            if (strSrc.trim().equals("")) return strRet;

            StringTokenizer tokenTemp = new StringTokenizer(strSrc, key);

            for (int i = 1; tokenTemp.hasMoreTokens() && (i <= nCount); i++) {
                String strTemp = tokenTemp.nextToken();
                if (i == nCount) strRet = strTemp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strRet;
    }

    public static String getTokenString(String str, int col)
            throws Exception {
        StringTokenizer st = new StringTokenizer(str, "|");
        if (st.hasMoreTokens()) {
            if (col == 2) st.nextToken();
            if (st.hasMoreTokens()) return st.nextToken();
        }
        return null;
    }

    /**
     * �־��� ���ڿ����� parameter�� ������ delemeter�� ��ũ����¡ �� ���ϴ� ���; ��ȯ�Ѵ�.
     *
     * @param strSrc    ���ڿ�
     * @param nCount    �ε���
     * @param delemeter ������
     * @return �ش� ��ū
     * @throws
     */
    public static String getToken(String strSrc, int nCount, String delemeter)
            throws Exception {
        String strRet = "";
        try {
            StringTokenizer tokenTemp = new StringTokenizer(strSrc, delemeter);

            for (int i = 1; tokenTemp.hasMoreTokens() && (i <= nCount); i++) {
                String strTemp = tokenTemp.nextToken();

                if (i == nCount) {
                    strRet = strTemp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }

    /**
     * ���ڿ�(strSrc)�� Ưd����(strKey); �ٸ� ���ڿ�(strKey)�� �����Ѵ�.
     *
     * @param strSrc   �� ���ڿ�
     * @param strKey   ������� ����
     * @param strValue ������ ���ڿ�
     * @return ����� ��~���ڿ�
     * @throws
     */
    public static String replaceString(String strSrc, String strKey, String strValue)
            throws Exception {
        String strRet = "";
        String strTemp = "";
        try {
            StringTokenizer tokenTemp = new StringTokenizer(strSrc, strKey);
            int nCount = tokenTemp.countTokens();

            for (int i = 1; i <= nCount; i++) {
                if (i == nCount) {
                    strRet += getTokenString(strSrc, i, strKey);
                } else {
                    strRet += (getTokenString(strSrc, i, strKey) + strValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }


    /**
     * word���� ���𿡵��ͷ� copy�� �߻�Ǵ� <![endif]>�� ���� �޼ҵ�
     *
     * @param src �Խù����Ͽ��� �о���� �Խù����� ��Ʈ��
     * @param key ���ϰ����ϴ� �ױ׵� ��Ʈ�� ��)<![endif]>
     * @return strRet key�±׸� ���� �Խù����� ��Ʈ��
     */
    public static String deleteString(String src, String key)
            throws Exception { //word���� ���𿡵��ͷ� copy�� �߻�Ǵ� <![endif]>�� ���� �޼ҵ�

        String beforeTag = "";
        String afterTag = "";
        String strRet = "";

        try {
            int begin = 0;
            int end = 0;
            int nLen = key.length();
            boolean iscontinue = true;

            while (iscontinue) {
                if (src.indexOf(key) > -1) { //��f�ϰ����ϴ� ��ũ�� x���ϴ� ���

                    end = src.indexOf(key);
                    beforeTag = src.substring(begin, end);
                    afterTag = src.substring(end + nLen);
                    src = afterTag;
                    strRet += beforeTag;

                    continue;
                } else { //��f�ϰ����ϴ� �±װ� x������ �ʴ� ���
                    strRet += src;
                    iscontinue = false;

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }

    /**
     * ��Ʈ���߿� �ѱ��� ���ԵǾ���� �Ǵ��Ѵ�.
     *
     * @param uni20
     * @return �ѱ��� ���ԵǾ� ���̸� true, �ƴϸ�false�� ����
     * @throws
     */
    public boolean checkHan(String uni20) throws Exception {
        boolean nResult = false;

        try {
            if (uni20 == null) {
                return nResult;
            }

            int len = uni20.length();
            char[] carry = new char[len];

            for (int i = 0; i < len; i++) {
                char c = uni20.charAt(i);

                if ((c < 0xac00) || (0xd7a3 < c)) {
                    carry[i] = c;
                } else {
                    nResult = true;

                    byte[] ksc = String.valueOf(c).getBytes("KSC5601");

                    if (ksc.length != 2) {
                        carry[i] = '\ufffd';

                        //                    System.out.print("Warning : Some of Unicode 2.0 Hangul character was ignored");
                    } else {
                        carry[i] = (char) (0x3400 + (((ksc[0] & 0xff) - 0xb0) * 94) +
                                (ksc[1] & 0xff) - 0xa1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return nResult;
    }

    /**
     * ���ڿ��� �������� üũ
     *
     * @param digitStr String ���ڷ� ������ ���ڿ�
     * @return boolean  ���ڷ� �����Ǿ� ��8�� true, �ƴϸ� false
     */
    public static boolean isDigit(String digitStr) {
        if (digitStr != null) {
            for (int i = 0; i < digitStr.length(); i++)
                if (!Character.isDigit(digitStr.charAt(i)))
                    return false;
        }
        return true;
    }

    /**
     * html ����; ��Ʈ������� ��ȯ�Ѵ�.
     *
     * @param docPath html���� ���
     * @return html ����(��Ʈ��)
     * @throws
     */
    public static String getStringFromHtmlFile(String docPath)
            throws Exception {
        String strRet = "";
        String tmpLine = "";

        if (docPath == null) {
            return "����� ������ ��f�Ǿ�ų� ������ ã; �� ��4ϴ�.!";
        }

        StringBuffer sb = new StringBuffer();

        File tmpFile = new File(docPath);

        if (!tmpFile.exists()) {
            return "����� ������ ��f�Ǿ�ų� ������ ã; �� ��4ϴ�.!";
        }

        BufferedReader br = null;

        try {
            //��Ĺ����� ���Ϸ� �о�´�.
            br = new BufferedReader(new FileReader(docPath));

            while ((tmpLine = br.readLine()) != null) {
                sb.append(tmpLine);
                sb.append("\n");

                //                  strRet += tmpLine + "\n";
            }

            if (br != null) {
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return sb.toString();
    }

    /**
     * NOTE : �ѱ��̳� �Ϻ����� ��� ���� ũ�Ⱑ ������ �޶� ���ڼ� �׳� 10�ڷ�
     * f���� ��� ���ڼ� �����ϴ� ���� �޶���� �ȴ�.
     * ������ �ѱ��̳� �Ϻ���� ���� ascii ��; ����� ��� �� ���ڸ�
     * �� ���ڸ� �ν��Ͽ� ó���ϵ��� �Ѵ�.
     */
    public static String getLimitString(String str, int limitSize) {
        limitSize = limitSize--;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (i > limitSize) {
                // 5���ڸ� ���͵� ������ 9�ڰ� display �Ǵ� �ͺ��� �ѱ���
                // ��ġ�ϴ� ���� �� �д�. ������ ���ڸ� �� ���δ�.
                sb.deleteCharAt(sb.length() - 1);
                sb.append("..");
                break;
            }
            int value = (int) str.charAt(i);
            if (value > 127) {
                limitSize--;
            }
            sb.append(str.charAt(i));
        }
        return sb.toString();
    }


    /**
     * ���� full path�� ���ϸ? ��ȯ�Ѵ�.
     *
     * @param strPath ������ ��ü���
     * @return ���ϸ�
     * @throws
     */
    public static String getSimpleFileName(String strPath) {
        String strRet = " ";
        try {
            File fTemp = new File(strPath);
            if (fTemp.exists()) {
                strRet = fTemp.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }

    /**
     * ���ϸ?�� Ȯ���ڸ� ��ȯ�Ѵ�.
     *
     * @param strPath ���ϰ��
     * @return ������ Ȯ����
     * @throws
     */
    public static String getFileExt(String strPath) {
        String strRet = "";
        try {
            int index = strPath.lastIndexOf(".") + 1;

            if (index > -1) {
                strRet = strPath.substring(index);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }

    /**
     * ���ϰ�θ� f���� �����̸��� ��ȯ
     *
     * @param strPath ���ϰ��
     * @return �����̸�
     * @throws
     */
    public static String getFileName(String strPath) {
        strPath = replace(strPath, "\\", "/");
        String strRet = "";
        try {
            int index = strPath.lastIndexOf('/') + 1;

            if (index > -1) {
                strRet = strPath.substring(index);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }

    /**
     * ���� ��ü��ο��� ���ϸ�; f���� ��θ� ��ȯ�Ѵ�.
     *
     * @param strPath ���ϰ��
     * @return ������ Ȯ����
     * @throws
     */
    public static String getFilePath(String strPath) {
        strPath = replace(strPath, "\\", "/");
        String strRet = "";
        try {
            int index = strPath.lastIndexOf('/');

            if (index > -1) {
                strRet = strPath.substring(0, index);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strRet;
    }


    /**
     * 8859_1�� ǥ��� ���ڿ�; �Էµ� CharSet Ÿ�Կ� �´� ���ڿ��� �ٲ��ش�.
     *
     * @param 8859_1�� ǥ��� ���ڿ�
     * @return �ش� ��� CharacterSet 8�� ��ȯ�� ���ڿ�
     * @throws UnsupportedEncodingException
     */
    public static String toEncode(String str) {
        if (false) {
            return str;
        }

        String encodedStr = null;
        if (str == null) return encodedStr;
        try {
            encodedStr = new String(str.getBytes("8859_1"), "UTF-8");
            if (checkEncoding(encodedStr) && (encodedStr.length() != 0)) {
//				System.out.println("UTF-8");
                return encodedStr;
            }
            encodedStr = new String(str.getBytes("8859_1"), "KSC5601");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toEncode(String str, String encoding) {
        String encodedStr = null;
        if (str == null) return encodedStr;
        try {
            encodedStr = new String(str.getBytes("8859_1"), encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toURLEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String[] toEncode(String[] strs) {
        if (false) {
            return strs;
        }

        if (strs == null) return null;
        String[] tempStrs = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            tempStrs[i] = toEncode(strs[i]);
        }
        return tempStrs;
    }

    public static String[] toEncode(String[] strs, String encoding) {
        if (strs == null) return null;
        String[] tempStrs = new String[strs.length];
        for (int i = 0; i < strs.length; i++) {
            tempStrs[i] = toEncode(strs[i], encoding);
        }
        return tempStrs;
    }

    /**
     * ���ڿ��� ���Ե� ĳ���� �߿� ���� ���ڰ� �ִ��� �˻�
     */
    public static boolean checkEncoding(String uni20) throws Exception {
        boolean nResult = true;
        try {
            if (uni20 == null) return nResult;
            int cnt = 0;
            int len = uni20.length();
            char[] carry = new char[len];
            for (int i = 0; i < len; i++) {
                char c = uni20.charAt(i);
                if (Integer.toHexString(c).equals("fffd")) cnt++;
                if (cnt > 1) return false;
                //System.out.println(type + " c : " + c + ", " + Integer.toHexString(c));
            }
            if (len == 1 && cnt == 1) nResult = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nResult;
    }


    /**
     * KSC5601 ǥ��� ���ڿ�; �ѱ� ���ڿ��� �ٲ��ش�.
     *
     * @param �ѱ۷� ǥ��� ���ڿ�
     * @return 8859_1 ���ڿ�
     * @throws UnsupportedEncodingException
     */
    public static String fromEncode(String str) {
        if (str == null)
            return null;
        try {
            return new String(str.getBytes("UTF-8"), "8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * KSC5601 ǥ��� ���ڿ�; �ѱ� ���ڿ��� �ٲ��ش�.
     *
     * @param �ѱ۷� ǥ��� ���ڿ�
     * @return 8859_1 ���ڿ�
     * @throws UnsupportedEncodingException
     */
    public static String fromEncode(String str, String encoding) {
        if (str == null)
            return null;
        try {
            return new String(str.getBytes(encoding), "8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * NOTE : StringBuffer�� ���� ��x�� String Ŭ������ replace �޼ҵ带 ����ϴ� ����
     * 50~60 �� d�� ��.
     */
    public static String replace(String src, String oldstr, String newstr) {
        if (src == null)
            return null;
        StringBuffer dest = new StringBuffer("");
        int len = oldstr.length();
        int srclen = src.length();
        int pos = 0;
        int oldpos = 0;

        while ((pos = src.indexOf(oldstr, oldpos)) >= 0) {
            dest.append(src.substring(oldpos, pos));
            dest.append(newstr);
            oldpos = pos + len;
        }

        if (oldpos < srclen)
            dest.append(src.substring(oldpos, srclen));

        return dest.toString();
    }


    /**
     * Ưd ��ū; �����Ѵ�.
     *
     * @param strSrc   ������ ��ū ���ڿ�
     * @param nIndex   ������ ��ū�� �ε���
     * @param strToken ������ ����
     * @return String  ����� ���ڿ�
     * @throws
     */
    public static String replaceToken(String strSrc, int nIndex, String strToken)
            throws Exception {
        if (strSrc == null) {
            return strSrc;
        }

        try {
            StringTokenizer st = new StringTokenizer(strSrc, 27 + "");
            int nCount = st.countTokens();

            if (nCount < nIndex) {
                return strSrc;
            }

            StringBuffer sb = new StringBuffer();

            for (int i = 1; i < (nCount + 1); i++) {
                if (nIndex == i) {
                    sb.append(strToken);
                } else {
                    sb.append(getTokenChar(strSrc, i));
                }

                sb.append(27);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }


    /**
     * �ؽ�ȭ�� �н���带 ��´�.
     */
    public static String getHashPassword(String str) {
        byte[] b;
        try {
            b = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            b = str.getBytes();
            System.out.println("Warning: UTF8 not supported.");
        }

        MessageDigest currentAlgorithm = null;

        try {
            currentAlgorithm = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println("No Such Algorithm");
            return "Failed";
        }
        currentAlgorithm.reset();
        currentAlgorithm.update(b);

        byte[] hash = currentAlgorithm.digest();

        byte[] password = new byte[8];

        for (int i = 0; i < 8; i++) {
            password[i] = (byte) ((hash[i] ^ hash[i + 8]) & 0x7f);
            if (password[i] == 0) password[i] = (byte) '0';
        }

        try {
            str = new String(password, "UTF8");
        } catch (UnsupportedEncodingException e) {
            str = new String(password);
            System.out.println("Warining: UTF8 not supported.");
        }

        return str;
    }

    public static void copyToFile(File inFile, File outFile) throws IOException {
        if (!outFile.getParentFile().exists()) outFile.getParentFile().mkdirs();

        BufferedInputStream fin = null;
        BufferedOutputStream fout = null;

        try {
            fin = new BufferedInputStream(new FileInputStream(inFile), 2048);
            fout = new BufferedOutputStream(new FileOutputStream(outFile), 2048);

            synchronized (fin) {
                synchronized (fout) {
                    int intReadByte = -1;
                    while ((intReadByte = fin.read()) != -1) {
                        fout.write(intReadByte);
                    }
                }
            }
            //_copy(fin, fout);
        } finally {
            try {
                if (fin != null) fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fout != null) fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ���Ϻ���
     */
    public static void copyToFile(String inFile, String outFile) throws IOException {
        copyToFile(new File(inFile), new File(outFile));
    }


    /**
     * Temporary (only test)
     */
    public static void _copy(InputStream in, OutputStream out) throws IOException {
        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[2048];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }


    //--------------------- from gwLibUtil
    public static String fillString(String strValue, char chVal, int intLength, int intGu) {
        String strReturn = "";
        for (int i = 0; i < (intLength - strValue.length()); i++) {
            strReturn = chVal + strReturn;
        }
        if (intGu == 0) {
            // strVal�� �տ� chVal�� ä���
            strReturn = strReturn + strValue;
        } else if (intGu == 1) {
            // strVal�� �ڿ� chVal�� ä���
            strReturn = strValue + strReturn;
        } else {
            // �׳� �״�� ������
            strReturn = strValue;
        }
        return strReturn;
    }


    /**
     * String str���� userID,int in ���� DB character�� ũ��
     */
    public static String changeChar(String str, int in) {
        String _str = str;
        int counMe = str.length();
        while (in - counMe > 0) {
            _str = _str + " ";
            counMe++;
        }
        return _str;
    }

    /**
     *
     */
    public static String createFileName(String strUserId) {
        java.util.Calendar caltmp = java.util.Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return formatter.format(caltmp.getTime()) + strUserId;
    }

    public static boolean getBoolean(int param) {
        return param == 1 ? true : false;
    }

    public static int getInt(boolean param) {
        int intReturn = 0;
        if (param) intReturn = 1;
        return intReturn;
    }

    public static String getString(String param) {
        if (param == null) return "";
        return param;
    }

    public static String getCDATAString(String param) {
        return "<![CDATA[" + getString(param) + "]]>";
    }

    /**
     * jdk1.2.2 version algorithms
     */
    private static int hashCode(String str) {
        int count = str.length();
        char[] value = new char[count];
        str.getChars(0, count, value, 0);

        int h = 0;
        //int off = offset;
        int off = 0;
        char val[] = value;
        int len = count;

        for (int i = 0; i < len; i++)
            h = 31 * h + val[off++];

        return h;
    }

    /**
     * ���ڿ����� Scriptlet �±׸� ��f�Ѵ�.
     *
     * @param str ��ȯ�� ���ڿ�..
     * @return Scriptlet �±׸� ��f�� ���ڿ�.
     */
    public static String removeScriptletTag(String str) {
        if (str == null || str.indexOf("<%") == -1) {
            return str;
        }
        return str.replaceAll("\\Q<%=\\E|\\Q<%\\E|\\Q%>\\E", "");
    }

    public static Object getBeanProperty(Object bean, String key) throws Exception {
        return getBeanProperty(bean, key, null, false);
    }

    public static Object getBeanProperty(Object bean, String key, boolean ignoreBeanPropertyResolver) throws Exception {
        return getBeanProperty(bean, key, null, ignoreBeanPropertyResolver);
    }

    public static Object getBeanProperty(Object bean, String key, ProcessInstance instance, boolean ignoreBeanPropertyResolver) throws Exception {
        //resolve parts
        String[] wholePartPath = key.replace('.', '@').split("@");

        if (wholePartPath.length == 0) {
            wholePartPath = new String[]{null};
        }

        for (int i = 0; i < wholePartPath.length; i++) {
            String partName = wholePartPath[i];

            if (bean instanceof BeanPropertyResolver && !ignoreBeanPropertyResolver) {
                bean = ((BeanPropertyResolver) bean).getBeanProperty(partName);
            } else {
                if (partName == null) {
                    return bean;
                } else {
                    try {
                        String methodName = "get" + partName.substring(0, 1).toUpperCase() + partName.substring(1);
                        if (instance == null) {
                            Method getter = bean.getClass().getMethod(methodName, new Class[]{});
                            bean = (Serializable) getter.invoke(bean, new Object[]{});
                        } else {
                            Method getter = bean.getClass().getMethod(methodName, new Class[]{ProcessInstance.class});
                            bean = (Serializable) getter.invoke(bean, new Object[]{instance});
                        }
                    } catch (NoSuchMethodException e) {
                        throw new UEngineException("No such bean property '" + partName + "' in object " + bean);
                    }
                }
            }
        }

        return bean;
    }

    public static void setBeanProperty(Object bean, String key, Object propertyValue) throws Exception {
        setBeanProperty(bean, key, propertyValue, null, false);
    }

    public static void setBeanProperty(Object bean, String key, Object propertyValue, boolean ignoreBeanPropertyResolver) throws Exception {
        setBeanProperty(bean, key, propertyValue, null, ignoreBeanPropertyResolver);
    }

    private static Class getBeanValueClass(Object obj) {
        Class cls = null;
        if (obj instanceof Calendar) {
            cls = Calendar.class;
        } else {
            cls = obj.getClass();
        }

        return cls;
    }

    public static void setBeanProperty(Object bean, String key, Object propertyValue, ProcessInstance instance, boolean ignoreBeanPropertyResolver) throws Exception {

        if (bean == null) throw new UEngineException("Bean object is null");

        //resolve parts
        String[] wholePartPath = key.replace('.', '@').split("@");
        if (wholePartPath.length == 0) {
            wholePartPath = new String[]{null};
        }

        for (int i = 0; i < wholePartPath.length; i++) {
            String partName = wholePartPath[i];

            if (i == wholePartPath.length - 1) {
                if (bean instanceof BeanPropertyResolver && !ignoreBeanPropertyResolver) {
                    ((BeanPropertyResolver) bean).setBeanProperty(partName, propertyValue);
                } else {
                    Object beanValue = null;
                    if (propertyValue instanceof ProcessVariableValue)
                        beanValue = ((ProcessVariableValue) propertyValue).getValue();
                    else beanValue = propertyValue;

                    //TODO: the logic getter getter method should be enhanced to list all the methods which starts with "get"
                    String methodName = "set" + partName.substring(0, 1).toUpperCase() + partName.substring(1);
                    Class beanValueClass = getBeanValueClass(beanValue);

                    if (instance == null) {
                        Method setter = bean.getClass().getMethod(methodName, new Class[]{beanValueClass});
                        setter.invoke(bean, new Object[]{beanValue});
                    } else {
                        Method setter = bean.getClass().getMethod(methodName, new Class[]{ProcessInstance.class, beanValueClass});
                        setter.invoke(bean, new Object[]{instance, beanValue});
                    }
                }
            } else {
                if (bean instanceof BeanPropertyResolver && !ignoreBeanPropertyResolver) {
                    bean = ((BeanPropertyResolver) bean).getBeanProperty(partName);
                } else {
                    Method getter = bean.getClass().getMethod("get" + partName.substring(0, 1).toUpperCase() + partName.substring(1), new Class[]{});
                    bean = (Serializable) getter.invoke(bean, new Object[]{});
                }
            }
        }

    }

    public static Point getRelativeLocation(Container container, Component comp) {

        Point location = comp.getLocation();
        while (comp.getParent() != container && comp.getParent() != null) {
            Point parentLocation = comp.getParent().getLocation();
            location.setLocation(location.getX() + parentLocation.x, location.getY() + parentLocation.getY());
            comp = comp.getParent();
        }

        if (comp.getParent() == null) {
            throw new RuntimeException(new UEngineException("Couldn't find the container " + container + " from the parent stack."));
        }

        return location;
    }

    public static Object putMultipleEntryMap(Map map, Object key, Object value) {
        if (map.containsKey(key)) {

            Object existingValue = map.get(key);

            ArrayList multiValue;
            if (existingValue instanceof ArrayList) {
                multiValue = (ArrayList) existingValue;
            } else {
                multiValue = new ArrayList();
                multiValue.add(existingValue);
            }

            multiValue.add(value);
            return map.put(key, multiValue);
        } else
            return map.put(key, value);
    }

    public static boolean boolValue(Boolean booleanValue) {
        return (booleanValue != null ? booleanValue.booleanValue() : false);
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(emailAddress);
        boolean matchFound = m.matches();

        if (matchFound)
            return true;
        else
            return false;
    }

    public static String encodeUTF8(String s) {
        if (isNotEmpty(s)) {
            try {
                s = URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return s;
    }

    /*
     * SQL Injection 취약점을 보완하기 위한 필터 메서드
     */
    public static String searchStringFilter(String s) {
        s = s.replaceAll("'", "''");
        s = s.replaceAll("\"", "\"\"");
//		s = s.replaceAll("\\", "\\\\");
        s = s.replaceAll(";", "");
        s = s.replaceAll("#", "");
        s = s.replaceAll("--", "");
//		s = s.replaceAll(" ", "");
        return s;
    }

    @Deprecated
    public static HashMap<String, Object> sessionToHashMap(HttpSession session) {
        if (session == null) return null;
        Enumeration enu = session.getAttributeNames();
        HashMap<String, Object> map = new HashMap<String, Object>();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            Object value = session.getAttribute(key);
            map.put(key, value);
        }
        return map;
    }

    public static boolean isNumeric(String value){
        Pattern pattern = Pattern.compile("[+-]?\\d+");
        return pattern.matcher(value).matches();
    }
}
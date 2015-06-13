package org.uengine.util;


import java.io.IOException;

import java.io.Reader;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;

import java.sql.SQLException;

import java.sql.Types;

import java.util.Enumeration;

import java.util.HashMap;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;




/**

 *  Class Name : IntegrationDTO

 *  Description : DATA를 담는 해쉬맵 클래스 

 *  Modification Information

 *

 *    수정일            수정자               수정내용

 *  ---------       ----------          -----------------

 *  2007. 07. 12          Cho Wan Hee           최초 생성

 *

 *  @author Cho Wan Hee

 *  @since 2007. 07. 12

 *  @version 1.0



 *  Copyright (C) 2007 by Uengine All right reserved.

 */
public class IntegrationDTO extends HashMap {

    public IntegrationDTO() {

    }

    
    
    
    /**

     * Request를 파싱해서 파라미터를 key로 파라미터값을 value로 해서 저장

     * @param request

     * @throws ServletException

     */

    public void parseRequest(HttpServletRequest request)

    throws ServletException {

        //HashMap map = new HashMap();

    	
    	Enumeration enumr  = request.getParameterNames();

        String param = null;

        String[] values = null;

        //Logger.debug.println(this, "[parameter start]#########");

        //setDebugInfo(debugInfo, "AnalysisProcessHandler.doPostBpmProcess() Start");

        while (enumr.hasMoreElements()) {

            param = (String) enumr.nextElement();

            values = request.getParameterValues(param);

            if (values.length == 1) {

                setValue(param, values[0]);

            }

            //동일한 파라미터가 복수개일 때 배열로 처리

            else {

                for (int i = 0; i < values.length; i++) {

                    values[i] = values[i];

                }

                setValue(param, values);

            }

        }

        //Logger.debug.println(this, "[parameter end]#########");

    }

    /**

     *

     *  ResultSet 에서 컬럼 이름을 key로 해서 그 값을 Entity 에 저장하는 method

     *  @param ResultSet rs

     *  @exception SQLException

     */

    public void parseResultSet(ResultSet rs) throws SQLException, IOException {

        ResultSetMetaData md = rs.getMetaData();

        int size = md.getColumnCount();

        for (int i = 1; i <= size; i++) {

            if (md.getColumnType(i) == Types.CLOB) {

                String clobData = readClobData(rs.getCharacterStream(i));

                setValue(md.getColumnName(i).toUpperCase(), clobData);

            } else {

                setValue(md.getColumnName(i).toUpperCase(), rs.getString(i));

            }

        }

    }

    /**

     * key와 값을 저장

     * @param sKey

     * @param sValue

     */

    public void setValue(String sKey, String sValue) {

        if (sValue != null) {

            put(sKey.toUpperCase(), sValue);

        }

    }

    /**

     * key와 값(배열)을 저장

     * @param sKey

     * @param sValues

     */

    public void setValue(String sKey, String sValues[]) {

        put(sKey.toUpperCase(), sValues);

    }

    /**

     * key에 해당하는 값을 리턴

     * @param sKey

     * @return

     */

    public String getValue(String sKey) {

        String sValue = null;

        Object obj = null;

        try {

            obj = get(sKey.toUpperCase());

            if (obj instanceof String) {

                sValue = (String) obj;

            } else if (obj instanceof String[]) {

                sValue = ((String[]) obj)[0];

            } else {

                sValue = "";

            }

        } catch (Exception e) {

            sValue = "";

        }

        return sValue;

    }

    /**

     * key에 해당하는 배열을 리턴

     * @param sKey

     * @return

     */

    public String[] getValues(String sKey) {

        String sValues[] = null;

        Object obj = null;

        try {

            obj = get(sKey.toUpperCase());

            if (obj instanceof String) {

                sValues = new String[1];

                sValues[0] = (String) obj;

            } else {

                sValues = (String[]) obj;

            }

        } catch (Exception e) {

        }

        return sValues;

    }

    /**

     * key에 해당하는 값을 int로 리턴

     * @param sKey

     * @return

     */

    public int getInt(String sKey) {

        int iResult = 0;

        try {

            iResult = Integer.parseInt(getValue(sKey.toUpperCase()));

        } catch (Exception e) {

        }

        return iResult;

    }

    /**

     * key에 해당하는 값을 int로 리턴

     * @param sKey

     * @return

     */

    public float getFloat(String sKey) {

        float iResult = 0f;

        try {

            iResult = Float.parseFloat(getValue(sKey.toUpperCase()));

        } catch (Exception e) {

        }

        return iResult;

    }
    public static String readClobData(Reader reader) throws IOException {

        StringBuffer data = new StringBuffer();

        char[] buf = new char[1024];

        int cnt = 0;

        if (null != reader) {

            while ((cnt = reader.read(buf)) != -1) {

                data.append(buf, 0, cnt);

            }

        }

        return data.toString();

    }

}
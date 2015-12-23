package org.uengine.modeling.resource;

import com.thoughtworks.xstream.XStream;
import org.uengine.kernel.NeedArrangementToSerialize;

import java.io.*;

/**
 * Created by jangjinyoung on 15. 7. 12..
 */
public class Serializer {

    public static XStream xstream = new XStream(/*new DomDriver()*/);
    static{
        xstream.ignoreUnknownElements();
    }
    public final static String DATABASE_ENCODING = "UTF-8";


    public static void serialize(Object obj, OutputStream os) throws Exception{
        if(obj instanceof NeedArrangementToSerialize)
            ((NeedArrangementToSerialize)obj).beforeSerialization();

        try{
            xstream.toXML(obj, new OutputStreamWriter(os, "UTF-8"));
            //
        }catch(Exception ex){
            throw ex;
        }finally{
            try{os.close();}catch(Exception exx){};
        }
    }

    public static String serialize(Object obj) throws Exception{
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        serialize(obj, bao);
        return bao.toString(DATABASE_ENCODING);
    }

    public static Object deserialize(String src) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(src.getBytes(DATABASE_ENCODING));

        return deserialize(bis);
    }

    public static Object deserialize(InputStream is) throws Exception{

        Object obj = null;

        try{
            obj = xstream.fromXML(new InputStreamReader(is, "UTF-8"));
        }catch(Exception e){
            throw e;
        }finally{
            try{is.close();}catch(Exception exx){};
        }

        if(obj instanceof NeedArrangementToSerialize)
            ((NeedArrangementToSerialize)obj).afterDeserialization();

        return obj;
    }



}

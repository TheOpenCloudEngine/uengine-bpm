package org.uengine.util;

import java.io.File;

import org.uengine.components.serializers.XStreamSerializer;
import org.uengine.kernel.Serializer;

import com.thoughtworks.xstream.XStream;

public class DefinitionFileConverter {
	public static void main(String args[]) throws Exception{
		if(args.length < 3){
			System.out.println("USAGE: java org.uengine.util.DefinitionFileConverter <Search Directory> <Class Name for Source Serializer> <Class Name for Target Serializer>");
			System.out.println(" (You may provide the classpath pointing 'uengine.jar')");
		}
		
		int j=0;
		for(int i=0; i<args.length; i++){
			String theArg = args[i];
			if(theArg.startsWith("\"")){
				theArg = theArg.substring(1, theArg.length());
				for(i++; i<args.length; i++){
					if(args[i].endsWith("\"")){
						theArg = theArg + " " + args[i].substring(0, args[i].length()-1);
						break;
					}
					
					theArg = theArg + args[i];
				}
			}
			args[j++] = theArg;
		}

		if(args[1].indexOf(".") == -1) args[1] = "org.uengine.components.serializers." + args[1]; 
		if(args[2].indexOf(".") == -1) args[2] = "org.uengine.components.serializers." + args[2]; 
		
		Serializer sourceSerializer = (Serializer)Thread.currentThread().getContextClassLoader().loadClass(args[1]).newInstance();
		Serializer targetSerializer = (Serializer)Thread.currentThread().getContextClassLoader().loadClass(args[2]).newInstance();
		
		find(args[0], sourceSerializer, targetSerializer);
	}
	
	protected static void convert(String src, Serializer sourceSerializer, Serializer targetSerializer) throws Exception{
		String srcXMLPath = src;
		String targetXMLPath = srcXMLPath.split("\\.")[0] + ".converted";// + ".xpd";
		
		try{
			System.out.println(srcXMLPath + " to " + targetXMLPath + ".");

			Object srcObj = sourceSerializer.deserialize(new java.io.FileInputStream(srcXMLPath), null);
			targetSerializer.serialize(srcObj, new java.io.FileOutputStream(targetXMLPath), null);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
    protected static void find(String s, Serializer sourceSerializer, Serializer targetSerializer) throws Exception
    {
        File file = new File(s);
        File afile[] = file.listFiles();
        for(int i = 0; i < afile.length; i++){
            if(afile[i].getName().endsWith(".upd")){
                convert(afile[i].getPath(), sourceSerializer, targetSerializer);
            }else
            if(afile[i].isDirectory())
                find(afile[i].getPath(), sourceSerializer, targetSerializer);
        }
    }
}

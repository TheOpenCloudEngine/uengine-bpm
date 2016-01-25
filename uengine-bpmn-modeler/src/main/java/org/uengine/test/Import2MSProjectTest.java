package org.uengine.test;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.microsoft.importer.MSProjectFileAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by MisakaMikoto on 2015. 10. 21..
 */
public class Import2MSProjectTest {
    public static void main(String[] args){
        MSProjectFileAdapter msProjectFileAdapter = new MSProjectFileAdapter();
        ProcessDefinition processDefinition = null;

        // MS Project 에 Import 하기 위해 만든 xml 파일을 다시 ProcessDefinition 으로 만들고
        // 이를 .process 파일로 저장하여 import 기능을 구현합니다.
        File file = new File("export_process_exampleproject.xml");

        try {
            processDefinition = msProjectFileAdapter.convert(file);
            GlobalContext.serialize(processDefinition, new FileOutputStream("example.process"), String.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.uengine.migrator;

import org.uengine.components.serializers.ActivityRepositoryConverter;
import org.uengine.kernel.*;
import org.uengine.kernel.bpmn.CallActivity;
import org.uengine.modeling.resource.Serializer;
import org.uengine.processpublisher.MigUtils;
import org.uengine.processpublisher.uengine3.importer.ProcessDefinitionAdapter;
import org.uengine.util.ActivityFor;
import org.uengine.util.UEngineUtil;

import java.io.*;
import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class UEngine3Converter {

    public static void main(String... args) throws Exception {

        if(args.length == 0) args = new String[]{"example.3upd"};

//        System.out.println(new File(".").getAbsoluteFile().getPath());

        //Serializer.xstream.registerConverter(new ActivityRepositoryConverter());

        FileInputStream inputFileStream = new FileInputStream(args[0]);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        UEngineUtil.copyStream(inputFileStream, bao);

        String inputString = bao.toString();
        
        //LGD 추가
        inputString = inputString.replace("org.uengine.kernel.OCAPProcessDefinition", "org.uengine.kernel.ProcessDefinition");
        inputString = inputString.replace("org.uengine.kernel.EndActivity", "org.uengine.kernel.DefaultActivity");
        inputString = inputString.replace("com.lgdisplay.activity.OCAPBackActivity", "org.uengine.kernel.DefaultActivity");
        
        inputString = inputString.replace("org.uengine.kernel.ActivityRepository", "java.util.ArrayList");
        inputString = inputString.replace("kitech.apr.activity.KitechHumanActivity", "org.uengine.kernel.HumanActivity");
        inputString = inputString.replace(SubProcessActivity.class.getName(), CallActivity.class.getName());


        ProcessDefinition processDefinition3 = (ProcessDefinition) Serializer.deserialize(inputString);
        ProcessDefinition processDefinition5;

        //set max tracing tag value
        MigUtils.setMaxTracingTag(processDefinition3);

        ProcessDefinitionAdapter processDefinitionAdapter = new ProcessDefinitionAdapter();

        processDefinition5 = processDefinitionAdapter.convert(processDefinition3, new Hashtable());
//        processDefinition5.afterDeserialization();//정리작업

        Serializer.serialize(processDefinition5, new FileOutputStream(args[0]+".5.process"));
    }
}

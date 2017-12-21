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

//        if(args.length == 0) args = new String[]{"example.3upd"};
//
//
//        FileInputStream inputFileStream = new FileInputStream(args[0]);
//        ByteArrayOutputStream bao = new ByteArrayOutputStream();
//
//        UEngineUtil.copyStream(inputFileStream, bao);
//
//        String inputString = bao.toString();
//
//        inputString = inputString.replace("org.uengine.kernel.ActivityRepository", "java.util.ArrayList");
//        inputString = inputString.replace("kitech.apr.activity.KitechHumanActivity", "org.uengine.kernel.HumanActivity");
//        inputString = inputString.replace(SubProcessActivity.class.getName(), CallActivity.class.getName());
//
//
//        ProcessDefinition processDefinition3 = (ProcessDefinition) Serializer.deserialize(inputString);

        ProcessDefinition processDefinition3 = new ProcessDefinition();

        SwitchActivity switchActivity = new SwitchActivity();
        switchActivity.addChildActivity(new DefaultActivity());
        switchActivity.addChildActivity(new DefaultActivity());
        switchActivity.addChildActivity(new DefaultActivity());

        SequenceActivity sequenceActivity = new SequenceActivity();
        sequenceActivity.addChildActivity(new DefaultActivity());
        sequenceActivity.addChildActivity(new DefaultActivity());
        sequenceActivity.addChildActivity(new DefaultActivity());

        switchActivity.addChildActivity(sequenceActivity);


        processDefinition3.addChildActivity(switchActivity);


        //set max tracing tag value
        MigUtils.setMaxTracingTag(processDefinition3);

        ProcessDefinitionAdapter processDefinitionAdapter = new ProcessDefinitionAdapter();

        Hashtable hashtable = new Hashtable();
        processDefinitionAdapter.convert(processDefinition3, hashtable);

        ProcessDefinition processDefinition5 = (ProcessDefinition) hashtable.get("root");



//        Serializer.serialize(processDefinition5, new FileOutputStream(args[0]+".5.process"));
        Serializer.serialize(processDefinition5, System.out);
    }
}

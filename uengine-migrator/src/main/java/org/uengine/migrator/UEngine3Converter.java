package org.uengine.migrator;

import org.uengine.kernel.*;
import org.uengine.modeling.resource.Serializer;
import org.uengine.processpublisher.uengine3.importer.ProcessDefinitionAdapter;
import org.uengine.util.ActivityFor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class UEngine3Converter {

    public static void main(String... args) throws Exception {

        ProcessDefinition processDefinition3 = (ProcessDefinition) Serializer.deserialize(new FileInputStream(args[0]));
        ProcessDefinition processDefinition5;

        ProcessDefinitionAdapter processDefinitionAdapter = new ProcessDefinitionAdapter();

        processDefinition5 = processDefinitionAdapter.convert(processDefinition3, new Hashtable());

        Serializer.serialize(processDefinition5, new FileOutputStream(args[0]+".5.process"));
    }
}

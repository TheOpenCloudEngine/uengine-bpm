package org.uengine.migrator;

import org.uengine.kernel.*;
import org.uengine.modeling.resource.Serializer;
import org.uengine.util.ActivityFor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by uengine on 2017. 6. 16..
 */
public class UEngine3Converter {

    public static void main(String... args) throws Exception {

        ProcessDefinition processDefinition3 = (ProcessDefinition) Serializer.deserialize(new FileInputStream("/Users/uengine/Downloads/26.process"));

        final ProcessDefinition processDefinition5 = new ProcessDefinition();

        ActivityFor activityFor = new ActivityFor(){

            @Override
            public void logic(Activity activity) {
                if(activity instanceof SwitchActivity){

                }else if(activity instanceof AllActivity){

                }else if(activity instanceof SequenceActivity){

                }else if(activity instanceof HumanActivity){

                }
            }
        };

        activityFor.run(processDefinition3);


    }
}

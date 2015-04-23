package org.uengine.bpmn.testmain;

import org.omg.spec.bpmn._20100524.di.BPMNDiagram;
import org.omg.spec.bpmn._20100524.di.BPMNPlane;
import org.omg.spec.bpmn._20100524.di.BPMNShape;
import org.omg.spec.bpmn._20100524.model.TDefinitions;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.processpublisher.AdapterUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ConvertBPMN2UETest {

    public static void  main(String ... args) throws Exception {

        TDefinitions definitions = new TDefinitions();
        //definitions.

//            JAXBElement<GreetingListType> gl =
//                    of.createGreetings( grList );
        JAXBContext jc = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.model:org.omg.spec.bpmn._20100524.di");
        JAXBContext jc2 = JAXBContext.newInstance("org.omg.spec.bpmn._20100524.di");
//            Marshaller m = jc.createMarshaller();
        //           m.marshal( definitions, System.out );
        Unmarshaller um = jc.createUnmarshaller();
        Unmarshaller um2 = jc2.createUnmarshaller();

      //  JAXBContext.
        Object object = um.unmarshal(new File("/java/autoinsurance.bpmn"));

        System.out.println(object);




        Object object2 = um2.unmarshal(new File("/java/auto_insurance.bpmn_only_diagram.xml"));

        System.out.println(object2);

        JAXBElement element = (JAXBElement)object;

        BPMNPlane plane = ((TDefinitions) element.getValue()).getBPMNDiagram().get(0).getBPMNPlane();


        ProcessDefinition definition = (ProcessDefinition) AdapterUtil.adapt(element.getValue());

        System.out.println(definition);

    }

}

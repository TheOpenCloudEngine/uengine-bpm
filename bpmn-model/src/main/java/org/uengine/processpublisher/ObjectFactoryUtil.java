package org.uengine.processpublisher;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by MisakaMikoto on 2015. 8. 13..
 */
public class ObjectFactoryUtil {
    public static <T> T createBPMNObject(Class<T> targetClass) {
        T bpmnObject = null;

        // find ObjectFactory
        try {
            Object objectFactory = Thread.currentThread().getContextClassLoader().loadClass(targetClass.getPackage().getName() + "." + "ObjectFactory").newInstance();

            Method method = objectFactory.getClass().getMethod("create" + targetClass.getSimpleName());
            bpmnObject = (T) method.invoke(objectFactory);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return bpmnObject;

    }

    public static <T> JAXBElement<T> createDefaultJAXBElement(Class<T> targetClass, Object targetObject) {
        JAXBElement<T> jaxbElement = null;

        // find ObjectFactory
        try {
            Object objectFactory = Thread.currentThread().getContextClassLoader().loadClass(targetClass.getPackage().getName() + "." + "ObjectFactory").newInstance();

            // model.ObjectFactory method pattern is (ex: TUserTask -> UserTask())
            // but otherwise ObjectFactory method pattern is (ex: Bounds -> Bounds())
            String targetClassSimpleName = null;
            if(targetClass.getPackage().getName().contains("model")) {
                targetClassSimpleName = targetClass.getSimpleName().substring(1, targetClass.getSimpleName().length());

            } else {
                targetClassSimpleName = targetClass.getSimpleName();

            }

            Method method = objectFactory.getClass().getMethod("create" + targetClassSimpleName, targetObject.getClass());
            jaxbElement = (JAXBElement<T>) method.invoke(objectFactory, targetObject);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return jaxbElement;
    }

    public static <T> JAXBElement<T> createObjectJAXBElement(String targetMethodName, Object targetObject) {
        // ObjectMethod used only model.ObjectFactory
        org.omg.spec.bpmn._20100524.model.ObjectFactory objectFactory = new org.omg.spec.bpmn._20100524.model.ObjectFactory();
        JAXBElement<T> jaxbElement = null;

        // find ObjectFactory
        try {
            // Object Method's parameter is Object.class
            Method method = objectFactory.getClass().getMethod("create" + targetMethodName, Object.class);
            jaxbElement = (JAXBElement<T>) method.invoke(objectFactory, targetObject);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return jaxbElement;
    }

}

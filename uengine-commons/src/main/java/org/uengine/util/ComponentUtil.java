package org.uengine.util;


/**
 * @author Jinyoung Jang
 */

public class ComponentUtil {

    static public String getClassNameOnly(Class activityCls) {
        return getClassNameOnly(activityCls.getName());
    }

    static public String getClassNameOnly(String clsName) {
        return clsName.substring(clsName.lastIndexOf(".") + 1);
    }


    static public String getComponentClassName(Class cls, String compType) {
        return getComponentClassName(cls, compType, false, false);
    }

    static public String getDomainClassName(Class cls, String compType) {

        String componentClassName = cls.getName();
        int whereComponentPackageNameStarts = componentClassName.lastIndexOf("." + compType);

        String domainClassName = componentClassName.substring(whereComponentPackageNameStarts + compType.length() + 1, componentClassName.length() - compType.length());

        String domainPackageName = componentClassName.substring(0, whereComponentPackageNameStarts);

        return domainPackageName + domainClassName;
    }

    static public String getComponentClassName(Class cls, String compType, boolean isDefault, boolean overridesPackage) {
        String pkgName = (cls.getPackage().getName());
        String clsName = getClassNameOnly(cls);

        return pkgName + "." + compType + (isDefault ? ".Default" : ".") + clsName + compType.substring(0, 1).toUpperCase() + compType.substring(1, compType.length());
    }

    static public Object getComponentByEscalation(Class activityCls, String compType) {
        return getComponentByEscalation(activityCls, compType, null);
    }

    static public Object getComponentByEscalation(Class activityCls, String compType, Object defaultValue) {
        Class componentClass = null;
        Class copyOfActivityCls = activityCls;

//        //try to find proper component by escalation (prior to overriding package)
//        String overridingPackageName = GlobalContext.ACTIVITY_DESCRIPTION_COMPONENT_OVERRIDER_PACKAGE;
//        if (overridingPackageName != null) {
//            do {
//                String componentClsName = UEngineUtil.getComponentClassName(copyOfActivityCls, compType, false, true);
//
//                try {
//                    componentClass = Thread.currentThread().getContextClassLoader().loadClass(componentClsName);
//                } catch (Exception e) {
//                }
//
//                //try to find proper component by escalation (with original package)
//                if (componentClass == null) {
//                    componentClsName = UEngineUtil.getComponentClassName(copyOfActivityCls, compType);
//                    try {
//                        componentClass = Thread.currentThread().getContextClassLoader().loadClass(componentClsName);
//                    } catch (ClassNotFoundException e) {
//                    }
//                }
//                copyOfActivityCls = copyOfActivityCls.getSuperclass();
//            } while (componentClass == null && copyOfActivityCls != Activity.class);
//        }

        if (componentClass == null) {
            copyOfActivityCls = activityCls;
            //try to find proper component by escalation (with original package)
            do {
                String componentClsName = getComponentClassName(copyOfActivityCls, compType);

                try {
                    componentClass = Thread.currentThread().getContextClassLoader().loadClass(componentClsName);
                } catch (Exception e) {
                }

                copyOfActivityCls = copyOfActivityCls.getSuperclass();
            } while (componentClass == null && copyOfActivityCls != Object.class);

            //try default one
            if (componentClass == null) {
                if (defaultValue != null) {
                    return defaultValue;
                } else {
                    try {
                        componentClass = Thread.currentThread().getContextClassLoader().loadClass(getComponentClassName(activityCls, compType, true, false));
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        }

        try {
            return componentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }



}
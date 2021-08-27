package com.delitto.izumo.framework.util;

import java.lang.reflect.Type;

public class TypeUtil {
    private static final String TYPE_NAME_PREFIX = "class ";

    static String getClassName(Type type) {
        if (type==null) {
            return "";
        }
        String className = type.toString();
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    public static Class<?> getClass(Type type)
            throws ClassNotFoundException {
        String className = getClassName(type);
        return Class.forName(className);
    }

    public static Class<?> getClass(Type type, ClassLoader classLoader) throws ClassNotFoundException{
        String className = getClassName(type);
        return Class.forName(className, false, classLoader);
    }
}

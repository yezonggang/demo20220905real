package com.quick.start.demo.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author y25958
 */
public final class ProxyUtils {
    public static <T> T newProxyInstance(Class<T> type, InvocationHandler handler) {
        return newProxyInstance(type.getClassLoader(), type, handler);
    }

    public static <T> T newProxyInstance(ClassLoader cl, Class<T> type, InvocationHandler handler) {
        Class<?>[] interfaces = {type};
        Object wrapper = Proxy.newProxyInstance(cl, interfaces, handler);
        return type.cast(wrapper);
    }

    public static Throwable unwrapInvocationThrowable(Throwable t) {
        if (t instanceof InvocationTargetException) {
            return unwrapInvocationThrowable(((InvocationTargetException) t).getTargetException());
        } else {
            return t;
        }

    }


}

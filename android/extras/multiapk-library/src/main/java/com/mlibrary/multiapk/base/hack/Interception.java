package com.mlibrary.multiapk.base.hack;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class Interception {
    public interface Intercepted {
    }

    private Interception() {
    }

    public static <T> T proxy(Object obj, Class<T> cls, InterceptionHandler<T> interceptionHandler) throws IllegalArgumentException {
        if (obj instanceof Intercepted)
            return (T) obj;
        interceptionHandler.setDelegate((T) obj);
        return (T) Proxy.newProxyInstance(Interception.class.getClassLoader(), new Class[]{cls, Intercepted.class}, interceptionHandler);
    }

    public static <T> T proxy(Object obj, InterceptionHandler<T> interceptionHandler, Class<?>... clsArr) throws IllegalArgumentException {
        interceptionHandler.setDelegate((T) obj);
        return (T) Proxy.newProxyInstance(Interception.class.getClassLoader(), clsArr, interceptionHandler);
    }

    public static abstract class InterceptionHandler<T> implements InvocationHandler {
        private T mDelegate;

        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            Object obj2 = null;
            try {
                obj2 = method.invoke(delegate(), objArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj2;
        }

        public T delegate() {
            return this.mDelegate;
        }

        public void setDelegate(T t) {
            this.mDelegate = t;
        }
    }
}

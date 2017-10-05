package com.fanwe.library.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理处理类
 *
 * @param <T>
 */
public abstract class SDProxyHandler<T> implements InvocationHandler
{
    private T mProxy;
    private Class<T> mClass;

    public SDProxyHandler(Class<T> clazz)
    {
        if (clazz == null)
        {
            throw new NullPointerException("clazz is null");
        }
        if (!clazz.isInterface())
        {
            throw new IllegalArgumentException("clazz is not an interface");
        }
        this.mClass = clazz;
    }

    /**
     * 创建代理
     *
     * @param clazz   要创建代理的接口
     * @param handler
     * @return
     */
    public static <P> P newProxyInstance(Class<P> clazz, InvocationHandler handler)
    {
        return (P) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
    }

    /**
     * 对某个对象触发某个方法
     *
     * @param receiver 对象
     * @param method   方法
     * @param args     方法参数
     * @return
     * @throws Throwable
     */
    public static Object invokeMethod(Object receiver, Method method, Object[] args) throws Throwable
    {
        if (receiver == null || method == null)
        {
            return null;
        }
        return method.invoke(receiver, args);
    }

    /**
     * 获得代理对象
     *
     * @return
     */
    public T getProxy()
    {
        if (mProxy == null)
        {
            mProxy = newProxyInstance(mClass, this);
        }
        return mProxy;
    }
}

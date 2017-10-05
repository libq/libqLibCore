package com.fanwe.library.holder;

import com.fanwe.library.reflect.SDProxyHandler;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理对象持有者
 *
 * @param <T>
 */
public class SDProxyHolder<T> extends SDObjectHolder<T> implements InvocationHandler
{
    private SDProxyHandler<T> mProxyHandler;
    private WeakReference mChildReference;

    public SDProxyHolder(Class<T> clazz)
    {
        mProxyHandler = new SDProxyHandler<T>(clazz)
        {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
            {
                return SDProxyHolder.this.invoke(proxy, method, args);
            }
        };
    }

    /**
     * 获得内部持有的代理对象
     *
     * @return
     */
    protected T getProxy()
    {
        return mProxyHandler.getProxy();
    }

    /**
     * 返回的是代理对象
     *
     * @return
     */
    @Override
    public T get()
    {
        return getProxy();
    }

    /**
     * 设置child对象<br>
     * 当内部代理对象方法被触发的时候会先通知内部真实对象的方法，然后再通知child对象内部持有对象的方法
     *
     * @param child
     * @param <C>
     */
    public <C extends T> void notify(SDProxyHolder<C> child)
    {
        if (this == child)
        {
            throw new IllegalArgumentException("child should not be current instance");
        }
        if (getChild() != child)
        {
            if (mChildReference != null)
            {
                mChildReference.clear();
                mChildReference = null;
            }
            if (child != null)
            {
                mChildReference = new WeakReference<>(child);
            }
        }
    }

    /**
     * 获得child对象
     *
     * @return
     */
    protected SDProxyHolder<T> getChild()
    {
        if (mChildReference == null)
        {
            return null;
        } else
        {
            return (SDProxyHolder<T>) mChildReference.get();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if (super.get() != null)
        {
            //先触发内部持有对象，再触发child中的持有对象
            Object realResult = method.invoke(super.get(), args);
            if (getChild() != null)
            {
                method.invoke(getChild().get(), args);
            }
            return realResult;
        } else
        {
            if (getChild() != null)
            {
                return method.invoke(getChild().get(), args);
            } else
            {
                return null;
            }
        }
    }
}

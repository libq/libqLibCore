package com.fanwe.library.listener;

/**
 * Created by Administrator on 2017/6/22.
 */

public interface SDObjectChangedCallback<T>
{
    void onObjectChanged(T newObject, T oldObject);
}

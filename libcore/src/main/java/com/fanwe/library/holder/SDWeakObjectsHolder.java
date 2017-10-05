package com.fanwe.library.holder;

import com.fanwe.library.listener.SDIterateCallback;
import com.fanwe.library.utils.SDCollectionUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 采用弱引用持有对象的对象持有者
 *
 * @param <T>
 */
public class SDWeakObjectsHolder<T> implements ISDObjectsHolder<T>
{
    private WeakHashMap<T, Integer> mMapObjects = new WeakHashMap<>();
    private List<Map.Entry<T, Integer>> mListEntry = new ArrayList<>();
    private int mObjectPosition = -1;

    private int getObjectPosition()
    {
        mObjectPosition++;
        if (mObjectPosition >= Integer.MAX_VALUE)
        {
            mObjectPosition = -1;
            resetMapCount();
        }
        return mObjectPosition;
    }

    private void resetMapCount()
    {
        foreach(false, new SDIterateCallback<Map.Entry<T, Integer>>()
        {
            @Override
            public boolean next(int i, Map.Entry<T, Integer> item, Iterator<Map.Entry<T, Integer>> it)
            {
                item.setValue(getObjectPosition());
                return false;
            }
        });
    }

    @Override
    public synchronized void add(T object)
    {
        if (object == null)
        {
            return;
        }
        if (!contains(object))
        {
            mMapObjects.put(object, getObjectPosition());
        }
    }

    @Override
    public synchronized boolean remove(T object)
    {
        if (object == null)
        {
            return false;
        }
        return mMapObjects.remove(object) != null;
    }

    @Override
    public synchronized boolean contains(T object)
    {
        if (object == null)
        {
            return false;
        }
        return mMapObjects.containsKey(object);
    }

    @Override
    public synchronized int size()
    {
        return mMapObjects.size();
    }

    @Override
    public void clear()
    {
        mListEntry.clear();
        mMapObjects.clear();
        mObjectPosition = -1;
    }

    @Override
    public synchronized boolean foreach(final SDIterateCallback<T> callback)
    {
        return foreach(false, new SDIterateCallback<Map.Entry<T, Integer>>()
        {
            @Override
            public boolean next(int i, Map.Entry<T, Integer> item, Iterator<Map.Entry<T, Integer>> it)
            {
                return callback.next(i, item.getKey(), null);
            }
        });
    }

    @Override
    public synchronized boolean foreachReverse(final SDIterateCallback<T> callback)
    {
        return foreach(true, new SDIterateCallback<Map.Entry<T, Integer>>()
        {
            @Override
            public boolean next(int i, Map.Entry<T, Integer> item, Iterator<Map.Entry<T, Integer>> it)
            {
                return callback.next(i, item.getKey(), null);
            }
        });
    }

    /**
     * 遍历map
     *
     * @param reverse
     * @param callback
     * @return
     */
    private boolean foreach(boolean reverse, SDIterateCallback<Map.Entry<T, Integer>> callback)
    {
        List<Map.Entry<T, Integer>> listSorted = sortMap(reverse);
        if (listSorted == null)
        {
            return false;
        }

        boolean result = SDCollectionUtil.foreach(listSorted, callback);
        listSorted.clear();
        return result;
    }

    /**
     * 对map排序
     *
     * @param reverse
     * @return
     */
    private List<Map.Entry<T, Integer>> sortMap(boolean reverse)
    {
        if (reverse)
        {
            return SDCollectionUtil.sortMap(mMapObjects, foreachReverseComparator, mListEntry);
        } else
        {
            return SDCollectionUtil.sortMap(mMapObjects, foreachComparator, mListEntry);
        }
    }

    /**
     * 升序比较器
     */
    private Comparator<Map.Entry<T, Integer>> foreachComparator = new Comparator<Map.Entry<T, Integer>>()
    {
        @Override
        public int compare(Map.Entry<T, Integer> o1, Map.Entry<T, Integer> o2)
        {
            return o1.getValue() - o2.getValue();
        }
    };

    /**
     * 降序比较器
     */
    private Comparator<Map.Entry<T, Integer>> foreachReverseComparator = new Comparator<Map.Entry<T, Integer>>()
    {
        @Override
        public int compare(Map.Entry<T, Integer> o1, Map.Entry<T, Integer> o2)
        {
            return o2.getValue() - o1.getValue();
        }
    };
}

package com.fanwe.library.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 可以锁定和解锁view宽高的类
 */
public class SDViewSizeLocker
{

    private View mView;

    private int mTempWidth;
    private int mTempHeight;
    private float mTempWeight;
    /**
     * 宽度是否已经被锁住
     */
    private boolean mHasLockWidth;
    /**
     * 高度是否已经被锁住
     */
    private boolean mHasLockHeight;

    public SDViewSizeLocker(View view)
    {
        this.mView = view;
    }

    /**
     * 设置要处理的view
     *
     * @param view
     */
    public void setView(View view)
    {
        if (this.mView != view)
        {
            reset();
            this.mView = view;
        }
    }

    private void reset()
    {
        mTempWidth = 0;
        mTempHeight = 0;
        mTempWeight = 0;
        mHasLockWidth = false;
        mHasLockHeight = false;
    }

    /**
     * 宽度是否已经被锁住
     */
    public boolean hasLockWidth()
    {
        return mHasLockWidth;
    }

    /**
     * 高度是否已经被锁住
     *
     * @return
     */
    public boolean hasLockHeight()
    {
        return mHasLockHeight;
    }

    private ViewGroup.LayoutParams getLayoutParams()
    {
        if (getView() == null)
        {
            return null;
        }
        return getView().getLayoutParams();
    }

    private void setLayoutParams(ViewGroup.LayoutParams params)
    {
        if (getView() == null)
        {
            return;
        }
        getView().setLayoutParams(params);
    }

    public View getView()
    {
        return mView;
    }

    /**
     * 锁定宽度
     */
    public void lockWidth()
    {
        if (getView() == null)
        {
            return;
        }
        lockWidth(getView().getWidth());
    }

    /**
     * 锁定宽度
     *
     * @param lockWidth 要锁定的宽度
     */
    public void lockWidth(int lockWidth)
    {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null)
        {
            return;
        }

        if (mHasLockWidth)
        {
            //如果已经锁了，直接赋值
            params.width = lockWidth;
        } else
        {
            if (params instanceof LinearLayout.LayoutParams)
            {
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                mTempWeight = lParams.weight;
                lParams.weight = 0;
            }

            mTempWidth = params.width;
            params.width = lockWidth;
            mHasLockWidth = true;
        }
        setLayoutParams(params);
    }

    /**
     * 解锁宽度
     */
    public void unlockWidth()
    {
        if (mHasLockWidth)
        {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null)
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    lParams.weight = mTempWeight;
                }
                params.width = mTempWidth;
                setLayoutParams(params);
            }
            mHasLockWidth = false;
        }
    }

    /**
     * 锁定高度
     */
    public void lockHeight()
    {
        if (getView() == null)
        {
            return;
        }
        lockHeight(getView().getHeight());
    }

    /**
     * 锁定高度
     *
     * @param lockHeight 要锁定的高度
     */
    public void lockHeight(int lockHeight)
    {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null)
        {
            return;
        }

        if (mHasLockHeight)
        {
            //如果已经锁了，直接赋值
            params.height = lockHeight;
        } else
        {
            if (params instanceof LinearLayout.LayoutParams)
            {
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                mTempWeight = lParams.weight;
                lParams.weight = 0;
            }

            mTempHeight = params.height;
            params.height = lockHeight;
            mHasLockHeight = true;
        }
        setLayoutParams(params);

    }

    /**
     * 解锁高度
     */
    public void unlockHeight()
    {
        if (mHasLockHeight)
        {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null)
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    lParams.weight = mTempWeight;
                }
                params.height = mTempHeight;
                setLayoutParams(params);
            }
            mHasLockHeight = false;
        }
    }

}

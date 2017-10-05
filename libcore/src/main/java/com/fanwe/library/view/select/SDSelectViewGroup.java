package com.fanwe.library.view.select;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.library.listener.SDOnFinishInflateCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SDSelectViewGroup extends LinearLayout
{
    public SDSelectViewGroup(Context context)
    {
        super(context);
        init();
    }

    public SDSelectViewGroup(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDSelectViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private SDSelectViewManager mSelectViewManager = new SDSelectViewManager();
    private List<View> mListView = new ArrayList<>();
    private SDOnFinishInflateCallback mOnFinishInflateCallback;

    private void init()
    {

    }

    public void setOnFinishInflateCallback(SDOnFinishInflateCallback onFinishInflateCallback)
    {
        mOnFinishInflateCallback = onFinishInflateCallback;
    }

    public SDSelectViewManager getSelectViewManager()
    {
        return mSelectViewManager;
    }

    public void initChildView()
    {
        mListView.clear();
        for (int i = 0; i < getChildCount(); i++)
        {
            mListView.add(getChildAt(i));
        }

        mSelectViewManager.clearSelected();
        mSelectViewManager.setItems(mListView);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (mOnFinishInflateCallback != null)
        {
            mOnFinishInflateCallback.onFinishInflate();
        }
        initChildView();
    }
}

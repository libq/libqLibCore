package com.fanwe.library.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fanwe.library.gesture.SDViewDragCallback;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SDDragRelativeLayout extends RelativeLayout
{
    private ViewDragHelper mDragHelper;

    public SDDragRelativeLayout(Context context)
    {
        super(context);
        init();
    }

    public SDDragRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDDragRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragcallback);
    }

    private SDViewDragCallback mDragcallback = new SDViewDragCallback()
    {
        @Override
        public ViewGroup getParentView()
        {
            return SDDragRelativeLayout.this;
        }

        @Override
        public ViewDragHelper getViewDragHelper()
        {
            return mDragHelper;
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);

        mDragHelper.processTouchEvent(event);
        if (mDragcallback.hasCapturedView())
        {
            result = true;
        }
        return result;
    }
}

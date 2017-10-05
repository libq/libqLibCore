package com.fanwe.library.listener;

import android.app.Activity;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/6/8.
 */

public abstract class SDSimpleActivityDispatchTouchEventCallback implements SDActivityDispatchTouchEventCallback
{
    @Override
    public boolean dispatchTouchEvent(Activity activity, MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                return onTouchDown(ev);
            case MotionEvent.ACTION_MOVE:
                return onTouchMove(ev);
            case MotionEvent.ACTION_UP:
                return onTouchUp(ev);
            default:
                break;
        }
        return false;
    }

    protected boolean onTouchDown(MotionEvent ev)
    {
        return false;
    }

    protected boolean onTouchMove(MotionEvent ev)
    {
        return false;
    }

    protected boolean onTouchUp(MotionEvent ev)
    {
        return false;
    }
}

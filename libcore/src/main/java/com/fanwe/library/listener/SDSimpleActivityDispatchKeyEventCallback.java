package com.fanwe.library.listener;

import android.app.Activity;
import android.view.KeyEvent;

/**
 * Created by Administrator on 2017/6/8.
 */

public abstract class SDSimpleActivityDispatchKeyEventCallback implements SDActivityDispatchKeyEventCallback
{
    @Override
    public boolean dispatchKeyEvent(Activity activity, KeyEvent event)
    {
        switch (event.getAction())
        {
            case KeyEvent.ACTION_DOWN:
                if (KeyEvent.KEYCODE_BACK == event.getKeyCode())
                {
                    return onPressBack();
                } else if (KeyEvent.KEYCODE_HOME == event.getKeyCode())
                {
                    return onPressHome();
                } else if (KeyEvent.KEYCODE_MENU == event.getKeyCode())
                {
                    return onPressMenu();
                }
                break;

            default:
                break;
        }
        return false;
    }

    /**
     * 返回键按下
     *
     * @return
     */
    protected boolean onPressBack()
    {
        return false;
    }

    /**
     * Home键按下
     *
     * @return
     */
    protected boolean onPressHome()
    {
        return false;
    }

    /**
     * Menu键按下
     *
     * @return
     */
    protected boolean onPressMenu()
    {
        return false;
    }
}

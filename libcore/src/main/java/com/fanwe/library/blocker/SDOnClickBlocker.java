package com.fanwe.library.blocker;

import android.view.View;

import com.fanwe.library.utils.LogUtil;

/**
 * OnClickListener点击拦截
 */
public class SDOnClickBlocker implements View.OnClickListener
{
    private View.OnClickListener mOriginal;
    private static SDDurationBlocker sGlobalBlocker = new SDDurationBlocker(500);
    private SDDurationBlocker mPrivateBlocker;

    SDOnClickBlocker(View.OnClickListener original, long blockDuration)
    {
        this.mOriginal = original;
        if (blockDuration < 0)
        {
            //全局拦截
        } else
        {
            mPrivateBlocker = new SDDurationBlocker();
            mPrivateBlocker.setBlockDuration(blockDuration);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (mPrivateBlocker != null)
        {
            if (mPrivateBlocker.block())
            {
                LogUtil.i("Private Block " + mPrivateBlocker.getBlockDuration() + ":" + mOriginal.getClass().getName());
            } else
            {
                mOriginal.onClick(v);
            }
        } else
        {
            if (sGlobalBlocker.block())
            {
                LogUtil.i("Global Block " + sGlobalBlocker.getBlockDuration() + ":" + mOriginal.getClass().getName());
            } else
            {
                mOriginal.onClick(v);
            }
        }
    }

    /**
     * 设置全局拦截间隔
     *
     * @param blockDuration
     */
    public static void setGlobalBlockDuration(long blockDuration)
    {
        sGlobalBlocker.setBlockDuration(blockDuration);
    }

    /**
     * 设置拦截view的点击事件，默认拦截间隔为500毫秒
     *
     * @param view
     * @param onClickListener
     */
    public static void setOnClickListener(View view, View.OnClickListener onClickListener)
    {
        setOnClickListener(view, -1, onClickListener);
    }

    /**
     * 设置拦截view的点击事件<br>
     * 当blockDuration大于0：按设置的时间间隔拦截当前view<br>
     * 当blockDuration等于0：不拦截当前view<br>
     * 当blockDuration小于0：按全局设置的间隔拦截当前view（默认500毫秒）
     *
     * @param view
     * @param blockDuration   拦截间隔
     * @param onClickListener
     */
    public static void setOnClickListener(View view, long blockDuration, View.OnClickListener onClickListener)
    {
        if (view == null)
        {
            return;
        }
        if (onClickListener == null)
        {
            view.setOnClickListener(null);
            return;
        }

        SDOnClickBlocker blocker = new SDOnClickBlocker(onClickListener, blockDuration);
        view.setOnClickListener(blocker);
    }

}

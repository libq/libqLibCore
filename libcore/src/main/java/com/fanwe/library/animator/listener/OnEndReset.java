package com.fanwe.library.animator.listener;

import android.animation.Animator;
import android.view.View;

import com.fanwe.library.utils.SDViewUtil;

/**
 * 动画结束重置view
 */
public class OnEndReset extends SDAnimatorListener
{
    public OnEndReset()
    {
        super();
    }

    public OnEndReset(View target)
    {
        super(target);
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        super.onAnimationEnd(animation);
        SDViewUtil.resetView(getTarget());
    }
}

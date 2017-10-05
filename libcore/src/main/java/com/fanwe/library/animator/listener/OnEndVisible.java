package com.fanwe.library.animator.listener;

import android.animation.Animator;
import android.view.View;

/**
 * 动画结束设置view可见
 */
public class OnEndVisible extends SDAnimatorListener
{
    public OnEndVisible()
    {
        super();
    }

    public OnEndVisible(View target)
    {
        super(target);
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        super.onAnimationEnd(animation);
        if (getTarget() != null)
        {
            getTarget().setVisibility(View.VISIBLE);
        }
    }
}

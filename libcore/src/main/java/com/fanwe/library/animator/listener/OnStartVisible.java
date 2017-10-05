package com.fanwe.library.animator.listener;

import android.animation.Animator;
import android.view.View;

/**
 * 动画开始设置view可见
 */
public class OnStartVisible extends SDAnimatorListener
{
    public OnStartVisible()
    {
        super();
    }

    public OnStartVisible(View target)
    {
        super(target);
    }

    @Override
    public void onAnimationStart(Animator animation)
    {
        super.onAnimationStart(animation);
        if (getTarget() != null)
        {
            getTarget().setVisibility(View.VISIBLE);
        }
    }
}

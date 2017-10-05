package com.fanwe.library.animator.listener;

import android.animation.Animator;
import android.view.View;

/**
 * 动画开始设置view为View.INVISIBLE
 */
public class OnStartInvisible extends SDAnimatorListener
{
    public OnStartInvisible()
    {
        super();
    }

    public OnStartInvisible(View target)
    {
        super(target);
    }

    @Override
    public void onAnimationStart(Animator animation)
    {
        super.onAnimationStart(animation);
        if (getTarget() != null)
        {
            getTarget().setVisibility(View.INVISIBLE);
        }
    }
}

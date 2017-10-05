package com.fanwe.library.animator.listener;

import android.animation.Animator;
import android.view.View;

/**
 * 动画结束设置view为View.INVISIBLE
 */
public class OnEndInvisible extends SDAnimatorListener
{
    public OnEndInvisible()
    {
        super();
    }

    public OnEndInvisible(View target)
    {
        super(target);
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        super.onAnimationEnd(animation);
        if (getTarget() != null)
        {
            getTarget().setVisibility(View.INVISIBLE);
        }
    }
}

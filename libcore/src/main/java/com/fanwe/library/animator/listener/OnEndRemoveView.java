package com.fanwe.library.animator.listener;

import android.animation.Animator;
import android.view.View;

import com.fanwe.library.utils.SDViewUtil;

/**
 * 动画结束移除view
 */
public class OnEndRemoveView extends SDAnimatorListener
{
    public OnEndRemoveView()
    {
        super();
    }

    public OnEndRemoveView(View target)
    {
        super(target);
    }

    @Override
    public void onAnimationEnd(Animator animation)
    {
        super.onAnimationEnd(animation);
        SDViewUtil.removeView(getTarget());
    }
}

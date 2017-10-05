package com.fanwe.library.view.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fanwe.library.animator.SDAnim;
import com.fanwe.library.animator.listener.SDAnimatorListener;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDAppView;

/**
 * 模仿dialog的view
 */
public class SDDialogView extends SDAppView
{
    public static final int DEFAULT_MARGIN_LEFT_RIGHT = SDViewUtil.getScreenWidthPercent(0.1f);
    public static final int DEFAULT_MARGIN_TOP_BOTTOM = SDViewUtil.getScreenWidthPercent(0.1f);

    /**
     * 窗口移动过整个屏幕高度所需的时间
     */
    public static final long DURATION_SCREEN_HEIGHT = 320;

    private View mTranslucentView;
    private View mContentView;

    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = false;
    private boolean mIsTranslucentBackground = true;
    private boolean mDismissAfterClick = true;

    private int mMarginLeft = DEFAULT_MARGIN_LEFT_RIGHT;
    private int mMarginTop = DEFAULT_MARGIN_TOP_BOTTOM;
    private int mMarginRight = DEFAULT_MARGIN_LEFT_RIGHT;
    private int mMarginBottom = DEFAULT_MARGIN_TOP_BOTTOM;

    private SDAnim mAnimShow;
    private SDAnim mAnimDismiss;
    private boolean mIsInDismissAnim;

    private OnDismissListener mOnDismissListener;

    public SDDialogView(Activity activity)
    {
        super(activity);

        setInterceptTouchEvent(true);
        initTranslucentView();
    }

    /**
     * 初始化半透明view
     */
    private void initTranslucentView()
    {
        mTranslucentView = new View(getContext());
        mTranslucentView.setBackgroundColor(Color.parseColor("#77000000"));
        mTranslucentView.setVisibility(View.INVISIBLE);
        addView(mTranslucentView);
    }

    /**
     * 获得内容view
     *
     * @return
     */
    public View getContentView()
    {
        return mContentView;
    }

    @Override
    public void setContentView(int layoutId)
    {
        setContentView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    @Override
    public void setContentView(View contentView)
    {
        if (mContentView != null)
        {
            removeView(mContentView);
        }
        mContentView = contentView;
        addView(mContentView);
    }

    @Override
    public void setContentView(View contentView, ViewGroup.LayoutParams params)
    {
        contentView.setLayoutParams(params);
        setContentView(contentView);
    }

    /**
     * 设置是否点击返回键移除view
     *
     * @param cancelable
     */
    public void setCancelable(boolean cancelable)
    {
        mCancelable = cancelable;
    }

    /**
     * 设置是否触摸外部消失
     *
     * @param canceledOnTouchOutside
     */
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside)
    {
        if (canceledOnTouchOutside && !mCancelable)
        {
            mCancelable = true;
        }
        mCanceledOnTouchOutside = canceledOnTouchOutside;
    }

    /**
     * 设置view显示的时候背景是否半透明
     *
     * @param translucentBackground
     * @return
     */
    public SDDialogView setTranslucentBackground(boolean translucentBackground)
    {
        mIsTranslucentBackground = translucentBackground;
        return this;
    }

    /**
     * 设置是否点击按钮后自动关闭,默认true(是)
     *
     * @param dismissAfterClick
     * @return
     */
    public SDDialogView setDismissAfterClick(boolean dismissAfterClick)
    {
        mDismissAfterClick = dismissAfterClick;
        return this;
    }

    protected void dismissAfterClickIfNeed()
    {
        if (mDismissAfterClick)
        {
            dismiss();
        }
    }

    private void showTranslucentView(boolean show)
    {
        if (show)
        {
            SDViewUtil.setVisible(mTranslucentView);
        } else
        {
            SDViewUtil.setGone(mTranslucentView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (isVisible() && mContentView != null)
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    if (SDViewUtil.isTouchView(mContentView, ev))
                    {
                        onTouchDownInside(ev);
                    } else
                    {
                        onTouchDownOutside(ev);
                    }
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(Activity activity, KeyEvent event)
    {
        if (isVisible() && getParent() != null)
        {
            super.dispatchKeyEvent(activity, event);
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    protected boolean onTouchDownOutside(MotionEvent ev)
    {
        if (mCanceledOnTouchOutside)
        {
            dismiss();
            return true;
        }
        return super.onTouchDownOutside(ev);
    }

    @Override
    public boolean onBackPressed()
    {
        if (mCancelable)
        {
            dismiss();
        }
        return super.onBackPressed();
    }

    @Override
    public SDDialogView setWidth(int width)
    {
        SDViewUtil.setWidth(mContentView, width);
        return this;
    }

    @Override
    public SDDialogView setHeight(int height)
    {
        SDViewUtil.setHeight(mContentView, height);
        return this;
    }

    public SDDialogView setMargins(int margins)
    {
        setMargin(margins, margins, margins, margins);
        return this;
    }

    public SDDialogView setMarginLeft(int left)
    {
        mMarginLeft = left;
        dealMargins();
        return this;
    }

    public SDDialogView setMarginTop(int top)
    {
        mMarginTop = top;
        dealMargins();
        return this;
    }

    public SDDialogView setMarginRight(int right)
    {
        mMarginRight = right;
        dealMargins();
        return this;
    }

    public SDDialogView setMarginBottom(int bottom)
    {
        mMarginBottom = bottom;
        dealMargins();
        return this;
    }

    public SDDialogView setMargin(int left, int top, int right, int bottom)
    {
        mMarginLeft = left;
        mMarginTop = top;
        mMarginRight = right;
        mMarginBottom = bottom;
        dealMargins();
        return this;
    }

    private void dealMargins()
    {
        SDViewUtil.setMargin(mContentView, mMarginLeft, mMarginTop, mMarginRight, mMarginBottom);
    }

    /**
     * 窗口是否显示
     *
     * @return
     */
    public boolean isShowing()
    {
        return getParent() != null;
    }

    public void show()
    {
        showCenter();
    }

    /**
     * 根据传进来的距离换算出所需要移动的时长
     *
     * @param distance
     * @return
     */
    private float getScaleDuration(int distance)
    {
        float result = DURATION_SCREEN_HEIGHT * ((float) Math.abs(distance) / (float) SDViewUtil.getScreenHeight());
        return result;
    }

    /**
     * 显示在顶部
     */
    public void showTop()
    {
        if (showInside(Gravity.TOP | Gravity.CENTER_HORIZONTAL))
        {
            int deltaY = -SDViewUtil.getHeight(mContentView) - Math.abs(mMarginTop);
            long duration = (long) getScaleDuration(deltaY);
            mAnimShow = SDAnim.from(mContentView).translationY(deltaY, 0).setDuration(duration);
            mAnimShow.start();

            mAnimDismiss = SDAnim.from(mContentView).translationY(0, deltaY).setDuration(duration);
        }
    }

    /**
     * 显示在屏幕中央
     */
    public void showCenter()
    {
        if (showInside(Gravity.CENTER))
        {
            float deltaAlpha = 1.0f;
            mAnimShow = SDAnim.from(this).alpha(0, deltaAlpha).setDuration(200);
            mAnimShow.start();

            mAnimDismiss = SDAnim.from(this).alpha(deltaAlpha, 0).setDuration(200);
        }
    }

    /**
     * 显示在底部
     */
    public void showBottom()
    {
        if (showInside(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL))
        {
            int deltaY = SDViewUtil.getHeight(mContentView) + Math.abs(mMarginBottom);
            long duration = (long) getScaleDuration(deltaY);
            mAnimShow = SDAnim.from(mContentView).translationY(deltaY, 0).setDuration(duration);
            mAnimShow.start();

            mAnimDismiss = SDAnim.from(mContentView).translationY(0, deltaY).setDuration(duration);
        }
    }

    private boolean showInside(int gravity)
    {
        if (mContentView == null)
        {
            return false;
        }
        if (isShowing())
        {
            return false;
        }

        cancelAnimIfNeed();
        SDViewUtil.setLayoutGravity(mContentView, gravity);
        addToActivity(true);
        showTranslucentView(mIsTranslucentBackground);
        dealMargins();

        return true;
    }

    private void cancelAnimIfNeed()
    {
        if (mAnimShow != null)
        {
            mAnimShow.cancel();
            SDViewUtil.resetView(mContentView);
        }
        if (mAnimDismiss != null)
        {
            mAnimDismiss.cancel();
            SDViewUtil.resetView(mContentView);
        }
    }

    /**
     * 关闭窗口
     */
    public void dismiss()
    {
        if (!isShowing())
        {
            return;
        }

        if (mAnimDismiss == null)
        {
            removeSelf();
        } else
        {
            if (mIsInDismissAnim)
            {
                return;
            }
            cancelAnimIfNeed();
            mIsInDismissAnim = true;
            mAnimDismiss.addListener(new SDAnimatorListener()
            {
                @Override
                public void onAnimationEnd(Animator animation)
                {
                    super.onAnimationEnd(animation);
                    mAnimDismiss = null;
                    removeSelf();
                    mIsInDismissAnim = false;
                }
            }).start();
        }
    }

    private void addToActivity(boolean add)
    {
        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(android.R.id.content);

        if (add)
        {
            if (getParent() == frameLayout)
            {
                return;
            }
            frameLayout.addView(this);
        } else
        {
            frameLayout.removeView(this);
        }
    }

    /**
     * 设置消失监听
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(OnDismissListener onDismissListener)
    {
        mOnDismissListener = onDismissListener;
    }

    /**
     * 延迟多久关闭窗口
     *
     * @param delay
     */
    public void startDismissRunnable(long delay)
    {
        mDismissRunnable.runDelay(delay);
    }

    public void stopDismissRunnable()
    {
        mDismissRunnable.removeDelay();
    }

    private SDDelayRunnable mDismissRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            dismiss();
        }
    };

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stopDismissRunnable();
        cancelAnimIfNeed();
        onDismiss(this);
        if (mOnDismissListener != null)
        {
            mOnDismissListener.onDismiss(this);
        }
    }

    public void onDismiss(SDDialogView view)
    {

    }

    public interface OnDismissListener
    {
        void onDismiss(SDDialogView view);
    }

}

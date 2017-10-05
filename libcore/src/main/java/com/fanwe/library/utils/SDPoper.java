package com.fanwe.library.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/4/27.
 */

public class SDPoper
{
    private View mPopView;
    private Position mPosition;
    private boolean mDynamicUpdate;

    private int mHeightStatusBar;
    private int mWidthScreen;
    private int mHeightScreen;

    private FrameLayout flRoot;
    private FrameLayout.LayoutParams mParams;
    private int mMarginLeft;
    private int mMarginTop;
    private int mMarginRight;
    private int mMarginBottom;
    private int mGravity;

    private int mMarginX;
    private int mMarginY;

    private WeakReference<View> mTarget;
    private Rect mTargetRect = new Rect();

    public SDPoper()
    {

    }

    public void init(Activity activity)
    {
        mWidthScreen = SDViewUtil.getScreenWidth();
        mHeightScreen = SDViewUtil.getScreenHeight();
        mHeightStatusBar = SDViewUtil.getActivityStatusBarHeight(activity);
        if (flRoot == null)
        {
            flRoot = (FrameLayout) activity.findViewById(android.R.id.content);
        }
    }

    /**
     * 设置根部局
     *
     * @param frameLayout
     */
    public SDPoper setRootLayout(FrameLayout frameLayout)
    {
        if (flRoot != frameLayout)
        {
            boolean isAttached = isAttached();
            if (isAttached)
            {
                removePopViewFromRoot();
            }
            this.flRoot = frameLayout;
            if (isAttached)
            {
                attach(true);
            }
        }
        return this;
    }

    public SDPoper setPopView(View popView)
    {
        if (mPopView != popView)
        {
            releasePopView();
            mPopView = popView;
            initPopView();
        }
        return this;
    }

    private void releasePopView()
    {
        mPopView = null;
        mPosition = null;
    }

    private void initPopView()
    {
        if (mPopView != null)
        {
            init((Activity) mPopView.getContext());
        }
    }

    /**
     * 返回Target
     *
     * @return
     */
    public View getTarget()
    {
        if (mTarget != null)
        {
            return mTarget.get();
        } else
        {
            return null;
        }
    }

    /**
     * 设置目标view
     *
     * @param target
     */
    public SDPoper setTarget(View target)
    {
        if (getTarget() != target)
        {
            releaseTarget();
            if (target != null)
            {
                mTarget = new WeakReference<>(target);
            } else
            {
                mTarget = null;
            }
            initTarget();
        }
        return this;
    }

    /**
     * 初始化Target
     */
    private void initTarget()
    {
        if (getTarget() != null)
        {
            addTargetOnGlobalLayoutListenerIfNeed();
        }
    }

    /**
     * 释放Target
     */
    private void releaseTarget()
    {
        if (getTarget() != null)
        {
            getTarget().getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListenerTarget);
            mTarget = null;
        }
    }

    /**
     * 根据设置是否添加Target的OnGlobalLayoutListener回调
     */
    private void addTargetOnGlobalLayoutListenerIfNeed()
    {
        if (getTarget() != null)
        {
            if (mDynamicUpdate)
            {
                getTarget().getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListenerTarget);
                getTarget().getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListenerTarget);
            } else
            {
                getTarget().getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListenerTarget);
            }
        }
    }

    /**
     * 设置是否动态更新popview的位置，默认true
     *
     * @param dynamicUpdate
     */
    public SDPoper setDynamicUpdate(boolean dynamicUpdate)
    {
        mDynamicUpdate = dynamicUpdate;
        addTargetOnGlobalLayoutListenerIfNeed();
        return this;
    }

    /**
     * 设置显示的位置
     *
     * @param position
     */
    public SDPoper setPosition(Position position)
    {
        if (position != null)
        {
            mPosition = position;
        }
        return this;
    }

    /**
     * 设置x轴方向margin
     *
     * @param marginX
     */
    public SDPoper setMarginX(int marginX)
    {
        mMarginX = marginX;
        return this;
    }

    /**
     * 设置y轴方向margin
     *
     * @param marginY
     */
    public SDPoper setMarginY(int marginY)
    {
        mMarginY = marginY;
        return this;
    }

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenerTarget = new ViewTreeObserver.OnGlobalLayoutListener()
    {
        @Override
        public void onGlobalLayout()
        {
            if (mDynamicUpdate && isAttached())
            {
                updatePosition();
            }
        }
    };

    /**
     * 保存target的信息
     */
    private void saveTargetInfo()
    {
        if (getTarget() != null)
        {
            getTarget().getGlobalVisibleRect(mTargetRect);
        }
    }

    public void attach(boolean attach)
    {
        if (attach)
        {
            updatePosition();
        } else
        {
            removePopViewFromRoot();
        }
    }

    public boolean isAttached()
    {
        return mPopView != null && mPopView.getParent() != null && mPopView.getParent() == flRoot;
    }

    private void removePopViewFromRoot()
    {
        if (isAttached())
        {
            flRoot.removeView(mPopView);
        }
    }

    /**
     * 刷新popview的位置
     */
    private void updatePosition()
    {
        if (mPopView == null)
        {
            return;
        }
        if (mPosition == null)
        {
            return;
        }

        saveTargetInfo();
        addToRoot();
        switch (mPosition)
        {
            case TopLeft:
                alignTopLeft();
                break;
            case TopCenter:
                alignTopCenter();
                break;
            case TopRight:
                alignTopRight();
                break;
            case Center:
                alignCenter();
                break;
            case BottomLeft:
                alignBottomLeft();
                break;
            case BottomCenter:
                alignBottomCenter();
                break;
            case BottomRight:
                alignBottomRight();
                break;
            case LeftCenter:
                alignLeftCenter();
                break;
            case RightCenter:
                alignRightCenter();
                break;
            default:
                break;
        }
        updateParamsIfNeed();
    }

    //---------- position start----------

    private void alignTopLeft()
    {
        mGravity = Gravity.TOP | Gravity.LEFT;
        if (getTarget() != null)
        {
            mMarginLeft = mTargetRect.left;
            mMarginTop = mTargetRect.top - mHeightStatusBar;
        } else
        {
            mMarginLeft = 0;
            mMarginTop = 0;
        }
        mMarginLeft += mMarginX;
        mMarginTop += mMarginY;
        mMarginRight = 0;
        mMarginBottom = 0;
    }

    private void alignTopCenter()
    {
        mGravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        if (getTarget() != null)
        {
            mMarginLeft = mTargetRect.left - mWidthScreen / 2 + mTargetRect.width() / 2;
            mMarginTop = mTargetRect.top - mHeightStatusBar;
        } else
        {
            mMarginLeft = 0;
            mMarginTop = 0;
        }
        mMarginLeft += mMarginX;
        mMarginTop += mMarginY;
        mMarginRight = 0;
        mMarginBottom = 0;
    }

    private void alignTopRight()
    {
        mGravity = Gravity.TOP | Gravity.RIGHT;
        if (getTarget() != null)
        {
            mMarginTop = mTargetRect.top - mHeightStatusBar;
            mMarginRight = mWidthScreen - mTargetRect.left - mTargetRect.width();
        } else
        {
            mMarginTop = 0;
            mMarginRight = 0;
        }
        mMarginLeft = 0;
        mMarginTop += mMarginY;
        mMarginRight -= mMarginX;
        mMarginBottom = 0;
    }

    private void alignCenter()
    {
        mGravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        if (getTarget() != null)
        {
            mMarginLeft = mTargetRect.left - mWidthScreen / 2 + mTargetRect.width() / 2;
            mMarginTop = mTargetRect.top - mHeightScreen / 2 + mTargetRect.height() / 2 - mHeightStatusBar / 2;
        } else
        {
            mMarginLeft = 0;
            mMarginTop = 0;
        }
        mMarginLeft += mMarginX;
        mMarginTop += mMarginY;
        mMarginRight = 0;
        mMarginBottom = 0;
    }

    private void alignBottomLeft()
    {
        mGravity = Gravity.BOTTOM | Gravity.LEFT;
        if (getTarget() != null)
        {
            mMarginLeft = mTargetRect.left;
            mMarginBottom = mHeightScreen - mTargetRect.top - mTargetRect.height();
        } else
        {
            mMarginLeft = 0;
            mMarginBottom = 0;
        }
        mMarginLeft += mMarginX;
        mMarginTop = 0;
        mMarginRight = 0;
        mMarginBottom -= mMarginY;
    }

    private void alignBottomCenter()
    {
        mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        if (getTarget() != null)
        {
            mMarginLeft = mTargetRect.left - mWidthScreen / 2 + mTargetRect.width() / 2;
            mMarginBottom = mHeightScreen - mTargetRect.top - mTargetRect.height();
        } else
        {
            mMarginLeft = 0;
            mMarginBottom = 0;
        }
        mMarginLeft += mMarginX;
        mMarginTop = 0;
        mMarginRight = 0;
        mMarginBottom -= mMarginY;
    }

    private void alignBottomRight()
    {
        mGravity = Gravity.BOTTOM | Gravity.RIGHT;
        if (getTarget() != null)
        {
            mMarginRight = mWidthScreen - mTargetRect.left - mTargetRect.width();
            mMarginBottom = mHeightScreen - mTargetRect.top - mTargetRect.height();
        } else
        {
            mMarginRight = 0;
            mMarginBottom = 0;
        }
        mMarginLeft = 0;
        mMarginTop = 0;
        mMarginRight -= mMarginX;
        mMarginBottom -= mMarginY;
    }

    private void alignLeftCenter()
    {
        mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        if (getTarget() != null)
        {
            mMarginLeft = mTargetRect.left;
            mMarginTop = mTargetRect.top - mHeightScreen / 2 + mTargetRect.height() / 2 - mHeightStatusBar / 2;
        } else
        {
            mMarginLeft = 0;
            mMarginTop = 0;
        }
        mMarginLeft += mMarginX;
        mMarginTop += mMarginY;
        mMarginRight = 0;
        mMarginBottom = 0;
    }

    private void alignRightCenter()
    {
        mGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        if (getTarget() != null)
        {
            mMarginTop = mTargetRect.top - mHeightScreen / 2 + mTargetRect.height() / 2 - mHeightStatusBar / 2;
            mMarginRight = mWidthScreen - mTargetRect.left - mTargetRect.width();
        } else
        {
            mMarginTop = 0;
            mMarginRight = 0;
        }
        mMarginLeft = 0;
        mMarginTop += mMarginY;
        mMarginRight -= mMarginX;
        mMarginBottom = 0;
    }

    //---------- position end----------

    private void addToRoot()
    {
        if (mPopView.getParent() != flRoot)
        {
            SDViewUtil.removeView(mPopView);

            FrameLayout.LayoutParams p = null;
            if (mPopView.getLayoutParams() != null)
            {
                p = new FrameLayout.LayoutParams(mPopView.getLayoutParams().width, mPopView.getLayoutParams().height);
            } else
            {
                p = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            }

            flRoot.addView(mPopView, p);
        }
        mParams = (FrameLayout.LayoutParams) mPopView.getLayoutParams();
    }

    private void updateParamsIfNeed()
    {
        boolean needUpdate = false;

        if (mParams.leftMargin != mMarginLeft)
        {
            mParams.leftMargin = mMarginLeft;
            needUpdate = true;
        }
        if (mParams.leftMargin != mMarginLeft)
        {
            mParams.leftMargin = mMarginLeft;
            needUpdate = true;
        }
        if (mParams.topMargin != mMarginTop)
        {
            mParams.topMargin = mMarginTop;
            needUpdate = true;
        }
        if (mParams.rightMargin != mMarginRight)
        {
            mParams.rightMargin = mMarginRight;
            needUpdate = true;
        }
        if (mParams.bottomMargin != mMarginBottom)
        {
            mParams.bottomMargin = mMarginBottom;
            needUpdate = true;
        }
        if (mParams.gravity != mGravity)
        {
            mParams.gravity = mGravity;
            needUpdate = true;
        }

        if (needUpdate)
        {
            mPopView.setLayoutParams(mParams);
        }
    }

    public enum Position
    {
        TopLeft,
        TopCenter,
        TopRight,
        Center,
        BottomLeft,
        BottomCenter,
        BottomRight,
        LeftCenter,
        RightCenter
    }
}

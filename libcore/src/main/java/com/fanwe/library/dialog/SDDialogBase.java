package com.fanwe.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fanwe.library.R;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.drawable.SDDrawableManager;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.SDViewUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

public class SDDialogBase extends Dialog implements View.OnClickListener, OnDismissListener, SDEventObserver
{
    public static final int DEFAULT_PADDING_LEFT_RIGHT = SDViewUtil.getScreenWidthPercent(0.1f);
    public static final int DEFAULT_PADDING_TOP_BOTTOM = SDViewUtil.getScreenWidthPercent(0.1f);

    private View mContentView;
    public SDDrawableManager mDrawableManager = new SDDrawableManager();
    private SDLibraryConfig mLibraryConfig = SDLibrary.getInstance().getConfig();
    private boolean mDismissAfterClick = true;

    public SDDialogBase(Activity activity)
    {
        this(activity, R.style.dialogBaseBlur);
    }

    public SDDialogBase(Activity activity, int theme)
    {
        super(activity, theme);
        setOwnerActivity(activity);
        baseInit();
    }

    private void baseInit()
    {
        SDEventManager.register(this);
        setOnDismissListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v)
    {

    }

    // ------------------getter setter-----------------

    public SDLibraryConfig getLibraryConfig()
    {
        return mLibraryConfig;
    }

    /**
     * 是否点击按钮后自动关闭窗口
     *
     * @return
     */
    public boolean isDismissAfterClick()
    {
        return mDismissAfterClick;
    }

    /**
     * 设置是否点击按钮后自动关闭窗口,默认true(是)
     *
     * @param dismissAfterClick
     * @return
     */
    public SDDialogBase setDismissAfterClick(boolean dismissAfterClick)
    {
        this.mDismissAfterClick = dismissAfterClick;
        return this;
    }

    // ---------------------show gravity

    /**
     * 设置窗口显示的位置
     *
     * @param gravity
     * @return
     */
    public SDDialogBase setGrativity(int gravity)
    {
        getWindow().setGravity(gravity);
        return this;
    }

    public void showTop()
    {
        showTop(true);
    }

    /**
     * 显示在顶部
     *
     * @param anim 是否需要动画
     */
    public void showTop(boolean anim)
    {
        setGrativity(Gravity.TOP);
        if (anim)
        {
            setAnimations(R.style.anim_top_top);
        }
        show();
    }

    public void showBottom()
    {
        showBottom(true);
    }

    /**
     * 显示在底部
     *
     * @param anim 是否需要动画
     */
    public void showBottom(boolean anim)
    {
        setGrativity(Gravity.BOTTOM);
        if (anim)
        {
            setAnimations(R.style.anim_bottom_bottom);
        }
        show();
    }

    public void showCenter()
    {
        setGrativity(Gravity.CENTER);
        show();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        stopDismissRunnable();
        SDEventManager.unregister(this);
    }

    @Override
    public void show()
    {
        try
        {
            if (getOwnerActivity() != null && !getOwnerActivity().isFinishing())
            {
                super.show();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置窗口的显示和隐藏动画
     *
     * @param resId
     */
    public void setAnimations(int resId)
    {
        getWindow().setWindowAnimations(resId);
    }

    // -----------------------padding

    public SDDialogBase paddingTop(int top)
    {
        View view = getWindow().getDecorView();
        view.setPadding(view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
        return this;
    }

    public SDDialogBase paddingBottom(int bottom)
    {
        View view = getWindow().getDecorView();
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottom);
        return this;
    }

    public SDDialogBase paddingLeft(int left)
    {
        View view = getWindow().getDecorView();
        view.setPadding(left, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        return this;
    }

    public SDDialogBase paddingRight(int right)
    {
        View view = getWindow().getDecorView();
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), right, view.getPaddingBottom());
        return this;
    }

    public SDDialogBase paddings(int paddings)
    {
        padding(paddings, paddings, paddings, paddings);
        return this;
    }

    /**
     * 设置窗口上下左右的边距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public SDDialogBase padding(int left, int top, int right, int bottom)
    {
        View view = getWindow().getDecorView();
        view.setPadding(left, top, right, bottom);
        return this;
    }

    protected void dismissAfterClick()
    {
        if (mDismissAfterClick)
        {
            dismiss();
        }
    }

    // ------------------------setContentView

    @Override
    public void setContentView(int layoutId)
    {
        FrameLayout tempLayout = new FrameLayout(getContext());
        View view = LayoutInflater.from(getContext()).inflate(layoutId, tempLayout, false);
        tempLayout.removeView(view);
        setDialogView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view)
    {
        setDialogView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        setDialogView(view, params);
    }

    private SDDialogBase setDialogView(View view, ViewGroup.LayoutParams params)
    {
        saveContentView(view);
        if (params == null)
        {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        super.setContentView(mContentView, params);
        padding(DEFAULT_PADDING_LEFT_RIGHT, DEFAULT_PADDING_TOP_BOTTOM, DEFAULT_PADDING_LEFT_RIGHT, DEFAULT_PADDING_TOP_BOTTOM);
        synchronizeWidth();
        return this;
    }

    /**
     * 保存contentView
     *
     * @param view
     */
    private void saveContentView(View view)
    {
        mContentView = view;
    }

    /**
     * 把contentView的宽度同步到window
     */
    public void synchronizeWidth()
    {
        if (mContentView == null)
        {
            return;
        }
        ViewGroup.LayoutParams p = mContentView.getLayoutParams();
        if (p == null)
        {
            return;
        }

        WindowManager.LayoutParams wParams = getWindow().getAttributes();
        if (wParams.width != p.width)
        {
            wParams.width = p.width;
            getWindow().setAttributes(wParams);
        }
    }

    /**
     * 把contentView的高度同步到window
     */
    public void synchronizeHeight()
    {
        if (mContentView == null)
        {
            return;
        }
        ViewGroup.LayoutParams p = mContentView.getLayoutParams();
        if (p == null)
        {
            return;
        }

        WindowManager.LayoutParams wParams = getWindow().getAttributes();
        if (wParams.height != p.height)
        {
            wParams.height = p.height;
            getWindow().setAttributes(wParams);
        }
    }

    public View getContentView()
    {
        return mContentView;
    }

    /**
     * 设置高度
     *
     * @param width
     * @return
     */
    public SDDialogBase setWidth(int width)
    {
        SDViewUtil.setWidth(mContentView, width);
        synchronizeWidth();
        return this;
    }

    /**
     * 设置宽度
     *
     * @param height
     * @return
     */
    public SDDialogBase setHeight(int height)
    {
        SDViewUtil.setHeight(mContentView, height);
        synchronizeHeight();
        return this;
    }

    /**
     * 设置全屏
     *
     * @return
     */
    public SDDialogBase setFullScreen()
    {
        paddings(0);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {

    }

    public void startDismissRunnable(long delay)
    {
        dismissRunnable.runDelay(delay);
    }

    public void stopDismissRunnable()
    {
        dismissRunnable.removeDelay();
    }

    @Override
    public void dismiss()
    {
        stopDismissRunnable();
        try
        {
            super.dismiss();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private SDDelayRunnable dismissRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            dismiss();
        }
    };

    // ------------------------------background

    /**
     * 边框：top，right 圆角：bottomLeft
     *
     * @return
     */
    public Drawable getBackgroundBottomLeft()
    {
        SDDrawable drawableCancel = new SDDrawable();
        drawableCancel.strokeColor(mLibraryConfig.getColorStroke()).strokeWidth(0, mLibraryConfig.getWidthStroke(), mLibraryConfig.getWidthStroke(), 0)
                .cornerBottomLeft(mLibraryConfig.getCorner());

        SDDrawable drawableCancelPressed = new SDDrawable();
        drawableCancelPressed.strokeColor(mLibraryConfig.getColorStroke()).color(mLibraryConfig.getColorGrayPress())
                .strokeWidth(0, mLibraryConfig.getWidthStroke(), mLibraryConfig.getWidthStroke(), 0).cornerBottomLeft(mLibraryConfig.getCorner());

        return SDDrawable.getStateListDrawable(drawableCancel, null, null, drawableCancelPressed);
    }

    /**
     * 边框：top 圆角：bottomRight
     *
     * @return
     */
    public Drawable getBackgroundBottomRight()
    {
        SDDrawable drawableConfirm = new SDDrawable();
        drawableConfirm.strokeColor(mLibraryConfig.getColorStroke()).strokeWidth(0, mLibraryConfig.getWidthStroke(), 0, 0)
                .cornerBottomRight(mLibraryConfig.getCorner());

        SDDrawable drawableConfirmPressed = new SDDrawable();
        drawableConfirmPressed.strokeColor(mLibraryConfig.getColorStroke()).color(mLibraryConfig.getColorGrayPress())
                .strokeWidth(0, mLibraryConfig.getWidthStroke(), 0, 0).cornerBottomRight(mLibraryConfig.getCorner());

        return SDDrawable.getStateListDrawable(drawableConfirm, null, null, drawableConfirmPressed);
    }

    /**
     * 边框：top 圆角：bottomLeft，bottomRight
     *
     * @return
     */
    public Drawable getBackgroundBottomSingle()
    {
        SDDrawable drawableConfirm = new SDDrawable();
        drawableConfirm.strokeColor(mLibraryConfig.getColorStroke()).strokeWidth(0, mLibraryConfig.getWidthStroke(), 0, 0)
                .corner(0, 0, mLibraryConfig.getCorner(), mLibraryConfig.getCorner());

        SDDrawable drawableConfirmPressed = new SDDrawable();
        drawableConfirmPressed.strokeColor(mLibraryConfig.getColorStroke()).color(mLibraryConfig.getColorGrayPress())
                .strokeWidth(0, mLibraryConfig.getWidthStroke(), 0, 0).corner(0, 0, mLibraryConfig.getCorner(), mLibraryConfig.getCorner());
        return SDDrawable.getStateListDrawable(drawableConfirm, null, null, drawableConfirmPressed);
    }

    @Override
    public void onEventMainThread(SDBaseEvent event)
    {

    }

}

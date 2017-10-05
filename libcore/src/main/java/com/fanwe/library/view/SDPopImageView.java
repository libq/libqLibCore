package com.fanwe.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.utils.SDPoper;
import com.fanwe.library.utils.SDViewUtil;

/**
 * Created by Administrator on 2017/5/11.
 */

public class SDPopImageView extends ImageView
{
    private SDPoper poper = new SDPoper();

    public SDPopImageView(Context context)
    {
        super(context);
        poper.setPopView(this).setDynamicUpdate(false).setPosition(SDPoper.Position.TopLeft);
    }

    public SDPopImageView(View drawingCacheView)
    {
        this(drawingCacheView.getContext());
        setDrawingCacheView(drawingCacheView);
        setTarget(drawingCacheView);
    }

    /**
     * 设置要截图的view
     *
     * @param drawingCacheView
     * @return
     */
    public SDPopImageView setDrawingCacheView(View drawingCacheView)
    {
        Bitmap bitmap = SDViewUtil.createViewBitmap(drawingCacheView);
        setImageBitmap(bitmap);
        return this;
    }

    /**
     * 设置要覆盖的view
     *
     * @param target
     * @return
     */
    public SDPopImageView setTarget(View target)
    {
        poper.setTarget(target);
        return this;
    }

    /**
     * 设置是否pop
     *
     * @param pop
     * @return
     */
    public SDPopImageView pop(boolean pop)
    {
        poper.attach(pop);
        return this;
    }
}

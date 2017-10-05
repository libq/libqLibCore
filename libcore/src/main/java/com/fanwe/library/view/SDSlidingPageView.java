package com.fanwe.library.view;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanwe.library.R;
import com.fanwe.library.adapter.iml.SDSimpleIndicatorAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.customview.SDViewPager;
import com.fanwe.library.looper.ISDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;
import com.fanwe.library.model.SelectableModel;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 可以设置指示器的翻页view
 */
public class SDSlidingPageView extends FrameLayout
{
    public SDSlidingPageView(@NonNull Context context)
    {
        super(context);
        init();
    }

    public SDSlidingPageView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SDSlidingPageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static final long DEFAULT_PLAY_SPAN = 1000 * 7;

    private SDGridViewPager vpg_content;
    private SDRecyclerView view_indicator;

    private ISDLooper mLooperAutoPlay;
    private long mPlaySpan = DEFAULT_PLAY_SPAN;
    private boolean mIsNeedPlay = false;

    private SDSimpleIndicatorAdapter mAdapterIndicator;
    private IndicatorConfig mIndicatorConfig;

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_lib_sliding_page_view, this, true);
        vpg_content = (SDGridViewPager) findViewById(R.id.vpg_content);
        view_indicator = (SDRecyclerView) findViewById(R.id.view_indicator);

        initViewPager();
        initViewPagerIndicator();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager()
    {
        vpg_content.setMeasureMode(SDViewPager.MeasureMode.MAX_CHILD);
        vpg_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                if (mAdapterIndicator != null)
                {
                    mAdapterIndicator.getSelectManager().setSelected(position, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
        vpg_content.setDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                updateIndicatorCount();
            }
        });
        vpg_content.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    stopPlayInternal();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    stopPlayInternal();
                } else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    startPlayInternal();
                }
                return false;
            }
        });
    }

    /**
     * 设置内容为MatchParent
     */
    public void setContentHeightMatchParent()
    {
        getViewPager().setMeasureMode(SDViewPager.MeasureMode.NORMAL);
        SDViewUtil.setHeightMatchParent(getViewPager());
    }

    /**
     * 设置内容为WrapContent
     */
    public void setContentHeightWrapContent()
    {
        getViewPager().setMeasureMode(SDViewPager.MeasureMode.MAX_CHILD);
        SDViewUtil.setHeightWrapContent(getViewPager());
    }

    /**
     * 初始化ViewPager指示器
     */
    private void initViewPagerIndicator()
    {
        view_indicator.setLinearHorizontal();
        setIndicatorAdapter(mAdapterIndicatorDefault);
    }

    /**
     * 获得ViewPager
     *
     * @return
     */
    public SDGridViewPager getViewPager()
    {
        return vpg_content;
    }

    /**
     * 获得一共有几页
     *
     * @return
     */
    private int getPageCount()
    {
        if (getViewPager().getAdapter() != null)
        {
            return getViewPager().getAdapter().getCount();

        } else
        {
            return 0;
        }
    }

    /**
     * 设置指示器适配器
     *
     * @param adapterIndicator
     */
    public void setIndicatorAdapter(SDSimpleIndicatorAdapter adapterIndicator)
    {
        mAdapterIndicator = adapterIndicator;
        view_indicator.setAdapter(mAdapterIndicator);
        updateIndicatorCount();
    }

    /**
     * 获得指示器配置
     *
     * @return
     */
    public IndicatorConfig getIndicatorConfig()
    {
        if (mIndicatorConfig == null)
        {
            mIndicatorConfig = new IndicatorConfig();
        }
        return mIndicatorConfig;
    }

    /**
     * 更新指示器数量
     */
    public void updateIndicatorCount()
    {
        if (mAdapterIndicator != null)
        {
            int pageCount = getPageCount();

            mAdapterIndicator.updateIndicatorCount(pageCount);
            if (pageCount > 0)
            {
                mAdapterIndicator.getSelectManager().setSelected(getViewPager().getCurrentItem(), true);
            }
            LogUtil.i("updateIndicatorCount:" + pageCount);
        }
    }

    //----------auto play start----------

    /**
     * 是否可以轮播
     *
     * @return
     */
    private boolean canPlay()
    {
        if (getViewPager().getAdapter() == null)
        {
            stopPlay();
            return false;
        }
        if (getViewPager().getAdapter().getCount() <= 1)
        {
            stopPlay();
            return false;
        }
        return true;
    }

    /**
     * 开始轮播
     */
    public void startPlay()
    {
        startPlay(DEFAULT_PLAY_SPAN);
    }

    /**
     * 开始轮播
     *
     * @param playSpan 轮播间隔(毫秒)
     */
    public void startPlay(long playSpan)
    {
        if (!canPlay())
        {
            return;
        }
        if (playSpan < 1000)
        {
            playSpan = DEFAULT_PLAY_SPAN;
        }

        this.mPlaySpan = playSpan;
        mIsNeedPlay = true;
        startPlayInternal();
    }

    private void startPlayInternal()
    {
        if (!canPlay())
        {
            return;
        }
        if (!mIsNeedPlay)
        {
            return;
        }
        if (mLooperAutoPlay == null)
        {
            mLooperAutoPlay = new SDSimpleLooper();
        }
        mLooperAutoPlay.start(mPlaySpan, mPlaySpan, new Runnable()
        {
            @Override
            public void run()
            {
                if (canPlay())
                {
                    int current = getViewPager().getCurrentItem();
                    current++;
                    if (current >= getViewPager().getAdapter().getCount())
                    {
                        current = 0;
                    }
                    getViewPager().setCurrentItem(current, true);
                }
            }
        });
    }

    /**
     * 停止轮播
     */
    public void stopPlay()
    {
        stopPlayInternal();
        mIsNeedPlay = false;
    }

    private void stopPlayInternal()
    {
        if (mLooperAutoPlay != null)
        {
            mLooperAutoPlay.stop();
            mLooperAutoPlay = null;
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stopPlay();
    }

    //----------auto play end----------

    /**
     * 默认指示器适配器
     */
    private SDSimpleIndicatorAdapter mAdapterIndicatorDefault = new SDSimpleIndicatorAdapter((Activity) getContext())
    {
        @Override
        public int getLayoutId(ViewGroup parent, int viewType)
        {
            return R.layout.item_lib_imageview_page_indicator;
        }

        @Override
        public void onBindData(SDRecyclerViewHolder<SelectableModel> holder, int position, SelectableModel model)
        {
            ImageView iv_image = holder.get(R.id.iv_image);

            SDViewUtil.setSize(iv_image, getIndicatorConfig().width, getIndicatorConfig().height);

            if (model.isSelected())
            {
                iv_image.setImageResource(getIndicatorConfig().imageResIdSelected);
            } else
            {
                iv_image.setImageResource(getIndicatorConfig().imageResIdNormal);
            }

            holder.itemView.setPadding(getIndicatorConfig().paddingLeft,
                    getIndicatorConfig().paddingTop,
                    getIndicatorConfig().paddingRight,
                    getIndicatorConfig().paddingBottom);
        }
    };

    public static class IndicatorConfig
    {
        public int imageResIdNormal;
        public int imageResIdSelected;
        public int width;
        public int height;
        public int paddingLeft;
        public int paddingTop;
        public int paddingRight;
        public int paddingBottom;

        public IndicatorConfig()
        {
            this.imageResIdNormal = R.drawable.ic_lib_indicator_normal;
            this.imageResIdSelected = R.drawable.ic_lib_indicator_selected;
            this.width = SDViewUtil.dp2px(5);
            this.height = SDViewUtil.dp2px(5);
            this.paddingLeft = 0;
            this.paddingTop = SDViewUtil.dp2px(2);
            this.paddingRight = SDViewUtil.dp2px(5);
            this.paddingBottom = SDViewUtil.dp2px(2);
        }
    }
}

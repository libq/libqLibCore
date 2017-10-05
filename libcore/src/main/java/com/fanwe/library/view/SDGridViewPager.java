package com.fanwe.library.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.R;
import com.fanwe.library.customview.SDViewPager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class SDGridViewPager extends SDViewPager
{
    /**
     * 每页要显示的item数量
     */
    private int mItemCountPerPage = 1;
    /**
     * 每页的数据要按几列展示
     */
    private int mColumnCountPerPage = 1;
    private RecyclerView.Adapter mGridAdapter;
    /**
     * 保存每一页对应的真实adapter
     */
    private HashMap<Integer, RecyclerView.Adapter> mMapRealAdapter = new HashMap<>();
    /**
     * 每一页对应的布局id
     */
    private int mPageLayoutId = R.layout.item_lib_pager_grid;
    /**
     * 横分割线
     */
    private Drawable mDividerDrawableHorizontal;
    /**
     * 竖分割线
     */
    private Drawable mDividerDrawableVertical;
    /**
     * 横分割线左右间距
     */
    private int mDividerPaddingHorizontal;
    /**
     * 竖分割线上下间距
     */
    private int mDividerPaddingVertical;
    /**
     * 每一页View被创建的回调
     */
    private OnPageViewCreatedCallback mOnPageViewCreatedCallback;

    public SDGridViewPager(Context context)
    {
        super(context);
    }

    public SDGridViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 每一页对应的布局id
     *
     * @param pageLayoutId
     */
    public void setPageLayoutId(int pageLayoutId)
    {
        mPageLayoutId = pageLayoutId;
    }

    /**
     * 返回每一页对应的布局id
     *
     * @return
     */
    public int getPageLayoutId()
    {
        return mPageLayoutId;
    }

    /**
     * 设置每一页View被创建的回调
     *
     * @param onPageViewCreatedCallback
     */
    public void setOnPageViewCreatedCallback(OnPageViewCreatedCallback onPageViewCreatedCallback)
    {
        mOnPageViewCreatedCallback = onPageViewCreatedCallback;
    }

    /**
     * 设置横分割线
     *
     * @param dividerDrawableHorizontal
     */
    public void setDividerDrawableHorizontal(Drawable dividerDrawableHorizontal)
    {
        mDividerDrawableHorizontal = dividerDrawableHorizontal;
    }

    /**
     * 设置竖分割线
     *
     * @param dividerDrawableVertical
     */
    public void setDividerDrawableVertical(Drawable dividerDrawableVertical)
    {
        mDividerDrawableVertical = dividerDrawableVertical;
    }

    /**
     * 设置横分割线左右间距
     *
     * @param dividerPaddingHorizontal
     */
    public void setDividerPaddingHorizontal(int dividerPaddingHorizontal)
    {
        mDividerPaddingHorizontal = dividerPaddingHorizontal;
    }

    /**
     * 设置竖分割线上下间距
     *
     * @param dividerPaddingVertical
     */
    public void setDividerPaddingVertical(int dividerPaddingVertical)
    {
        mDividerPaddingVertical = dividerPaddingVertical;
    }

    /**
     * 设置每页要显示的item数量
     *
     * @param itemCountPerPage
     */
    public void setItemCountPerPage(int itemCountPerPage)
    {
        mItemCountPerPage = itemCountPerPage;
    }

    /**
     * 返回每页要显示的item数量
     *
     * @return
     */
    public int getItemCountPerPage()
    {
        return mItemCountPerPage;
    }

    /**
     * 设置每页的数据要按几列展示
     *
     * @param columnCountPerPage
     */
    public void setColumnCountPerPage(int columnCountPerPage)
    {
        mColumnCountPerPage = columnCountPerPage;
    }

    /**
     * 返回每页的数据按几列展示
     *
     * @return
     */
    public int getColumnCountPerPage()
    {
        return mColumnCountPerPage;
    }

    /**
     * 返回总共有几页
     *
     * @return
     */
    public int getPageCount()
    {
        if (mGridAdapter != null)
        {
            int left = mGridAdapter.getItemCount() % getItemCountPerPage();
            int page = mGridAdapter.getItemCount() / getItemCountPerPage();
            if (left == 0)
            {
                return page;
            } else
            {
                return page + 1;
            }
        } else
        {
            return 0;
        }
    }

    /**
     * 返回该页有几个item
     *
     * @param pageIndex
     * @return
     */
    public int getPageItemCount(int pageIndex)
    {
        int pageCount = getPageCount();
        if (pageCount <= 0)
        {
            return 0;
        }
        if (pageIndex < 0 || pageIndex >= pageCount)
        {
            return 0;
        }

        int start = pageIndex * getItemCountPerPage();
        int end = start + getItemCountPerPage() - 1;
        if (end < mGridAdapter.getItemCount())
        {
            return getItemCountPerPage();
        } else
        {
            return mGridAdapter.getItemCount() - start;
        }
    }

    /**
     * 返回itemPosition在第几页
     *
     * @param itemPosition
     * @return
     */
    public int indexOfPage(int itemPosition)
    {
        if (itemPosition >= 0 && mGridAdapter != null && itemPosition < mGridAdapter.getItemCount())
        {
            return itemPosition / getItemCountPerPage();
        } else
        {
            return -1;
        }
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setGridAdapter(RecyclerView.Adapter adapter)
    {
        if (mGridAdapter != null)
        {
            mGridAdapter.unregisterAdapterDataObserver(mDataSetObserver);
        }
        mGridAdapter = adapter;
        if (adapter != null)
        {
            adapter.registerAdapterDataObserver(mDataSetObserver);
        }
        dealAdapter();
    }

    private RecyclerView.AdapterDataObserver mDataSetObserver = new RecyclerView.AdapterDataObserver()
    {
        @Override
        public void onChanged()
        {
            dealAdapter();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount)
        {
            onItemRangeChanged(positionStart, itemCount, null);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload)
        {
            int positionEnd = positionStart + itemCount - 1;

            int pageStart = indexOfPage(positionStart);
            int pageEnd = indexOfPage(positionEnd);
            if (pageStart == pageEnd)
            {
                RecyclerView.Adapter adapterReal = mMapRealAdapter.get(pageStart);
                if (adapterReal != null)
                {
                    int realPositionStart = positionStart % getItemCountPerPage();
                    adapterReal.notifyItemRangeChanged(realPositionStart, itemCount, payload);
                }
            } else
            {
                dealAdapter();
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
        {
            dealAdapter();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount)
        {
            dealAdapter();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount)
        {
            dealAdapter();
        }
    };

    private void dealAdapter()
    {
        if (getAdapter() != mDefaultPagerAdapter)
        {
            setAdapter(mDefaultPagerAdapter);
        } else
        {
            mDefaultPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter)
    {
        mMapRealAdapter.clear();
        super.setAdapter(adapter);
    }

    /**
     * ViewPager默认适配器
     */
    private PagerAdapter mDefaultPagerAdapter = new PagerAdapter()
    {
        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public int getCount()
        {
            return getPageCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, final int position)
        {
            final int startPosition = position * getItemCountPerPage();

            View pageView = LayoutInflater.from(container.getContext()).inflate(getPageLayoutId(), container, false);
            SDRecyclerView recycler_view = (SDRecyclerView) pageView.findViewById(R.id.recycler_view);

            recycler_view.setGridVertical(getColumnCountPerPage());
            if (mDividerDrawableHorizontal != null)
            {
                recycler_view.addDividerHorizontal(mDividerDrawableHorizontal, mDividerPaddingHorizontal);
            }
            if (mDividerDrawableVertical != null)
            {
                recycler_view.addDividerVertical(mDividerDrawableVertical, mDividerPaddingVertical);
            }

            RecyclerView.Adapter pageRecyclerAdapter = new RecyclerView.Adapter()
            {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
                {
                    return mGridAdapter.onCreateViewHolder(parent, viewType);
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
                {
                    mGridAdapter.onBindViewHolder(holder, startPosition + position);
                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads)
                {
                    mGridAdapter.onBindViewHolder(holder, startPosition + position, payloads);
                }

                @Override
                public int getItemCount()
                {
                    return getPageItemCount(position);
                }
            };
            mMapRealAdapter.put(position, pageRecyclerAdapter);
            recycler_view.setAdapter(pageRecyclerAdapter);
            container.addView(pageView);
            if (mOnPageViewCreatedCallback != null)
            {
                mOnPageViewCreatedCallback.onPageViewCreated(pageView);
            }
            return pageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            mMapRealAdapter.remove(position);
            container.removeView((View) object);
        }
    };

    public interface OnPageViewCreatedCallback
    {
        /**
         * 每一页View被创建的回调
         *
         * @param pageView
         */
        void onPageViewCreated(View pageView);
    }
}

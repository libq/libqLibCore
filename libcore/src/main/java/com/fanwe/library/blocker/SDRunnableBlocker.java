package com.fanwe.library.blocker;

import com.fanwe.library.model.SDDelayRunnable;

/**
 * 实现某个时段内重复Runnable的拦截，当拦截次数超过最大拦截次数后则执行Runnable<br>
 * 在界面销毁的时候需要调用onDestroy()
 */
public class SDRunnableBlocker
{
    /**
     * 拦截间隔
     */
    private long mBlockDuration = 0;
    /**
     * 最大拦截次数
     */
    private int mMaxBlockCount = 0;
    /**
     * 当前已拦截次数
     */
    private int mBlockCount = 0;
    /**
     * 需要拦截的Runnable
     */
    private Runnable mBlockRunnable;

    /**
     * 设置拦截间隔
     *
     * @param blockDuration
     * @return
     */
    public synchronized SDRunnableBlocker setBlockDuration(long blockDuration)
    {
        mBlockDuration = blockDuration;
        return this;
    }

    /**
     * 设置最大拦截次数
     *
     * @param maxBlockCount
     * @return
     */
    public synchronized SDRunnableBlocker setMaxBlockCount(int maxBlockCount)
    {
        mMaxBlockCount = maxBlockCount;
        return this;
    }

    /**
     * 返回拦截间隔
     *
     * @return
     */
    public synchronized long getBlockDuration()
    {
        return mBlockDuration;
    }

    /**
     * 返回最大拦截次数
     *
     * @return
     */
    public synchronized int getMaxBlockCount()
    {
        return mMaxBlockCount;
    }

    /**
     * 返回已拦截次数
     *
     * @return
     */
    public synchronized int getBlockCount()
    {
        return mBlockCount;
    }

    /**
     * 执行Runnable
     *
     * @return true-立即执行成功，false-拦截掉
     */
    public synchronized boolean post(Runnable runnable)
    {
        mBlockRunnable = runnable;
        if (mBlockRunnable != null)
        {
            if (mMaxBlockCount > 0)
            {
                mBlockCount++;
                if (mBlockCount > mMaxBlockCount)
                {
                    // 大于最大拦截次数，立即执行Runnable
                    mDelayRunnable.runImmediately();
                    return true;
                } else
                {
                    mDelayRunnable.runDelayOrImmediately(mBlockDuration);
                    return false;
                }
            } else
            {
                // 没有设置最大拦截次数，立即执行Runnable
                mDelayRunnable.runImmediately();
                return true;
            }
        } else
        {
            return false;
        }
    }

    private SDDelayRunnable mDelayRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            synchronized (SDRunnableBlocker.this)
            {
                if (mBlockRunnable != null)
                {
                    resetBlockCount();
                    mBlockRunnable.run();
                }
            }
        }
    };

    /**
     * 重置拦截次数
     */
    private void resetBlockCount()
    {
        mBlockCount = 0;
    }

    /**
     * 销毁
     */
    public synchronized void onDestroy()
    {
        mDelayRunnable.removeDelay();
        mBlockRunnable = null;
    }
}

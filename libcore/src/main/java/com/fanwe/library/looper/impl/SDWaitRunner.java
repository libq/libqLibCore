package com.fanwe.library.looper.impl;

import com.fanwe.library.looper.ISDTimeouter;

/**
 * 等待执行类，等待Condition对象返回true的时候，执行设置的Runnable对象。可以设置等待超时，默认等待10秒，超时后执行超时Runnable
 */
public class SDWaitRunner implements ISDTimeouter
{
    /**
     * 默认检测条件是否成立间隔
     */
    public static final long DEFAULT_PERIOD = 300;

    private SDSimpleTimeoutLooper mLooper = new SDSimpleTimeoutLooper();
    private Condition mCondition;
    private Runnable mRunnable;

    public SDWaitRunner run(Runnable runnable)
    {
        this.mRunnable = runnable;
        return this;
    }

    /**
     * 设置执行条件
     *
     * @param condition
     * @return
     */
    public SDWaitRunner condition(Condition condition)
    {
        this.mCondition = condition;
        return this;
    }

    /**
     * 开始等待执行
     *
     * @return
     */
    public SDWaitRunner startWait()
    {
        startWait(DEFAULT_PERIOD);
        return this;
    }

    /**
     * 开始等待执行
     *
     * @param period 检测条件是否成立的触发间隔
     * @return
     */
    public SDWaitRunner startWait(long period)
    {
        if (getTimeout() == 0)
        {
            // 如果超时为0，设置一个默认超时
            setTimeout(DEFAULT_TIMEOUT);
        }
        startTimeout();
        mLooper.start(period, mLoopRunnable);
        return this;
    }

    private Runnable mLoopRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            if (mCondition != null)
            {
                if (mCondition.canRun())
                {
                    runRunnable();
                    stopWait();
                } else
                {
                    // wait...
                }
            } else
            {
                stopWait();
            }
        }
    };

    private void runRunnable()
    {
        if (mRunnable != null)
        {
            mRunnable.run();
        }
    }

    /**
     * 停止等待
     *
     * @return
     */
    public SDWaitRunner stopWait()
    {
        mLooper.stop();
        return this;
    }

    @Override
    public long getTimeout()
    {
        return mLooper.getTimeout();
    }

    @Override
    public boolean isTimeout()
    {
        return mLooper.isTimeout();
    }

    @Override
    public SDWaitRunner setTimeoutRunnable(Runnable timeoutRunnable)
    {
        mLooper.setTimeoutRunnable(timeoutRunnable);
        return this;
    }

    @Override
    public SDWaitRunner runTimeoutRunnable()
    {
        mLooper.runTimeoutRunnable();
        return this;
    }

    @Override
    public SDWaitRunner setTimeout(long timeout)
    {
        mLooper.setTimeout(timeout);
        return this;
    }

    @Override
    public SDWaitRunner startTimeout()
    {
        mLooper.startTimeout();
        return this;
    }

    @Override
    public SDWaitRunner stopTimeout()
    {
        mLooper.stopTimeout();
        return this;
    }

    public interface Condition
    {
        /**
         * 是否可以执行Runnable
         *
         * @return true-可以执行
         */
        boolean canRun();
    }

}

package com.fanwe.library.utils;

import com.fanwe.library.looper.ISDLooper;
import com.fanwe.library.looper.impl.SDSimpleLooper;

/**
 * Created by Administrator on 2016/7/18.
 */
public class SDCountDownTimer
{
    private ISDLooper looper = new SDSimpleLooper();

    private long time;
    private long interval;
    private long currentTime;

    public void start(long time, final long interval, final SDCountDownTimerListener listener)
    {
        if (listener != null)
        {
            stop();

            this.time = time;
            this.interval = interval;
            this.currentTime = time;

            looper.start(interval, new Runnable()
            {
                @Override
                public void run()
                {
                    if (currentTime <= 0)
                    {
                        listener.onFinish();
                    } else
                    {
                        listener.onTick(currentTime);
                    }
                    currentTime -= interval;
                }
            });
        }
    }

    public void stop()
    {
        looper.stop();
    }

    public interface SDCountDownTimerListener
    {
        void onTick(long leftTime);

        void onFinish();
    }

}

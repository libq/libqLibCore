package com.fanwe.library;

import android.app.Application;

import com.fanwe.library.common.SDCookieManager;
import com.fanwe.library.config.SDConfig;
import com.fanwe.library.config.SDLibraryConfig;
import com.fanwe.library.utils.SDCache;

public class SDLibrary
{

    private static SDLibrary sInstance;

    private Application mApplication;
    private SDLibraryConfig mConfig;

    public SDLibraryConfig getConfig()
    {
        return mConfig;
    }

    public void setConfig(SDLibraryConfig config)
    {
        if (config != null)
        {
            this.mConfig = config;
        }
    }

    private SDLibrary()
    {
        mConfig = new SDLibraryConfig();
    }

    public Application getApplication()
    {
        return mApplication;
    }

    public void init(Application application)
    {
        this.mApplication = application;
        initDefaultConfig();
        SDConfig.getInstance().init(application);
        SDCookieManager.getInstance().init(application);
        SDCache.init(application);
    }

    private void initDefaultConfig()
    {
        int main_color = mApplication.getResources().getColor(R.color.main_color);
        int main_color_press = mApplication.getResources().getColor(R.color.main_color_press);

        int bg_title_bar = mApplication.getResources().getColor(R.color.bg_title_bar);
        int bg_title_bar_pressed = mApplication.getResources().getColor(R.color.bg_title_bar_pressed);

        int stroke = mApplication.getResources().getColor(R.color.stroke);
        int gray_press = mApplication.getResources().getColor(R.color.gray_press);

        int text_title_bar = mApplication.getResources().getColor(R.color.text_title_bar);

        int height_title_bar = mApplication.getResources().getDimensionPixelOffset(R.dimen.height_title_bar);
        int corner = mApplication.getResources().getDimensionPixelOffset(R.dimen.corner);
        int width_stroke = mApplication.getResources().getDimensionPixelOffset(R.dimen.width_stroke);

        mConfig.setColorMain(main_color);
        mConfig.setColorMainPress(main_color_press);
        mConfig.setColorGrayPress(gray_press);
        mConfig.setColorTitleBarBackground(bg_title_bar);
        mConfig.setColorTitleBarItemPressed(bg_title_bar_pressed);
        mConfig.setColorTitleText(text_title_bar);
        mConfig.setHeightTitleBar(height_title_bar);
        mConfig.setColorStroke(stroke);
        mConfig.setWidthStroke(width_stroke);
        mConfig.setCorner(corner);
    }

    public static SDLibrary getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new SDLibrary();
        }
        return sInstance;
    }

}

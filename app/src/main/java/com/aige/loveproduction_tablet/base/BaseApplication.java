package com.aige.loveproduction_tablet.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aige.loveproduction_tablet.manager.ActivityManager;
import com.aige.loveproduction_tablet.util.SharedPreferencesUtils;
import com.tencent.smtt.sdk.QbSdk;

public class BaseApplication extends Application {

    private static boolean mOnlyWifi = false;
    //记录是否加载完成
    private static boolean mInit = false;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getBaseContext();
        //Activity栈管理初始化
        ActivityManager.getInstance().init(this);
        //加载腾讯tbs内核
        if(SharedPreferencesUtils.getBoolean(mContext,"X5","initStatus")) {
            QbSdk.initX5Environment(this,null);
        }
    }
}

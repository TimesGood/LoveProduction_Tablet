package com.aige.loveproduction_tablet.action;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.aige.loveproduction_tablet.mvp.ui.activity.QrCodeActivity;
import com.aige.loveproduction_tablet.mvp.ui.fragment.ApplyFragment;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Activity相关意图
 */
public interface ActivityAction {
    /**
     * 获取Context对象
     * @return 上下文对象
     */
    Context getContext();
    /**
     * 获取Activity对象
     * default关键字表示定义一个方法，在java8引入，使得可以在接口中使用方法
     * @return
     */
    default Activity getActivity() {
        Context context = getContext();
        do {
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();

            } else {
                return null;
            }
        } while (context != null);
        return null;
    }

    /**
     * 跳转 Activity简化
     * @param clazz
     */
    default void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getContext(),clazz));
    }

    /**
     * 跳转 Activity
     * addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
     *  1. 新活动会成为历史栈中的新任务（一组活动）的开始。
     *  2. 通常用于具有"launcher"行为的活动：让用户完成一系列事情，完全独立于之前的活动。
     *  3. 如果新活动已存在于一个为它运行的任务中，那么不会启动，只会把该任务移到屏幕最前。
     *  4. 如果新活动要返回result给启动自己的活动，就不能用这个flag。
     * @param intent
     */
    default void startActivity(Intent intent) {
        if(getContext() instanceof Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        getContext().startActivity(intent);
    }

    /**
     * 在Fragment调用扫描二维码
     */
    default void startFragmentCapture(Fragment fragment) {
        IntentIntegrator.forSupportFragment(fragment)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                .setCaptureActivity(QrCodeActivity.class)//自定义扫码界面
                .initiateScan();// 初始化扫码
    }
    default void startActivityCapture() {
        new IntentIntegrator(getActivity())
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                .setCaptureActivity(QrCodeActivity.class)//自定义扫码界面
                .initiateScan();// 初始化扫码
    }

}
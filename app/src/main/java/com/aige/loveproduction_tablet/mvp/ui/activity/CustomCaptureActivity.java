//package com.aige.loveproduction_tablet.mvp.ui.activity;
//
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.aige.loveproduction_tablet.R;
//import com.uuzuche.lib_zxing.activity.CaptureFragment;
//import com.uuzuche.lib_zxing.activity.CodeUtils;
//
///**
// * 二维码扫描定制界面
// */
//public class CustomCaptureActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_custom_capture);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
//        /**
//         * 执行扫面Fragment的初始化操作
//         */
//        CaptureFragment captureFragment = new CaptureFragment();
//        // 为二维码扫描界面设置定制化界面
//        CodeUtils.setFragmentArgs(captureFragment, R.layout.view_qr);
//
//        captureFragment.setAnalyzeCallback(analyzeCallback);
//        /**
//         * 替换我们的扫描控件
//         */ getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
//    }
//    /**
//     * 二维码解析回调函数
//     */
//    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
//        @Override
//        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            CustomCaptureActivity.this.setResult(RESULT_OK, resultIntent);
//            CustomCaptureActivity.this.finish();
//        }
//
//        @Override
//        public void onAnalyzeFailed() {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//            bundle.putString(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            CustomCaptureActivity.this.setResult(RESULT_OK, resultIntent);
//            CustomCaptureActivity.this.finish();
//        }
//    };
//
//}
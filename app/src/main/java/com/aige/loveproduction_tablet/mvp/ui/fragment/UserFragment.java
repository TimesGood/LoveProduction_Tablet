package com.aige.loveproduction_tablet.mvp.ui.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.base.BaseDialog;
import com.aige.loveproduction_tablet.base.BaseFragment;
import com.aige.loveproduction_tablet.dialog.DownloadDialog;
import com.aige.loveproduction_tablet.dialog.MessageDialog;
import com.aige.loveproduction_tablet.mvp.contract.UserContract;
import com.aige.loveproduction_tablet.mvp.presenter.UserPresender;
import com.aige.loveproduction_tablet.mvp.ui.activity.LoginActivity;
import com.aige.loveproduction_tablet.util.SharedPreferencesUtils;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.HashMap;

public class UserFragment extends BaseFragment<UserPresender, UserContract.View>{
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView(View view) {
        setOnClickListener(R.id.about_us_layout,R.id.logout_layout,R.id.download_layout);
        String userName = SharedPreferencesUtils.getValue(mActivity,"loginInfo","userName");
        ((TextView)findViewById(R.id.user_text)).setText(userName);
        try {
            PackageInfo info = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(),0);//获取程序包信息，获取程序包有时获取不到，所以要异常处理，
            ((TextView)findViewById(R.id.tv_version)).setText("V"+info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            ((TextView)findViewById(R.id.tv_version)).setText("V");//获取不到程序包信息时，直接显示V
        }
    }

    @Override
    protected UserPresender createPresenter() {
        return new UserPresender();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.logout_layout) {
            SharedPreferencesUtils.saveSetting(mActivity,"loginInfo","isLogin",false);
            startActivity(LoginActivity.class);
            finish();
        }else if(id == R.id.download_layout) {
            if(!SharedPreferencesUtils.getBoolean(mActivity,"X5","initStatus")) {
                new MessageDialog.Builder(mActivity)
                        .setTitle("下载通知")
                        .setMessage("X5内核未加载完成,需下载约30M数据，请在网络良好下进行下载")
                        .setConfirm("立即下载")
                        .setListener(dialog -> initX5())
                        .show();
            }else{
                showToast("已加载X5内核");
            }
        }

    }
    private void initX5() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setDownloadWithoutWifi(true);
        QbSdk.disableAutoCreateX5Webview();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //TbsDownloader.startDownload(mActivity);
        DownloadDialog.Builder dialog = new DownloadDialog.Builder(mActivity).setTitle("下载X5内核中...").setMessage("请勿中断下载").setConfirm("确定").setCancel("取消")
                .setListener(new DownloadDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                    }
                }).setCanceledOnTouchOutside(false);
        dialog.show();
        //SharedPreferencesUtils.saveSetting(mActivity,"X5","download_status",true);
        QbSdk.reset(mActivity);//清除缓存
        QbSdk.setTbsListener(
                new TbsListener() {
                    @Override
                    public void onDownloadFinish(int progress) {
                        Log.d("QbSdk", "onDownloadFinish -->下载X5内核完成：" + progress);
                        //if(progress >= 100) SharedPreferencesUtils.saveSetting(mActivity,"X5","download_status",true);
                    }
                    @Override
                    public void onInstallFinish(int progress) {
                        Log.d("QbSdk", "onInstallFinish -->安装X5内核进度：" + progress);
                    }
                    @Override
                    public void onDownloadProgress(int progress) {
                        dialog.setProgress(progress);
                        Log.d("QbSdk", "onDownloadProgress -->下载X5内核进度：" + progress);
                    }

                });

        //x5内核初始化接口
        QbSdk.initX5Environment(mActivity, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，此接口回调并表示已经加载起来了x5
                showToast("X5内核加载完成");
                SharedPreferencesUtils.saveSetting(mActivity,"X5" ,"initStatus",true);

            }
            @Override
            public void onViewInitFinished(boolean b) {
                if(b) {
                    Log.d("QbSdk", " x5 内核加载成功 ");
                    Log.d("QbSdk", " x5 内核版本号:"+QbSdk.getTbsVersion(mActivity));
                }else{
                    Log.d("QbSdk", " x5 内核加载失败，启用系统内核 ");
                    showToast("X5内核加载失败，弃用系统内核");
                    SharedPreferencesUtils.saveSetting(mActivity,"X5" ,"initStatus",false);
                }
                dialog.dismiss();
            }
        });
    }
}

package com.aige.loveproduction_tablet.mvp.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.base.BaseDialog;
import com.aige.loveproduction_tablet.base.BaseFragment;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.mvp.contract.HomeContract;
import com.aige.loveproduction_tablet.mvp.ui.activity.QrCodeActivity;
import com.aige.loveproduction_tablet.mvp.ui.dialog.LoadingDialog;
import com.aige.loveproduction_tablet.mvp.ui.dialog.MessageDialog;
import com.aige.loveproduction_tablet.listener.DownloadListener;
import com.aige.loveproduction_tablet.mvp.presenter.HomePresenter;
import com.aige.loveproduction_tablet.permission.Permission;
import com.aige.loveproduction_tablet.util.FileViewerUtils;
import com.aige.loveproduction_tablet.util.IntentUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;


public class HomeFragment extends BaseFragment<HomePresenter, HomeContract.View> implements HomeContract.View {
    private FrameLayout left_fragment, right_fragment;
    private ListView left_recyclerview;
    private ViewGroup errorHandleLayout;
    private TbsReaderView mTbsReaderView;
    private List<DownloadBean> mBean;
    private ProgressBar download_bar;
    private TextView download_text, find_edit;
    private RelativeLayout download_layout;
    private LoadingDialog.Builder dialog;
    //打开文件目录
    private File mFile;

    private final String[] permission_group = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        download_layout = findViewById(R.id.download_layout);
        download_text = findViewById(R.id.download_text);
        download_bar = findViewById(R.id.download_bar);
        left_fragment = findViewById(R.id.left_fragment);
        right_fragment = findViewById(R.id.right_fragment);
        left_recyclerview = findViewById(R.id.left_recyclerview);
        errorHandleLayout = findViewById(R.id.ll_error_handle);
        find_edit = findViewById(R.id.find_edit);
        setOnClickListener(R.id.image_camera, R.id.find_img, R.id.btn_retry_with_tbs, R.id.btn_view_with_other_app);
        find_edit.requestFocus();
        find_edit.setOnEditorActionListener((v, actionId, event) -> {
            //这里有时候莫名其妙出现空指针
            if (event == null) return true;

            if (v.getText() != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                requestReady(v.getText().toString());
            }
            return true;
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            left_recyclerview.setNestedScrollingEnabled(true);
        }
    }

    //请求前的操作
    private void requestReady(String orderId) {
        find_edit.setText("");
        //每一次搜索请求清空之前下载的文件
        FileViewerUtils.deleteDir(new File(mActivity.getExternalCacheDir() + "/DownloadFile"));
        if (orderId.isEmpty()) {
            showToast("请输入销售单号");
        } else {
            mPresenter.getDownloadUrl(orderId);
        }
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    public void showLoading() {
        dialog = new LoadingDialog.Builder(mActivity);
        dialog.setCanceledOnTouchOutside(false)
                .setTitle("加载中...")
                .setBackgroundDimEnabled(false)
                .setCancel("")
                .setConfirm("取消")
                .setListener(new LoadingDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        mPresenter.dispose();
                    }
                })
                .show();
    }

    @Override
    public void hideLoading() {
        dialog.dismiss();
    }

    @Override
    public void onError(String message) {
        showToast(message);
        dialog.dismiss();
    }

    @Override
    public void showDownload() {

    }

    @Override
    public void hideDownload() {

    }

    @Override
    public void onErrorDownload(String msg) {

    }

    @Override
    public void onGetDownloadUrlSuccess(List<DownloadBean> bean) {
        mBean = bean;
        String[] downloadStr = new String[bean.size()];
        for (int i = 0; i < bean.size(); i++) {
            downloadStr[i] = bean.get(i).getFileName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.text_item, downloadStr);
        left_recyclerview.setAdapter(adapter);
        left_recyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                permission.applyPermission( permission_group, new Permission.ApplyListener() {
                    @Override
                    public void apply(String[] permission) {
                        HomeFragment.this.requestPermissions(permission, 1);
                    }
                    @Override
                    public void applySuccess() {

                        TextView v = (TextView) view;
                        if (mTbsReaderView != null) mTbsReaderView.onStop();
                        //文件展示，与adapter类似
                        mTbsReaderView = new TbsReaderView(mActivity, null);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                        );
                        mTbsReaderView.setLayoutParams(layoutParams);
                        right_fragment.addView(mTbsReaderView);
                        //判断缓存目录中是否存在这个文件,存在直接打开
                        mFile = new File(mActivity.getExternalCacheDir() + "/DownloadFile/" + v.getText());
                        if (mFile.exists()) {
                            displayFile(mFile);
                        } else {
                            //不存在就下载
                            DownloadBean downloadBean = mBean.get(position);
                            mPresenter.getFile(downloadBean.getFileUrl());
                        }
                    }
                });
            }
        });
        left_recyclerview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(mTbsReaderView != null) mTbsReaderView.onStop();
                //文件展示，与adapter类似
                mTbsReaderView = new TbsReaderView(mActivity, null);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                );
                mTbsReaderView.setLayoutParams(layoutParams);
                right_fragment.addView(mTbsReaderView);
                TextView v = (TextView) view;
                mFile = new File(mActivity.getExternalCacheDir() + "/DownloadFile/" + v.getText());
                if (mFile.exists()) {
                    fullScreenOpenFile(mFile);
                } else {
                    //不存在就下载
                    DownloadBean downloadBean = mBean.get(position);
                    mPresenter.getFile(downloadBean.getFileUrl());
                }
                return true;
            }
        });

    }

    //下载
    @Override
    public void onGetFileSuccess(ResponseBody body) {
        FileViewerUtils.createOrExistsDir(FileViewerUtils.getFilePath(mFile));
        //下载监听
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeFile2Disk(body, mFile);
            }
        }).start();

    }

    //打开文件显示文件
    private void displayFile(File file) {
        download_layout.setVisibility(View.GONE);
        if (file == null) {
            showToast("无法识别的文件路径");
            return;
        }
        //需要两个值，一个是要打开的文件目录，一个是临时文件目录
        Bundle bundle = new Bundle();
        bundle.putString("filePath", file.getAbsolutePath());
        //创建临时缓存数据
        String dirPath = mActivity.getExternalCacheDir() + "/tempPath";
        if (FileViewerUtils.createOrExistsDir(dirPath)) {
            bundle.putString("tempPath", dirPath);
        } else {
            showToast("创建缓存文件夹失败，请确认是否开启读写手机存储权限");
        }
        //获取文件后缀名
        String extension = FileViewerUtils.getExtension(file).substring(1);
        //preOpen 需要文件后缀名 用以判断是否支持打开该文件
        boolean result = mTbsReaderView.preOpen(extension, true);
        if (result) {
            //打开成功
            mTbsReaderView.openFile(bundle);
            mTbsReaderView.setVisibility(View.VISIBLE);
            errorHandleLayout.setVisibility(View.GONE);

        } else {
            //打开失败
            right_fragment.removeView(mTbsReaderView);
            mTbsReaderView.setVisibility(View.GONE);
            errorHandleLayout.setVisibility(View.VISIBLE);
        }
    }
    //用Qbsdk打开文件浏览
    private void fullScreenOpenFile(File file) {
        HashMap<String,String> map = new HashMap<>();
        map.put("style", "2");
        map.put("local", "true");
        QbSdk.openFileReader(mActivity,file.getAbsolutePath(),map,null);
    }

    //下载文件并显示进度
    private void writeFile2Disk(ResponseBody response, File file) {
        //downloadListener.onStart();
        long currentLength = 0;
        OutputStream os = null;
        if (response == null) {
            //downloadListener.onFailure("资源错误！");
            return;
        }
        InputStream is = response.byteStream();
        //获取流总长度
        long totalLength = response.contentLength();
        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                //downloadListener.onProgress((int) (100 * currentLength / totalLength));
                if ((int) (100 * currentLength / totalLength) == 100) {
                    //downloadListener.onFinish(file);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayFile(file);
                        }
                    });
                }
            }
        } catch (FileNotFoundException e) {
            //downloadListener.onFailure("未找到文件！");
            e.printStackTrace();
        } catch (IOException e) {
            //downloadListener.onFailure("IO错误！");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.find_img) {
            requestReady(find_edit.getText().toString());
        } else if (id == R.id.image_camera) {
            permission.applyPermission(new String[]{Manifest.permission.CAMERA}, new Permission.ApplyListener() {
                @Override
                public void apply(String[] permission) {
                    HomeFragment.this.requestPermissions(permission, 1);
                }

                @Override
                public void applySuccess() {
                    startFragmentCapture(HomeFragment.this);

                }
            });
        } else if (id == R.id.btn_retry_with_tbs) {
            //重试打开文件
            displayFile(mFile);
        } else if (id == R.id.btn_view_with_other_app) {
            //使用其他应用打开
            FileViewerUtils.viewFile4_4(v.getContext(), mFile);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFragmentCapture(this);
            } else {
                new MessageDialog.Builder(mActivity)
                        .setTitle("温馨提醒")
                        .setMessage("权限拒绝后某些功能将不能使用，为了使用完整功能请打开"+permission.getPermissionHint(Arrays.asList(permissions)))
                        .setConfirm("去开启")
                        .setListener(dialog -> IntentUtils.gotoPermission(mActivity))
                        .show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    //扫码失败
                    showToast("解析二维码失败");
                } else {
                    String result = intentResult.getContents();//返回值
                    requestReady(result);
                }

            }

    }

    @Override
    public void onDestroy() {
        if (mTbsReaderView != null) mTbsReaderView.onStop();
        mActivity = null;
        super.onDestroy();
    }
}

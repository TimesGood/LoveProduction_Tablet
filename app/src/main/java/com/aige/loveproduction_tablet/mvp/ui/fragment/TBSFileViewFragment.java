//package com.aige.loveproduction_tablet.ui.fragment;
//
//import android.os.Bundle;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.aige.loveproduction_tablet.R;
//import com.aige.loveproduction_tablet.base.BaseFragment;
//import com.aige.loveproduction_tablet.base.BasePresenter;
//import com.aige.loveproduction_tablet.util.FileViewerUtils;
//import com.tencent.smtt.sdk.TbsReaderView;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.io.File;
//
//
//public class TBSFileViewFragment extends BaseFragment {
//    private View view;
//    private ViewGroup errorHandleLayout;
//    private TbsReaderView mTbsReaderView;
//    private String filePath;
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_tbs_file_view_layout;
//    }
//
//    @Override
//    protected void initView(View view) {
//        //错误界面
//        errorHandleLayout = (ViewGroup) findViewById(R.id.ll_error_handle);
//        initErrorHandleLayout(errorHandleLayout);
//        filePath = handleIntent();
//        if (TextUtils.isEmpty(filePath) || !new File(filePath).isFile()) {
//            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        //文件展示，与adapter类似
//        mTbsReaderView = new TbsReaderView(this, this);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT
//        );
//        mTbsReaderView.setLayoutParams(layoutParams);
//        rootViewParent.addView(mTbsReaderView);
//        displayFile(filePath);
//
//    }
//    private void initErrorHandleLayout(ViewGroup errorHandleLayout) {
//        findViewById(R.id.btn_retry_with_tbs).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //重试，重新加载文件
//                displayFile(filePath);
//            }
//        });
//        findViewById(R.id.btn_view_with_other_app).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //使用其他应用打开文件
//                FileViewerUtils.viewFile4_4(v.getContext(), filePath);
//            }
//        });
//    }
//    //打开文件显示文件
//    private void displayFile(String fileAbsPath) {
//        Bundle bundle = new Bundle();
//        bundle.putString("filePath", fileAbsPath);
//        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
//        // preOpen 需要文件后缀名 用以判断是否支持打开该文件
//        boolean result = mTbsReaderView.preOpen(parseFormat(fileAbsPath), true);
//        if (result) {
//            mTbsReaderView.openFile(bundle);
//            mTbsReaderView.setVisibility(View.VISIBLE);
//            errorHandleLayout.setVisibility(View.GONE);
//        } else {
//            mTbsReaderView.setVisibility(View.GONE);
//            errorHandleLayout.setVisibility(View.VISIBLE);
//        }
//    }
//    //获取文件后缀名
//    private String parseFormat(String fileName) {
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
//
//    @Override
//    protected BasePresenter createPresenter() {
//        return null;
//    }
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//
//    @Override
//    public void onError(String message) {
//
//    }
//}

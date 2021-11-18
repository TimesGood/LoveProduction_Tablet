package com.aige.loveproduction_tablet.mvp.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.base.BaseActivity;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.mvp.contract.ApplyContract;
import com.aige.loveproduction_tablet.mvp.presenter.ApplyPresenter;
import com.aige.loveproduction_tablet.mvp.ui.customui.view.DrawMprView;
import com.aige.loveproduction_tablet.util.FileUtil;
import com.aige.loveproduction_tablet.util.FileViewerUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

public class ApplyActivity extends BaseActivity<ApplyPresenter, ApplyContract.View> implements ApplyContract.View {
    private FrameLayout main_body;
    private DrawMprView apply_view;
    private EditText find_edit;
    @Override
    protected ApplyPresenter createPresenter() {
        return new ApplyPresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply;
    }

    @Override
    public void initView() {
        main_body = findViewById(R.id.main_body);
        apply_view = findViewById(R.id.apply_view);
        find_edit = findViewById(R.id.find_edit);
        //apply_view.setOnTouchListener(this);
        setOnClickListener(R.id.image_camera,R.id.find_img);


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
        if(id == R.id.find_img) {
            FileViewerUtils.createOrExistsDir(getExternalCacheDir() + "mprFile");
            String edit = find_edit.getText().toString();
            Map<String, List<Map<String, Float>>> data = null;
            if("1".equals(edit)) {
                data = FileUtil.readFile(new File(getExternalCacheDir() + "/mprFile/A01211007880510003.mpr"));
            }else if("2".equals(edit)) {
                data = FileUtil.readFile(new File(getExternalCacheDir() + "/mprFile/A01211008860110018.mpr"));
            }else if("3".equals(edit)) {
                data = FileUtil.readFile(new File(getExternalCacheDir() + "/mprFile/A01211008870310028.mpr"));
            }else if("4".equals(edit)) {
                data = FileUtil.readFile(new File(getExternalCacheDir() + "/mprFile/A01211010940110016.mpr"));
            }else if("5".equals(edit)) {
                data = FileUtil.readFile(new File(getExternalCacheDir() + "/mprFile/A35211000050610008.mpr"));
            }
            if(data == null){
                showToast("数据解析失败");
                return;
            }
            apply_view.setData(data);
        }
    }

    @Override
    public void onGetMPRByBatchNoSuccess(List<DownloadBean> beans) {

    }

    @Override
    public void onGetFileSuccess(ResponseBody body) {

    }
}
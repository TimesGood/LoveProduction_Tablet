package com.aige.loveproduction_tablet.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.base.BaseFragment;

import com.aige.loveproduction_tablet.mvp.contract.HomeContractBack;
import com.aige.loveproduction_tablet.mvp.ui.customui.viewgroup.FlowLayout;
import com.aige.loveproduction_tablet.mvp.ui.dialog.DateDialog;
import com.aige.loveproduction_tablet.mvp.presenter.HomePresenterBack;


public class HomeFragmentBack extends BaseFragment<HomePresenterBack, HomeContractBack.View> {
    private EditText audit_date;
    private Button find_btn;
    private FlowLayout flow_layout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_back;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(View view) {
        flow_layout = findViewById(R.id.flow_layout);
        flow_layout.setMargin(5,5);
        audit_date = findViewById(R.id.audit_date);
        audit_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DateDialog.Builder(getContext())
                            .setTitle("请选择日期")
                            .setConfirm("确定")
                            .setCancel("取消")
                            .setListener((dialog, year, month, day) -> audit_date.setText(year + "-" + month + "-" + day))
                            .show();
                    return true;
                }
                return false;
            }
        });
        find_btn = findViewById(R.id.find_btn);
        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected HomePresenterBack createPresenter() {
        return new HomePresenterBack();
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
}

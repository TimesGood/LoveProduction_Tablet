package com.aige.loveproduction_tablet.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.aige.loveproduction_tablet.action.ActivityAction;
import com.aige.loveproduction_tablet.action.ClickAction;
import com.aige.loveproduction_tablet.permission.Permission;

import org.jetbrains.annotations.NotNull;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public abstract class BaseFragment<P extends BasePresenter,V extends IBaseView> extends Fragment implements IBaseView, ClickAction, ActivityAction {
    protected P mPresenter;
    protected View view;
    protected Activity mActivity;
    protected Toast toast;
    protected Permission permission;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mActivity = requireActivity();

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(this.getLayoutId(), container, false);
        mPresenter = createPresenter();
        mPresenter.onAttach((V) this);
        initView(view);
        initSDK();
        return view;
    }

    /**
     * 对Toast广播进行一些初始化的改造，把广播通知带有的软件名擦掉
     */
    public void showToast(String msg) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(mActivity,"",Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }
    /**
     * 设置Layout
     * @return Layout Id
     */
    protected abstract int getLayoutId();
    /**
     * 初始化动作
     * @param view Layout对象
     */
    protected abstract void initView(View view);
    protected void initSDK() {
        if(permission == null) permission = new Permission(mActivity);
    }
    /**
     * 创建对应的P层实体对象
     * @return new Presenter实体对象
     */
    protected abstract P createPresenter();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPresenter != null) {
            mPresenter.onDetach();
            mActivity = null;
        }
        if(permission != null) permission = null;
    }
    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    public void finish() {
        if (mActivity == null || mActivity.isFinishing() || mActivity.isDestroyed()) {
            return;
        }
        mActivity.finish();
    }
    @Override
    public <V extends View> V findViewById(int id) {
        return view.findViewById(id);
    }

    /**
     * 绑定生命周期 防止MVP内存泄漏
     * @param <P>
     * @return
     */
    @Override
    public <P> AutoDisposeConverter<P> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }
}

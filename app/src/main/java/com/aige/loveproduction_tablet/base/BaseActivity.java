package com.aige.loveproduction_tablet.base;


import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.action.ActivityAction;
import com.aige.loveproduction_tablet.action.ClickAction;
import com.aige.loveproduction_tablet.action.KeyboardAction;
import com.aige.loveproduction_tablet.action.TitleBarAction;
import com.aige.loveproduction_tablet.permission.Permission;
import com.aige.loveproduction_tablet.util.SoundUtils;

import autodispose2.AutoDispose;
import autodispose2.AutoDisposeConverter;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;


/**
 * 业务基类
 */
public abstract class BaseActivity<P extends BasePresenter,V extends IBaseView> extends AppCompatActivity
        implements IBaseView, ActivityAction, ClickAction , KeyboardAction , TitleBarAction {
    protected P mPresenter;
    protected SoundUtils soundUtils;
    protected Permission permission;
    private Toolbar toolbar;
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //锁定横屏
        //setRequestedOrientation(ActivityInfo .SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        //创建相应的P层对象
        mPresenter = createPresenter();
        //绑定V层
        mPresenter.onAttach((V) this);
        //初始化Activity
        initActivity();
    }

    /**
     * 获取标题栏，继承本类的Activity的layout必须放入这个Toolbar，否则报错
     */
    @Override
    @Nullable
    public Toolbar getTitleBar() {
        toolbar = findViewById(R.id.toolbar);
        return toolbar;
    }
    /**
     * 初始化标题，设置属性
     */
    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if(toolbar == null) return;
        hideTitle();
        setCenterTitle("居中标题");
        toolbar.setBackground(ContextCompat.getDrawable(this,R.color.blue));
        toolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(toolbar);
    }
    /**
     * 对Toast广播进行一些初始化的改造，把广播通知带有的软件名擦掉
     */
    public void showToast(String msg) {
        if(toast != null) toast.cancel();
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

    /**
     * 创建对应的P层实体对象，继承此类的Activity都需要创建相应的P层来实例化
     * @return new Presenter实体对象
     */
    protected abstract P createPresenter();
    /**
     * 设置layout布局
     * @return layout Id
     */
    @LayoutRes
    public abstract int getLayoutId();
    protected void initActivity() {
        initLayout();
        initToolbar();
        initView();
        initSDK();
    }
    //初始化一些SDK
    public void initSDK() {
        //初始化音乐库
        soundUtils = new SoundUtils(this,SoundUtils.MEDIA_SOUND);
        soundUtils.putSound(0,R.raw.ok);
        soundUtils.putSound(1,R.raw.no);
        if(permission == null) permission = new Permission(this);
    }


    /**
     * 初始化视图
     */
    public abstract void initView();
    /**
     * 初始化布局
     */
    protected void initLayout() {
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
            //initSoftKeyboard();
        }
    }
    @Override
    public Context getContext() {
        return this;
    }

    /**
     * 初始化软键盘
     */
//    protected void initSoftKeyboard() {
//        // 点击外部隐藏软键盘，提升用户体验
//        getContentView().setOnClickListener(v -> {
//            // 隐藏软键，避免内存泄漏
//            hideKeyboard(getCurrentFocus());
//        });
//    }

    /**
     * 获取当前Activity视图
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }
    /**
     * 解除绑定
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetach();
        }
        if(permission != null) permission = null;
    }
    /**
     * 绑定生命周期 防止MVP内存泄漏
     * 在使用Rxjava时，当我们一个网络请求出去了，在结果还没有返回时用户退出当前Activity，而Activity被RxJava订阅了，使得Activity
     * 没有及时的释放内存，导致内存泄漏，所以我们使用AutoDispose在Activity调用onDestroy时进行解除订阅，使得内存得以及时释放
     * 使用这个需要我们的类实现LifecycleOwner接口，而Android的AppCompatActivity已经实现了此类，所以我们直接this使用即可
     */
    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider
                .from(this, Lifecycle.Event.ON_DESTROY));
    }

    /**
     * 重写菜单监听，监听几乎每个页面都有的返回按钮
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

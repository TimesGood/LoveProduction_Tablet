package com.aige.loveproduction_tablet.mvp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aige.loveproduction_tablet.R;
import com.aige.loveproduction_tablet.action.HandlerAction;
import com.aige.loveproduction_tablet.adapter.FragmentPagerAdapter;
import com.aige.loveproduction_tablet.base.BaseAnimation;
import com.aige.loveproduction_tablet.mvp.ui.customui.viewgroup.NoScrollViewPager;
import com.aige.loveproduction_tablet.manager.ActivityManager;
import com.aige.loveproduction_tablet.mvp.ui.fragment.ApplyFragment;
import com.aige.loveproduction_tablet.mvp.ui.fragment.UserFragment;
import com.aige.loveproduction_tablet.mvp.ui.fragment.HomeFragment;
import com.aige.loveproduction_tablet.permission.Permission;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements HandlerAction {
    private FragmentPagerAdapter<Fragment> fragmentPagerAdapter;
    private NoScrollViewPager main_body;
    private final BaseAnimation animation = new BaseAnimation();
    private Permission permission = new Permission(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private  final int REQUEST_EXTERNAL_STORAGE = 1;
    private final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private void initView() {
        main_body = findViewById(R.id.main_body);
        fragmentPagerAdapter = new FragmentPagerAdapter<>(this);
        fragmentPagerAdapter.addFragment(new HomeFragment());
        fragmentPagerAdapter.addFragment(new ApplyFragment());
        fragmentPagerAdapter.addFragment(new UserFragment());
        main_body.setAdapter(fragmentPagerAdapter);
        main_body.setCurrentItem(0);
        //底部菜单监听事件
        BottomNavigationView view = findViewById(R.id.home_navigation);
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int itemId = item.getItemId();
                View viewById1 = findViewById(R.id.home);
                View viewById2 = findViewById(R.id.user);
                View viewById3 = findViewById(R.id.apply);
                if(itemId == R.id.home) {
                    animation.scaleXYAnimation(viewById1,0.5f,1f,1000);
                    main_body.setCurrentItem(0);
                    //toolbar_title.setText("首页");
                }else if(itemId == R.id.user) {
                    animation.scaleXYAnimation(viewById2,0.5f,1f,1000);
                    main_body.setCurrentItem(2);
                    //toolbar_title.setText("个人中心");
                }else if(itemId == R.id.apply) {
                    animation.scaleXYAnimation(viewById3,0.5f,1f,1000);
                    main_body.setCurrentItem(1);
                }
                return true;
            }
        });
        permission.applyPermission(PERMISSIONS_STORAGE, new Permission.ApplyListener() {
            @Override
            public void apply(String[] permission) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }

            @Override
            public void applySuccess() {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if(!(grantResults[grantResults.length-1] == PackageManager.PERMISSION_GRANTED)) {

            }
        }
    }

    //双击返回手机返回键，关闭软件
    protected long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                Toast toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
                toast.setText("再按一次退出爱生产");
                toast.show();
                exitTime = System.currentTimeMillis();
            }else {
                ActivityManager.getInstance().finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
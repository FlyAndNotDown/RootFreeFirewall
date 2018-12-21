package com.nuaa.is.rootfreefirewall.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.view.fragment.MessageFragment;
import com.nuaa.is.rootfreefirewall.view.fragment.NetworkFragment;

/**
 * 启动 Activity
 */
public class LaunchActivity extends AppCompatActivity {

    // Fragments
    private Fragment[] fragments;
    private NetworkFragment networkFragment;
    private MessageFragment messageFragment;
    // 上一个选择的 Fragment
    private int lastFragment;

    // BottomNavigationView
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // 初始化 fragment
        this.initFragment();
        // 获取 UI 组件
        this.getUIComponent();
        // 设置监听器
        this.setEventListener();
    }

    // 获取 UI 组件
    private void getUIComponent() {
        this.bottomNavigationView = findViewById(R.id.activity_launch__navigation);
    }

    // 初始化 fragment
    private void initFragment() {
        this.lastFragment = 0;
        this.networkFragment = new NetworkFragment();
        this.messageFragment = new MessageFragment();
        this.fragments = new Fragment[] {
                this.networkFragment,
                this.messageFragment
        };

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_launch__container, networkFragment)
                .show(networkFragment)
                .commit();
    }

    // 注册事件监听器
    private void setEventListener() {
        this.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.activity_launch__navigation_network:
                        if (lastFragment != 0) {
                            switchFragment(lastFragment, 0);
                            lastFragment = 0;
                        }
                        return true;
                    case R.id.activity_launch__navigation_message:
                        if (lastFragment != 1) {
                            switchFragment(lastFragment, 1);
                            lastFragment = 1;
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // 切换 Fragment
    private void switchFragment(int lastFragment, int nextFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(this.fragments[lastFragment]);

        if (!this.fragments[nextFragment].isAdded()) {
            fragmentTransaction.add(R.id.activity_launch__container, this.fragments[nextFragment]);
        }
        fragmentTransaction.show(this.fragments[nextFragment]).commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

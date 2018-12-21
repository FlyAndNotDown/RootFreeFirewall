package com.nuaa.is.rootfreefirewall.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import com.nuaa.is.rootfreefirewall.R;

/**
 * 启动 Activity
 */
public class LaunchActivity extends AppCompatActivity {

    // 底部导航栏切换事件监听器
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // 获取 UI 组件、设置监听器
        this.getUIComponent();
        this.setEventListener();
    }

    // 获取 UI 组件函数
    private void getUIComponent() {

    }

    // 注册事件监听器
    private void setEventListener() {

    }
}

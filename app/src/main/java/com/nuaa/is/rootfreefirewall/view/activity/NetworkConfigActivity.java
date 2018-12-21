package com.nuaa.is.rootfreefirewall.view.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.nuaa.is.rootfreefirewall.R;

/**
 * 网络配置 Activity
 */
public class NetworkConfigActivity extends AppCompatActivity {

    // UI 组件
    private Spinner firewallModeSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_config);

        // 获取 UI 组件
        this.getUIComponent();
    }

    private void getUIComponent() {
        this.firewallModeSpinner = findViewById(R.id.activity_network_config__firewall_mode_spinner);
    }

}

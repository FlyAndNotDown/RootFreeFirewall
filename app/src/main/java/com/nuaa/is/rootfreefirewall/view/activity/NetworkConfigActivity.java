package com.nuaa.is.rootfreefirewall.view.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.view.adapter.NetworkConfigActivityFirewallModeSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络配置 Activity
 */
public class NetworkConfigActivity extends AppCompatActivity {

    // UI 组件
    private Spinner firewallModeSpinner;

    // 防火墙模式数据
    private List<String> firewallModeDatas;
    // 防火墙模式适配器
    private NetworkConfigActivityFirewallModeSpinnerAdapter networkConfigActivityFirewallModeSpinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_config);

        // 获取 UI 组件
        this.getUIComponent();
        // 初始化数据
        this.initData();
        // 设置适配器
        this.setAdapter();
    }

    // 初始化数据
    private void initData() {
        this.firewallModeDatas = new ArrayList<>();
        this.firewallModeDatas.add(getString(R.string.activity_network_config__firewall_mode__spy));
        this.firewallModeDatas.add(getString(R.string.activity_network_config__firewall_mode__kill));
    }

    // 获取 UI 组件
    private void getUIComponent() {
        this.firewallModeSpinner = findViewById(R.id.activity_network_config__firewall_mode_spinner);
    }

    // 设置适配器
    private void setAdapter() {
        this.networkConfigActivityFirewallModeSpinnerAdapter = new NetworkConfigActivityFirewallModeSpinnerAdapter(this);
        this.firewallModeSpinner.setAdapter(this.networkConfigActivityFirewallModeSpinnerAdapter);
        this.networkConfigActivityFirewallModeSpinnerAdapter.setDatas(this.firewallModeDatas);
    }

}

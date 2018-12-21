package com.nuaa.is.rootfreefirewall.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.view.adapter.NetworkConfigActivityFirewallModeSpinnerAdapter;
import com.nuaa.is.rootfreefirewall.view.fragment.NetworkFragment;

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

    // 防火墙模式
    private String firewallMode;
    private boolean enableTcp;
    private boolean enableUdp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_config);

        // 解析参数
        this.dealParams();
        // 获取 UI 组件
        this.getUIComponent();
        // 初始化数据
        this.initData();
        // 设置适配器
        this.setAdapter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.save();
    }

    // 解析参数
    private void dealParams() {
        Intent intent = getIntent();
        this.firewallMode = intent.getStringExtra("firewallMode");
        this.enableTcp = intent.getBooleanExtra("enableTcp", NetworkFragment.DEFAULT_ENABLE_TCP);
        this.enableUdp = intent.getBooleanExtra("enableUdp", NetworkFragment.DEFAULT_ENABLE_UDP);
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
        this.firewallModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                firewallMode = firewallModeDatas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                firewallMode = firewallModeDatas.get(0);
            }
        });
        this.firewallModeSpinner.setSelection(firewallModeDatas.indexOf(firewallMode), true);
    }

    // 保存数据
    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firewallMode", this.firewallMode);
        editor.putBoolean("enableTcp", this.enableTcp);
        editor.putBoolean("enableUdp", this.enableUdp);
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

}

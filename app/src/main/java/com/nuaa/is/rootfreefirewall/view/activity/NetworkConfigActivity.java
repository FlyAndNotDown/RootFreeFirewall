package com.nuaa.is.rootfreefirewall.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.view.adapter.FlowModeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络配置 Activity
 */
public class NetworkConfigActivity extends AppCompatActivity {

    // UI 组件
    private Spinner tcpFlowModeSpinner;
    private Spinner udpFlowModeSpinner;

    // 流控模式备选数据
    private List<Boolean> tcpFlowModeDatas;
    private List<Boolean> udpFlowModeDatas;
    // 适配器
    private FlowModeAdapter tcpFlowModeAdapter;
    private FlowModeAdapter udpFlowModeAdapter;

    // Spinner数据
    private static final boolean DEFAULT_IS_TCP_FLOW_MODE_SPY = true;
    private static final boolean DEFAULT_IS_UDP_FLOW_MODE_SPY = true;
    private boolean isTcpFlowModeSpy;
    private boolean isUdpFlowModeSpy;

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

    @Override
    protected void onPause() {
        super.onPause();
        this.save();
    }

    // 解析参数
    private void dealParams() {
        Intent intent = getIntent();
        this.isTcpFlowModeSpy = intent.getBooleanExtra("isTcpFlowModeSpy", DEFAULT_IS_TCP_FLOW_MODE_SPY);
        this.isUdpFlowModeSpy = intent.getBooleanExtra("isUdpFlowModeSpy", DEFAULT_IS_UDP_FLOW_MODE_SPY);
    }

    // 初始化数据
    private void initData() {
        this.tcpFlowModeDatas = new ArrayList<>();
        this.tcpFlowModeDatas.add(true);
        this.tcpFlowModeDatas.add(false);
        this.udpFlowModeDatas = new ArrayList<>();
        this.udpFlowModeDatas.add(true);
        this.udpFlowModeDatas.add(false);
    }

    // 获取 UI 组件
    private void getUIComponent() {
        this.tcpFlowModeSpinner = findViewById(R.id.activity_network_config__tcp_flow_mode_spinner);
        this.udpFlowModeSpinner = findViewById(R.id.activity_network_config__udp_flow_mode_spinner);
    }

    // 设置适配器
    private void setAdapter() {
        // TCP 流控模式适配器
        this.tcpFlowModeAdapter = new FlowModeAdapter(this);
        this.tcpFlowModeSpinner.setAdapter(this.tcpFlowModeAdapter);
        this.tcpFlowModeAdapter.setDatas(this.tcpFlowModeDatas);
        this.tcpFlowModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isTcpFlowModeSpy = tcpFlowModeDatas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isTcpFlowModeSpy = tcpFlowModeDatas.get(0);
            }
        });

        // UDP 流控模式适配器
        this.udpFlowModeAdapter = new FlowModeAdapter(this);
        this.udpFlowModeSpinner.setAdapter(this.udpFlowModeAdapter);
        this.udpFlowModeAdapter.setDatas(this.udpFlowModeDatas);
        this.udpFlowModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isUdpFlowModeSpy = udpFlowModeDatas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isUdpFlowModeSpy = udpFlowModeDatas.get(0);
            }
        });

        // 默认流控模式
        this.tcpFlowModeSpinner.setSelection(
                this.tcpFlowModeDatas.indexOf(isTcpFlowModeSpy),
                true
        );
        this.udpFlowModeSpinner.setSelection(
                this.udpFlowModeDatas.indexOf(isUdpFlowModeSpy),
                true
        );

    }

    // 保存数据
    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTcpFlowModeSpy", this.isTcpFlowModeSpy);
        editor.putBoolean("isUdpFlowModeSpy", this.isUdpFlowModeSpy);
        editor.apply();
        setResult(RESULT_OK);
        finish();
    }

}

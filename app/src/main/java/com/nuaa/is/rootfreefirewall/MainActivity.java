package com.nuaa.is.rootfreefirewall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.VpnService;
import android.view.View;
import android.widget.Button;

import com.nuaa.is.rootfreefirewall.service.FirewallVpnService;

/**
 * MainActivity
 * 主Activity类
 * @author John Kindem
 * @version v1.0
 */
public class MainActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = "RFF-MainActivity";

    // Service & Activity 请求码
    private static final int REQUEST_CODE__FIREWALL_VPN_SERVICE = 678;

    // 等待 Vpn 启动状态
    private boolean waittingVpnStart;

    // UI组件
    private Button startFirewallButton;

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 如果收到了服务已经运行的广播，则刚更新 flag
            if (FirewallVpnService.BROADCAST_VPN_STATE.equals(intent.getAction()) &&
                    intent.getBooleanExtra("running", false)) {
                waittingVpnStart = false;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        // 设置按钮状态
        this.setStartFirewallButtonEnable(!waittingVpnStart && !FirewallVpnService.isRunning());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取UI组件
        this.getUIComponent();
        // 添加组件监听事件
        this.addComponentListener();
        // 注册广播接收器
        this.registerBroadcastReceiver();

        this.waittingVpnStart = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 如果是启动VpnInterface的请求
        if (requestCode == REQUEST_CODE__FIREWALL_VPN_SERVICE) {
            if (resultCode == RESULT_OK) {
                // 开启服务
                startService(new Intent(this, FirewallVpnService.class));
                this.waittingVpnStart = true;

                // 调整启动 VPN 按钮状态
                this.setStartFirewallButtonEnable(false);
            }
        }

    }

    // 注册广播接收器函数
    private void registerBroadcastReceiver() {
        // VpnState 广播
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(vpnStateReceiver, new IntentFilter(FirewallVpnService.BROADCAST_VPN_STATE));
    }

    // 获取UI组件函数
    private void getUIComponent() {
        this.startFirewallButton = findViewById(R.id.activity_main__start_firewall_button);
    }

    // 添加组件监听事件函数
    private void addComponentListener() {
        // 开启防火墙监听事件
        this.startFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 准备VpnService
                prepareVpnService();
            }
        });
    }

    // 准备VpnService函数
    private void prepareVpnService() {
        // 让用户确认是否允许使用VpnService的权限
        Intent intent = VpnService.prepare(this);

        if (intent != null) {
            // 如果用户没有同意，则询问用户是否允许，并且在用户同意之后启动VpnInterface
            startActivityForResult(intent, REQUEST_CODE__FIREWALL_VPN_SERVICE);
        } else {
            // 如果用户已经确认过了，则直接开启VpnInterface
            onActivityResult(REQUEST_CODE__FIREWALL_VPN_SERVICE, RESULT_OK, null);
        }
    }

    // 设置启动防火墙按钮状态
    private void setStartFirewallButtonEnable(boolean enable) {
        // 设置激活状态
        this.startFirewallButton.setEnabled(enable);
        // 设置文字
        this.startFirewallButton.setText(
                enable ? getString(R.string.main_activity__start_firewall_button__enable__text) :
                        getString(R.string.main_activity__start_firewall_button__disenable__text)
        );
    }
}

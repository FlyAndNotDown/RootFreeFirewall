package com.nuaa.is.rootfreefirewall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.support.annotation.Nullable;
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

    // VpnInterface请求码
    private static final int REQUEST_CODE__VPN_INTERFACE = 678;
    // 等待 Vpn 启动状态
    private boolean waittingVpnStart;

    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO
        }
    };



    // UI组件
    private Button startFirewallButton;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, FirewallVpnService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取UI组件
        this.getUIComponent();
        // 添加组件监听事件
        this.addComponentListener();
    }

    // 获取UI组件函数
    private void getUIComponent() {
        this.startFirewallButton = findViewById(R.id.startFirewallButton);
    }

    // 添加组件监听事件函数
    private void addComponentListener() {
        // 开启防火墙监听事件
        this.startFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 准备VpnService
                prepareVpnService();
                // 暂时禁用启动防火墙按钮
                startFirewallButton.setEnabled(false);
            }
        });
    }

    // 准备VpnService函数
    private void prepareVpnService() {
        // 让用户确认是否允许使用VpnService的权限
        Intent intent = VpnService.prepare(this);

        if (intent != null) {
            // 如果用户没有同意，则询问用户是否允许，并且在用户同意之后启动VpnInterface
            startActivityForResult(intent, REQUEST_CODE__VPN_INTERFACE);
        } else {
            // 如果用户已经确认过了，则直接开启VpnInterface
            onActivityResult(REQUEST_CODE__VPN_INTERFACE, RESULT_OK, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 如果是启动VpnInterface的请求
        if (requestCode == REQUEST_CODE__VPN_INTERFACE) {
            if (resultCode == RESULT_OK) {
                // 开启服务
                startService(new Intent(this, FirewallVpnService.class));
            } else {
                // 启用开启防火墙按钮
                startFirewallButton.setEnabled(true);
            }
        }

    }
}

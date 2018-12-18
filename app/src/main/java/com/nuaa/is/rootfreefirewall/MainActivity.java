package com.nuaa.is.rootfreefirewall;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.VpnService;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * MainActivity
 * 主Activity类
 * @author John Kindem
 * @version v1.0
 */
public class MainActivity extends AppCompatActivity {

    // VpnInterfaceService
    private VpnInterface vpnInterface;
    // 服务是否已经 bind
    private boolean vpnInterfaceIsBound;

    private class VpnInterfaceServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("firewallDebug", "VpnInterfaceConnection-connected");
            VpnInterface.VpnInterfaceBinder vpnInterfaceBinder = (VpnInterface.VpnInterfaceBinder) service;
            vpnInterface = vpnInterfaceBinder.getService();
            vpnInterfaceIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("firewallDebug", "VpnInterfaceConnection-disconnected");
            vpnInterfaceIsBound = false;
        }

    }

    // VpnInterface请求码
    private static final int REQUEST_CODE__VPN_INTERFACE = 678;

    // 服务连接
    private VpnInterfaceServiceConnection vpnInterfaceServiceConnection;

    // UI组件
    private Button startFirewallButton;
    private Button closeFirewallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取UI组件
        this.getUIComponent();
        // 添加组件监听事件
        this.addComponentListener();

        this.vpnInterfaceIsBound = false;
    }

    // 获取UI组件函数
    private void getUIComponent() {
        this.startFirewallButton = findViewById(R.id.startFirewallButton);
        this.closeFirewallButton = findViewById(R.id.closeFirewallButton);
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
                // 启用关闭防火墙按钮
                closeFirewallButton.setEnabled(true);
            }
        });

        // 关闭防火墙监听事件
        this.closeFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 禁用关闭防火墙按钮
                closeFirewallButton.setEnabled(false);
                // 启用开启防火墙按钮
                startFirewallButton.setEnabled(true);
                // 真正关闭VpnService
                if (vpnInterfaceIsBound) {
                    try {
                        unbindService(vpnInterfaceServiceConnection);
                    } catch (Exception e) {
                        Log.e("firewallDebug", "vpnInterfaceServiceConnection-can't unbind");
                        e.printStackTrace();
                    }
                }
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
                Intent intent = new Intent(this, VpnInterface.class);
                this.vpnInterfaceServiceConnection = new VpnInterfaceServiceConnection();
                bindService(intent, vpnInterfaceServiceConnection, BIND_AUTO_CREATE);
            } else {
                // 禁用关闭防火墙按钮
                closeFirewallButton.setEnabled(false);
                // 启用开启防火墙按钮
                startFirewallButton.setEnabled(true);
            }
        }
    }
}

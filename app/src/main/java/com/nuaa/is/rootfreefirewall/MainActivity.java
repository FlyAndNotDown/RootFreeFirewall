package com.nuaa.is.rootfreefirewall;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.VpnService;

/**
 * MainActivity
 * 主Activity类
 * @author John Kindem
 * @version v1.0
 */
public class MainActivity extends AppCompatActivity {

    // VpnInterface请求码
    private static final int REQUEST_CODE__VPN_INTERFACE = 678;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if (requestCode == REQUEST_CODE__VPN_INTERFACE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, VpnInterface.class);
            // 开启服务
            startService(intent);
        }
    }
}

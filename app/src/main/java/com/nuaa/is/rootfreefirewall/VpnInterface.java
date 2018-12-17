package com.nuaa.is.rootfreefirewall;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

/**
 * VpnInterface
 * 继承自VpnService的Vpn服务类
 * @author John Kindem
 * @version v1.0
 */
public class VpnInterface extends VpnService {

    // Vpn文件描述符
    private ParcelFileDescriptor vpnFileDescriptor;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建一个构造器
        Builder builder = new Builder();

        // 添加参数
        // TODO

        // 建立连接
        this.vpnFileDescriptor = builder.establish();

        return super.onStartCommand(intent, flags, startId);
    }
}

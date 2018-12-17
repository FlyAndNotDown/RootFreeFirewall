package com.nuaa.is.rootfreefirewall;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.IOUtils;

/**
 * VpnInterface
 * 继承自VpnService的Vpn服务类
 * @author John Kindem
 * @version v1.0
 */
public class VpnInterface extends VpnService {

    // Vpn文件描述符
    private ParcelFileDescriptor vpnFileDescriptor;
    // 配置json对象
    private JSONObject config;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 初始化配置
        this.loadConfig();

        // 创建一个构造器
        Builder builder = new Builder();

        // 添加参数
        builder.addAddress(
                config.getString("address"),
                config.getInteger("addressPrefixLength")
        );
        builder.addRoute(
                config.getString("routeAddress"),
                config.getInteger("routeAddressPrefixLength")
        );

        // 建立连接
        this.vpnFileDescriptor = builder.establish();

        return super.onStartCommand(intent, flags, startId);
    }

    private void loadConfig() {
        // 获取资源文件
        String configString = "{}";
        try {
            configString = IOUtils.toString(getAssets().open("config/vpn-interface.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.config = JSON.parseObject(configString);
    }

}

package com.nuaa.is.rootfreefirewall;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nuaa.is.rootfreefirewall.model.IpPacket;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * VpnInterface
 * 继承自VpnService的Vpn服务类
 * @author John Kindem
 * @version v1.0
 */
public class VpnInterface extends VpnService {

    private static final int BYTE_BUFFER_SIZE = 65535;

    // Vpn文件描述符
    private ParcelFileDescriptor vpnParcelFileDescriptor;
    // 配置json对象
    private JSONObject config;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 初始化配置
        this.loadConfig();

        // 创建一个构造器
        Builder builder = new Builder();

        // 添加参数
        builder.setSession(getString(R.string.app_name));
        builder.setBlocking(false);
        builder.addAddress(
                config.getString("address"),
                config.getInteger("addressPrefixLength")
        );
        builder.addRoute(
                config.getString("routeAddress"),
                config.getInteger("routeAddressPrefixLength")
        );

        // 建立连接
        this.vpnParcelFileDescriptor = builder.establish();

        // 创建线程进行抓包
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 创建输入输出流
                FileInputStream fileInputStream = new FileInputStream(vpnParcelFileDescriptor.getFileDescriptor());
                FileOutputStream fileOutputStream = new FileOutputStream(vpnParcelFileDescriptor.getFileDescriptor());

                // buffer
                ByteBuffer buffer = ByteBuffer.allocate(BYTE_BUFFER_SIZE);

                // 不断读取数据包
                int length;
                while (true) {
                    length = 0;
                    buffer.clear();

                    try {
                        length = fileInputStream.read(buffer.array());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (length > 0) {
                        // 拆包
                        IpPacket ipPacket = new IpPacket(buffer, length);

                        // 日志信息
                        Log.i(
                                "firewallDebug",
                                String.format(
                                        "get a IP packet: \n" +
                                                "\tversion: %d\theaderLength: %d\ttotalLength: %d\n" +
                                                "\toffset: %d\tttl: %d\tprotocol: %s\n" +
                                                "\tsourceIpAddress: %s\t destIpAddress: %s\n",
                                        ipPacket.getVersion(),
                                        ipPacket.getHeaderLength(),
                                        ipPacket.getTotalLength(),
                                        ipPacket.getOffset(),
                                        ipPacket.getTtl(),
                                        ipPacket.getProtocol(),
                                        ipPacket.getSourceIpAddress(),
                                        ipPacket.getDestIpAddress()
                                )
                        );
                    }
                }
            }
        }).start();

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

package com.nuaa.is.rootfreefirewall.service;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.nuaa.is.rootfreefirewall.R;
import com.nuaa.is.rootfreefirewall.model.ByteBufferPool;
import com.nuaa.is.rootfreefirewall.model.Packet;
import com.nuaa.is.rootfreefirewall.net.TCPInput;
import com.nuaa.is.rootfreefirewall.net.TCPOutput;
import com.nuaa.is.rootfreefirewall.net.UDPInput;
import com.nuaa.is.rootfreefirewall.net.UDPOutput;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * FirewallVpnService
 * @author John Kindem
 * @version v1.0
 */
public class FirewallVpnService extends VpnService {

    // TAG
    private static final String TAG = "RFF-FirewallVpnService";

    // VPN 参数
    private static final String VPN_ADDRESS = "10.0.0.2";
    private static final int VPN_ADDRESS_MASK = 32;
    private static final String VPN_ROUTE = "0.0.0.0";
    private static final int VPN_ROUTE_MASK = 0;

    // 广播 Vpn 状态
    public static final String BROADCAST_VPN_STATE = "com.nuaa.is.VPN_STATE";

    // 运行状态
    private static boolean isRunning = false;

    // 文件描述符
    private ParcelFileDescriptor parcelFileDescriptor = null;

    // 队列
    private ConcurrentLinkedQueue<Packet> deviceToNetworkUDPQueue;
    private ConcurrentLinkedQueue<Packet> deviceToNetworkTCPQueue;
    private ConcurrentLinkedQueue<ByteBuffer> networkToDeviceQueue;
    // 线程池
    private ExecutorService executorService;

    // TCP、UDP选择器
    private Selector udpSelector;
    private Selector tcpSelector;

    @Override
    public void onCreate() {
        super.onCreate();

        // 设置运行状态
        FirewallVpnService.isRunning = true;

        // 建立 VPN 连接
        if (this.parcelFileDescriptor == null) {
            Builder builder = new Builder();
            builder.setSession(getString(R.string.app_name));
            builder.addAddress(VPN_ADDRESS, VPN_ADDRESS_MASK);
            builder.addAddress(VPN_ROUTE, VPN_ROUTE_MASK);
            this.parcelFileDescriptor = builder.establish();
        }

        try {
            // 配置选择器
            udpSelector = Selector.open();
            tcpSelector = Selector.open();

            // 创建队列
            deviceToNetworkUDPQueue = new ConcurrentLinkedQueue<>();
            deviceToNetworkTCPQueue = new ConcurrentLinkedQueue<>();
            networkToDeviceQueue = new ConcurrentLinkedQueue<>();

            // 创建线程池
            executorService = Executors.newFixedThreadPool(5);

            // 每一个线程负责一个任务
            executorService.submit(new UDPInput(networkToDeviceQueue, udpSelector));
            executorService.submit(new UDPOutput(deviceToNetworkUDPQueue, udpSelector, this));
            executorService.submit(new TCPInput(networkToDeviceQueue, tcpSelector));
            executorService.submit(new TCPOutput(deviceToNetworkTCPQueue, networkToDeviceQueue, tcpSelector, this));
            executorService.submit(new VPNRunnable(
                    parcelFileDescriptor.getFileDescriptor(),
                    deviceToNetworkUDPQueue,
                    deviceToNetworkTCPQueue,
                    networkToDeviceQueue
            ));

            // 发送广播告知 FirewallVpnService 已经运行
            LocalBroadcastManager
                    .getInstance(this)
                    .sendBroadcast(
                            new Intent(BROADCAST_VPN_STATE).putExtra("running", true)
                    );

            Log.i(FirewallVpnService.TAG, "FirewallVpnService Started");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(FirewallVpnService.TAG, "Can't start FirewallVpnService");

            // 清理
            clean();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        executorService.shutdownNow();
        clean();
        Log.i(FirewallVpnService.TAG, "FirewallVpnService Stopped");
    }

    // 清理函数
    private void clean() {
        this.deviceToNetworkUDPQueue = null;
        this.deviceToNetworkTCPQueue = null;
        this.networkToDeviceQueue = null;
        ByteBufferPool.clear();
        closeReousrce(udpSelector, tcpSelector, parcelFileDescriptor);
    }

    // 清理资源
    private void closeReousrce(Closeable... resources) {
        for (Closeable resource : resources) {
            try {
                resource.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(FirewallVpnService.TAG, "Clean resources failed");
            }
        }
    }

    // Vpn 服务线程
    public static class VPNRunnable implements Runnable {

        private static final String TAG = "RFF-VpnThread";

        // 文件描述符
        private FileDescriptor fileDescriptor;

        // 队列
        private ConcurrentLinkedQueue<Packet> deviceToNetworkUDPQueue;
        private ConcurrentLinkedQueue<Packet> deviceToNetworkTCPQueue;
        private ConcurrentLinkedQueue<ByteBuffer> networkToDeviceQueue;

        // 构造
        public VPNRunnable(
                FileDescriptor fileDescriptor,
                ConcurrentLinkedQueue<Packet> deviceToNetworkUDPQueue,
                ConcurrentLinkedQueue<Packet> deviceToNetworkTCPQueue,
                ConcurrentLinkedQueue<ByteBuffer> networkToDeviceQueue
        ) {
            this.fileDescriptor = fileDescriptor;
            this.deviceToNetworkUDPQueue = deviceToNetworkUDPQueue;
            this.deviceToNetworkTCPQueue = deviceToNetworkTCPQueue;
            this.networkToDeviceQueue = networkToDeviceQueue;
        }

        @Override
        public void run() {
            // TODO
        }

    }
}

package com.nuaa.is.rootfreefirewall.net;

import android.net.VpnService;

import com.nuaa.is.rootfreefirewall.model.IpPacket;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;

/**
 * 网络发包代理
 * @author John Kindem
 * @version v1.0
 */
public class OutputProxy extends Thread {

    // 运行中状态标识
    private volatile boolean running = false;

    // 输入输出队列
    private BlockingQueue<IpPacket> inputQueue;
    private BlockingQueue<ByteBuffer> outputQueue;

    // channel selector
    private Selector channelSelector;

    // 服务
    private VpnService vpnService;

    // 构造
    public OutputProxy(
            BlockingQueue<IpPacket> inputQueue,
            BlockingQueue<ByteBuffer> outputQueue,
            VpnService vpnService,
            Selector channelSelector
    ) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.vpnService = vpnService;
        this.channelSelector = channelSelector;
    }

    // 线程主函数
    @Override
    public void run() {
        IpPacket ipPacket;

        // 循环发送
        while (true) {
            // TODO
        }
    }
}

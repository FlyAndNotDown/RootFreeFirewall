package com.nuaa.is.rootfreefirewall.net;

import android.net.VpnService;

import com.nuaa.is.rootfreefirewall.model.IpPacket;
import com.nuaa.is.rootfreefirewall.model.TCB;
import com.nuaa.is.rootfreefirewall.model.TcpPacket;

import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.util.concurrent.BlockingQueue;

/**
 * 网络发包代理
 * @author John Kindem
 * @version v1.0
 */
public class OutputProxy extends Thread {

    // BUFFER_SIZE
    private static final int BUFFER_SIZE = 65535;

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
        this.running = true;

        IpPacket ipPacket;

        // 循环发送
        while (true) {
            // 阻塞到有数据才开始处理
            try {
                ipPacket = inputQueue.poll();
                Thread.sleep(15);

                if (ipPacket == null) {
                    continue;
                }
            } catch (InterruptedException e) {
                if (!this.running) {
                    return;
                }
                continue;
            }

            // 根据情况讨论
            // 如果是 tcp
            switch (ipPacket.getProtocol()) {
                case "TCP":
                    // 创建响应缓冲区
                    ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);

                    // 拆包
                    TcpPacket tcpPacket = new TcpPacket(ipPacket.getData());

                    // 获取目的地址、源端口、目的端口
                    String destIpAddress = ipPacket.getDestIpAddress();
                    int sourcePort = tcpPacket.getSourcePort();
                    int destPort = tcpPacket.getDestPort();

                    // 尝试获取 TCB
                    TCB tcb = TCBPool.getTCB(destIpAddress, sourcePort, destPort);

                    // 如果没能获取到 TCB，说明 TCB 还没创建，尝试创建 TCB
                    if (tcb == null) {
                        initTCB(ipPacket, responseBuffer);
                    } else if (tcpPacket.getSyn()) {
                        dealDuplicateSYN(tcb, ipPacket, responseBuffer);
                    } else if (tcpPacket.getFin()) {
                        finishConnection(tcb, ipPacket, responseBuffer);
                    } else if (tcpPacket.getAck()) {
                        transportData(tcb, ipPacket, responseBuffer);
                    }
                case "UDP":
                    break;
                default:
                    break;
            }
        }
    }

    // 初始化 tcb
    private void initTCB(
            IpPacket ipPacket,
            ByteBuffer responseBuffer
    ) {
        // TODO
    }

    // 处理重复的 SYN
    private void dealDuplicateSYN(
            TCB tcb,
            IpPacket ipPacket,
            ByteBuffer responseBuffer
    ) {
        // TODO
    }

    // 结束连接
    private void finishConnection(
            TCB tcb,
            IpPacket ipPacket,
            ByteBuffer responseBuffer
    ) {
        // TODO
    }

    // 传输数据
    private void transportData(
            TCB tcb,
            IpPacket ipPacket,
            ByteBuffer responseBuffer
    ) {
        // TODO
    }

}

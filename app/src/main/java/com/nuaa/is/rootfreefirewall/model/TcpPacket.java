package com.nuaa.is.rootfreefirewall.model;

/**
 * TCP 包
 * @author John Kindem
 * @version v1.0
 */
public class TcpPacket {

    // 所有字节
    private byte [] bytes;
    // 源端口号
    private int sourcePort;
    // 目的端口号
    private int destPort;
    // 序号
    private int serial;
    // 确认好
    private int confirm;
    // 头部长度
    private int headerLength;
    // 保留
    private int hold;
    // urg
    private boolean urg;
    // ack
    private boolean ack;
    // psh
    private boolean psh;
    // rst
    private boolean rst;
    // syn
    private boolean syn;
    // fin
    private boolean fin;
    // 窗口大小
    private int windowSize;
    // 校验和
    private int checksum;
    // 紧急指针
    private int seriousPoint;

    // 构造
    public TcpPacket(byte [] bytes) {
        // 先将所有字节复制一份
        this.bytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            this.bytes[i] = bytes[i];
        }

        // 开始解构
        // TODO
    }

}

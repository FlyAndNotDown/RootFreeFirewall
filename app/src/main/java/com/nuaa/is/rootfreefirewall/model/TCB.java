package com.nuaa.is.rootfreefirewall.model;

import java.nio.channels.SelectionKey;

/**
 * Transport Control Block 传输控制块
 * @author John Kindem
 * @version v1.0
 */
public class TCB {

    // TCP 状态信息
    public static final int TCP_STATUS_NONE = 0;
    public static final int TCP_STATUS_SYN_SENT = 1;
    public static final int TCP_STATUS_SYN_RECEIVED = 2;
    public static final int TCP_STATUS_ESTABLISHED = 3;
    public static final int TCP_STATUS_LAST_ACK = 4;

    // 源地址
    private String sourceIpAddress;
    // 源端口
    private int sourcePort;
    // 目的地址
    private String destIpAddress;
    // 目的端口
    private int destPort;

    // 已经发送的数据量
    private int bytesCount;

    // 顺序码
    private long serial;
    private long serverSerial;
    // 确认码 (发过来的 serverSerial + serverDataLength)
    private long confirm;
    private long serverConfirm;

    // TCP 状态信息
    private int tcpStatus;

    // selectionKey
    private SelectionKey selectionKey;

    public TCB(
            String sourceIpAddress,
            int sourcePort,
            String destIpAddress,
            int destPort,
            int bytesCount,
            long serial,
            long serverSerial,
            long confirm,
            long serverConfirm,
            SelectionKey selectionKey
    ) {
        this.sourceIpAddress = sourceIpAddress;
        this.sourcePort = sourcePort;
        this.destIpAddress = destIpAddress;
        this.destPort = destPort;
        this.bytesCount = bytesCount;
        this.serial = serial;
        this.serverSerial = serverSerial;
        this.confirm = confirm;
        this.serverConfirm = serverConfirm;

        this.tcpStatus = 0;

        this.selectionKey = selectionKey;
    }

    // 记录流量
    public void logBytesCount(int newBytesCount) {
        this.bytesCount += newBytesCount;
    }

    public int getBytesCount() {
        return bytesCount;
    }

    public void setBytesCount(int bytesCount) {
        this.bytesCount = bytesCount;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }

    public long getServerSerial() {
        return serverSerial;
    }

    public void setServerSerial(long serverSerial) {
        this.serverSerial = serverSerial;
    }

    public long getConfirm() {
        return confirm;
    }

    public void setConfirm(long confirm) {
        this.confirm = confirm;
    }

    public long getServerConfirm() {
        return serverConfirm;
    }

    public void setServerConfirm(long serverConfirm) {
        this.serverConfirm = serverConfirm;
    }

    public int getTcpStatus() {
        return tcpStatus;
    }

    public void setTcpStatus(int tcpStatus) {
        this.tcpStatus = tcpStatus;
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    public void setSourceIpAddress(String sourceIpAddress) {
        this.sourceIpAddress = sourceIpAddress;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestIpAddress() {
        return destIpAddress;
    }

    public void setDestIpAddress(String destIpAddress) {
        this.destIpAddress = destIpAddress;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }
}

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

    // ip地址
    private String ipAddress;
    // 端口号
    private int port;

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
            String ipAddress,
            int port,
            int bytesCount,
            long serial,
            long serverSerial,
            long confirm,
            long serverConfirm,
            SelectionKey selectionKey
    ) {
        this.ipAddress = ipAddress;
        this.port = port;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
}

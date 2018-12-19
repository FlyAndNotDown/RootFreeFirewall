package com.nuaa.is.rootfreefirewall.model;

import android.util.Log;

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
    private long serial;
    // 确认号
    private long confirm;
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
    // 数据
    private byte [] data;

    // 构造
    public TcpPacket(byte [] bytes) {
        // 先将所有字节复制一份
        this.bytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            this.bytes[i] = bytes[i];
        }

        // 开始解构
        // 源端口
        this.sourcePort = (this.bytes[0] << 8) + this.bytes[1];
        // 目的端口
        this.destPort = (this.bytes[2] << 8) + this.bytes[3];
        // 序号
        this.serial = (this.bytes[4] << 24) + (this.bytes[5] << 16) +
                (this.bytes[6] << 8) + this.bytes[7];
        // 确认号
        this.confirm = (this.bytes[8] << 24) + (this.bytes[9] << 16) +
                (this.bytes[10] << 8) + this.bytes[11];
        // 头部长度
        this.headerLength = (this.bytes[12] & 0xf0) >>> 4;
        // 保留
        this.hold = (this.bytes[12] & 0x0f) << 2 + (this.bytes[13] & 0xc0) >>> 6;
        // urg
        this.urg = (this.bytes[13] & 0x20) >>> 5 == 1;
        // ack
        this.ack = (this.bytes[13] & 0x10) >>> 4 == 1;
        // psh
        this.psh = (this.bytes[13] & 0x08) >>> 3 == 1;
        // rst
        this.rst = (this.bytes[13] & 0x04) >>> 2 == 1;
        // syn
        this.syn = (this.bytes[13] & 0x02) >>> 1 == 1;
        // fin
        this.fin = (this.bytes[13] & 0x01) == 1;
        // 窗口大小
        this.windowSize = this.bytes[14] << 8 + this.bytes[15];
        // 校验和
        this.checksum = this.bytes[16] << 8 + this.bytes[17];
        // 紧急指针
        this.seriousPoint = this.bytes[18] << 8 + this.bytes[19];
        // 数据
        this.data = new byte[this.bytes.length - this.headerLength];
        for (int i = this.headerLength; i < this.bytes.length; i++) {
            this.data[i - this.headerLength] = this.bytes[i];
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getDestPort() {
        return destPort;
    }

    public long getSerial() {
        return serial;
    }

    public long getConfirm() {
        return confirm;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public int getHold() {
        return hold;
    }

    public boolean getUrg() {
        return urg;
    }

    public boolean getAck() {
        return ack;
    }

    public boolean getPsh() {
        return psh;
    }

    public boolean getRst() {
        return rst;
    }

    public boolean getSyn() {
        return syn;
    }

    public boolean getFin() {
        return fin;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getChecksum() {
        return checksum;
    }

    public int getSeriousPoint() {
        return seriousPoint;
    }

    public byte[] getData() {
        return data;
    }

    // 打印主要信息
    public void printMainInfo() {
        String all = "";
        int temp;
        for (int i = 0; i < this.bytes.length;) {
            temp = this.bytes[i++] & 0xff;
            all += temp + " ";
            if (i % 4 == 0) all += "\t";
            if (i % 8 == 0) all += "\n";
        }

        Log.i(
                "firewallDebug",
                String.format(
                        "TCP packet: \n" +
                                "\tsourcePort: %d\tdestPort: %d\tserial: %d\n" +
                                "\tconfirmNumber: %d\theaderLength: %d\n" +
                                "\turg: %b\tack: %b\tpsh: %b\n" +
                                "\trst: %b\tsyn: %b\tfin: %b\n" +
                                "%s",
                        this.getSourcePort(),
                        this.getDestPort(),
                        this.getSerial(),
                        this.getConfirm(),
                        this.getHeaderLength(),
                        this.getUrg(),
                        this.getAck(),
                        this.getPsh(),
                        this.getRst(),
                        this.getSyn(),
                        this.getFin(),
                        all
                )
        );
    }
}

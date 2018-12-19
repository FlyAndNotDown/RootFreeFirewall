package com.nuaa.is.rootfreefirewall.model;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * IP包头
 * @author John Kindem
 * @version v1.0
 */
public class IpPacket {

    // 上层协议矩阵
    private static final String [] PROTOCOL_VERTEX = new String[] {
            "None",
            "ICMP",
            "IGMP",
            "GGP",
            "None",
            "ST",
            "TCP",
            "EGP",
            "IGP",
            "NVP",
            "UDP"
    };

    // 服务类型结构体
    public class ServiceType {
        // 优先级 (0 - 8)
        private int priority;
        // 短延迟位
        private boolean delay;
        // 高吞吐量位
        private boolean throughput;
        // 高可靠位
        private boolean reliability;
        // 保留位
        private int hold;

        // 构造
        public ServiceType(byte source) {
            this.priority = (source & 0xe0) >>> 5;
            this.delay = ((source & 0x10) >>> 4) == 1;
            this.throughput = ((source & 0x08) >>> 3) == 1;
            this.reliability = ((source & 0x04) >>> 2) == 1;
            this.hold = source & 0x03;
        }

        public int getPriority() {
            return priority;
        }

        public boolean isDelay() {
            return delay;
        }

        public boolean isThroughput() {
            return throughput;
        }

        public boolean isReliability() {
            return reliability;
        }

        public int getHold() {
            return hold;
        }
    }

    // 所有字节
    private byte [] bytes;
    // 版本号
    private int version;
    // 头部长度
    private int headerLength;
    // 服务类型
    private ServiceType serviceType;
    // 总长度
    private int totalLength;
    // 标识
    private byte [] flag;
    // 标志
    private int mark;
    // 片偏移
    private int offset;
    // time to live
    private int ttl;
    // 上层协议
    private String protocol;
    // 校验和
    private int checksum;
    // 源 IP
    private String sourceIpAddress;
    // 目的 IP
    private String destIpAddress;
    // 数据
    private byte [] data;

    // 构造
    public IpPacket(ByteBuffer byteBuffer, int packetLength) {
        // 将所有字节存入变量
        this.bytes = new byte[packetLength];
        for (int i = 0; i < packetLength; i++) {
            this.bytes[i] = byteBuffer.array()[i];
        }

        // 开始解构
        // 版本号
        this.version = (this.bytes[0] & 0xf0) >>> 4;

        // 头长度
        this.headerLength = this.bytes[0] & 0x0f;

        // 服务类型
        this.serviceType = new ServiceType(this.bytes[1]);

        // 总长度
        this.totalLength = this.bytes[2] << 8 + this.bytes[3];

        // 标识
        this.flag = new byte[2];
        for (int i = 0; i < 2; i++) this.flag[i] = this.bytes[4 + i];

        // 标志
        this.mark = (this.bytes[6] & 0xe0) >>> 5;

        // 片偏移
        this.offset = (this.bytes[6] & 0x1f) << 8 + (this.bytes[7]);

        // ttl
        this.ttl = this.bytes[8];

        // 上层协议
        this.protocol = this.bytes[9] >= 0 & this.bytes[9] < PROTOCOL_VERTEX.length ?
                PROTOCOL_VERTEX[this.bytes[9]] :
                "None";

        // 校验和
        this.checksum = this.bytes[10] << 8 + this.bytes[11];

        // 源地址
        this.sourceIpAddress = "";
        for (int i = 0; i < 4; i++) {
            int temp = this.bytes[12 + i] & 0xff;
            sourceIpAddress += temp;
            if (i != 3) sourceIpAddress += ".";
        }

        // 目的地址
        this.destIpAddress = "";
        for (int i = 0; i < 4; i++) {
            int temp = this.bytes[16 + i] & 0xff;
            destIpAddress += temp;
            if (i != 3) destIpAddress += ".";
        }

        // 数据区
        if (this.bytes.length > headerLength) {
            this.data = new byte[this.bytes.length - headerLength];
            for (int i = headerLength; i < this.bytes.length; i++) {
                this.data[i - headerLength] = this.bytes[i];
            }
        } else {
            this.data = null;
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getVersion() {
        return version;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public byte[] getFlag() {
        return flag;
    }

    public int getMark() {
        return mark;
    }

    public int getOffset() {
        return offset;
    }

    public int getTtl() {
        return ttl;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getChecksum() {
        return checksum;
    }

    public String getSourceIpAddress() {
        return sourceIpAddress;
    }

    public String getDestIpAddress() {
        return destIpAddress;
    }

    public byte[] getData() {
        return data;
    }

    // 打印关键信息
    public void printMainInfo() {
        String all = "";
        int temp;
        for (int i = 0; i < this.bytes.length;) {
            temp = this.bytes[i++] & 0xff;
            all += temp + " ";
            if (i % 4 == 0) all += "\t";
            if (i % 8 == 0) all += "\n";
        }
        // 日志信息
        Log.i(
                "firewallDebug",
                String.format(
                        "get a IP packet: \n" +
                                "\tversion: %d\theaderLength: %d\ttotalLength: %d\n" +
                                "\toffset: %d\tttl: %d\tprotocol: %s\n" +
                                "\tsourceIpAddress: %s\t destIpAddress: %s\n" +
                                "%s",
                        this.getVersion(),
                        this.getHeaderLength(),
                        this.getTotalLength(),
                        this.getOffset(),
                        this.getTtl(),
                        this.getProtocol(),
                        this.getSourceIpAddress(),
                        this.getDestIpAddress(),
                        all
                )
        );
    }
}

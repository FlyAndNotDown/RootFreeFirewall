package com.nuaa.is.rootfreefirewall.model;

/**
 * 配合短信拦截后端使用的数据结构
 */
public class Phone {

    // 手机号码
    private String number;
    // 被举报次数
    private int blockNum;

    public Phone() {
        this.number = "";
        this.blockNum = 0;
    }

    public Phone(String number, int blockNum) {
        this.number = number;
        this.blockNum = blockNum;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }
}

package com.nuaa.is.rootfreefirewall.model;

/**
 * 隔离区短信 Java Bean
 */
public class SandboxSms {

    // 发送者手机号
    private String phoneNumer;
    // 危险程度
    private String dangerousDegree;
    // 日期
    private String date;
    // 短信内容
    private String content;

    // 构造
    public SandboxSms() {
        this.phoneNumer = "";
        this.dangerousDegree = "";
        this.date = "";
        this.content = "";
    }

    public SandboxSms(
            String phoneNumer,
            String dangerousDegree,
            String date,
            String content
    ) {
        this.phoneNumer = phoneNumer;
        this.dangerousDegree = dangerousDegree;
        this.date = date;
        this.content = content;
    }

    public String getPhoneNumer() {
        return phoneNumer;
    }

    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }

    public String getDangerousDegree() {
        return dangerousDegree;
    }

    public void setDangerousDegree(String dangerousDegree) {
        this.dangerousDegree = dangerousDegree;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

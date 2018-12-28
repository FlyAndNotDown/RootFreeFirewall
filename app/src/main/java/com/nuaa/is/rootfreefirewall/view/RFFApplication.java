package com.nuaa.is.rootfreefirewall.view;

import android.app.Application;
import android.content.Context;

import com.nuaa.is.rootfreefirewall.model.Phone;
import com.nuaa.is.rootfreefirewall.model.SandboxSms;
import com.nuaa.is.rootfreefirewall.view.adapter.MessageSandboxAdapter;

import java.util.List;

public class RFFApplication extends Application {

    // 拦截数据库
    private List<Phone> phones;
    // 短信沙盒数据、适配器
    private List<SandboxSms> sandboxSmsList;
    private MessageSandboxAdapter messageSandboxAdapter;

    private int warningNum = 20;

    public int getWarningNum() {
        return warningNum;
    }

    public void setWarningNum(int warningNum) {
        this.warningNum = warningNum;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public List<SandboxSms> getSandboxSmsList() {
        return sandboxSmsList;
    }

    public void setSandboxSmsList(List<SandboxSms> sandboxSmsList) {
        this.sandboxSmsList = sandboxSmsList;
    }

    public MessageSandboxAdapter getMessageSandboxAdapter() {
        return messageSandboxAdapter;
    }

    public void setMessageSandboxAdapter(MessageSandboxAdapter messageSandboxAdapter) {
        this.messageSandboxAdapter = messageSandboxAdapter;
    }
}

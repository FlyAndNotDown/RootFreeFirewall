package com.nuaa.is.rootfreefirewall.model;

import android.graphics.drawable.Drawable;

/**
 * App信息 JavaBean
 */
public class AppInfo {

    // 图标
    private Drawable icon;
    // 应用名
    private String name;
    // 包名
    private String packageName;
    // 防火墙是否激活
    private boolean firewallActive;

    public AppInfo() {
        this.icon = null;
        this.name = "";
        this.packageName = "";
        this.firewallActive = false;
    }

    public AppInfo(Drawable icon, String name, String packageName) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.firewallActive = false;
    }

    public AppInfo(Drawable icon, String name, String packageName, boolean firewallActive) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
        this.firewallActive = firewallActive;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isFirewallActive() {
        return firewallActive;
    }

    public void setFirewallActive(boolean firewallActive) {
        this.firewallActive = firewallActive;
    }
}

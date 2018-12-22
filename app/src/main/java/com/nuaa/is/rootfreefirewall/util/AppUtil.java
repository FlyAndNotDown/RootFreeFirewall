package com.nuaa.is.rootfreefirewall.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nuaa.is.rootfreefirewall.model.AppInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App 工具
 */
public class AppUtil {
    private static String TAG = "RFF-AppUtil";

    public static List<AppInfo> getUserAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();

        // 获取包管理器
        PackageManager packageManager = context.getPackageManager();
        // 获取所有包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        // 打开数据文件
        boolean flowAllowListFileFound = true;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput("appFlowAllowList");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(AppUtil.TAG, "appFlowAllowList file not found");
            flowAllowListFileFound = false;
        }

        // 获取应用包名与流量控制关系
        Map<String, Boolean> packageNameToFlowAllow = new HashMap<>();
        byte [] bytes = null;
        if (flowAllowListFileFound) {
            // 读取数据
            try {
                bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(AppUtil.TAG, "get a IO exception when read data from file");
            }
        }
        String string = bytes == null ? "" : new String(bytes);
        if (!string.isEmpty()) {
            for (String line : string.split("\n")) {
                packageNameToFlowAllow.put(line.split(" ")[0], Boolean.valueOf(line.split(" ")[1]));
            }
        }

        // 存取信息
        for (PackageInfo packageInfo : packageInfos) {
            // 判断是否为系统级别应用，不是系统级别应用才添加
            Drawable icon = packageInfo.applicationInfo.loadIcon(packageManager);
            String name = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString();
            String packageName = packageInfo.applicationInfo.packageName;

            // 查看是否允许流量通过
            boolean flowAllow;
            try {
                flowAllow = packageNameToFlowAllow.get(packageName);
            } catch (NullPointerException e) {
                flowAllow = true;
            }

            int flags = packageInfo.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appInfos.add(
                        new AppInfo(
                                icon,
                                name,
                                packageName,
                                flowAllow
                        )
                );
            }
        }

        return appInfos;
    }

}

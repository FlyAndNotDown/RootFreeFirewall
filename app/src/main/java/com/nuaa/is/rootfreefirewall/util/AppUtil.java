package com.nuaa.is.rootfreefirewall.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.nuaa.is.rootfreefirewall.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * App 工具
 */
public class AppUtil {

    public static List<AppInfo> getUserAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();

        // 获取包管理器
        PackageManager packageManager = context.getPackageManager();
        // 获取所有包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        // 存取信息
        for (PackageInfo packageInfo : packageInfos) {
            // 判断是否为系统级别应用，不是系统级别应用才添加
            int flags = packageInfo.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appInfos.add(
                        new AppInfo(
                                packageInfo.applicationInfo.loadIcon(packageManager),
                                packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                                packageInfo.applicationInfo.packageName
                        )
                );
            }
        }

        return appInfos;
    }

}

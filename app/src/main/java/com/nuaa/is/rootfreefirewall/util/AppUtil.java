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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * App 工具
 */
public class AppUtil {
    // TAG
    private static String TAG = "RFF-AppUtil";
    // 文件名
    private static String SAVE_FILE_NAME = "appFlowAllowList";

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
            fileInputStream = context.openFileInput(AppUtil.SAVE_FILE_NAME);
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
            StringTokenizer stringTokenizer = new StringTokenizer(string, "\n");
            while (stringTokenizer.hasMoreTokens()) {
                StringTokenizer stringTokenizer1 = new StringTokenizer(stringTokenizer.nextToken(), " ");
                packageNameToFlowAllow.put(
                        stringTokenizer1.nextToken(),
                        Boolean.valueOf(stringTokenizer1.nextToken())
                );
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

    // 储存信息到文件
    public static void save(Context context, List<AppInfo> appInfos) {

        // 尝试打开文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(AppUtil.SAVE_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(AppUtil.TAG, "Can't open file");
        }

        // 开始写入内容
        if (fileOutputStream != null) {
            String result = "";
            for (AppInfo appInfo : appInfos) {
                result += appInfo.getPackageName() + " " + appInfo.isFlowAllow() + "\n";
            }
            try {
                fileOutputStream.write(result.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(AppUtil.TAG, "get a IO exception");
            }
        }

    }

}

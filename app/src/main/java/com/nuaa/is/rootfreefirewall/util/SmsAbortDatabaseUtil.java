package com.nuaa.is.rootfreefirewall.util;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.nuaa.is.rootfreefirewall.model.Phone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 短信拦截数据库工具
 */
public class SmsAbortDatabaseUtil {

    // server config 位置
    private static final String SERVER_CONFIG_PATH = "config/server.json";
    private static final String SERVER_CONFIG__HOST_KEY = "host";

    // RESTful API 地址
    private static final String RESTFUL_API_URL = "/request/rff/sms/phone";

    // TAG
    private static String TAG = "RFF-SmsAbortDatabaseUtil";
    // 文件名
    private static String SAVE_FILE_NAME = "smsAbortDatabase";

    // 获取完整 RESTful API 地址
    private static String getFullRESTfulAPIAddress(Context context) {
        JSONObject jsonObject = null;
        try {
            jsonObject = ConfigUtil.getJSONConfig(context, SmsAbortDatabaseUtil.SERVER_CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(SmsAbortDatabaseUtil.TAG, "Can't parse config to JSON object");
        }
        return jsonObject == null ?
                "" :
                SmsAbortDatabaseUtil.RESTFUL_API_URL + jsonObject.getString(SmsAbortDatabaseUtil.SERVER_CONFIG__HOST_KEY);
    }

    // 获取信息
    public static List<Phone> getPhoneList(Context context) {
        List<Phone> phones = new ArrayList<>();

        // 打开数据文件
        boolean fileFound = true;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(SmsAbortDatabaseUtil.SAVE_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(SmsAbortDatabaseUtil.TAG, "smsAbortDatabase file not found");
            fileFound = false;
        }

        // 读取信息
        byte [] bytes = null;
        if (fileFound) {
            // 读取数据
            try {
                bytes = new byte[fileInputStream.available()];
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(SmsAbortDatabaseUtil.TAG, "get a IO exception");
            }
        }
        String string = bytes == null ? "" : new String(bytes);
        if (!string.isEmpty()) {
            StringTokenizer stringTokenizerLine = new StringTokenizer(string, "\n");
            while (stringTokenizerLine.hasMoreTokens()) {
                StringTokenizer stringTokenizer = new StringTokenizer(stringTokenizerLine.nextToken(), " ");
                phones.add(new Phone(
                        stringTokenizer.nextToken(),
                        Integer.valueOf(stringTokenizer.nextToken())
                ));
            }
        }

        // 返回结果
        return phones;
    }

    // 存储信息
    public static void save(Context context, List<Phone> phones) {
         // 尝试打开文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(SmsAbortDatabaseUtil.SAVE_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(SmsAbortDatabaseUtil.TAG, "Can't open file");
        }

        // 开始写入内容
        if (fileOutputStream != null) {
            String result = "";
            for (Phone phone : phones) {
                result += phone.getNumber() + " " + phone.getBlockNum() + "\n";
            }

            try {
                fileOutputStream.write(result.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(SmsAbortDatabaseUtil.TAG, "get a IO exception");
            }
        }
    }
}

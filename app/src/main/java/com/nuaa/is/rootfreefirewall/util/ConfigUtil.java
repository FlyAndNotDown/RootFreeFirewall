package com.nuaa.is.rootfreefirewall.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 配置工具
 */
public class ConfigUtil {

    // TAG
    private static final String TAG = "RFF-ConfigUtil";

    // buffer size
    private static final int BUFFER_SIZE = 1024;

    // 获取JSON格式的配置
    public static JSONObject getJSONConfig(Context context, String path) throws IOException {
        // 尝试打开文件
        String configString = null;
        try {
            InputStream inputStream = context.getAssets().open(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte [] buffer = new byte[ConfigUtil.BUFFER_SIZE];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            configString = outputStream.toString();
        } catch (IOException e) {
            throw e;
        }

        if (configString == null) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(configString);
        return jsonObject;
    }

}

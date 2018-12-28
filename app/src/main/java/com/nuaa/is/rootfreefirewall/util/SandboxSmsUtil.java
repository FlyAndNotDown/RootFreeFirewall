package com.nuaa.is.rootfreefirewall.util;

import android.content.Context;
import android.util.Log;

import com.nuaa.is.rootfreefirewall.model.SandboxSms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SandboxSmsUtil {

    // TAG
    private static String TAG = "RFF-SandboxSmsUtil";
    // 文件名
    private static String SAVE_FILE_NAME = "sandboxSmsSave";

    // 获取信息
    public static List<SandboxSms> getSandboxSmsList(Context context) {
        List<SandboxSms> sandboxSmsList = new ArrayList<>();

        // 打开数据文件
        boolean found = true;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput(SAVE_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "get a file not found exception");
            found = false;
        }

        // 读取信息
        byte [] bytes = null;
        if (found) {
            // 读取数据
            try {
                bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "get a IO exception");
            }
        }
        String string = bytes == null ? "" : new String(bytes);
        if (!string.isEmpty()) {
            StringTokenizer stringTokenizerLine = new StringTokenizer(string, "~");
            while (stringTokenizerLine.hasMoreTokens()) {
                StringTokenizer stringTokenizer = new StringTokenizer(stringTokenizerLine.nextToken(), "\t");
                sandboxSmsList.add(new SandboxSms(
                        stringTokenizer.nextToken(),
                        stringTokenizer.nextToken(),
                        stringTokenizer.nextToken(),
                        stringTokenizer.nextToken()
                ));
            }
        }

        return sandboxSmsList;
    }

    public static void save(Context context, List<SandboxSms> sandboxSmsList) {
        // 尝试打开文件
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "get a file not found exception");
        }

        // 写入内容
        if (fileOutputStream != null) {
            String result = "";
            for (SandboxSms sandboxSms : sandboxSmsList) {
                result += sandboxSms.getPhoneNumer() + "\t" +
                        sandboxSms.getDangerousDegree() + "\t" +
                        sandboxSms.getDate() + "\t" +
                        sandboxSms.getContent() + "~";
            }

            try {
                fileOutputStream.write(result.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "get a IO exception");
            }
        }
    }

}

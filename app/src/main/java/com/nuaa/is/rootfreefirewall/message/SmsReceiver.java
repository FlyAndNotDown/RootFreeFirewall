package com.nuaa.is.rootfreefirewall.message;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.nuaa.is.rootfreefirewall.model.Phone;
import com.nuaa.is.rootfreefirewall.model.SandboxSms;
import com.nuaa.is.rootfreefirewall.util.ConfigUtil;
import com.nuaa.is.rootfreefirewall.view.adapter.MessageSandboxAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SmsReceiver extends BroadcastReceiver {

    // TAG
    private static final String TAG = "RFF-SmsReceiver";

    // Message uri
    private static final Uri MESSAGE_INBOX_URI = Uri.parse("content://sms/inbox");

    // 定义 Action
    private static final String ACTION_SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";

    // default value
    private static final int DEFAULT_WARNING_NUM = 20;

    // Context
    Context context;
    // 拦截数据库
    private List<Phone> phones;
    // 短信沙盒数据、适配器
    private List<SandboxSms> sandboxSmsList;
    private MessageSandboxAdapter messageSandboxAdapter;

    // 危险举报数量
    private int warningNum;

    public SmsReceiver(Context context, List<Phone> phones, List<SandboxSms> sandboxSmsList, MessageSandboxAdapter messageSandboxAdapter) {
        this.context = context;
        this.phones = phones;
        this.sandboxSmsList = sandboxSmsList;
        this.messageSandboxAdapter = messageSandboxAdapter;

        try {
            this.warningNum = ConfigUtil.getJSONConfig(this.context, ConfigUtil.CONFIG_PATH__SMS).getInteger("warningNum");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(SmsReceiver.TAG, "get a IO exception");
            this.warningNum = SmsReceiver.DEFAULT_WARNING_NUM;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 日志
        Log.i(SmsReceiver.TAG, "enter onReceive()");

        try {
            // 如果收到了新的短信
            if (SmsReceiver.ACTION_SMS_DELIVER.equals(intent.getAction())) {
                // 获取短信
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object [] objects = (Object []) bundle.get("pdus");
                    final SmsMessage [] smsMessages = new SmsMessage[objects.length];
                    for (int i = 0; i < objects.length; i++) {
                        smsMessages[i] = SmsMessage.createFromPdu((byte []) objects[i], intent.getStringExtra("format"));
                    }

                    // 如果确实收到了短信
                    if (smsMessages.length > 0) {
                        // 获取发送者手机号和短信内容
                        String body = smsMessages[0].getMessageBody();
                        String phoneNumber = smsMessages[0].getOriginatingAddress();
                        long date = smsMessages[0].getTimestampMillis();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
                        Date formatedDate = new Date(date * 1000);

                        // 日志
                        Log.i(SmsReceiver.TAG, "get a message from " + phoneNumber + ":\n" + body);

                        // 判断是否在禁止名单之列
                        boolean found = false;
                        for (Phone phone : this.phones) {
                            if (phone.getNumber().equals(phoneNumber) && phone.getBlockNum() >= this.warningNum) {
                                found = true;
                                break;
                            }
                        }

                        if (found) {
                            // 如果被ban了
                            sandboxSmsList.add(new SandboxSms(
                                    phoneNumber,
                                    "危险",
                                    formatedDate.toString(),
                                    body
                            ));
                        } else {
                            // 如果没有被 ban
                            // 写入短信
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("address", phoneNumber);
                            contentValues.put("type", "1");
                            contentValues.put("read", "0");
                            contentValues.put("body", body);
                            contentValues.put("date", date);
                            contentValues.put("person", "null");
                            context
                                    .getContentResolver()
                                    .insert(SmsReceiver.MESSAGE_INBOX_URI, contentValues);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(SmsReceiver.TAG, "get a exception");
        }
    }

}

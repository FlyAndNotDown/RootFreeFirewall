package com.nuaa.is.rootfreefirewall.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

/**
 * 短信广播接收器
 */
public class MessageService extends BroadcastReceiver {

    // TAG
    private static final String TAG = "RFF-MessageService";

    // 要阻止的号码列表
    private ArrayList<String> preventPhoneNumbers;

    public MessageService(ArrayList<String> preventPhoneNumbers) {
        this.preventPhoneNumbers = preventPhoneNumbers;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Object [] objects = (Object []) bundle.get("pdus");
        // 获取广播中的所有短信
        for (Object object : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte []) object, Telephony.Sms.Intents.SMS_RECEIVED_ACTION);

            // 日志
            Log.i(TAG, smsMessage.getOriginatingAddress());
            Log.i(TAG, smsMessage.getMessageBody());

            // 每当收到一条短信，进行判断，看在不在拒绝列表里，如果在的话
            for (String preventPhoneNumber : this.preventPhoneNumbers) {
                if (smsMessage.getOriginatingAddress().equals(preventPhoneNumber)) {
                    // 拦截下来
                    abortBroadcast();
                }
            }
        }
    }

}

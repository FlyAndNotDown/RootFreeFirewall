package com.nuaa.is.rootfreefirewall.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.transition.Slide;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    // TAG
    private static final String TAG = "RFF-SmsReceiver";

    // Message uri
    private static final Uri MESSAGE_URI = Uri.parse("content://sms");

    // 定义 Action
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 日志
        Log.i(SmsReceiver.TAG, "enter onReceive()");

        Cursor cursor = null;

        try {
            // 如果收到了新的短信
            if (SmsReceiver.ACTION_SMS_RECEIVED.equals(intent.getAction())) {
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

                        // 日志
                        Log.i(SmsReceiver.TAG, "get a message from " + phoneNumber + ":\n" + body);
                    }
                }

                // 查询短信资源池
                cursor = context
                    .getContentResolver()
                    .query(
                        SmsReceiver.MESSAGE_URI,
                        new String [] { "_id", "address", "read", "body", "date" },
                        "read = ?",
                        new String [] { "0" },
                        "date desc"
                    );

                if (null == cursor) {
                    return;
                }

                // TODO
                Log.i(SmsReceiver.TAG, "cursor count: " + cursor.getCount());
                Log.i(SmsReceiver.TAG, "first of cursor is " + cursor.moveToFirst());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(SmsReceiver.TAG, "get a exception");
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

}

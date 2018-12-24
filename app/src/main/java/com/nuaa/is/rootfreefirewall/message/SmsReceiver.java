package com.nuaa.is.rootfreefirewall.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    // TAG
    private static final String TAG = "RFF-SmsReceiver";

    // 定义 Action
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

    }

}

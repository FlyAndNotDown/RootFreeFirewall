package com.nuaa.is.rootfreefirewall.message;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SmsSendService extends Service {

    // TAG
    private static final String TAG = "RFF-SmsSendService";

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(SmsSendService.TAG, "onBind()");
        return null;
    }

}

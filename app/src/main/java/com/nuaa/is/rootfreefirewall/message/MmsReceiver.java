package com.nuaa.is.rootfreefirewall.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MmsReceiver extends BroadcastReceiver {

    // TAG
    private static final String TAG = "RFF=MmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(MmsReceiver.TAG, "onCreate()");
    }

}

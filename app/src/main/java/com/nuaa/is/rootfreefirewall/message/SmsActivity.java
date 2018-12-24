package com.nuaa.is.rootfreefirewall.message;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SmsActivity extends Activity {

    // TAG
    private static final String TAG = "RFF-SmsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(SmsActivity.TAG, "onCreate()");
    }

}

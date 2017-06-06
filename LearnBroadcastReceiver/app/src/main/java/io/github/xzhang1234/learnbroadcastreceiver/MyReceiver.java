package io.github.xzhang1234.learnbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by xiaoyun on 6/6/17.
 */

public class MyReceiver extends BroadcastReceiver {
    final String className = this.getClass().getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(className, "Intent received");
    }
}

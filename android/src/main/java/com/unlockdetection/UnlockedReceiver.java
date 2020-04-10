package com.unlockdetection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.os.PowerManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.view.Window;
import android.view.WindowManager;
import android.content.BroadcastReceiver;

public class UnlockedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {

            Log.d("RNU","detection");

            float r = (float)Math.random();

            SharedPreferences prefs = context.getSharedPreferences(
            "com.quicklocker.app", Context.MODE_PRIVATE);
            float p = prefs.getFloat("proba",0.5f);
            Log.d("RNU",String.valueOf(p));

            if (r<p){
              Intent i = new Intent(context, this.getActivityClass());
              context.startActivity(i);
            }

        }
    }

    private Class getActivityClass() {
        String className = "com.quicklocker.UnlockActivity";
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
      }
    }
}

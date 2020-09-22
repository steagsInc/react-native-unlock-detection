package com.unlockdetection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import java.util.Calendar;
import android.content.Context;
import android.os.PowerManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.view.Window;
import android.view.WindowManager;
import android.content.BroadcastReceiver;
import com.unlockdetection.Probas;

public class UnlockedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {

            Log.d("RNU","detection");

            SharedPreferences prefs = context.getSharedPreferences(
            "com.sphynx.app", Context.MODE_PRIVATE);

              if (Probas.show(prefs)){
                prefs.edit().putInt("UD-count",current+1).apply();
                Intent i = new Intent(context, this.getActivityClass());
                context.startActivity(i);
              }
        }
    }

    private Class getActivityClass() {
        String className = "com.sphynx.UnlockActivity";
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
      }
    }
}

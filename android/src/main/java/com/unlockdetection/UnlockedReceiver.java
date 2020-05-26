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

public class UnlockedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {

            Log.d("RNU","detection");

            float r = (float)Math.random();

            SharedPreferences prefs = context.getSharedPreferences(
            "com.sphynx.app", Context.MODE_PRIVATE);

            resetPerHourCount(prefs);

            float p = prefs.getFloat("proba",0.5f);
            int start = prefs.getInt("start",0);
            int end = prefs.getInt("end",24);
            int max = prefs.getInt("maxPerHour",5);
            int current = prefs.getInt("count",0);

            int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            if((start<end && (currentTime>=start && currentTime<end)) || (start>end && (currentTime>=start || currentTime<end))) {
              if (r<p && current<max){
                prefs.edit().putInt("count",current+1).apply();
                Intent i = new Intent(context, this.getActivityClass());
                context.startActivity(i);
              }
            }
        }
    }

    private void resetPerHourCount(SharedPreferences prefs) {

      int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

      if(prefs.getInt("hour",0)!=hour){

        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("hour",hour);
        editor.putInt("count",0);

        editor.apply();
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

package com.unlockdetection;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder ;
import android.util.Log;
import android.content.Context;
import android.os.PowerManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.view.Window;
import android.view.WindowManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.app.Service ;

public class UnlockReceiverService extends Service
{
 private final UnlockedReceiver unlockReceiver = new UnlockedReceiver();

 private static boolean running = false;
 private static float probablity = 0.5f;

 @Override
 public IBinder onBind(Intent arg0)
 {
  return null;
 }

 @Override
 public void onCreate()
 {
  this.registerReceiver(unlockReceiver, new IntentFilter("android.intent.action.USER_PRESENT"));
  running = true;
  Log.d("RNU",String.valueOf(probablity));
 }

 @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      // The service is starting, due to a call to startService()
      return startId;
  }


 @Override
 public void onDestroy()
 {
  this.unregisterReceiver(unlockReceiver);
  running = false;
 }

  public static boolean isRunning() {
      return running;
  }

  public static void setProbability(float proba) {
    probablity = proba;
  }

}

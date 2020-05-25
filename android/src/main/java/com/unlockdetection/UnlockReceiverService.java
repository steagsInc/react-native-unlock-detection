package com.unlockdetection;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder ;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.view.Window;
import android.view.WindowManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.app.Service ;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.Notification;
import java.util.Calendar;

public class UnlockReceiverService extends Service
{
  private final static String TAG = "RNU";
  private static final String CHANNEL_ID = "channel_sphynx_lock";
  private final int SERVICE_NOTIFICATION_ID = 3159;

 private final UnlockedReceiver unlockReceiver = new UnlockedReceiver();
 private NotificationCompat.Builder notificationBuilder;
 private NotificationManager notificationManager;

 private static boolean running = false;

 @Override
 public IBinder onBind(Intent arg0)
 {
  return null;
 }

 @Override
 public void onCreate()
 {
 }

 @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    this.registerReceiver(unlockReceiver, new IntentFilter("android.intent.action.USER_PRESENT"));
    running = true;

    SharedPreferences prefs = this.getSharedPreferences(
    "com.sphynx.app", Context.MODE_PRIVATE);

    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    if(prefs.getInt("hour",0)!=hour){

      SharedPreferences.Editor editor = prefs.edit();

      editor.putInt("hour",hour);
      editor.putInt("count",0);

      editor.apply();
    }

    // create notif
    createNotificationChannel();
    Resources res = this.getResources();
    String packageName = this.getPackageName();
    int smallIconResId = res.getIdentifier("ic_launcher", "mipmap", packageName);
    notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Sphynx")
      .setContentText("Active")
      .setSmallIcon(smallIconResId)
      .setOngoing(true);
    // start in foreground
    startForeground(SERVICE_NOTIFICATION_ID, notificationBuilder.build());
    return START_STICKY;
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

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Sphynx", importance);
        channel.setDescription("Sphynx locker");
        channel.setSound(null, null);
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
  }

}

package com.unlockdetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Process;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class UnlockDetectionModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Intent service;
    private SharedPreferences prefs;

    public UnlockDetectionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.service = new Intent(this.reactContext, UnlockReceiverService.class);
        this.prefs = reactContext.getSharedPreferences(
        "com.QualiaInteractive.UnlockDetection", Context.MODE_PRIVATE);
    }

    @Override
    public String getName() {
        return "UnlockDetection";
    }

    @ReactMethod
    public void Activate() {
      Log.d("RNU","start");
      this.reactContext.startService(this.service);
    }

    @ReactMethod
    public void Deactivate() {
      Log.d("RNU","stop");
      this.reactContext.stopService(this.service);
    }

    @ReactMethod
    public void setClassName(String className){
      SharedPreferences.Editor editor = prefs.edit();

      editor.putString("ClassName",className);

      editor.apply();
    }

    @ReactMethod
    public void setParameter(float newProbability,int newStart,int newEnd,int newMaxPerHour){
      SharedPreferences.Editor editor = prefs.edit();

      editor.putFloat("UD-proba",newProbability);
      editor.putInt("UD-start",newStart);
      editor.putInt("UD-end",newEnd);
      editor.putInt("UD-maxPerHour",newMaxPerHour);

      editor.apply();
    }

    @ReactMethod
    public void resetCount(){
      prefs.edit().putInt("UD-count",0).apply();
    }

    @ReactMethod
    public void isRunning(Callback booleanCallback) {
      boolean bool = UnlockReceiverService.isRunning();
      booleanCallback.invoke(bool);
    }

    @ReactMethod
    public void quit() {
      android.os.Process.killProcess(android.os.Process.myPid());
    }
}

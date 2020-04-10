package com.unlockdetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

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
        "com.quicklocker.app", Context.MODE_PRIVATE);
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
    public void setProbability(float newProbability){
      this.prefs.edit().putFloat("proba",newProbability).apply();

    }

    @ReactMethod
    public void getProbability(Callback floatCallback){
      float p = prefs.getFloat("proba",0.5f);
      floatCallback.invoke(p);
    }

    @ReactMethod
    public void isRunning(Callback booleanCallback) {
      boolean bool = UnlockReceiverService.isRunning();
      booleanCallback.invoke(bool);
    }
}

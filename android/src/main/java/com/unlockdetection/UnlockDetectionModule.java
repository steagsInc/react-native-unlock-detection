package com.unlockdetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class UnlockDetectionModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private Intent service;

    private static float probablity = 0.5f;

    public UnlockDetectionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.service = new Intent(this.reactContext, UnlockReceiverService.class);
    }

    @Override
    public String getName() {
        return "UnlockDetection";
    }

    @ReactMethod
    public void Activate() {
      Log.d("RNU","start");
      Log.d("RNU",String.valueOf(probablity));
      this.reactContext.startService(this.service);
    }

    @ReactMethod
    public void Deactivate() {
      Log.d("RNU","stop");
      this.reactContext.stopService(this.service);
    }

    @ReactMethod
    public void setProbability(float newProbablity){
      probablity = newProbablity;
      Log.d("RNU",String.valueOf(probablity));
      UnlockReceiverService.setProbability(probablity);
    }

    @ReactMethod
    public void getProbability(Callback floatCallback){
      floatCallback.invoke(probablity);
    }

    @ReactMethod
    public void isRunning(Callback booleanCallback) {
      boolean bool = UnlockReceiverService.isRunning();
      booleanCallback.invoke(bool);
    }
}

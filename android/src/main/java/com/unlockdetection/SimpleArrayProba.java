package com.unlockdetection;

import java.lang.Math;
import java.util.HashSet;
import android.content.SharedPreferences;

public class Probas {

  public static boolean show(SharedPreferences prefs){

    float r = (float)Math.random();

    String type = prefs.getString("type","Uniform");

    switch(type){
      case "Uniform":
        return UniformProba(prefs,r);
        break;
      case "Parabolic":
        return false;
        break;
      case : "Smart":
        return false;
        break;
      default:
        return false;
    }

  }

  private boolean UniformProba(SharedPreferences prefs,float r){

    resetPerHourCount(prefs);

    float p = prefs.getFloat("UD-proba",0.5f);
    int start = prefs.getInt("UD-start",0);
    int end = prefs.getInt("UD-end",24);
    int max = prefs.getInt("UD-maxPerHour",5);
    int current = prefs.getInt("UD-count",0);

    int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    if((start<end && (currentTime>=start && currentTime<end)) || (start>end && (currentTime>=start || currentTime<end))) {
      if (r<p && current<max){
        return true;
      }
    }

    return false;

  }

  private boolean ParabolicProba(SharedPreferences prefs,float r){

    resetPerHourCount(prefs);

    int max = prefs.getInt("UD-maxPerHour",5);
    int current = prefs.getInt("UD-count",0);

    int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    int p = prefs.getFloat("UD-parabola"+currentTime.toString(),0.5f)

    if (r<p && current<max){
      return true;
    }

    return false;

  }

  public static void SimpleArray(float minp, float maxp, int start, int end,SharedPreferences prefs){

    int lenght = start < end ? end-start : 24 - start + end;

    float mid = lenght/2;

    float max = -Math.pow(mid,2)+lenght*mid;

    float[] probas = new float[24];

    for(int i =0;i<24;i++){
      if(start < end){
        if(i<start||i>max){
          probas[i]=0;
        }
        else{
          probas[i]=parabola(i-start,lenght,minp,maxp,max);
        }
      }
      else{
        if(i>start||i<end){
          probas[i]=parabola(i-start,lenght,minp,maxp,max);
        }else{
          probas[i]=0;
        }
      }
    }

    saveArray(probas,"parabola",prefs);

  }

  public static void SpleepArray(){

    float[] sleep = new float[24]

    for(int i=0;i<24;i++){
      sleep[i]=0.5;
    }

    saveArray(sleep,"sleep",prefs);

  }

  private float parabola(x,b,minp,maxp,max){

    return (-Math.pow(x,2)+b*x)/(max*(1/maxp-minp))+minp;

  }

  private void resetPerHourCount(SharedPreferences prefs) {

    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    if(prefs.getInt("UD-hour",0)!=hour){

      SharedPreferences.Editor editor = prefs.edit();

      editor.putInt("UD-hour",hour);
      editor.putInt("UD-count",0);

      editor.apply();
    }

  }

  private void saveArray(float[] array,String key,SharedPreferences prefs){

    SharedPreferences.Editor editor = prefs.edit();

    for (int i = 0;i<array.length;i++){

      editor.putFloat("UD-"+key+i.toString(),array[i])

    }

    editor.apply();

  }

}

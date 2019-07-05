package com.nowarzz.rnepson;

import android.content.BroadcastReceiver;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.Arguments;
import android.content.Context;
import android.content.Intent;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import javax.annotation.Nullable;


public class LocalBroadcastReceiver extends BroadcastReceiver {

  private ReactApplicationContext mReactContext;

  public LocalBroadcastReceiver(ReactApplicationContext reactContext){
    this.mReactContext = reactContext;
  }

  @Override
  public void onReceive(Context context, Intent intent){
    String PrinterName = intent.getStringExtra("PrinterName");
    String Target = intent.getStringExtra("Target");
    WritableMap params = Arguments.createMap();
    params.putString("PrinterName",PrinterName);
    params.putString("Target", Target);
    sendEvent("TMPrinterFound",params);
  }

   private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        this.mReactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
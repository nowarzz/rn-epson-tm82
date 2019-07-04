package com.nowarzz.rnepson;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Promise;


public class RNReactNativeEpsonTm82Module extends ReactContextBaseJavaModule implements PrinterEventListener {

  private final ReactApplicationContext reactContext;

  MyPrinter printer;
  Promise mPromise;

  public RNReactNativeEpsonTm82Module(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNReactNativeEpsonTm82";
  }

  @Override
  public void onInitializeSuccess(String deviceInfo) {
    if(mPromise != null){
      mPromise.resolve(deviceInfo);
      Toast.makeText(getReactApplicationContext(), deviceInfo, Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onInitializeError(Error error) {
    if(mPromise != null){
      mPromise.reject(error.toString());
      Toast.makeText(getReactApplicationContext(), "ERROR:" + error.toString(), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onPrinterClosed(String message) {
    Toast.makeText(getReactApplicationContext(), "Printer closed: " + message, Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void initilize(Promise promise) {
    mPromise = promise;
    printer = new EpsonTM82(getReactApplicationContext(), this);
  }

  @ReactMethod
  public void writeText(String text, ReadableMap property,Promise promise) {
    MyReturnValue res = printer.writeText(text, property);
    if(res.success){
      promise.resolve(message);
    }else{
      promise.reject(message);
    }
  }

  @ReactMethod
  public void writeQRCode(String content, ReadableMap property) {
    printer.writeQRCode(content, property);
  }

  @ReactMethod
  public void writeImage(String path, ReadableMap property) {
    printer.writeImage(path, property);
  }

  @ReactMethod
  public MyReturnValue writeFeed(int length,Promise promise) {
    MyReturnValue res = printer.writeFeed(length);
    if(res.success){
      promise.resolve(res.message);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public MyReturnValue writeCut(ReadableMap property,Promise promise) { 
    MyReturnValue res = printer.writeCut(property);
    if(res.success){
      promise.resolve(res.message);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public MyReturnValue startPrint(Promise promise) {
    MyReturnValue res = printer.startPrint(); 
    if(res.success){
      promise.resolve(res.message);
    }else{
      promise.reject(res.message);
    }
  }
}
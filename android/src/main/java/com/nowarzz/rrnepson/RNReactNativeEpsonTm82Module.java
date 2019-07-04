package com.nowarzz.rnepson;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;


public class RNReactNativeEpsonTm82Module extends ReactContextBaseJavaModule implements PrinterEventListener {

  private final ReactApplicationContext reactContext;

  MyPrinter printer;

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
    Toast.makeText(getReactApplicationContext(), deviceInfo, Toast.LENGTH_LONG).show();
  }

  @Override
  public void onInitializeError(Error error) {
    Toast.makeText(getReactApplicationContext(), "ERROR:" + error.toString(), Toast.LENGTH_LONG).show();
  }

  @Override
  public void onPrinterClosed(String message) {
    Toast.makeText(getReactApplicationContext(), "Printer closed: " + message, Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void initilize(String type) {
    printer = new EpsonTM82(getReactApplicationContext(), this);
  }

  @ReactMethod
  public void writeText(String text, ReadableMap property) {
    printer.writeText(text, property);
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
  public void writeFeed(int length) {
    printer.writeFeed(length);
  }

  @ReactMethod
  public void writeCut(ReadableMap property) { printer.writeCut(property); }

  @ReactMethod
  public void startPrint() { printer.startPrint(); }

  @ReactMethod
  public void endPrint() {
      printer.endPrint();
  }
}
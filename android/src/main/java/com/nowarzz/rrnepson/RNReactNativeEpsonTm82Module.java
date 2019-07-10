package com.nowarzz.rnepson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Promise;

import java.util.List;
import java.util.ArrayList;
import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;

import com.epson.epos2.printer.Printer;

public class RNReactNativeEpsonTm82Module extends ReactContextBaseJavaModule implements PrinterEventListener {

  public static final int WIDTH_58 = 384;
  public static final int WIDTH_80 = 576;

  private int deviceWidth = WIDTH_80;

  private final ReactApplicationContext reactContext;

  private LocalBroadcastReceiver mLocalBroadcastReceiver;

  MyPrinter printer;
  Promise mPromise;
  Promise pPromise;
  EpsonDiscovery discovery;

  public RNReactNativeEpsonTm82Module(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.mLocalBroadcastReceiver = new LocalBroadcastReceiver(reactContext);
    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(reactContext);
    localBroadcastManager.registerReceiver(mLocalBroadcastReceiver, new IntentFilter("TMPrinterFound"));
  }

  @Override
  public String getName() {
    return "RNReactNativeEpsonTm82";
  }

  @Override
  public void onInitializeSuccess(String deviceInfo) {
    if(mPromise != null){
      mPromise.resolve(deviceInfo);
      // Toast.makeText(getReactApplicationContext(), deviceInfo, Toast.LENGTH_LONG).show();
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
    if(pPromise != null){
      if(message != "Printer has successfully closed"){
        pPromise.reject(message);
      }else{
        pPromise.resolve(message);
      }
    }
    // Toast.makeText(getReactApplicationContext(), "Printer closed: " + message, Toast.LENGTH_LONG).show();
  }

  @ReactMethod
  public void initializeDiscovery(Promise promise){
    discovery = new EpsonDiscovery(getReactApplicationContext());
    promise.resolve(null);
  }

  @Override
  public Map<String,Object> getConstants(){
    final Map<String, Object> constants = new HashMap<>();
    constants.put("ALIGN_LEFT", Printer.ALIGN_LEFT);
    constants.put("ALIGN_CENTER", Printer.ALIGN_CENTER);
    constants.put("ALIGN_RIGHT", Printer.ALIGN_RIGHT);
    constants.put("PARAM_DEFAULT", Printer.PARAM_DEFAULT);
    return constants;
  }



  @ReactMethod
  public void startDiscovery(Promise promise){
    MyReturnValue res = discovery.startDiscovery();
    if(res.success){
      promise.resolve(null);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public void stopDiscovery(Promise promise){
    MyReturnValue res = discovery.stopDiscovery();
    if(res.success){
      promise.resolve(null);
    }else{
      promise.reject(res.message);
    }
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
      promise.resolve(res.message);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public void addTextAlign(int align, Promise promise){
    MyReturnValue res = printer.addTextAlign(align);
    if(res.success){
      promise.resolve(null);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public void printColumn(ReadableArray columnWidths, ReadableArray columnAligns, ReadableArray columnTexts, @Nullable ReadableMap options, final Promise promise){
    if(columnWidths.size() != columnTexts.size() || columnWidths.size() != columnAligns.size()){
      promise.reject("COLUMN_WIDTH_ALIGNS_AND_TEXTS_NOT_MATCH");
      return;
    }
    int totalLen = 0;
    for(int i=0;i<columnWidths.size();i++){
      totalLen += columnWidths.getInt(i);
    }
    int maxLen = deviceWidth/8;
    if(totalLen>maxLen){
        promise.reject("COLUNM_WIDTHS_TOO_LARGE");
        return;
    }
    List<List<String>> table = new ArrayList<List<String>>();
    int padding = 1;
    for(int i=0;i<columnWidths.size();i++){
      int width = columnWidths.getInt(i)-padding;
      String text = String.copyValueOf(columnTexts.getString(i).toCharArray());
      List<ColumnSplitedString> splited = new ArrayList<ColumnSplitedString>();
      int shorter = 0;
      int counter = 0;
      String temp = "";
      for(int c=0;c<text.length();c++){
        char ch = text.charAt(c);
        int l = isChinese(ch) ? 2 : 1;
        if(l==2){
          shorter++;
        }
        temp=temp+ch;
        if(counter+1<width){
          counter = counter+1;
        }else{
          splited.add(new ColumnSplitedString(shorter,temp));
          temp = "";
          counter = 0;
          shorter = 0;
        }
      }
      if(temp.length()>0){
        splited.add(new ColumnSplitedString(shorter,temp));
      }
      int align = columnAligns.getInt(i);
      if(align == Printer.PARAM_DEFAULT) align = Printer.ALIGN_LEFT;
      List<String> formated = new ArrayList<String>();
      for(ColumnSplitedString s: splited){
        StringBuilder empty = new StringBuilder();
        for(int w=0;w<(width+padding-s.getShorter());w++){
          empty.append(" ");
        }
        int startIdx = 0;
        String ss = s.getStr();
        if(align == Printer.ALIGN_CENTER && ss.length()<(width-s.getShorter())){
          startIdx = (width-s.getShorter()-ss.length())/2;
          if(startIdx+ss.length()>width-s.getShorter()){
            startIdx--;
          }
          if(startIdx<0){
            startIdx=0;
          }
        }else if(align == Printer.ALIGN_RIGHT && ss.length()<(width-s.getShorter())){
          startIdx = width - s.getShorter() - ss.length();
        }
        empty.replace(startIdx,startIdx+ss.length(),ss);
        formated.add(empty.toString());
      }
      table.add(formated);
    }
    int maxRowCount = 0;
    for(int i=0;i<table.size();i++){
      List<String> row = table.get(i);
      if(row.size()>maxRowCount){maxRowCount = row.size();}
    }
    StringBuilder[] rowsToPrint = new StringBuilder[maxRowCount];
    for(int column=0;column<table.size();column++){
      List<String> rows = table.get(column);
      for(int row=0;row<maxRowCount;row++){
        if(rowsToPrint[row]==null){
          rowsToPrint[row]= new StringBuilder();
        }
        if(row<rows.size()){
          rowsToPrint[row].append(rows.get(row));
        }else{
          int w = columnWidths.getInt(column);
          StringBuilder empty = new StringBuilder();
          for(int i=0;i<w;i++){
            empty.append(" ");
          }
          rowsToPrint[row].append(empty.toString());
        }
      }
    }
    for(int i=0;i<rowsToPrint.length;i++){
      rowsToPrint[i].append("\n\r");
      try{
        MyReturnValue res = printer.writeText(rowsToPrint[i].toString(), options);
        if(!res.success){
          promise.reject(res.message);
          return;
        }
      }catch(Exception e){
        promise.reject("Error");
      }
    }
    promise.resolve(null);
  }

  @ReactMethod
  public void writeQRCode(String content, ReadableMap property, Promise promise) {
    MyReturnValue res = printer.writeQRCode(content, property);
    if(res.success){
      promise.resolve(null);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public void writeImage(String base64encodeStr, @Nullable ReadableMap property, Promise promise) {
    int width = 0;
    int leftPadding = 0;
    if(property != null){
      width = property.hasKey("width") ? property.getInt("width") : 0;
      leftPadding = property.hasKey("left") ? property.getInt("left") :0;
    }
    if(width > deviceWidth || width == 0){
      width = deviceWidth;
    }
    byte[] bytes= Base64.decode(base64encodeStr, Base64.DEFAULT);
    Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    if(mBitmap != null){
      double ratio = (double) mBitmap.getHeight() / (double) mBitmap.getWidth();
      int height = (int) (width * ratio);
      int nMode = 0;
      if(width > 0 && height > 0){
        Bitmap rBitmap = Bitmap.createScaledBitmap(mBitmap,width,height,true);
        MyReturnValue res = printer.writeImage(rBitmap,leftPadding,0,width,height);
        if(res.success){
          promise.resolve(null);
        }else{
          promise.reject(res.message);
        }
      }else{
        promise.reject(String.format("Width and height must be more than 0. Width:%s Height:%s Ratio: %s, Bitmap Width: %s, Bitmap Height: %s",Integer.toString(width), Integer.toString(height), Double.toString(ratio), Integer.toString(mBitmap.getWidth()), Integer.toString(mBitmap.getHeight())));
      }
    }else{
      promise.resolve(null);
    }
  }

  @ReactMethod
  public void writeFeed(int length,Promise promise) {
    MyReturnValue res = printer.writeFeed(length);
    if(res.success){
      promise.resolve(res.message);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public void writeCut(ReadableMap property,Promise promise) { 
    MyReturnValue res = printer.writeCut(property);
    if(res.success){
      promise.resolve(res.message);
    }else{
      promise.reject(res.message);
    }
  }

  @ReactMethod
  public void startPrint(String ipAddress, Promise promise) {
    pPromise = promise;
    MyReturnValue res = printer.startPrint(ipAddress); 
  }

  private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

  private static class ColumnSplitedString{
        private int shorter;
        private String str;

        public ColumnSplitedString(int shorter, String str) {
            this.shorter = shorter;
            this.str = str;
        }

        public int getShorter() {
            return shorter;
        }

        public String getStr() {
            return str;
        }
    }
}
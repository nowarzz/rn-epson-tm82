package com.nowarzz.rnepson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.Arguments;

import java.util.ArrayList;
import java.util.HashMap;

import com.epson.epos2.discovery.Discovery;
import com.epson.epos2.discovery.DiscoveryListener;
import com.epson.epos2.discovery.FilterOption;
import com.epson.epos2.discovery.DeviceInfo;
import com.epson.epos2.Epos2Exception;


import javax.annotation.Nullable;


public class EpsonDiscovery{
    private Context mContext = null;
    private FilterOption mFilterOption = null;

    public EpsonDiscovery(final Context context){
      this.mContext = context;
      this.mFilterOption = new FilterOption();
      this.mFilterOption.setDeviceType(Discovery.TYPE_PRINTER);
      this.mFilterOption.setEpsonFilter(Discovery.FILTER_NAME);
      this.mFilterOption.setPortType(Discovery.PORTTYPE_TCP);
    }

    public MyReturnValue startDiscovery(){
      MyReturnValue res = new MyReturnValue();
      try{
        Discovery.start(this.mContext,mFilterOption, mDiscoveryListener);
      }catch(Epos2Exception e){
        String message;
        int errorStatus = e.getErrorStatus();
        switch (errorStatus) {
          case Epos2Exception.ERR_PARAM:
            message = "Invalid Parameter saat inisialisasi";
            break;
          case Epos2Exception.ERR_ILLEGAL:
            message = "Pencarian sudah dimulai terlebih dahulu";
            break;
          case Epos2Exception.ERR_MEMORY:
            message = "Memory tidak cukup";
            break;
          case Epos2Exception.ERR_FAILURE:
            message = "Unknown error";
            break;
          case Epos2Exception.ERR_PROCESSING:
            message = "Tidak dapat menjalankan operasi";
            break;
          default:
            message = Integer.toString(errorStatus);
            break;
        }
        res.success = false;
        res.message = message;
        return res;
      }
      res.success = true;
      res.message="Sukses";
      return res;
    }

    public MyReturnValue stopDiscovery(){
      MyReturnValue res = new MyReturnValue();
      try{
        Discovery.stop();
      }catch(Epos2Exception e){
        String message;
        int errorStatus = e.getErrorStatus();
        switch (errorStatus) {
          case Epos2Exception.ERR_ILLEGAL:
            message = "Pencarian sudah dimulai terlebih dahulu";
            break;
          case Epos2Exception.ERR_FAILURE:
            message = "Unknown error";
            break;
          default:
            message = Integer.toString(errorStatus);
            break;
        }
        res.success = false;
        res.message = message;
        return res;
      }
      res.success = true;
      res.message="Sukses";
      return res;
    }
    
    private DiscoveryListener mDiscoveryListener = new DiscoveryListener(){
      @Override
      public void onDiscovery(final DeviceInfo deviceInfo){
          Intent intent = new Intent("TMPrinterFound");
          intent.putExtra("PrinterName",deviceInfo.getDeviceName());
          intent.putExtra("Target",deviceInfo.getTarget());
          LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
          localBroadcastManager.sendBroadcast(intent);
        }
      };
} 
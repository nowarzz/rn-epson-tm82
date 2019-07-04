package com.nowarzz.rnepson;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.facebook.react.bridge.ReadableMap;

import com.epson.epos2.Epos2Exception;
import com.epson.epos2.Log;
import com.epson.epos2.printer.Printer;
import com.epson.epos2.printer.PrinterStatusInfo;
import com.epson.epos2.printer.ReceiveListener;

import java.util.Map;

public class EpsonTM82 implements MyPrinter{

    private String TAG = "EPSONPrinter";
    private Printer mPrinter = null;
    private Context reactContext;
    private PrinterEventListener listener;

    public EpsonTM82(final Context context, final PrinterEventListener eventListener) {
        this.reactContext = context;
        this.listener = eventListener;
        try{
            this.mPrinter = new Printer(Printer.TM_T82,Printer.MODEL_SOUTHASIA, context);
        }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_PARAM : message = "Invalid Parameter saat inisialisasi"; break;
                case Epos2Exception.ERR_MEMORY : message = "Memory tidak cukup"; break;
                case Epos2Exception.ERR_UNSUPPORTED : message = "Model tidak didukung"; break;
                default: message = Integer.toString(errorStatus);
            }
            this.listener.onInitializeError(new Error(message));
        } 
        this.listener.onInitializeSuccess("Success Connected");       
    }

    public void connect(){
        

    }

    @Override
    public MyReturnValue writeText(String text, ReadableMap property) {
        MyReturnValue res = new MyReturnValue();
        if(this.mPrinter == null){
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        try{
            this.mPrinter.addText(text);
        }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_PARAM: message = "Parameter Invalid"; break;
                case Epos2Exception.ERR_MEMORY: message = "Out of memory"; break;
                case Epos2Exception.ERR_FAILURE: message = "Unknown Error"; break;
                default: message = Integer.toString(errorStatus);
            }
            Toast.makeText(this.reactContext,message,1).show();
            res.success = false;
            res.message = message;
            return res;
        }
        res.success = true;
        res.message = "Added";
    }

    @Override
    public void writeQRCode(String content, ReadableMap property) {
        Toast.makeText(this.reactContext,"Currently Not Supported",1).show();
    }

    @Override
    public void writeImage(String path, ReadableMap property) {
        Toast.makeText(this.reactContext,"Currently Not Supported",1).show();
    }

    @Override
    public MyReturnValue writeFeed(int length) {
       MyReturnValue res = new MyReturnValue();
       if(this.mPrinter == null){
           res.success = false;
           res.message = "Printer belum di inisiasi";
           return res;
       }
       try{
           this.mPrinter.addFeedLine(length);
       }catch(Epos2Exception e){
           String message;
           int errorStatus = e.getErrorStatus();
           switch(errorStatus){
               case Epos2Exception.ERR_PARAM : message = "Invalid parameter"; break;
               case Epos2Exception.ERR_MEMORY: message = "Out of Memory"; break;
               case Epos2Exception.ERR_FAILURE : message = "Unknown Error"; break;
               default: message = Integer.toString(errorStatus);
           }
           res.success = false;
           res.message = message;
           return res;
       }
       res.success = true;
       res.message = "Sukses";
       return res;
    }

    @Override
    public void writeCut(ReadableMap property) {
       MyReturnValue res = new MyReturnValue();
       if(this.mPrinter == null){
           res.success = false;
           res.message = "Printer belum di inisiasi";
           return res;
       }
       try{
           this.mPrinter.addCut(Printer.PARAM_DEFAULT);
       }catch(Epos2Exception e){
           String message;
           int errorStatus = e.getErrorStatus();
           switch(errorStatus){
               case Epos2Exception.ERR_PARAM : message = "Invalid parameter"; break;
               case Epos2Exception.ERR_MEMORY: message = "Out of Memory"; break;
               case Epos2Exception.ERR_FAILURE : message = "Unknown Error"; break;
               default: message = Integer.toString(errorStatus);
           }
           res.success = false;
           res.message = message;
           return res;
       }
       res.success = true;
       res.message = "Sukses";
       return res;
    }

    private boolean connectPrinter() {
        boolean isBeginTransaction = false;
        if this.(mPrinter == null) {
            return false;
        }
        try {
            this.mPrinter.connect("TCP:192.168.0.117", Printer.PARAM_DEFAULT);
        }
        catch (Epos2Exception e) {
             String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_PARAM: message = "Parameter Invalid"; break;
                case Epos2Exception.ERR_CONNECT: message = "Gagal terkoneksi dengan printer"; break;
                case Epos2Exception.ERR_TIMEOUT: message = "Koneksi ke printer timeout"; break;
                case Epos2Exception.ERR_ILLEGAL: message = "Koneksi printer telah terbuka"; break;
                case Epos2Exception.ERR_MEMORY: message = "Out of memory"; break;
                case Epos2Exception.ERR_FAILURE: message = "Unknown Error"; break;
                case Epos2Exception.ERR_PROCESSING: message = "Tidak dapat menjalankan operasi"; break;
                case Epos2Exception.ERR_NOT_FOUND: message = "Printer tidak ditemukan"; break;
                case Epos2Exception.ERR_IN_USE: message = "Printer sedang dipakai"; break;
                case Epos2Exception.ERR_TYPE_INVALID: message = "Printer bukan TM 82"; break;
                default: message = "Error";
            }
            Toast.makeText(this.reactContext,message,1).show();
            return false;
        }
        try {
            mPrinter.beginTransaction();
            isBeginTransaction = true;
        }
        catch (Exception e) {
            
        }
        if (isBeginTransaction == false) {
            try {
                this.mPrinter.disconnect();
            }
            catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }
        return true;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status){
        String warrningsMsg = "";
        if(status == null) return;
        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += getString("Kertas Sudah Mau Habis";
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += getString("Baterai sudah mau habis");
        }

        if(warningsMsg.length > 0){
            Toast.makeText(this.reactContext,warrningsMsg,1).show();
        }
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        }
        else if (status.getOnline() == Printer.FALSE) {
            return false;
        }
        else {
            ;//print available
        }

        return true;
    }

    @Override
    public MyReturnValue startPrint() {
        MyReturnValue res = new MyReturnValue();
        if(this.mPrinter == null){
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        if(!connectPrinter()){
            res.success = false;
            res.message = "Gagal terhubung dengan printer";
            return res;
        }
        PrinterStatusInfo status = mPrinter.getStatus();
        dispPrinterWarnings(status);
        if(!isPrintable(status)){
            try{
                this.mPrinter.disconnect();
            }catch( Exception e){

            }
            res.success = false;
            res.message = "Printer tidak siap print";
            return res;
        }
        try{
            this.mPrinter.sendData(Printer.PARAM_DEFAULT);
        }catch(Epos2Exception e){
            try{
                this.mPrinter.disconnect();
            }catch( Exception e){

            }
            String message;
           int errorStatus = e.getErrorStatus();
           switch(errorStatus){
               case Epos2Exception.ERR_PARAM : message = "Invalid parameter"; break;
               case Epos2Exception.ERR_MEMORY: message = "Out of Memory"; break;
               case Epos2Exception.ERR_FAILURE : message = "Unknown Error"; break;
               default: message = Integer.toString(errorStatus);
           }
           res.success = false;
           res.message = message;
           return res;
        }
        this.mPrinter.clearCommandBuffer();
        this.mPrinter.setReceiveeEventListener(null);
        this.onPrinterClosed("Berhasil ditutup");
        this.mPrinter = null;
        res.success = true;
        res.message = "Berhasil";
        return res;
    }



}
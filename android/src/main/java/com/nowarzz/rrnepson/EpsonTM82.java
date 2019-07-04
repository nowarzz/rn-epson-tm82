package com.nowarzz.rnepson;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
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
        Boolean connected = false;
        try{
            this.mPrinter = new Printer(Printer.TM_T82,Printer.MODEL_SOUTHASIA, context);
        }catch(Epos2Exception e){
            this.listener.onInitializeError(new Error(e));
        }
        try{
            this.mPrinter.connect("TCP:192.168.0.117", Printer.PARAM_DEFAULT);
            PrinterStatusInfo mPrinterConn = this.mPrinter.getStatus();
            connected = mPrinterConn.getConnection() == Printer.TRUE && mPrinterConn.getOnline() == Printer.TRUE;
        }catch(Epos2Exception e){
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
            this.listener.onInitializeError(new Error(message));
            return;
        }
        if(connected){
            try{
                this.mPrinter.disconnect();
                this.listener.onInitializeSuccess("Success connected to 192.168.0.117");
            }catch(Epos2Exception e){
String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_ILLEGAL: message = "Koneksi printer telah terbuka"; break;
                case Epos2Exception.ERR_MEMORY: message = "Out of memory"; break;
                case Epos2Exception.ERR_FAILURE: message = "Unknown Error"; break;
                case Epos2Exception.ERR_PROCESSING: message = "Tidak dapat menjalankan operasi"; break;
                case Epos2Exception.ERR_DISCONNECT : message = "Gagal memutuskan koneksi dengan printer"; break;
                default: message = "Error";
            }
            Toast.makeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
            }
        }
    }

    @Override
    public void writeText(String text, ReadableMap property) {
        try{
            this.mPrinter.connect("TCP:192.168.0.117", Printer.PARAM_DEFAULT);
        }catch(Epos2Exception e){
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
            this.listener.onInitializeError(new Error(message));
            return;
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
                default: message = "Error";
            }
            Toast.makeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
        }
        try{
                this.mPrinter.disconnect();
                this.listener.onInitializeSuccess("Success connected to 192.168.0.117");
            }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_ILLEGAL: message = "Koneksi printer telah terbuka"; break;
                case Epos2Exception.ERR_MEMORY: message = "Out of memory"; break;
                case Epos2Exception.ERR_FAILURE: message = "Unknown Error"; break;
                case Epos2Exception.ERR_PROCESSING: message = "Tidak dapat menjalankan operasi"; break;
                case Epos2Exception.ERR_DISCONNECT : message = "Gagal memutuskan koneksi dengan printer"; break;
                default: message = "Error";
            }
            Toast.makeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
            }
    }

    @Override
    public void writeQRCode(String content, ReadableMap property) {

    }

    @Override
    public void writeImage(String path, ReadableMap property) {
       
    }

    @Override
    public void writeFeed(int length) {
       
    }

    @Override
    public void writeCut(ReadableMap property) {
       
    }

    @Override
    public void startPrint() {
        
    }

    @Override
    public void endPrint() {
    }

}
package com.reactlibrary;
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

public class EpsonTM82 implements Printer{

    private String TAG = "EPSONPrinter";
    private Printer mPrinter = null;
    private Context reactContext;
    private PrinterEventListener listener;

    public EpsonTM82(final Context context, final PrinterEventListener eventListener) {
        this.reactContext = context;
        this.listener = eventListener;
        Boolean connected = false;
        try{
            this.mPrinter = new Printer(Printer.TM_T82,Printer.MODEL_SOUHTASIA);
        }catch(Epos2Exception e){
            this.listener.onInitializeError(new Error(e));
        }
        try{
            this.mPrinter.connect("TCP:192.168.0.117", Printer.PARAM_DEFAULT);
            PrinterStatusInfo mPrinterConn = this.mPrinter.getStatus();
            connected = mPrinterConn.connection == Printer.TRUE && mPrinterConn.online == Printer.TRUE;
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
                case Epos2Exception.ERR_TYPE_INVALID: "Printer bukan TM 82"; break;
            }
            Toast.makeeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
        }
        if(connected){
            try{
                this.mPrinter.disconnect()
                this.listener.onInitializeSuccess("Success connected to 192.168.0.117");
            }catch(Epos2Exception e){
String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_ILLEGAL: message = "Koneksi printer telah terbuka"; break;
                case Epos2Exception.ERR_MEMORY: message = "Out of memory"; break;
                case Epos2Exception.ERR_FAILURE: message = "Unknown Error"; break;
                case Epos2Exception.ERR_PROCESSING: message = "Tidak dapat menjalankan operasi"; break;
                case Epos2Exception.DISCONNECT : message = "Gagal memutuskan koneksi dengan printer"; break;
            }
            Toast.makeeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
            }
        }
    }

    @Override
    public void writeText(String text, ReadableMap property) {
        try{
            this.mPrinter.connect("TCP:192.168.0.117", Printer.PARAM_DEFAULT);
            PrinterStatusInfo mPrinterConn = this.mPrinter.getStatus();
            connected = mPrinterConn.connection == Printer.TRUE && mPrinterConn.online == Printer.TRUE;
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
                case Epos2Exception.ERR_TYPE_INVALID: "Printer bukan TM 82"; break;
            }
            Toast.makeeText(this.reactContext,message,1).show();
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
            }
            Toast.makeeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
        }
        try{
                this.mPrinter.disconnect()
                this.listener.onInitializeSuccess("Success connected to 192.168.0.117");
            }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch(errorStatus){
                case Epos2Exception.ERR_ILLEGAL: message = "Koneksi printer telah terbuka"; break;
                case Epos2Exception.ERR_MEMORY: message = "Out of memory"; break;
                case Epos2Exception.ERR_FAILURE: message = "Unknown Error"; break;
                case Epos2Exception.ERR_PROCESSING: message = "Tidak dapat menjalankan operasi"; break;
                case Epos2Exception.DISCONNECT : message = "Gagal memutuskan koneksi dengan printer"; break;
            }
            Toast.makeeText(this.reactContext,message,1).show();
            this.listener.onInitializeError(new Error(message));
            return;
            }
    }

    @Override
    public void writeQRCode(String content, ReadableMap property) {
        mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_MM, 0);
        mUsbPrinter.doFunction(Const.TX_QR_DOTSIZE, 7, 0);
        mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL, Const.TX_QR_ERRLEVEL_H, 0);
        if (property.hasKey("level")) {
            switch (property.getString("level")) {
                case "H":
                    mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL, Const.TX_QR_ERRLEVEL_H, 0);
                    break;
                case "L":
                    mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL, Const.TX_QR_ERRLEVEL_L, 0);
                    break;
                case "M":
                    mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL, Const.TX_QR_ERRLEVEL_M,0 );
                    break;
                case "Q":
                    mUsbPrinter.doFunction(Const.TX_QR_ERRLEVEL, Const.TX_QR_ERRLEVEL_Q, 0);
                    break;
            }
        }
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_RIGHT, 0);
        if (property.hasKey("align")) {
            switch (property.getString("align")) {
                case "center":
                    mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0);
                    break;
                case "left":
                    mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0);
                    break;
                case "right":
                    mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_RIGHT, 0);
                    break;
            }
        }
        this.mUsbPrinter.printQRcode(content);
    }

    @Override
    public void writeImage(String path, ReadableMap property) {
        mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_RIGHT, 0);
        if (property.hasKey("align")) {
            switch (property.getString("align")) {
                case "center":
                    mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_CENTER, 0);
                    break;
                case "left":
                    mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_LEFT, 0);
                    break;
                case "right":
                    mUsbPrinter.doFunction(Const.TX_ALIGN, Const.TX_ALIGN_RIGHT, 0);
                    break;
            }
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Toast.makeText(reactContext, "Image: " + bitmap.getWidth() + " x " + bitmap.getHeight(), 1).show();
        mUsbPrinter.printImage(path);
    }

    @Override
    public void writeFeed(int length) {
        mUsbPrinter.doFunction(Const.TX_UNIT_TYPE, Const.TX_UNIT_MM, 0);
        mUsbPrinter.doFunction(Const.TX_FEED, length, 0);
    }

    @Override
    public void writeCut(ReadableMap property) {
        if (property.hasKey("cut")) {
            switch (property.getString("cut")) {
                case "full":
                    mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0);
                    break;
                case "partial":
                    mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_PARTIAL, 0);
                    break;
            }
        } else {
            mUsbPrinter.doFunction(Const.TX_CUT, Const.TX_CUT_FULL, 0);
        }
    }

    @Override
    public void startPrint() {
        mUsbPrinter = new UsbPrinter(reactContext);
        UsbDevice dev = getCorrectDevice();
        if (dev != null && mUsbPrinter.open(dev)) {
            mUsbPrinter.init();
        }
    }

    @Override
    public void endPrint() {
        mUsbPrinter.close();
    }

    private UsbDevice getCorrectDevice() {
        Map<String, UsbDevice> devMap = ((UsbManager) reactContext.getSystemService(Context.USB_SERVICE)).getDeviceList();
        for (String name : devMap.keySet()) {
            if (UsbPrinter.checkPrinter(devMap.get(name))) {
                return devMap.get(name);
            }
        }
        return null;
    }
}
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
import java.util.HashMap;

public class EpsonTM82 implements MyPrinter, ReceiveListener {

    private String TAG = "EPSONPrinter";
    private Printer mPrinter = null;
    private Context reactContext;
    private PrinterEventListener listener;

    public EpsonTM82(final Context context, final PrinterEventListener eventListener) {
        this.reactContext = context;
        this.listener = eventListener;
        try {
            this.mPrinter = new Printer(Printer.TM_T82, Printer.MODEL_SOUTHASIA, context);
        } catch (Epos2Exception e) {
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid Parameter saat inisialisasi";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Memory tidak cukup";
                break;
            case Epos2Exception.ERR_UNSUPPORTED:
                message = "Model tidak didukung";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            this.listener.onInitializeError(new Error(message));
        }
        this.mPrinter.setReceiveEventListener(this);
        this.listener.onInitializeSuccess("Success Connected");
    }


    @Override
    public MyReturnValue writeText(String text, ReadableMap property) {
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        boolean bold = property.hasKey("bold") ? property.getBoolean("bold") : false;
        int fontSize = property.hasKey("fontSize") ? property.getInt("fontSize") : 1;
        int paramEM = bold ? Printer.TRUE : Printer.FALSE;
        if(fontSize > 8){
            res.success = false;
            res.message = "Invalid Font Size";
            return res;
        }
        try{
            this.mPrinter.addTextStyle(Printer.PARAM_UNSPECIFIED, Printer.PARAM_UNSPECIFIED, paramEM, Printer.PARAM_UNSPECIFIED);
        }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Parameter Invalid";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            Toast.makeText(this.reactContext, message, 1).show();
            res.success = false;
            res.message = message;
            return res;
        }
        try{
            this.mPrinter.addTextSize(Printer.PARAM_UNSPECIFIED,fontSize);
        }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Parameter Invalid";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            Toast.makeText(this.reactContext, message, 1).show();
            res.success = false;
            res.message = message;
            return res;
        }
        try {
            this.mPrinter.addText(text,property);
        } catch (Epos2Exception e) {
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Parameter Invalid";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            Toast.makeText(this.reactContext, message, 1).show();
            res.success = false;
            res.message = message;
            return res;
        }
        res.success = true;
        res.message = "Added";
        return res;
    }

    
    @Override
    public MyReturnValue addTextAlign(int align){
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        try{
            this.mPrinter.addTextAlign(align);
        }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Parameter Invalid";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            Toast.makeText(this.reactContext, message, 1).show();
            res.success = false;
            res.message = message;
            return res;
        }
        res.success = true;
        res.message="Sukses";
        return res;
    }

    @Override
    public MyReturnValue writePulse(ReadableMap property){
        MyReturnValue res = new MyReturnValue();
        String optionDrawer = property.hasKey("drawer") ? property.getString("drawer") : "default";
        int paramDrawer = Printer.PARAM_DEFAULT;
        switch (optionDrawer){
            case "2pin":
                paramDrawer = Printer.DRAWER_2PIN;
                break;
            case "5pin":
                paramDrawer = Printer.DRAWER_5PIN;
                break;
            default:
                paramDrawer = Printer.PARAM_DEFAULT;
        }
        int optionTime = property.hasKey("time") ? property.getInt("time") : 0;
        int paramTime = Printer.PARAM_DEFAULT;
        switch (optionTime){
            case 100:
                paramTime = Printer.PULSE_100;
                break;
            case 200:
                paramTime = Printer.PULSE_200;
                break;
            case 300:
                paramTime = Printer.PULSE_300;
                break;
            case 400:
                paramTime = Printer.PULSE_400;
                break;
            case 500:
                paramTime = Printer.PULSE_500;
                break;
            default:
                paramTime = Printer.PARAM_DEFAULT;
        }
        try {
            this.mPrinter.addPulse(paramDrawer, paramTime);
        } catch (Epos2Exception e) {
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid parameter";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of Memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            res.success = false;
            res.message = message;
            return res;
        }
        res.success=true;
        res.message="Sukses";
        return res;
    }

    @Override
    public MyReturnValue writeQRCode(String content, ReadableMap property) {
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        try{
            this.mPrinter.addSymbol(content,Printer.SYMBOL_QRRCODE_MODEL_1,Printer.PARAM_DEFAULT,Printer.PARAM_UNSPECIFIED, Printer.PARAM_UNSPECIFIED, Printerr.PARAM_UNSPECIFIED);
        }catch(Epos2Exception e){
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid parameter";
                message += String.format("X:%s , y=%s, width=%s, height=%s",Integer.toString(x),Integer.toString(y),Integer.toString(width),Integer.toString(height));
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of Memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            res.success = false;
            res.message = message;
            return res;
        }
        res.success=true;
        res.message="Sukses";
        return res;
    }

    @Override
    public MyReturnValue writeImage(Bitmap data, int x, int y, int width, int height) {
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        try {
            this.mPrinter.addImage(data,x,y,width,height,Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT, Printer.PARAM_DEFAULT);
        } catch (Epos2Exception e) {
             String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid parameter";
                message += String.format("X:%s , y=%s, width=%s, height=%s",Integer.toString(x),Integer.toString(y),Integer.toString(width),Integer.toString(height));
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of Memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
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
    public MyReturnValue writeFeed(int length) {
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        try {
            this.mPrinter.addFeedLine(length);
        } catch (Epos2Exception e) {
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid parameter";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of Memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
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
    public MyReturnValue writeCut(ReadableMap property) {
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        try {
            this.mPrinter.addCut(Printer.PARAM_DEFAULT);
        } catch (Epos2Exception e) {
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid parameter";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of Memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            res.success = false;
            res.message = message;
            return res;
        }
        res.success = true;
        res.message = "Sukses";
        return res;
    }

    private boolean connectPrinter(String ipAddress) {
        boolean isBeginTransaction = false;
        if (this.mPrinter == null) {
            return false;
        }
        try {
            this.mPrinter.connect(String.format("TCP:%s",ipAddress), Printer.PARAM_DEFAULT);
        } catch (Epos2Exception e) {
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Parameter Invalid";
                break;
            case Epos2Exception.ERR_CONNECT:
                message = "Gagal terkoneksi dengan printer";
                break;
            case Epos2Exception.ERR_TIMEOUT:
                message = "Koneksi ke printer timeout";
                break;
            case Epos2Exception.ERR_ILLEGAL:
                message = "Koneksi printer telah terbuka";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            case Epos2Exception.ERR_PROCESSING:
                message = "Tidak dapat menjalankan operasi";
                break;
            case Epos2Exception.ERR_NOT_FOUND:
                message = "Printer tidak ditemukan";
                break;
            case Epos2Exception.ERR_IN_USE:
                message = "Printer sedang dipakai";
                break;
            case Epos2Exception.ERR_TYPE_INVALID:
                message = "Printer bukan TM 82";
                break;
            default:
                message = "Error";
            }
            Toast.makeText(this.reactContext, message, 1).show();
            return false;
        }
        try {
            this.mPrinter.beginTransaction();
            isBeginTransaction = true;
        } catch (Exception e) {

        }
        if (isBeginTransaction == false) {
            try {
                this.mPrinter.disconnect();
            } catch (Epos2Exception e) {
                // Do nothing
                return false;
            }
        }
        return true;
    }

    private void dispPrinterWarnings(PrinterStatusInfo status) {
        String warningsMsg = "";
        if (status == null)
            return;
        if (status.getPaper() == Printer.PAPER_NEAR_END) {
            warningsMsg += "Kertas Sudah Mau Habis";
        }

        if (status.getBatteryLevel() == Printer.BATTERY_LEVEL_1) {
            warningsMsg += "Baterai sudah mau habis";
        }

        if (warningsMsg.length() > 0) {
            Toast.makeText(this.reactContext, warningsMsg, 1).show();
        }
    }

    private boolean isPrintable(PrinterStatusInfo status) {
        if (status == null) {
            return false;
        }

        if (status.getConnection() == Printer.FALSE) {
            return false;
        } else if (status.getOnline() == Printer.FALSE) {
            return false;
        } else {
            ;// print available
        }

        return true;
    }

    @Override
    public MyReturnValue startPrint(String ipAddress) {
        MyReturnValue res = new MyReturnValue();
        if (this.mPrinter == null) {
            res.success = false;
            res.message = "Printer belum di inisiasi";
            return res;
        }
        if (!connectPrinter(ipAddress)) {
            res.success = false;
            res.message = "Gagal terhubung dengan printer";
            return res;
        }
        PrinterStatusInfo status = mPrinter.getStatus();
        dispPrinterWarnings(status);
        if (!isPrintable(status)) {
            try {
                this.mPrinter.disconnect();
            } catch (Exception e) {

            }
            res.success = false;
            res.message = "Printer tidak siap print";
            return res;
        }
        try {
            this.mPrinter.sendData(Printer.PARAM_DEFAULT);
        } catch (Epos2Exception e) {
            try {
                this.mPrinter.disconnect();
            } catch (Exception ee) {

            }
            String message;
            int errorStatus = e.getErrorStatus();
            switch (errorStatus) {
            case Epos2Exception.ERR_PARAM:
                message = "Invalid parameter";
                break;
            case Epos2Exception.ERR_MEMORY:
                message = "Out of Memory";
                break;
            case Epos2Exception.ERR_FAILURE:
                message = "Unknown Error";
                break;
            default:
                message = Integer.toString(errorStatus);
            }
            res.success = false;
            res.message = message;
            return res;
        }
        res.success = true;
        res.message = "Berhasil";
        return res;
    }

    private void finalizeObject() {
        if (this.mPrinter == null) {
            return;
        }

        this.mPrinter.clearCommandBuffer();

        this.mPrinter.setReceiveEventListener(null);

        this.mPrinter = null;

        this.listener.onPrinterClosed("Printer has successfully closed");
    }

    private void disconnectPrinter() {
        if (this.mPrinter == null) {
            return;
        }

        try {
            this.mPrinter.endTransaction();
        } catch (final Epos2Exception e) {
            int errorStatus = e.getErrorStatus();
            this.listener.onPrinterClosed(Integer.toString(errorStatus));

        }

        try {
            this.mPrinter.disconnect();
        } catch (final Epos2Exception e) {
            int errorStatus = e.getErrorStatus();
            this.listener.onPrinterClosed(Integer.toString(errorStatus));
            
        }

        finalizeObject();
    }

    @Override
    public void onPtrReceive(final Printer printerObj, final int coode, final PrinterStatusInfo status,
            final String printJobId) {
        dispPrinterWarnings(status);
        new Thread(new Runnable(){
            @Override
            public void run(){
                disconnectPrinter();
            }
        }).start();
    }

}
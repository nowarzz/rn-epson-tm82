package com.reactlibrary;
public interface PrinterEventListener {
    void onInitializeSuccess(String deviceInfo);
    void onInitializeError(Error error);
    void onPrinterClosed(String message);
}
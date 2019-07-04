package com.reactlibrary;
import com.facebook.react.bridge.ReadableMap;

public interface Printer {
    void writeText(String text, ReadableMap property);
    void writeQRCode(String content, ReadableMap property);
    void writeImage(String path, ReadableMap property);
    void writeFeed(int length);
    void writeCut(ReadableMap property);
    void startPrint();
    void endPrint();
}
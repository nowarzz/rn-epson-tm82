package com.nowarzz.rnepson;
import com.facebook.react.bridge.ReadableMap;

public interface MyPrinter {
    void writeText(String text, ReadableMap property);
    void writeQRCode(String content, ReadableMap property);
    void writeImage(String path, ReadableMap property);
    void writeFeed(int length);
    void writeCut(ReadableMap property);
    void startPrint();
    void endPrint();
}
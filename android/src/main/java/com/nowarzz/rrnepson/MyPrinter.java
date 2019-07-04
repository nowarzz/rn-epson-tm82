package com.nowarzz.rnepson;
import com.facebook.react.bridge.ReadableMap;

public interface MyPrinter {
    MyReturnValue writeText(String text, ReadableMap property);
    void writeQRCode(String content, ReadableMap property);
    void writeImage(String path, ReadableMap property);
    MyReturnValue writeFeed(int length);
    MyReturnValue writeCut(ReadableMap property);
    MyReturnValue startPrint();
}
package com.nowarzz.rnepson;
import com.facebook.react.bridge.ReadableMap;
import android.graphics.Bitmap;

public interface MyPrinter {
    MyReturnValue writeText(String text, ReadableMap property);
    void writeQRCode(String content, ReadableMap property);
    MyReturnValue writeImage(Bitmap data, int x, int y, int width, int height);
    MyReturnValue writeFeed(int length);
    MyReturnValue writeCut(ReadableMap property);
    MyReturnValue startPrint();
}
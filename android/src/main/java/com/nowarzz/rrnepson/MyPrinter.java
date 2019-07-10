package com.nowarzz.rnepson;
import com.facebook.react.bridge.ReadableMap;
import android.graphics.Bitmap;

import javax.annotation.Nullable;


public interface MyPrinter {
    MyReturnValue writeText(String text, @Nullable ReadableMap property);
    MyReturnValue writeQRCode(String content, @Nullable ReadableMap property);
    MyReturnValue addTextAlign(int align);
    MyReturnValue writeImage(Bitmap data, int x, int y, int width, int height);
    MyReturnValue writeFeed(int length);
    MyReturnValue writeCut();
    MyReturnValue writePulse(@Nullable ReadableMap property);
    MyReturnValue startPrint(String ipAddress);
}
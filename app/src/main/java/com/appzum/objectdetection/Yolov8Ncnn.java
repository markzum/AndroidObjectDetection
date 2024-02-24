package com.appzum.objectdetection;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.Surface;

import org.opencv.core.Mat;


public class Yolov8Ncnn {
    public native boolean loadModel(AssetManager mgr, int modelid, int cpugpu);
    public native boolean openCamera(int facing);
    public native boolean closeCamera();
    public native boolean setOutputWindow(Surface surface);
    public native void detect2(long matIn, Bitmap bitmapOut);

    static {
        System.loadLibrary("yolov8ncnn");
    }
}

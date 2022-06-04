package com.mozhimen.opencvk;

import android.util.Log;

import org.opencv.android.OpenCVLoader;

/**
 * @ClassName OpenCVK
 * @Description TODO
 * @Author mozhimen / Kolin Zhao
 * @Date 2022/6/4 11:15
 * @Version 1.0
 */
public class OpenCVK {
    private final static String TAG = "OpenCVK>>>>>";

    static {
        System.loadLibrary("native-lib");
    }

    public static boolean initSDK() {
        Log.d(TAG, "initSDK: " + stringFromJNI());
        Log.d(TAG, "initSDK: " + getSignKeyFromJNI("abc"));
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "initSDK: init opencv success opencv vc " +OpenCVLoader.OPENCV_VERSION);
            return true;
        } else {
            Log.e(TAG, "initSDK: init opencv fail");
            return false;
        }
    }

    public native static String stringFromJNI();

    public native static String getSignKeyFromJNI(String origin);
}

package com.uberspot.a2048.helper;

import android.util.Log;

import com.uberspot.a2048.BuildConfig;


/**
 * Created by puck on 2018/10/18.
 */

public class TLog {
    private static String TAG = "TLog---------";

    public static void e(String message){
        try {
            if (BuildConfig.DEBUG){
                Log.e(TAG,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static void d(String message){
        try {
            if (BuildConfig.DEBUG){
                Log.d(TAG,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static void i(String message){
        try {
            if (BuildConfig.DEBUG){
                Log.i(TAG,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static void w(String message){
        try {
            if (BuildConfig.DEBUG){
                Log.w(TAG,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public static void v(String message){
        try {
            if (BuildConfig.DEBUG){
                Log.v(TAG,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public static void e(String tag,String message){
        try {
            if (BuildConfig.DEBUG){
                Log.e(tag,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static void d(String tag,String message){
        try {
            if (BuildConfig.DEBUG){
                Log.d(tag,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static void i(String tag,String message){
        try {
            if (BuildConfig.DEBUG){
                Log.i(tag,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    public static void w(String tag,String message){
        try {
            if (BuildConfig.DEBUG){
                Log.w(tag,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public static void v(String tag,String message){
        try {
            if (BuildConfig.DEBUG){
                Log.v(tag,message);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public static void logE(Throwable throwable){
        try {
            if (BuildConfig.DEBUG){
                throwable.printStackTrace();
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}

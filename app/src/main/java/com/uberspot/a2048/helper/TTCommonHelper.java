package com.uberspot.a2048.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.uberspot.a2048.BuildConfig;
import com.uberspot.a2048.TTSharedPreferencesUtil;

import java.util.Date;

import static com.uberspot.a2048.TTSharedPreferencesUtil.key_first_open;
import static com.uberspot.a2048.TTSharedPreferencesUtil.key_game_level;
import static com.uberspot.a2048.TTSharedPreferencesUtil.key_open_times;
import static com.uberspot.a2048.TTSharedPreferencesUtil.key_rate_five;
import static com.uberspot.a2048.TTSharedPreferencesUtil.key_show_rate_sec;

/**
 * Created by liu on 2018/12/15.
 */

public class TTCommonHelper {

    public static TTCommonHelper instance = null;

    public static TTCommonHelper getInstance() {
        if (instance == null) {
            instance = new TTCommonHelper();
        }
        return instance;
    }


    public TTCommonHelper() {

    }

    public int getPlayerLevel(Context context) {
        return (int)TTSharedPreferencesUtil.getInstance(context).getSharedPreference(key_game_level, 1);
    }

    public void setPlayerLevel(Context context, int level) {
        TTSharedPreferencesUtil.getInstance(context).put(key_game_level, level);
    }

   public void setFirstOpen(Context context, boolean firstOpen) {
       TTSharedPreferencesUtil.getInstance(context).put(key_first_open, firstOpen);
   }

   public boolean isFistOpen(Context context) {
       boolean firstOpen = (boolean)TTSharedPreferencesUtil.getInstance(context).getSharedPreference(TTSharedPreferencesUtil.key_first_open,true);
       return firstOpen;
   }

   public void setOpenTimes(Context context, int openTimes) {
       TTSharedPreferencesUtil.getInstance(context).put(key_open_times, openTimes);
   }

    public void plugOpenTimes(Context context) {
        int openTimes = getOpenTimes(context) + 1;
        TTSharedPreferencesUtil.getInstance(context).put(key_open_times, openTimes);
    }

    public int getOpenTimes(Context context) {
        int open = (int)TTSharedPreferencesUtil.getInstance(context).getSharedPreference(TTSharedPreferencesUtil.key_open_times, 0);
        return open;
    }

    public boolean hasRate(Context context) {
        boolean has = (boolean)TTSharedPreferencesUtil.getInstance(context).getSharedPreference(key_rate_five, false);
        return has;
    }

    public void setRate(Context context, boolean rate) {
        TTSharedPreferencesUtil.getInstance(context).put(key_rate_five, rate);
    }

    public boolean needRate(Context context) {
        if (!hasRate(context)) {
            long nowtime = new Date().getTime();
            long lastShowTime = (long)TTSharedPreferencesUtil.getInstance(context).getSharedPreference(key_show_rate_sec + TTCommonUtil.getAppVersionCode(context),0L);
            long duration = BuildConfig.DEBUG ? 1 : 5*60*1000;
            return nowtime - lastShowTime > duration;
        }
        return false;
    }

    public void setRateTime(Context context, long sec) {
        TTSharedPreferencesUtil.getInstance(context).put(key_show_rate_sec + + TTCommonUtil.getAppVersionCode(context), sec);
    }

    //强制跳转google play
    public void rateFiveStar(@NonNull Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.setPackage("com.android.vending");// pkg为包名
        it.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName ac = it.resolveActivity(activity.getPackageManager());
        String classname = (ac == null ? null : ac.getClassName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (classname != null && !classname.equals("") && !classname.equals("null")) {
            intent.setClassName("com.android.vending", classname);
        }
        TLog.v("classname:"+classname);
        activity.startActivity(intent);
  }
}

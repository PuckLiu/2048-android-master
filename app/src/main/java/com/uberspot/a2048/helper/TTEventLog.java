package com.uberspot.a2048.helper;

import android.support.annotation.NonNull;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;

/**
 * Created by liu on 2018/12/17.
 */

public class TTEventLog {

    private static TTEventLog instance = null;

    public static TTEventLog getInstance() {
        if (instance == null) {
            instance = new TTEventLog();
        }
        return instance;
    }

    public void LogEvent(String event) {
        try {
            Answers.getInstance().logCustom(new CustomEvent(event));
        } catch (Exception e) {
            TLog.logE(e);
        }
    }

    public void LogEvent(String event,@NonNull String params) {
        try {
            Answers.getInstance().logCustom(new CustomEvent(event)
            .putCustomAttribute("defaultKey", params));
        } catch (Exception e) {
            TLog.logE(e);
        }
    }

    public void LogEvent(String event, @NonNull String key, @NonNull String value) {
        try {
            Answers.getInstance().logCustom(new CustomEvent(event)
            .putCustomAttribute(key, value));
        } catch (Exception e) {
            TLog.logE(e);
        }
    }

    public void LogException(Exception ex) {

    }

    public void LogAdSuccess(String adtag) {
        try {
            LogEvent("AD_"+adtag + "_Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LogAdFailed(String adtag, String errorMsg) {
        try {
            LogEvent("AD_"+adtag + "_Failed", "failed", errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

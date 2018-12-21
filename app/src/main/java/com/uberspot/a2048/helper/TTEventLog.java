package com.uberspot.a2048.helper;

import android.support.annotation.NonNull;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

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
}

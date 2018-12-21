package com.uberspot.a2048.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liu on 2018/11/6.
 */

public class TTEmptyUtile {

    public static boolean isArrayEmpty(ArrayList arrayList) {
        try {
            if (arrayList == null || arrayList.size() <= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            TLog.logE(e);
        }
        return true;
    }

    public static boolean isArrayEmpty(List arrayList) {
        try {
            if (arrayList == null || arrayList.size() <= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            TLog.logE(e);
        }
        return true;
    }

    public static boolean isStringEmpty(String  string) {
        try {
            if (string == null || string.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            TLog.logE(e);
        }
        return true;
    }

    public static boolean isMapEmpty(HashMap map) {
        try {
            if (map == null || map.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            TLog.logE(e);
        }
        return true;
    }
}

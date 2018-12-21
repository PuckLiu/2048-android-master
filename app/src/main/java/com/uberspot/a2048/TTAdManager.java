package com.uberspot.a2048;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdManager {

    public static TTAdManager instance = null;

    public static TTAdManager getInstance() {
        if (instance == null) {
            instance = new TTAdManager();
        }
        return instance;
    }


}

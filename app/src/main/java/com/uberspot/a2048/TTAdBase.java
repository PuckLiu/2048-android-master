package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;

import com.facebook.ads.AdSize;

/**
 * Created by liu on 2018/12/5.
 */

public abstract class TTAdBase{
    protected Object ad;
    protected Context context;
    protected String place_id;
    protected boolean hasLoaded = false;
    protected boolean hasShown = false;
    protected TTAdInterface listener;
    protected int showAdCount = 0;
    protected int masShowCount = 3;

    public abstract ViewGroup getAdView();
    public abstract void loadAd();
    public abstract void showAd();
    public abstract void destroyAd();
    public boolean isDisplaying() {
        return hasShown;
    }

    public void setListener(TTAdInterface listener) {
        this.listener = listener;
    }
}

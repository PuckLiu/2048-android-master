package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.NativeAd;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdNative extends TTAdBase {

    private FrameLayout nativeAdLayout;//对应NativeAdLayout
    private RelativeLayout mediaView;//对应fb的MediaView 就是icon啥的

    public TTAdNative(Context context, String string, AdSize size) {
        ad = new NativeAd(context,string);
    }

    @Override
    public ViewGroup getAdView() {
        return nativeAdLayout;
    }

    @Override
    public void loadAd() {
        if (ad != null && ad instanceof NativeAd) {
            ((NativeAd)ad).loadAd();
            hasLoaded = true;
            if (listener != null) {
                listener.adLoaded();
            }
        }
    }

    @Override
    public void showAd() {
        hasShown = true;
    }

    @Override
    public void destroyAd() {
        if (ad != null && ad instanceof NativeAd) {
            ((NativeAd)ad).destroy();
        }
        hasShown = false;
        hasLoaded = false;
    }
}

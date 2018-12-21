package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.ads.AdSize;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeBannerAd;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdNativeBanner extends TTAdBase {
    private LinearLayout mAdView;
    private FrameLayout mAdChoicesContainer;
    private NativeAdLayout mNativeBannerAdContainer;
    public TTAdNativeBanner(Context context, String string, AdSize size) {
        ad = new NativeBannerAd(context,string);
    }

    @Override
    public ViewGroup getAdView() {
        return mAdView;
    }

    @Override
    public void loadAd() {
        if (ad != null && ad instanceof NativeBannerAd) {
            ((NativeBannerAd)ad).loadAd();
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
        if (ad != null && ad instanceof NativeBannerAd) {
            ((NativeBannerAd)ad).destroy();
        }
        hasShown = false;
        hasLoaded = false;
    }
}

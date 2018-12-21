package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InstreamVideoAdView;

/**
 * 插入视频
 * Created by liu on 2018/12/5.
 */

public class TTAdInstreamVideo extends TTAdBase {

    public TTAdInstreamVideo(Context context, String string, AdSize size) {
        ad = new InstreamVideoAdView(context, string, size);
    }

    @Override
    public ViewGroup getAdView() {
        if (ad != null && ad instanceof InstreamVideoAdView) {
            return (InstreamVideoAdView)ad;
        }
        return null;
    }

    @Override
    public void loadAd() {
        if (ad != null && ad instanceof InstreamVideoAdView) {
            ((InstreamVideoAdView)ad).loadAd();
            hasLoaded = true;
            if (listener != null) {
                listener.adLoaded();
            }
        }
    }

    @Override
    public void showAd() {
        if (ad != null && ad instanceof InstreamVideoAdView) {
            ((InstreamVideoAdView)ad).show();
            hasShown = true;
        }
    }

    @Override
    public void destroyAd() {
        if (ad != null && ad instanceof InstreamVideoAdView) {
            ((InstreamVideoAdView)ad).destroy();
        }
        hasShown = false;
        hasLoaded = false;
    }
}

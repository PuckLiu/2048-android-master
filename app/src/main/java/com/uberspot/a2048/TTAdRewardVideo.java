package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;

import com.facebook.ads.AdSize;
import com.facebook.ads.RewardedVideoAd;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdRewardVideo extends TTAdBase {

    public TTAdRewardVideo(Context context, String string, AdSize size) {
        ad = new RewardedVideoAd(context, string);
    }

    @Override
    public ViewGroup getAdView() {
        return null;
    }

    @Override
    public void loadAd() {
        if (ad != null && ad instanceof RewardedVideoAd) {
            ((RewardedVideoAd)ad).loadAd(true);
            hasLoaded = true;
        }
    }

    @Override
    public void showAd() {
        if (ad != null && ad instanceof RewardedVideoAd) {
            ((RewardedVideoAd)ad).show();
            hasShown = true;
            if (listener != null) {
                listener.adLoaded();
            }
        }
    }

    @Override
    public void destroyAd() {
        if (ad != null) {
            if ( ad instanceof RewardedVideoAd) {
                ((RewardedVideoAd)ad).destroy();
            }
            ad = null;
        }
        hasShown = false;
        hasLoaded = false;
    }
}

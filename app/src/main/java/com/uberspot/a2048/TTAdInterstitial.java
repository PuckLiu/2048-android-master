package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.uberspot.a2048.helper.TLog;

import java.sql.Time;
import java.util.EnumSet;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdInterstitial extends TTAdBase implements InterstitialAdListener {

    public TTAdInterstitial(Context context, String string, AdSize size) {
        this.context = context;
        this.place_id = string;
        ad = new InterstitialAd(context, string);
    }

    @Override
    public ViewGroup getAdView() {
        return null;
    }

    @Override
    public void loadAd() {
        if (ad == null || !(ad instanceof InterstitialAd)) {
            ad = new InterstitialAd(context, place_id);
        }
        InterstitialAd interstitialAd = (InterstitialAd)ad;
        interstitialAd.setAdListener(this);
        interstitialAd.loadAd(EnumSet.of(CacheFlag.VIDEO));
        TLog.v("load ad start time:" + System.currentTimeMillis());
    }

    @Override
    public void showAd() {
        if (ad != null && ad instanceof InterstitialAd && hasLoaded && !hasShown) {
            ((InterstitialAd)ad).show();
            hasShown = true;
            showAdCount++;
            if (showAdCount >= masShowCount) {
                this.destroyAd();
                this.loadAd();
                TLog.v("showAd maxCount and reload ad");
            }
        }
    }

    @Override
    public void destroyAd() {
        if (ad != null && ad instanceof InterstitialAd) {
            ((InterstitialAd)ad).destroy();
            ad = null;
        }
        hasShown = false;
        hasLoaded = false;
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        if (ad == this.ad) {
            TLog.v("onInterstitialDisplayed");
            hasShown = true;
        }
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        if (ad == this.ad) {
            TLog.v("onInterstitialDismissed");
            hasShown = false;
            this.destroyAd();
            if (listener != null) {
                listener.onAdDismissed();
            }
        }
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        if (ad == this.ad) {
            TLog.v("Interstitial onError:code:"+adError.getErrorCode()+" msg:"+ adError.getErrorMessage().toString());
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        TLog.v("load ad end time:" + System.currentTimeMillis());
        if (ad != null && ad == this.ad) {
            hasLoaded = true;
            TLog.v("onAdLoaded");
            if (listener != null) {
                listener.adLoaded();
            }
        }
    }

    @Override
    public void onAdClicked(Ad ad) {
        if (ad == this.ad) {
            TLog.v("onAdClicked");
        }
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        if (ad == this.ad) {
            TLog.v("onLoggingImpression");
        }
    }
}

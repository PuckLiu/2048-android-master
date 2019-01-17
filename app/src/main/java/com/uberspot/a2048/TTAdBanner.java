package com.uberspot.a2048;

import android.content.Context;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.uberspot.a2048.helper.TLog;
import com.uberspot.a2048.helper.TTEventLog;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdBanner extends TTAdBase implements AdListener{

    private final String ADTAG = "TTAdBanner";
    public TTAdBanner(Context context, String string, AdSize size) {
        ad = new AdView(context,string,size);
    }

    @Override
    public ViewGroup getAdView() {
        if (ad != null && ad instanceof AdView) {
            return (AdView)ad;
        }
        return null;
    }

    @Override
    public void loadAd() {
        if (ad != null && ad instanceof AdView) {
            ((AdView) ad).setAdListener(this);
            ((AdView)ad).loadAd();
        }
    }

    @Override
    public void showAd() {
        hasShown = true;
    }

    @Override
    public void destroyAd() {
        if (ad != null && ad instanceof AdView) {
            ((AdView)ad).destroy();
        }
        hasLoaded = false;
        hasShown = false;
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        TLog.v(TTAdBanner.class.toString(),"onError:" + adError.getErrorMessage() + " errCode:" + adError.getErrorCode());
        TTEventLog.getInstance().LogEvent(ADTAG, "failed");
        TTEventLog.getInstance().LogAdFailed(ADTAG, adError.getErrorMessage());

    }

    @Override
    public void onAdLoaded(Ad ad) {
        TLog.v(TTAdBanner.class.toString(),"onAdLoaded");
        if (ad == this.ad) {
            hasShown = true;
            hasLoaded = true;
            if (listener != null) {
                listener.adLoaded();
            }
            TTEventLog.getInstance().LogEvent(ADTAG, "success");
            TTEventLog.getInstance().LogAdSuccess(ADTAG);
        }
    }

    @Override
    public void onAdClicked(Ad ad) {
        TLog.v(TTAdBanner.class.toString(),"onAdClicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        TLog.v(TTAdBanner.class.toString(),"onLoggingImpression");
    }
}

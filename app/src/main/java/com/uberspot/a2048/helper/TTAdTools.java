package com.uberspot.a2048.helper;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.facebook.ads.AdSize;
import com.uberspot.a2048.TTAdBanner;
import com.uberspot.a2048.TTAdInterface;

/**
 * Created by liu on 2018/12/16.
 */

public class TTAdTools {
    //首页Banner
    public static final String AD_Banner_Id = "269502770427480_271468656897558";
    //首页插屏
    public static final String AD_Interstitial_Id = "269502770427480_271471210230636";
    //原生
    public static final String AD_Native_Id = "269502770427480_270133757031048";

    public static TTAdTools instance = null;

    private TTAdBanner banner;

    public static TTAdTools getInstance() {
        if (instance == null) {
            instance = new TTAdTools();
        }
        return instance;
    }

    public void loadBanner(Context context, String placement_id, final ViewGroup container) {
        banner = new TTAdBanner(context, placement_id, AdSize.BANNER_HEIGHT_50);
        banner.setListener(new TTAdInterface() {
            @Override
            public void adLoaded() {
                container.addView(banner.getAdView());
            }

            @Override
            public void onAdDismissed() {

            }
        });
        banner.loadAd();
    }

    public void showBanner(Context context, ViewGroup container) {
        if (banner != null) {
            if (container != banner.getAdView()) {
                ViewGroup lastParent = (ViewGroup) banner.getAdView().getParent();
                if (lastParent != null) {
                    lastParent.removeView(banner.getAdView());
                }
                container.addView(banner.getAdView());
            }
            banner.showAd();
        }
    }

    public void destoryBanner() {
        if (banner != null) {
            banner.destroyAd();
            banner = null;
        }
    }

}

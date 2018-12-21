package com.uberspot.a2048;

import android.content.Context;

import com.facebook.ads.AdSize;
import com.uberspot.a2048.helper.TTAdTools;

/**
 * Created by liu on 2018/12/5.
 */

public class TTAdFactory {

    public enum AD_TYPE{
        AD_TYPE_BANNER ,//banner广告
        AD_TYPE_INTERSTITIAL,//插屏广告
        AD_TYPE_INSTREAM_VIDEO,//插屏视频
        AD_TYPE_REWARDED_VIDEO,//激励视频
        AD_TYPE_NATIVE,//原生广告
        AD_TYPE_NATIVE_BANNER,//原生Banner广告
    }

    private static final String place_id = "YOUR_PLACEMENT_ID";

    public static TTAdBase createAdByType(Context context, AD_TYPE type) {
        TTAdBase ad = null;
        switch (type) {
            case AD_TYPE_BANNER:
                ad = new TTAdBanner(context, TTAdTools.AD_Banner_Id,AdSize.BANNER_HEIGHT_50);
                break;
            case AD_TYPE_INTERSTITIAL:
                ad = new TTAdInterstitial(context,TTAdTools.AD_Interstitial_Id, null);
                break;
            case AD_TYPE_INSTREAM_VIDEO:
                ad = new TTAdInstreamVideo(context, place_id, null);
                break;
            case AD_TYPE_REWARDED_VIDEO:
                ad = new TTAdRewardVideo(context, place_id, null);
                break;
            case AD_TYPE_NATIVE:
                ad = new TTAdNative(context, place_id, AdSize.RECTANGLE_HEIGHT_250);
                break;
            case AD_TYPE_NATIVE_BANNER:
                ad = new TTAdNativeBanner(context, place_id, AdSize.BANNER_HEIGHT_90);
                break;
        }
        return ad;
    }

    //反射
    public static TTAdBase createAd(Class <? extends TTAdBase> clazz) {
        TTAdBase ad = null;
        try {
            ad = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return ad;
    }
}

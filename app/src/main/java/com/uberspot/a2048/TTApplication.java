package com.uberspot.a2048;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;
import com.facebook.samples.ads.debugsettings.DebugSettings;
import com.uberspot.a2048.helper.TLog;

/**
 * Created by liu on 2018/12/5.
 */

public class TTApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
        DebugSettings.initialize(this);
        TLog.v("Application OnCreate");
        if (AudienceNetworkAds.isInAdsProcess(this)) {
            return;
        } // else continue with publisher's initialization code
    }
}

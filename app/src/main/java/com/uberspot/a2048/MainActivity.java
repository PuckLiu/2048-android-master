package com.uberspot.a2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.uberspot.a2048.helper.TLog;
import com.uberspot.a2048.helper.TTAdTools;
import com.uberspot.a2048.helper.TTCommonHelper;
import com.uberspot.a2048.helper.TTEventLog;
import com.uberspot.a2048.view.TTAlertView;

import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Locale;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

//import de.cketti.library.changelog.ChangeLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "2048_MainActivity";

    private WebView mWebView;
    private long mLastBackPress;
    private static final long mBackPressThreshold = 1000;
    private static final String IS_FULLSCREEN_PREF = "is_fullscreen_pref";
    private static boolean DEF_FULLSCREEN = true;
    private long mLastTouch;
    private static final long mTouchThreshold = 2000;
    private Toast pressBackToast;

    /**************************************************************************/
    private TTAdInterstitial interstitialAd;
    private boolean needShowAd = false;
    private boolean isHomeShow = true;
    private boolean firstIn = true;
    private int openTimes = 0;
    private boolean sessionRateShow = false;
    private int mLevel = 1;//level在[0,4]之间取值
    private final int[] levelArr = {3, 4, 5, 6, 8};
    private final int[] icons = {R.drawable.logo_3, R.drawable.logo_4, R.drawable.logo_5, R.drawable.logo_6, R.drawable.logo_8};
    private final int[] leaderBoards = {R.string.leaderboard_3x3, R.string.leaderboard_4x4, R.string.leaderboard_5x5, R.string.leaderboard_6x6, R.string.leaderboard_8x8};

    private FrameLayout mainView;
    private FrameLayout lyPic;
    private FrameLayout btnLeft;
    private FrameLayout btnRight;
    private FrameLayout btnStart;
    private FrameLayout btnLeader;
    private FrameLayout btnRemoveAd;
    private ImageView ivPic;
    private TextView tvLevel;
    private FrameLayout adBannerContainer;
    private ProgressBar loadingBar;

    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;
    private LeaderboardsClient mLeaderboardsClient;
    private GoogleSignInClient mGoogleSignInClient;

    @SuppressLint({"SetJavaScriptEnabled", "NewApi", "ShowToast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Don't show an action bar or title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // If on android 3.0+ activate hardware acceleration
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        Fabric.with(this, new Crashlytics());
        configScreen();
        setContentView(R.layout.activity_main);
        prepareData();
        initView();

//        ChangeLog cl = new ChangeLog(this);
//        if (cl.isFirstRun()) {
//            cl.getLogDialog().show();
//        }

        TTEventLog.getInstance().LogEvent("LaunchActivity");
        boolean firstOpen = TTCommonHelper.getInstance().isFistOpen(this);
        if (firstOpen) {
            //TODO: first open game
            TTEventLog.getInstance().LogEvent("FirstOpen");
        }
        TLog.v("is firstOpen:"+firstOpen);
        TTCommonHelper.getInstance().setFirstOpen(this, false);
        handleWebViewJs();
        loadAdBanner();
        loadAd();
//        startSignInIntent();

        pressBackToast = Toast.makeText(getApplicationContext(), R.string.press_back_again_to_exit,
                Toast.LENGTH_SHORT);
        firstIn = false;
        TTCommonHelper.getInstance().plugOpenTimes(this);

        //广告调试
//        if (BuildConfig.DEBUG) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(new Intent(getApplicationContext(), DebugSettingsActivity.class));
//                }
//            }, 1000);
//        }
    }

    private void prepareData() {
        mLevel = TTCommonHelper.getInstance().getPlayerLevel(this);

        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken("748166288949-nnoqs154pnf01id07968vnp6igfvdo44.apps.googleusercontent.com")
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        initFacebook();
    }

    private void configScreen() {
        // Apply previous setting about showing status bar or not
        applyFullScreen(isFullScreen());

        // Check if screen rotation is locked in settings
        boolean isOrientationEnabled = false;
        try {
            isOrientationEnabled = Settings.System.getInt(getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION) == 1;
        } catch (SettingNotFoundException e) {
            Log.d(TAG, "Settings could not be loaded");
        }

        // If rotation isn't locked and it's a LARGE screen then add orientation changes based on sensor
        int screenLayout = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (((screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE)
                || (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE))
                && isOrientationEnabled) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }
    }

    private void initView() {
        mainView = (FrameLayout)findViewById(R.id.ly_main);
        lyPic = (FrameLayout)findViewById(R.id.ly_pic);
        tvLevel = (TextView)findViewById(R.id.tv_level);
        btnLeft = (FrameLayout)findViewById(R.id.btn_left);
        btnRight = (FrameLayout)findViewById(R.id.btn_right);
        btnStart = (FrameLayout)findViewById(R.id.btn_start);
        btnLeader = (FrameLayout)findViewById(R.id.btn_leader);
        adBannerContainer = (FrameLayout)findViewById(R.id.ly_banner);
        ivPic = (ImageView)findViewById(R.id.iv_pic);
        loadingBar = (ProgressBar)findViewById(R.id.loading_indicator);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnLeader.setOnClickListener(this);
        tvLevel.setText(levelArr[mLevel] + "x" + levelArr[mLevel]);
        ivPic.setImageResource(icons[mLevel]);

        // Load webview with game
        mWebView = (WebView) findViewById(R.id.mainWebView);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setRenderPriority(RenderPriority.HIGH);
        settings.setDatabasePath(getFilesDir().getParentFile().getPath() + "/databases");
        settings.setUserAgentString("Name/android_2048");
//        hanldeWebView();
    }

    private void hanldeWebView() {
        // If there is a previous instance restore it in the webview
//        if (savedInstanceState != null) {
//            mWebView.restoreState(savedInstanceState);
//        } else {
            // Load webview with current Locale language
            mWebView.loadUrl("file:///android_asset/2048/index_"+ levelArr[mLevel] +".html");
//        }

//        mWebView.loadUrl("file:///android_asset/2048/index_3.html");

        // Set fullscreen toggle on webview LongClick
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long currentTime = System.currentTimeMillis();
                if ((event.getAction() == MotionEvent.ACTION_UP)
                        && (Math.abs(currentTime - mLastTouch) > mTouchThreshold)) {
                    boolean toggledFullScreen = !isFullScreen();
                    saveFullScreen(toggledFullScreen);
                    applyFullScreen(toggledFullScreen);
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastTouch = currentTime;
                }
                // return so that the event isn't consumed but used
                // by the webview as well
                return false;
            }
        });
    }

    @SuppressLint("JavascriptInterface")
    private void handleWebViewJs() {
        mWebView.addJavascriptInterface(new JavaScriptFunction() {
            @Override
            @JavascriptInterface
            public int getLevel() {
                TLog.v("getLevel:"+levelArr[mLevel]);
                return levelArr[mLevel];
            }

            @Override
            @JavascriptInterface
            public void gameOver(String over, String score) {
                TLog.v("JavaScriptFunction GameOver:"+ over + " score:" + score);
                if (!over.isEmpty() && "1".equals(over)) {
                    //赢了
                    TTEventLog.getInstance().LogEvent("Win", score);
                } else {
                    //输了
                    TTEventLog.getInstance().LogEvent("Lose", score);
                }
                int gameCount = (int)TTSharedPreferencesUtil.getInstance(MainActivity.this).getSharedPreference(TTSharedPreferencesUtil.key_game_count,0);
                TTSharedPreferencesUtil.getInstance(MainActivity.this).put(TTSharedPreferencesUtil.key_game_count,gameCount+1);
                needShowAd = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showAdAfterGameOver();
                            }
                        });
                    }
                },2000);
            }

            @Override
            @JavascriptInterface
            public void getScore(int score) {
                TLog.v("current score:" + score);
                updateLeaderboards(score);
            }

            @Override
            @JavascriptInterface
            public void needShowAd() {
                needShowAd = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showAdAfterGameOver();
                    }
                });
            }

            @Override
            @JavascriptInterface
            public void shareClick() {
                TLog.d("share click thread:" + Thread.currentThread().getName());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        shareToFacebook();
                    }
                });
            }

            @Override
            @JavascriptInterface
            public void log(String string) {
                TLog.v("JavaScriptFunction "+string);
            }
        },"JavaScriptFunction");
    }

    private void startGame() {
        isHomeShow = false;
        mainView.setVisibility(View.INVISIBLE);
        hanldeWebView();
        TTCommonHelper.getInstance().setPlayerLevel(this, mLevel);
    }

    private void ClickLeft() {
        if (mLevel == 0) {
            mLevel = 4;
        } else {
            mLevel = (mLevel - 1) % 5;
        }
        tvLevel.setText(levelArr[mLevel] + "x" + levelArr[mLevel]);
        ivPic.setImageResource(icons[mLevel]);
    }

    private void ClickRight() {
        mLevel = (mLevel+1)%5;
        tvLevel.setText(levelArr[mLevel] + "x" + levelArr[mLevel]);
        ivPic.setImageResource(icons[mLevel]);
    }

    /* 评分相关*/
    /***************************************************************/
    private void showRate() {
        if (!TTCommonHelper.getInstance().needRate(this)) {
            return;
        }
        sessionRateShow = true;
        TTCommonHelper.getInstance().setRateTime(this, new Date().getTime());

        TTAlertView alertView = new TTAlertView.Builder(this)
                .setBtnCancel(getString(R.string.later_rate))
                .setBtnOK(getString(R.string.rate_now))
                .setMessage(getString(R.string.rate_us))
                .setClickListener(new TTAlertView.Builder.AlertViewClickListener() {
                    @Override
                    public void onButtonClicked(boolean isBtnOk) {
                        if (isBtnOk) {
                            TLog.v("Click Rate Button");
                            //TODO Rate and record
                            TTCommonHelper.getInstance().rateFiveStar(MainActivity.this);
                            TTCommonHelper.getInstance().setRate(MainActivity.this, true);
                        } else {
                            TLog.v("Click Cancel Button");
                        }
                    }
                })
                .create();
        alertView.show();
    }

    /* ad相关 */
    /****************************************************************/
    private void loadAd() {
        interstitialAd = (TTAdInterstitial) TTAdFactory.createAdByType(this, TTAdFactory.AD_TYPE.AD_TYPE_INTERSTITIAL);
        interstitialAd.setListener(new TTAdInterface() {
            @Override
            public void adLoaded() {
                showAdAfterGameOver();
            }

            @Override
            public void onAdDismissed() {
                reloadAd();
            }
        });
        interstitialAd.loadAd();
    }

    private void reloadAd() {
        if (interstitialAd != null) {
            interstitialAd.loadAd();
        }
    }

    private void showAdAfterGameOver() {
        if (needShowAd && interstitialAd != null && interstitialAd.hasLoaded) {
            interstitialAd.showAd();
            needShowAd = false;
            TLog.v("game over show ads");
        }
        TLog.v("showAdAfterGameOver 3s");
    }

    private void showAdAfterBack() {
        if (interstitialAd != null && interstitialAd.hasLoaded) {
            interstitialAd.showAd();
            needShowAd = false;
            TLog.v("game over show ads");
        }
    }

    private void loadAdBanner() {
        TTAdTools.getInstance().loadBanner(this, TTAdTools.AD_Banner_Id,adBannerContainer);
    }

    /*排行榜相关*/
    /******************************************/
    private void showLeader() {
        if (mLeaderboardsClient != null) {
            mLeaderboardsClient.getAllLeaderboardsIntent()
                    .addOnSuccessListener(new OnSuccessListener<Intent>() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, RC_UNUSED);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            TLog.logE(e);
                        }
                    });
        } else {
            startSignInIntent();
        }
    }

    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signInSilently() {
        Log.d(TAG, "signInSilently()");
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
        });
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        TLog.d("onConnected(): connected to Google APIs");
        mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
    }

    private void onDisconnected() {
        TLog.d("onDisconnected()");
        mLeaderboardsClient = null;
    }

    private void updateLeaderboards(int finalScore) {
        if (isSignedIn()) mLeaderboardsClient.submitScore(getString(leaderBoards[mLevel]), finalScore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
                TLog.e(" sign in successful");
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }
                TLog.e("sign in failed: exception:" + message);
                onDisconnected();
            }
        } else {
            if (callbackManager != null) {
                callbackManager.onActivityResult(requestCode, resultCode, intent);
            }
        }
    }
    /*fb 分享*/
    /**************************************************/
    CallbackManager callbackManager = null;
    ShareDialog shareDialog = null;
    private void initFacebook() {
        //抽取成员变量
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                //分享成功的回调，在这里做一些自己的逻辑处理
                TLog.v("fb init onSuccess");
            }

            @Override
            public void onCancel() {
                TLog.v("fb init Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                TLog.v("fb init onError:"+ error.getLocalizedMessage());
            }
        });
    }

    public void shareToFacebook() {
        loadingBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingBar.setVisibility(View.INVISIBLE);
            }
        }, 1000);
        //这里分享一个链接，更多分享配置参考官方介绍：https://developers.facebook.com/docs/sharing/android
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.smallgame.by2048"))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mWebView.loadUrl("file:///android_asset/2048/index.html?lang=" + Locale.getDefault().getLanguage());
        TTAdTools.getInstance().showBanner(this, adBannerContainer);
        signInSilently();//需要添加测试账号
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
    }

    /**
     * Saves the full screen setting in the SharedPreferences
     *
     * @param isFullScreen
     */

    private void saveFullScreen(boolean isFullScreen) {
        // save in preferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean(IS_FULLSCREEN_PREF, isFullScreen);
        editor.commit();
    }

    private boolean isFullScreen() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(IS_FULLSCREEN_PREF,
                DEF_FULLSCREEN);
    }

    /**
     * Toggles the activity's fullscreen mode by setting the corresponding window flag
     *
     * @param isFullScreen
     */
    private void applyFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().clearFlags(LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void startDebugSetting() {
        startActivity(new Intent(getApplicationContext(), DebugSettingsActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAd != null) {
            interstitialAd.destroyAd();
            interstitialAd = null;
        }
        TTAdTools.getInstance().destoryBanner();

    }

    /**
     * Prevents app from closing on pressing back button accidentally.
     * mBackPressThreshold specifies the maximum delay (ms) between two consecutive backpress to
     * quit the app.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            backAction();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backAction() {
        if (isHomeShow) {
            if (!sessionRateShow && TTCommonHelper.getInstance().getOpenTimes(this) >= 3 &&
                    TTCommonHelper.getInstance().needRate(this)) {
                showRate();
                return;
            }
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - mLastBackPress) > mBackPressThreshold) {
                pressBackToast.show();
                mLastBackPress = currentTime;
            } else {
                pressBackToast.cancel();
                this.finish();
            }
        } else {
            isHomeShow = true;
            mainView.setVisibility(View.VISIBLE);
            if (interstitialAd.hasLoaded) {
                showAdAfterBack();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                ClickLeft();
                break;
            case R.id.btn_right:
                ClickRight();
                break;
            case R.id.btn_start:
                startGame();
                break;
            case R.id.btn_leader:
                showLeader();
                break;
            default:
                break;
        }
    }
}

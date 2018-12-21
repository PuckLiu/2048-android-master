package com.uberspot.a2048.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by puck on 2018/10/27.
 */

public class TTCommonUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 华氏度 转换为摄氏度
     *
     * @param f 华氏度数
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Number fToc(float f) {
        try {
            DecimalFormat floatFormat = new DecimalFormat("#.0");
            DecimalFormatSymbols custom = new DecimalFormatSymbols();
            custom.setDecimalSeparator('.');
            floatFormat.setDecimalFormatSymbols(custom);
            return floatFormat.parse(floatFormat.format((f - 32) / 1.8f));
        } catch (ParseException e) {

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    /**
//     * 摄氏度转换为华氏度
//     *
//     * @param c 摄氏度数
//     * @return
//     */
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static Number cTof(float c) {
//        try {
//            DecimalFormat floatFormat = new DecimalFormat("#.0");
//            DecimalFormatSymbols custom = new DecimalFormatSymbols();
//            custom.setDecimalSeparator('.');
//            floatFormat.setDecimalFormatSymbols(custom);
//            return floatFormat.parse(floatFormat.format(c * 1.8f + 32));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    public static int getDensityDpi(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static int getThumbnailWidth(Context context){
        int screenWidth = getScreenWidth(context);
        return (int) (340f*screenWidth/720);
    }
    public static int getThumbnailHeight(Context context){
        int screenHeight = getScreenHeight(context);
        return (int) (567f*screenHeight/1280);
    }
    public static String getAppPackageNames(Context context) {
        PackageInfo info;
        String packageName = "";
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            packageName = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }
    public static int getAppVersionCode(Context context) {
        PackageInfo info;
        int versionCode = 0;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static boolean isInstalled(Context context, String packageName){
        try {
            boolean bInstalled = false;
            if(packageName == null) return false;
            PackageInfo packageInfo = null;

            try {
                packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
            }
            if(packageInfo ==null){
                bInstalled = false;
            }else{
                bInstalled = true;
            }
            return bInstalled;
        }catch (Exception e){
            return false;
        }catch (Error error){
            return false;
        }
    }
    public static boolean checkNetWorkConnection(Context context) {
        try{
            boolean isConnect  = true;
            final ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                isConnect = false;
            }
            return isConnect;
        }catch (Throwable e){
            e.printStackTrace();
            return false;
        }
    }
    public static  boolean WTIsSimpleChinese(){
        boolean rtnVlue = false;
        Locale l = Locale.getDefault();
        String langcode = l.getLanguage();
        String countryCode = l.getCountry();
        if(langcode.equalsIgnoreCase("zh") && countryCode.equalsIgnoreCase("cn")){
            rtnVlue = true;
        }
        return rtnVlue;
    }

    public static  boolean WTIsTraditionalChinese(){
        boolean rtnVlue = false;
        Locale l = Locale.getDefault();
        String langcode = l.getLanguage();
        String countryCode = l.getCountry();
        if((langcode.equalsIgnoreCase("zh") && countryCode.equalsIgnoreCase("TW"))
                || langcode.equalsIgnoreCase("zh") && countryCode.equalsIgnoreCase("HK")){
            rtnVlue = true;
        }
        return rtnVlue;
    }
    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static boolean checkIfWifiState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null){
            return false;
        }
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected())
        {
            return true ;
        }
        return false ;
    }


    public static Uri getPhotoUriByPath(Context context, String picPath){
        Uri uri = null;
        try {
            if (!TextUtils.isEmpty(picPath)) {
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Images.ImageColumns.DATA)
                        .append("=")
                        .append("'" + picPath + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    //do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        } catch (Throwable e) {
            TLog.logE(e);
        }
        return uri;
    }
    public static Uri getVideoUriByPath(Context context, String videoPath){
        Uri uri = null;
        try {
            if (!TextUtils.isEmpty(videoPath)) {
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Video.VideoColumns.DATA)
                        .append("=")
                        .append("'" + videoPath + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Video.VideoColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Video.VideoColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    //do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/video/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        } catch (Throwable e) {
            TLog.logE(e);
        }
        return uri;
    }
}

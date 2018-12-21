package com.uberspot.a2048;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;
import java.util.Map;

/**
 * 保存信息配置类
 * Created by liu on 2018/12/2.
 */
public class TTSharedPreferencesUtil {
    private static final String default_key = "TTSharedPreferencesUtil";

    public static final String key_game_level = "game_level";
    public static final String key_game_count = "game_count";
    public static final String key_first_open = "first_open";
    public static final String key_open_times = "open_times";
    public static final String key_rate_five = "rate_five";
    public static final String key_show_rate_sec = "show_rate_sec";
    public static final String key_userInfo_picture = "userInfo_picture";

    public static TTSharedPreferencesUtil instance = null;

    public static TTSharedPreferencesUtil getInstance(Context context) {
        if (instance == null) {
            instance = new TTSharedPreferencesUtil(context,default_key);
        }
        return instance;
    }


        private SharedPreferences sharedPreferences;
        /*
         * 保存手机里面的名字
         */private SharedPreferences.Editor editor;

        public TTSharedPreferencesUtil(Context context,String FILE_NAME) {
            sharedPreferences = context.getSharedPreferences(FILE_NAME,
                    Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

        /**
         * 存储
         */
        public void put(String key, Object object) {
            if (object == null) return;
            if (object instanceof String) {
                if (((String) object).isEmpty()) return;
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else {
                editor.putString(key, object.toString());
            }
            editor.commit();
        }

        /**
         * 获取保存的数据
         */
        public Object getSharedPreference(String key, Object defaultObject) {
            if (defaultObject instanceof String) {
                return sharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sharedPreferences.getLong(key, (Long) defaultObject);
            } else {
                return sharedPreferences.getString(key, null);
            }
        }

        /**
         * 移除某个key值已经对应的值
         */
        public void remove(String key) {
            editor.remove(key);
            editor.commit();
        }

        /**
         * 清除所有数据
         */
        public void clear() {
            editor.clear();
            editor.commit();
        }

        /**
         * 查询某个key是否存在
         */
        public Boolean contain(String key) {
            return sharedPreferences.contains(key);
        }

        /**
         * 返回所有的键值对
         */
        public Map<String, ?> getAll() {
            return sharedPreferences.getAll();
        }


        /* 一些封装*/
        /*****************************************************************/

}

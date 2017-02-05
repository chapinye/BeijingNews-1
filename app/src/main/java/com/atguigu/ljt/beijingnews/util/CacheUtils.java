package com.atguigu.ljt.beijingnews.util;

import android.content.Context;

/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: 缓存工具类
 */

public class CacheUtils {
    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences("atguigu", Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences("atguigu", Context.MODE_PRIVATE).getBoolean(key, false);
    }

}

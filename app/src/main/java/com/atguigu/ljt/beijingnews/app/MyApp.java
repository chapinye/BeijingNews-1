package com.atguigu.ljt.beijingnews.app;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}

package com.atguigu.ljt.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public abstract class MenuDetailBasePager {
    /**
     * 上下文
     */
    public final Context mContext;
    /**
     * 代表各个菜单详情页面的实例，视图
     */
    public View rootView;
    public MenuDetailBasePager(Context context){
        this.mContext = context;
        rootView = initView();
    }

    /**
     * 由子类实现该方法，初始化子类的视图
     * @return
     */
    public abstract View initView() ;

    /**
     绑定数据或者请求数据再绑定数据
     */
    public void  initData(){

    }
}

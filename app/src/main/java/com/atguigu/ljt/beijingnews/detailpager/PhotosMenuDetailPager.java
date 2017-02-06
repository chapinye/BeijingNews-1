package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {
    private TextView textView;

    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        //互动详情页面的视图
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("图组详情页面内容");
    }
}

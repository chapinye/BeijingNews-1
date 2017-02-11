package com.atguigu.ljt.tablayoutviewpagerdemo;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 李金桐 on 2017/2/11.
 * QQ: 474297694
 * 功能: xxxx
 */

public class NewsFragment extends BaseFragment {

    private TextView textView;

    public NewsFragment(Context context) {
        super(context);
        textView = new TextView(mContext);

    }

    @Override
    protected View initView() {

        Log.e("TAG", "NewsFragment initView()"+textView.toString());
        return textView;
    }
    @Override
    public void initData() {
        super.initData();
        if(isInitData) {
            isInitData = !isInitData;
            Log.e("TAG", "NewsFragment initData()");
            textView.setText("新闻");
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(25);
        }

    }
}

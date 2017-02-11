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

public class GamesFragment extends BaseFragment {

    private TextView textView;

    public GamesFragment(Context context) {
        super(context);
        textView = new TextView(mContext);
    }

    @Override
    protected View initView() {
    Log.e("TAG", "GamesFragment initView()");
        return textView;
    }
    @Override
    public void initData() {
        super.initData();
        if(isInitData) {
            isInitData = !isInitData;
            Log.e("TAG", "GamesFragment initData()");
            textView.setText("游戏");
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(25);
        }

    }
}

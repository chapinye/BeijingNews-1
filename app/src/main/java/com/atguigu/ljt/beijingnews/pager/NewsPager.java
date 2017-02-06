package com.atguigu.ljt.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.base.BasePager;
import com.atguigu.ljt.beijingnews.util.Constants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: xxxx
 */

public class NewsPager extends BasePager {

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("新闻");
        tv_title.setTextSize(25);
        ib_menu.setVisibility(View.VISIBLE);
        TextView textView = new TextView(mContext);
        textView.setText("新闻页面");
        textView.setTextSize(25);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        fl_main.addView(textView);

        getDataFromNet();

    }
    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
            Log.e("TAG", "NewsPager onSuccess()result==" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}

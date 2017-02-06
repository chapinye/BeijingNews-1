package com.atguigu.ljt.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.activity.MainActivity;
import com.atguigu.ljt.beijingnews.base.BasePager;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.detailpager.InteractMenuDetailPager;
import com.atguigu.ljt.beijingnews.detailpager.NewsMenuDetailPager;
import com.atguigu.ljt.beijingnews.detailpager.PhotosMenuDetailPager;
import com.atguigu.ljt.beijingnews.detailpager.TopicMenuDetailPager;
import com.atguigu.ljt.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.ljt.beijingnews.util.Constants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;


/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: xxxx
 */

public class NewsPager extends BasePager {

    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;
    private NewsCenterBean newsCenterBean;

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
                Log.e("TAG", "NewsPager onSuccess()请求成功==");
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "NewsPager onError()请求失败=" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "NewsPager onCancelled()=  " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "NewsPager onFinished()");
            }
        });
    }

    private void processData(String json) {
        newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);
//        Log.e("TAG", "NewsPager processData()"+newsCenterBean.getData().get(0).getTitle());
        /**
         * 功过MainActivity的把数据传递给左侧的侧滑菜单
         */
        MainActivity mainActivity = (MainActivity) mContext;

        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();

        menuDetailBasePagers = new ArrayList<>();
        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity));//新闻详情页面
        menuDetailBasePagers.add(new TopicMenuDetailPager(mainActivity));//专题详情页面
        menuDetailBasePagers.add(new PhotosMenuDetailPager(mainActivity));//组图详情页面
        menuDetailBasePagers.add(new InteractMenuDetailPager(mainActivity));//互动详情页面
        leftMenuFragment.setData(newsCenterBean.getData());
    }

    public void switchPager(int mPosition) {

        tv_title.setText(newsCenterBean.getData().get(mPosition).getTitle());
        menuDetailBasePagers.get(mPosition).initData();
        fl_main.removeAllViews();
        fl_main.addView(menuDetailBasePagers.get(mPosition).rootView);
    }
}

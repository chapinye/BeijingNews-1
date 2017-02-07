package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.ljt.beijingnews.util.Constants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class TabDetailPager extends MenuDetailBasePager {

    ListView mListView;
    private NewsCenterBean.DataBean.ChildrenBean childrenBean;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }


    @Override
    public View initView() {
        View view  = View.inflate(mContext, R.layout.tab_detail_pager,null);
        mListView = (ListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params =  new RequestParams(Constants.BASE_URL+childrenBean.getUrl());
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "TabDetailPager onSuccess()");
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                  Log.e("TAG", "TabDetailPager onError()");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        TabDetailPagerBean pagerBean = new Gson().fromJson(json,TabDetailPagerBean.class);
        Log.e("TAG","数据解析成功==TabDetailPager=="+pagerBean.getData().getNews().get(0).getTitle());
    }


    class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}

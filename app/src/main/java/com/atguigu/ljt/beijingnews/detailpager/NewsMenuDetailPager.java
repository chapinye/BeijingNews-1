package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {

    private  List<NewsCenterBean.DataBean.ChildrenBean> dataBeans;
    private ViewPager viewpager;
    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBeans = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.news_menu_detail_pager, null);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        tabDetailPagers = new ArrayList<>();
        for(int i = 0; i <dataBeans.size() ; i++) {
            tabDetailPagers.add(new TabDetailPager(mContext,dataBeans.get(i)));
        }
        viewpager.setAdapter(new MyAdapter());
    }
    class  MyAdapter extends PagerAdapter{
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            tabDetailPagers.get(position).initData();
            container.addView(tabDetailPagers.get(position).rootView);
            return tabDetailPagers.get(position).rootView;
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}

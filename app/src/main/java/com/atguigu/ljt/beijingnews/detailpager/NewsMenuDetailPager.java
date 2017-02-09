package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.activity.MainActivity;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {


    @InjectView(R.id.ib_tab_next)
    ImageButton ibTabNext;

    @InjectView(R.id.viewpager)
    ViewPager viewpager;

    @InjectView(R.id.indicator)
    TabPageIndicator indicator;

    private List<NewsCenterBean.DataBean.ChildrenBean> dataBeans;
    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBeans = dataBean.getChildren();
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.news_menu_detail_pager, null);
        ButterKnife.inject(this, view);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    isShowSlidingMenu(true);
                } else {
                    isShowSlidingMenu(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < dataBeans.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(mContext, dataBeans.get(i)));
        }
        viewpager.setAdapter(new MyAdapter());
        indicator.setViewPager(viewpager);
    }

    @OnClick(R.id.ib_tab_next)
    public void onClick() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
    }

    /**
     * @param isShowSlidingMenu 判断是否显示侧滑菜单
     */
    private void isShowSlidingMenu(boolean isShowSlidingMenu) {
        MainActivity mainActivity = (MainActivity) mContext;
        if (isShowSlidingMenu) {
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return dataBeans.get(position).getTitle();
        }

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

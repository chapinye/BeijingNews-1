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
 * 功能: 新闻详情页面
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
    /**
     * 记录上一次ViewPager页面的下标
     */
    private int oldPosition;

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
                /**
                 * 当是第一个页面的时候 可以侧滑
                 */
                isShowSlidingMenu(position == 0);
            }

            /**
             * 当内层ViewPager 滑动第一个页面从左往右  或者最后一个页面从右往左滑动的时候
             * 将事件交给此类的外层ViewPager执行 内层的ViewPager将不会在有后续的事件
             * 所以内层ViewPager的OnPageChangeListener和触摸事件不会执行
             * 所以内层ViewPager的轮播图不会在继续播放
             *
             * 此方法在外层ViewPager SCROLL_STATE_IDLE状态的时候提取上次页面的下标
             *
             * oldPosition为上次外层ViewPager的下标 此下标为可能出现bug的内层页面的下标
             *
             * 让内层的ViewPager从新发送轮播消息即可解决此BUG
             * 注: 内层ViewPager的handler要为public 并且最好判断不为null在调用
             * @param state
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE && tabDetailPagers.get(oldPosition).handler != null) {
                    tabDetailPagers.get(oldPosition).handler.removeCallbacksAndMessages(null);
                    tabDetailPagers.get(oldPosition).handler.sendEmptyMessageDelayed(0, 3000);
                    oldPosition = viewpager.getCurrentItem();
                }
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
     * @param isShowSlidingMenu 设置侧滑菜单是否显示
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

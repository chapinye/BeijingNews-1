package com.atguigu.ljt.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.base.BaseFragment;
import com.atguigu.ljt.beijingnews.base.BasePager;
import com.atguigu.ljt.beijingnews.pager.HomePager;
import com.atguigu.ljt.beijingnews.pager.NewsPager;
import com.atguigu.ljt.beijingnews.pager.SettingPager;
import com.atguigu.ljt.beijingnews.view.MyViewPager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: ContentFragment 主页面的Fragment
 */

public class ContentFragment extends BaseFragment {


    @InjectView(R.id.viewpager)
    MyViewPager viewpager;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;
    private ArrayList<BasePager> pagers;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        initPager();

        setAdatper();

        setListener();
    }

    private void setListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case  R.id.rb_home:
                        viewpager.setCurrentItem(0,false);
                        break;
                    case  R.id.rb_news:
                        viewpager.setCurrentItem(1,false);
                        break;
                    case  R.id.rb_setting:
                        viewpager.setCurrentItem(2,false);
                        break;
                }
            }
        });
        rgMain.check(R.id.rb_news);
    }

    private void setAdatper() {
        viewpager.setAdapter(new MyAdapter());
    }

    private void initPager() {
        pagers = new ArrayList<>();
        pagers.add(new HomePager(mContext));
        pagers.add(new NewsPager(mContext));
        pagers.add(new SettingPager(mContext));
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = pagers.get(position);
            container.addView(pager.rootView);
            pager.initData();
            return pager.rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

package com.atguigu.ljt.tablayoutviewpagerdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity1 extends AppCompatActivity {

    @InjectView(R.id.tablayout)
    TabLayout tablayout;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    String[] names = {"新闻", "游戏"};
    String[] datas = { };
//    String[] datas = {
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U308.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U311.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U316.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U320.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U324.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U328.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U332.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U336.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U340.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U346.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U349.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U353.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U356.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U400.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U411.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U415.jpg",
//            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U422.jpg"
//    };
    private MyPagerAdapter adapter;
    private ArrayList<ShieldPreloadingDataFragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();
        setListener();
    }

    private void setListener() {
    }

    private void initData() {
        fragments = new ArrayList<>();
        for(int i = 0; i <datas.length ; i++) {
            fragments.add(new GamesFragment(datas[i]));
        }
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        @Override
        public CharSequence getPageTitle(int position) {
            return position+1+"页";
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

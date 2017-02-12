package com.atguigu.ljt.tablayoutviewpagerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.tablayout)
    TabLayout tablayout;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    String[] names = {"新闻", "游戏"};
     String  []  datas = {
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U308.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U311.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U316.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U320.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U324.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U328.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U332.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U336.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U340.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U346.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U349.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U353.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U356.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U400.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U411.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U415.jpg",
            "http://pic1.nhcun.com/uploads/allimg/1607/439-160H20U422.jpg"
    };
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initData();
        setListener();
    }

    private void setListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        adapter = new MyPagerAdapter();
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }
    class MyPagerAdapter extends PagerAdapter{
        @Override
        public CharSequence getPageTitle(int position) {
            return position+1+"页";
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView imageView = new ImageView(MainActivity.this);

            Glide.with(MainActivity.this).load(datas[position])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
            Log.e("TAG", "MyPagerAdapter instantiateItem()"+position);
            container.addView(imageView);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case  MotionEvent.ACTION_UP:
                            if(event.getX()<imageView.getWidth()/3) {
                                viewpager.setCurrentItem(viewpager.getCurrentItem()-1);
                            }else if(event.getX()>imageView.getWidth()/1.5){
                                viewpager.setCurrentItem(viewpager.getCurrentItem()+1);
                            }else{
                                startActivity(new Intent(MainActivity.this,PicassoSampleActivity.class)
                                .putExtra("url",datas[position]));
                            }
                            break;
                    }
                    return true;
                }
            });
            return imageView;
        }

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}

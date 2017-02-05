package com.atguigu.ljt.beijingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.util.CacheUtils;
import com.atguigu.ljt.beijingnews.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point)
    LinearLayout llPoint;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;
    private int ids[] = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private int leftMagin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);
        setListener();
        initData();


    }

    private void setListener() {
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                leftMagin = llPoint.getChildAt(1).getLeft() - llPoint.getChildAt(0).getLeft();
            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                params.leftMargin = position * leftMagin + (int) (leftMagin * positionOffset);
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position != ids.length - 1) {
                    btnStartMain.setVisibility(View.GONE);
                } else {
                    btnStartMain.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        viewpager.setAdapter(new MyAdapter());
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(this, 10);
            }
            imageView.setLayoutParams(params);
            llPoint.addView(imageView);
        }
    }

    class MyAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setBackgroundResource(ids[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @OnClick(R.id.btn_start_main)
    public void onClick() {
        CacheUtils.putBoolean(this, "start_main", true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

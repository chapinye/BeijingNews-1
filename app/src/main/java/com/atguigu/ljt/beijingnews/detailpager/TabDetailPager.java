package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.ljt.beijingnews.util.Constants;
import com.atguigu.ljt.beijingnews.util.DensityUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class TabDetailPager extends MenuDetailBasePager {

    ListView mListView;
    @InjectView(R.id.viewpager)
    ViewPager mViewpager;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_point)
    LinearLayout llPoint;
    private NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topNews;
    private MyBaseAdapter adapter;
    private int oldPosition;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }


    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        mListView = (ListView) view.findViewById(R.id.listview);
        View HeaderView = View.inflate(mContext, R.layout.header_view, null);
        ButterKnife.inject(this, HeaderView);
        mListView.addHeaderView(HeaderView);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(topNews.get(position).getTitle());

                llPoint.getChildAt(oldPosition).setEnabled(false);
                oldPosition = position;
                llPoint.getChildAt(oldPosition).setEnabled(true);
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

        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.BASE_URL + childrenBean.getUrl());
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "TabDetailPager onSuccess()");
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "TabDetailPager onError()" + ex.getMessage());
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
        TabDetailPagerBean pagerBean = new Gson().fromJson(json, TabDetailPagerBean.class);
//        Log.e("TAG","数据解析成功==TabDetailPager=="+pagerBean.getData().getNews().get(0).getTitle());
        news = pagerBean.getData().getNews();
        adapter = new MyBaseAdapter();
        mListView.setAdapter(adapter);
        topNews = pagerBean.getData().getTopnews();

        mViewpager.setAdapter(new MyPagerAdapter());
        tvTitle.setText(topNews.get(oldPosition).getTitle());
        addPoint();
    }

    /**
     * 根据数据的数量动态添加红点
     */
    private void addPoint() {
        llPoint.removeAllViews();
        for(int i = 0; i <topNews.size() ; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext,5),DensityUtil.dip2px(mContext,5));
            if(i!=0) {
                params.leftMargin = DensityUtil.dip2px(mContext,8);
                imageView.setEnabled(false);
            }else{
                imageView.setEnabled(true);
            }
            imageView.setLayoutParams(params);
            llPoint.addView(imageView);
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置默认的和联网请求
            //加载图片
            Glide.with(mContext).load(Constants.BASE_URL + topNews.get(position).getTopimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(imageView);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return topNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
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
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.tab_detail_pager_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TabDetailPagerBean.DataBean.NewsBean newsEntity = news.get(position);
            viewHolder.tvTitle.setText(newsEntity.getTitle());
            viewHolder.tvTime.setText(newsEntity.getPubdate());

            Glide.with(mContext).load(Constants.BASE_URL + newsEntity.getListimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.ivIcon);
            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.iv_icon)
            ImageView ivIcon;
            @InjectView(R.id.tv_title)
            TextView tvTitle;
            @InjectView(R.id.tv_time)
            TextView tvTime;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}

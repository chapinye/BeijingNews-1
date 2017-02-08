package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.activity.NewsDetailActivity;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.bean.TabDetailPagerBean;
import com.atguigu.ljt.beijingnews.util.CacheUtils;
import com.atguigu.ljt.beijingnews.util.Constants;
import com.atguigu.ljt.beijingnews.util.DensityUtil;
import com.atguigu.ljt.beijingnews.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.atguigu.ljt.beijingnews.util.CacheUtils.getString;

/**
 * Created by 李金桐 on 2017/2/6.
 * QQ: 474297694
 * 功能: xxxx
 */

public class TabDetailPager extends MenuDetailBasePager {

    public static final String ID_ARRAY = "id_array";

    ListView mListView;
    @InjectView(R.id.viewpager)
    HorizontalScrollViewPager mViewpager;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_point)
    LinearLayout llPoint;
    private PullToRefreshListView refreshListView;

    private NewsCenterBean.DataBean.ChildrenBean childrenBean;
    private List<TabDetailPagerBean.DataBean.NewsBean> news;
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topNews;
    private MyBaseAdapter adapter;
    private int oldPosition;

    private boolean isLoadMore;
    private String moreUrl;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                if(mViewpager!=null) {
                    mViewpager.setCurrentItem((mViewpager.getCurrentItem() + 1) % topNews.size());
                }
            }
        }
    };

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }


    @Override
    public View initView() {
        /**
         * 初始化ListView部分
         */
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mListView = refreshListView.getRefreshableView();
        /**
         * 初始化顶部ViewPager部分 然后添加到ListView的头部
         */
        View HeaderView = View.inflate(mContext, R.layout.header_view, null);
        ButterKnife.inject(this, HeaderView);
        mListView.addHeaderView(HeaderView);

        //设置各个控件的监听器
        setListener();

        return view;
    }

    private void setListener() {
        /**
         * 点击某条新闻item后让其变灰
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = news.get(position - 2).getId();

                String idArray = getString(mContext, ID_ARRAY);

                if (!idArray.contains(itemId + "")) {
                    CacheUtils.putString(mContext, ID_ARRAY, idArray + itemId + ",");
                    adapter.notifyDataSetChanged();
                }
                /**
                 * 根据点击的下标位置 携带对应的url 跳转到显示新闻页面的activity
                 */
                mContext.startActivity(new Intent(mContext, NewsDetailActivity.class)
                        .putExtra("url", Constants.BASE_URL + news.get(position - 2).getUrl()));

            }
        });
        /**
         * 上拉下拉的声音监听
         */
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        refreshListView.setOnPullEventListener(soundListener);
        /**
         * 上拉加载 下拉刷新的监听
         */
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                Toast.makeText(mContext, "下拉", Toast.LENGTH_SHORT).show();
                isLoadMore = false;
                getDataFromNet(Constants.BASE_URL + childrenBean.getUrl());

            }


            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                Toast.makeText(mContext, "上拉", Toast.LENGTH_SHOR).show();
                if (!TextUtils.isEmpty(moreUrl)) {
                    isLoadMore = true;
                    getDataFromNet(moreUrl);
                } else {
                    Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                    refreshListView.onRefreshComplete();
                }

            }
        });
        /**
         * mViewpager状态改变的监听 改变提示文本和红点高亮
         */
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //根据选中状态切换 小点高亮的状态
                llPoint.getChildAt(oldPosition).setEnabled(false);
                oldPosition = position;
                llPoint.getChildAt(oldPosition).setEnabled(true);

            }

            @Override
            public void onPageSelected(int position) {
                //切换不同页面时 切换不同的新闻文本
                tvTitle.setText(topNews.get(position).getTitle());


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.removeMessages(0);
                        handler.sendEmptyMessageDelayed(0, 3000);
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.removeMessages(0);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        getDataFromNet(Constants.BASE_URL + childrenBean.getUrl());
    }

    private void getDataFromNet(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "TabDetailPager onSuccess()");
                processData(result);
                refreshListView.onRefreshComplete();
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
//        Log.e("TAG","数据解析成功==TabDetailPage=="+pagerBean.getData().getNews().get(0).getTitle());
        if (TextUtils.isEmpty(pagerBean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + pagerBean.getData().getMore();
        }
        if (isLoadMore) {
            news.addAll(pagerBean.getData().getNews());
            adapter.notifyDataSetChanged();
        } else {
            //得到ListView的数据然后设置适配器
            news = pagerBean.getData().getNews();
            adapter = new MyBaseAdapter();
            mListView.setAdapter(adapter);
            //得到ViewPager的数据然后设置适配器
            topNews = pagerBean.getData().getTopnews();
            mViewpager.setAdapter(new MyPagerAdapter());
            tvTitle.setText(topNews.get(oldPosition).getTitle());
            /**
             * 添加红点的方法
             */
            addPoint();
            //让顶部轮播图持续播放的广播
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 3000);
        }

    }

    /**
     * 根据ViewPager页面的数量动态添加红点
     */
    private void addPoint() {
        llPoint.removeAllViews();
        for (int i = 0; i < topNews.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(mContext, 8);
                imageView.setEnabled(false);
            } else {
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

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeMessages(0);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.removeMessages(0);
                            handler.sendEmptyMessageDelayed(0, 3000);
                            break;
                    }

                    return true;
                }
            });
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

            TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);
            viewHolder.tvTitle.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());

            Glide.with(mContext).load(Constants.BASE_URL + newsBean.getListimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default)
                    .error(R.drawable.news_pic_default)
                    .into(viewHolder.ivIcon);

            int itemId = newsBean.getId();

            String idArray = CacheUtils.getString(mContext, ID_ARRAY);
            if (idArray.contains(itemId + "")) {
                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else {
                viewHolder.tvTitle.setTextColor(Color.BLACK);
            }

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

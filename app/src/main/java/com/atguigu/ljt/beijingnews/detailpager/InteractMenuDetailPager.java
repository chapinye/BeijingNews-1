package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.activity.PicassoSampleActivity;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.bean.PhotosMenuDetailbean;
import com.atguigu.ljt.beijingnews.util.CacheUtils;
import com.atguigu.ljt.beijingnews.util.Constants;
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
 * 功能: RecyclerView 实现图组
 */

public class InteractMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean bean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private List<PhotosMenuDetailbean.DataBean.NewsBean> datas;
    private boolean isList = true;

    public InteractMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.bean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.interact_menu_detail_pager, null);
        ButterKnife.inject(this, view);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(Constants.BASE_URL + bean.getUrl());
            }
        });
        swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_bright ,android.R.color.holo_red_light );
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet(Constants.BASE_URL + bean.getUrl());
    }

    private void getDataFromNet(final String url) {
        String cache = CacheUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "InteractMenuDetailPager onSuccess()");
                CacheUtils.putString(mContext, url, result);
                processData(result);
                swiperefreshlayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "InteractMenuDetailPager onError()" + ex.getMessage());
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
        PhotosMenuDetailbean pagerBean = new Gson().fromJson(json, PhotosMenuDetailbean.class);
        datas = pagerBean.getData().getNews();
        MyRectclerViewAdapter adapter = new MyRectclerViewAdapter();
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    public void switchListOrGrid(ImageButton list_or_grid) {
        isList = !isList;
        if (isList) {
            list_or_grid.setImageResource(R.drawable.icon_pic_grid_type);
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        } else {
            list_or_grid.setImageResource(R.drawable.icon_pic_list_type);
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        }
    }

    class MyRectclerViewAdapter extends RecyclerView.Adapter<MyRectclerViewAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(mContext, R.layout.photos_menu_pager_item, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tvTitle.setText(datas.get(position).getTitle());

            Glide.with(mContext).load(Constants.BASE_URL + datas.get(position).getListimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(holder.ivIcon);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @InjectView(R.id.iv_icon)
            ImageView ivIcon;
            @InjectView(R.id.tv_title)
            TextView tvTitle;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.inject(this, itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, PicassoSampleActivity.class)
                                .putExtra("url", Constants.BASE_URL + datas.get(getLayoutPosition()).getListimage()));
                    }
                });
            }
        }
    }
}

package com.atguigu.ljt.beijingnews.detailpager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.bean.PhotosMenuDetailbean;
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
 * 功能: xxxx
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean bean;
    @InjectView(R.id.listview)
    ListView mListview;
    @InjectView(R.id.gridview)
    GridView mGridview;
    private List<PhotosMenuDetailbean.DataBean.NewsBean> datas;
    private boolean isList = true;
    private MyBaseAdapter adapter;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.bean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.photos_menu_detail_pager, null);
        ButterKnife.inject(this, view);
        mListview.setVisibility(View.VISIBLE);
        mGridview.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        getDataFromNet(Constants.BASE_URL + bean.getUrl());
    }

    private void getDataFromNet(String url) {
        RequestParams params = new RequestParams(url);
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
        PhotosMenuDetailbean pagerBean = new Gson().fromJson(json, PhotosMenuDetailbean.class);
        datas = pagerBean.getData().getNews();
        adapter = new MyBaseAdapter();
        mListview.setAdapter(adapter);
        mGridview.setAdapter(adapter);
    }

    public void switchListOrGrid(ImageButton list_or_grid) {
        isList = !isList;
        if (isList) {
            mListview.setVisibility(View.VISIBLE);
            mGridview.setVisibility(View.GONE);
            list_or_grid.setImageResource(R.drawable.icon_pic_grid_type);
        } else {
            mListview.setVisibility(View.GONE);
            mGridview.setVisibility(View.VISIBLE);
            list_or_grid.setImageResource(R.drawable.icon_pic_list_type);
        }
        adapter.notifyDataSetChanged();
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
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
                convertView = View.inflate(mContext, R.layout.photos_menu_pager_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(datas.get(position).getTitle());

            Glide.with(mContext).load(Constants.BASE_URL + datas.get(position).getListimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.home_scroll_default)
                    .error(R.drawable.home_scroll_default)
                    .into(viewHolder.ivIcon);

            return convertView;
        }

        class ViewHolder {
            @InjectView(R.id.iv_icon)
            ImageView ivIcon;
            @InjectView(R.id.tv_title)
            TextView tvTitle;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}

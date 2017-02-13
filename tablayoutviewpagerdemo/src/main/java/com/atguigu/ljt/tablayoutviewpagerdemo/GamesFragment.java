package com.atguigu.ljt.tablayoutviewpagerdemo;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by 李金桐 on 2017/2/11.
 * QQ: 474297694
 * 功能: xxxx
 */

public class GamesFragment extends ShieldPreloadingDataFragment {

    private final String url;
    private ImageView imageView;

    public GamesFragment(String url) {
        super();
        this.url = url;
    }

    @Override
    protected View initView() {
        imageView = new ImageView(getContext());
        Log.e("TAG", "GamesFragment initView()");
        return imageView;
    }

    @Override
    protected void initData() {
        Glide.with(getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        Log.e("TAG", "GamesFragment initData()==");
    }

}

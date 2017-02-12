package com.atguigu.ljt.tablayoutviewpagerdemo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 李金桐 on 2017/2/11.
 * QQ: 474297694
 * 功能: xxxx
 */

public class GamesFragment extends BaseFragment {


    @InjectView(R.id.iv_icon)
    ImageView ivIcon;
    private final View view;

    public GamesFragment(Context context) {
        super(context);
        view = View.inflate(mContext, R.layout.game_fragment, null);
        ButterKnife.inject(this, view);
    }

    @Override
    protected View initView() {
        Log.e("TAG", "GamesFragment initView()");

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        if (isInitData) {
            isInitData = !isInitData;
            Log.e("TAG", "GamesFragment initData()");
            Glide.with(mContext.getApplicationContext()).load("")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(ivIcon);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

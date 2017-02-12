package com.atguigu.ljt.tablayoutviewpagerdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 李金桐 on 2017/2/11.
 * QQ: 474297694
 * 功能: xxxx
 */

public abstract class BaseFragment extends Fragment {
    public   Context mContext;
    protected boolean isInitData = true;
    public BaseFragment(Context context) {
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }


    protected abstract View initView();


    public void initData() {

    }
}

package com.atguigu.ljt.beijingnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by 李金桐 on 2017/2/12.
 * QQ: 474297694
 * 功能:
 * 1.可以屏蔽预加载数据的Fragment 用于配合ViewPager使用
 * 2.切换到其他页面时停止加载数据（可选）
 */

public abstract class ShieldPreloadingDataFragment extends Fragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInitView = false;
    protected boolean isInitData = false;
    protected Context mContext;

    public ShieldPreloadingDataFragment() {
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 用于初始化视图
     *
     * @return
     */
    protected abstract View initView();

    /**
     * 联网请求数据 并绑定数据
     */
    protected abstract void initData();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInitView = true;
        isCanLoadData();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInitView) {
            return;
        }
        if (getUserVisibleHint()) {
            if(!isInitData) {
                initData();
                isInitData = true;
            }
        } else if (isInitData) {
            stopLoad();
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitView = false;
        isInitData = false;

    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }
}


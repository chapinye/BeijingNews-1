package com.atguigu.ljt.beijingnews.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.base.BaseFragment;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;

import java.util.List;

/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: xxxx
 */

public class LeftMenuFragment extends BaseFragment {

    private TextView textView;
    private List<NewsCenterBean.DataBean> datas;

    @Override
    protected View initView() {
        textView = new TextView(mContext);
        return textView;
    }

    @Override
    protected void initData() {
        super.initData();
        textView.setText("侧滑页面");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(30);
    }

    public void setData(List<NewsCenterBean.DataBean> data) {
        this.datas = data;
        for(int i = 0; i <datas.size() ; i++) {
            Log.e("TAG", "LeftMenuFragment setData()"+ datas.get(i).getTitle());
        }
    }
}

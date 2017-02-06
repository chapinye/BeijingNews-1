package com.atguigu.ljt.beijingnews.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.base.BaseFragment;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.util.DensityUtil;

import java.util.List;

/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: xxxx
 */

public class LeftMenuFragment extends BaseFragment {

    private List<NewsCenterBean.DataBean> datas;
    private ListView mListView;
    private MyAdapter adapter;
    private int mPosition;

    @Override
    protected View initView() {
        mListView = new ListView(mContext);
        mListView.setPadding(0, DensityUtil.dip2px(mContext, 40), 0, 0);
        mListView.setBackgroundColor(Color.BLACK);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                adapter.notifyDataSetChanged();
            }
        });
        return mListView;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    public void setData(List<NewsCenterBean.DataBean> data) {
        this.datas = data;
        for (int i = 0; i < datas.size(); i++) {
            Log.e("TAG", "LeftMenuFragment setData()" + datas.get(i).getTitle());
        }
        //得到数据后设置适配器绑定数据
        adapter =  new MyAdapter();
        mListView.setAdapter(adapter);
    }
    class MyAdapter extends BaseAdapter{

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
            TextView textView = (TextView) View.inflate(mContext, R.layout.item_leftmenu,null);

            textView.setEnabled(mPosition==position);
            return textView;
        }
    }
}

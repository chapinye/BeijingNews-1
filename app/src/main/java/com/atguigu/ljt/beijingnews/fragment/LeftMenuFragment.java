package com.atguigu.ljt.beijingnews.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.activity.MainActivity;
import com.atguigu.ljt.beijingnews.base.BaseFragment;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.pager.NewsPager;
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
    private MyAdapter myAdapter;
    private int mPosition;

    @Override
    protected View initView() {
        mListView = new ListView(mContext);
        mListView.setPadding(0, DensityUtil.dip2px(mContext, 40), 0, 0);
        mListView.setBackgroundColor(Color.BLACK);
        /**
         * 设置点击时 item不变色就是透明
            */
                    mListView.setCacheColorHint(0);
            mListView.setSelector(android.R.color.transparent);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录点击的下标 刷新适配器 根据让被点击的item的Enabled为true使齐变色
                mPosition = position;
                myAdapter.notifyDataSetChanged();
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle();
                switchPager(mPosition);

            }
        });
        return mListView;
    }

    private void switchPager(int mPosition) {

        MainActivity mainActivity = (MainActivity) mContext;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = contentFragment.getNewsPager();
        newsPager.switchPager(mPosition);

    }

    @Override
    protected void initData() {
        super.initData();
    }

    public void setData(List<NewsCenterBean.DataBean> data) {
        this.datas = data;
//        for (int i = 0; i < datas.size(); i++) {
//            Log.e("TAG", "LeftMenuFragment setData()" + datas.get(i).getTitle());
//        }
        //得到数据后设置适配器绑定数据
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);

        switchPager(mPosition);
    }

    class MyAdapter extends BaseAdapter {

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

        /**
         * 添加对应的item选项  设置数据和选中状态
         *
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView textView = (TextView) View.inflate(mContext, R.layout.item_leftmenu, null);

            textView.setText(datas.get(position).getTitle());

            textView.setEnabled(mPosition == position);

            return textView;
        }
    }
}

package com.atguigu.ljt.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.activity.MainActivity;

/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: xxxx
 */

public class BasePager {

    protected Context mContext;
    public View rootView;
    public ImageButton ib_menu;
    public TextView tv_title;
    public  FrameLayout fl_main;
    public ImageButton list_or_grid;
    public BasePager(Context context) {
        this.mContext = context;
        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(mContext, R.layout.basepager, null);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        fl_main = (FrameLayout) view.findViewById(R.id.fl_main);
        list_or_grid = (ImageButton) view.findViewById(R.id.list_or_grid);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).getSlidingMenu().toggle();
            }
        });
        return view;
    }
    public  void initData(){

    }
}

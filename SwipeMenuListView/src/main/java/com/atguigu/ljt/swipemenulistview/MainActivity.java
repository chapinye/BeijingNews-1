package com.atguigu.ljt.swipemenulistview;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private SwipeMenuListView listView;

    private SwipeMenuCreator creator = Createcreator();
    private ArrayList<String> datas;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            listView = (SwipeMenuListView) findViewById(R.id.listView);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Toast.makeText(MainActivity.this, datas.get(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        datas.remove(position);
                        adapter.notifyDataSetChanged();
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        initData();
    }

    private void initData() {
        listView.setMenuCreator(creator);
        datas = new ArrayList<String>();
        for(int i = 0; i <30 ; i++) {
          datas.add("Datas--" + i)  ;
        }
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_test,null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_name.setText(datas.get(position));

            return convertView;
        }
        class ViewHolder{
        TextView tv_name;
            public ViewHolder(View view) {
                tv_name = (TextView)view.findViewById(R.id.tv_name);
            }
        }
    }
    @NonNull
    public SwipeMenuCreator Createcreator() {
        return new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(DensityUtil.dip2px(MainActivity.this, 70));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(DensityUtil.dip2px(MainActivity.this, 70));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_launcher);
                // add to menu
                menu.addMenuItem(deleteItem);

            }
        };
    }
}

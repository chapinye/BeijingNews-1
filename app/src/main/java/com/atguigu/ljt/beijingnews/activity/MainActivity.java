package com.atguigu.ljt.beijingnews.activity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.fragment.ContentFragment;
import com.atguigu.ljt.beijingnews.fragment.LeftMenuFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

public class MainActivity extends SlidingFragmentActivity {


    public static final String LEFTMENU_TAG = "leftmenu_tag";
    public static final String CONTENT_TAG = "content_tag";

    private ContentFragment contentFragment;
    private LeftMenuFragment leftMenuFragment;
    private DisplayMetrics met;
    private SlidingMenu slidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.leftmenu);
        slidingMenu = getSlidingMenu();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        slidingMenu.setMode(SlidingMenu.LEFT);

        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        met = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(met);
        slidingMenu.setBehindOffset((int) (met.widthPixels*0.60));

        setFragment();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        getWindowManager().getDefaultDisplay().getMetrics(met);
        slidingMenu.setBehindOffset((int) (met.widthPixels*0.60));
        super.onConfigurationChanged(newConfig);
    }

    private void setFragment() {
        contentFragment = new ContentFragment();
        leftMenuFragment = new LeftMenuFragment();
        /**
         * 添加主页面和侧滑菜单的Fragment  并保存对应的TAG用于获取Fragment实例
         */
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_leftmenu, leftMenuFragment, LEFTMENU_TAG)
                .replace(R.id.fl_main, contentFragment, CONTENT_TAG)
                .commit();
    }

    public LeftMenuFragment getLeftMenuFragment() {
//        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LEFTMENU_TAG);
        return leftMenuFragment;
    }

    public ContentFragment getContentFragment() {
//        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(CONTENT_TAG);
        return contentFragment;
    }

    @Override
    public void onDetachedFromWindow() {
        if (Util.isOnMainThread()) {
            Glide.with(getApplicationContext()).pauseRequests();
        }
        super.onDetachedFromWindow();
    }
}

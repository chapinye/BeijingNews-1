package com.atguigu.ljt.beijingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.util.CacheUtils;

/**
 * 应用启动的旋转动画 当动画结束的时候跳转到引导页面或者主页面
 */
public class WelcomeActivity extends AppCompatActivity {
    private RelativeLayout activity_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activity_welcome = (RelativeLayout) findViewById(R.id.activity_welcome);

        setListener();
    }

    private void setListener() {
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(ra);
        set.addAnimation(aa);
        set.addAnimation(sa);
        activity_welcome.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean b = CacheUtils.getBoolean(WelcomeActivity.this, "start_main");
                if (b) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

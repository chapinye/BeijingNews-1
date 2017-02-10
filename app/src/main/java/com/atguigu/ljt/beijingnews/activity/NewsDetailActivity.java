package com.atguigu.ljt.beijingnews.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ljt.beijingnews.R;
import com.atguigu.ljt.beijingnews.util.CacheUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ib_back)
    ImageButton ibBack;
    @InjectView(R.id.ib_textsize)
    ImageButton ibTextsize;
    @InjectView(R.id.ib_share)
    ImageButton ibShare;
    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    private String url;
    private int textSize;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.inject(this);
        url = getIntent().getStringExtra("url");

        if (url != null) {
            tvTitle.setVisibility(View.GONE);
            ibBack.setVisibility(View.VISIBLE);
            ibTextsize.setVisibility(View.VISIBLE);
            ibShare.setVisibility(View.VISIBLE);

            webview.loadUrl(url);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressbar.setVisibility(View.GONE);
                }
            });

            webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //添加缩放按钮-页面要支持
            webSettings.setBuiltInZoomControls(true);
            //支持双击变大变小-页面支持
            webSettings.setUseWideViewPort(true);

            cacheTextSize();
            chengeTextSize();
        } else {
            Toast.makeText(NewsDetailActivity.this, "网址错误无法预览", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @OnClick({R.id.ib_back, R.id.ib_textsize, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
//                Toast.makeText(NewsDetailActivity.this, "设置字体大小", Toast.LENGTH_SHORT).show();
                showChangeTextSizeDialog();
                break;
            case R.id.ib_share:
//                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                showShare();
                break;
        }
    }

    private void showChangeTextSizeDialog() {
        String[] item = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        cacheTextSize();
        new AlertDialog.Builder(this)
                .setTitle("设置文字大小")
                .setSingleChoiceItems(item, textSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textSize = which;
                    }
                })
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chengeTextSize();
                        CacheUtils.putString(NewsDetailActivity.this, "TextSize", textSize + "");
                    }
                })
                .show();
    }

    private void cacheTextSize() {
        if (TextUtils.isEmpty(CacheUtils.getString(this, "TextSize"))) {
            textSize = 2;
        } else {
            textSize = Integer.parseInt(CacheUtils.getString(this, "TextSize"));
        }
    }

    private void chengeTextSize() {
        switch (textSize) {
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
        }
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("测试");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("测试");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("测试");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.baidu.com");

// 启动分享GUI
        oks.show(this);
    }
}

package com.atguigu.ljt.beijingnews.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ljt.beijingnews.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
    private int textSize ;
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
                Toast.makeText(NewsDetailActivity.this, "分享", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showChangeTextSizeDialog() {
        String[] item = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        new AlertDialog.Builder(this)
                .setTitle("设置文字大小")
                .setSingleChoiceItems(item, 2, new DialogInterface.OnClickListener() {
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
                    }
                })
                .show();
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

}

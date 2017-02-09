package com.atguigu.ljt.beijingnews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.atguigu.ljt.beijingnews.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PicassoSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        String url = getIntent().getStringExtra("url");
        if(url!=null) {
            Picasso.with(this)
                    .load(url)
                    .into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            attacher.update();
                        }
                        @Override
                        public void onError() {
                        }
                    });
        }else{
            Toast.makeText(PicassoSampleActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}

package com.atguigu.ljt.beijingnews.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.atguigu.ljt.beijingnews.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 李金桐 on 2017/2/9.
 * QQ: 474297694
 * 功能: 三级缓存 获取网络图片  将图片设置到图片上
 */

public class BitmapCacheUtils {

    private final Context mContext;
    private LruCache<String, Bitmap> lruCache;

    public BitmapCacheUtils(Context mContext) {
        this.mContext = mContext;
        /**
         * 初始化LruCache  用于一级缓存
         */
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        this.lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

    }

    /**
     * 通过三级缓存 将请求到的图片设置到ImageView上
     * 根据三级缓存原理的精髓
     * 依次从一二三级缓存中获取图片
     *  @param url
     * @param ivIcon
     */
    public void setBitmap(String url, ImageView ivIcon) {
        Bitmap bitmap;
        //一级缓存 从内存获取
        bitmap = getBitmapFromMemory(url);
        if (bitmap != null) {
            ivIcon.setImageBitmap(bitmap);
            return;
        }
        bitmap = getBitmapFromLocal(url);
        //二级缓存 从本地获取
        if (bitmap != null) {
            ivIcon.setImageBitmap(bitmap);
            return;
        }
        //三级缓存 从网络获取
        setBitmapFromNet(url, ivIcon);
    }

    /**
     * 从本地获取图片
     * @param url
     * @return
     */
    private Bitmap getBitmapFromLocal(String url) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(url);
                File file = new File(mContext.getExternalFilesDir(null)+"/imageCache",fileName);
                if(file.exists()) {
                    //缓存到一级缓存中
                    setBitmapFromMemory(url,BitmapFactory.decodeStream(new FileInputStream(file)));
                   return BitmapFactory.decodeStream(new FileInputStream(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "BitmapCacheUtils setBitmapFromLocal()"+"本地图片获取失败");
            }
        }
        return null;
    }

    /**
     * 把图片保存到本地
     * @param url
     * @param bitmap
     */
    private void setBitmapFromLocal(String url, Bitmap bitmap) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                String fileName = MD5Encoder.encode(url);
                File file = new File(mContext.getExternalFilesDir(null)+"/imageCache",fileName);
                File parentFile =   file.getParentFile();
                if(!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
                //缓存到一级缓存中
                setBitmapFromMemory(url,bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "BitmapCacheUtils setBitmapFromLocal()本地图片缓存失败");
            }
        }

    }

    /**
     * 异步任务联网获取图片  如果获取到 设置到一二级缓存中 并且把图片设置到ImageView上
     * @param url
     * @param ivIcon
     */
    private void setBitmapFromNet(final String url, final ImageView ivIcon) {
        new AsyncTask<Void, Void, Bitmap>() {
            //启动异步任务之前给图片设置默认图片
            @Override
            protected void onPreExecute() {
                ivIcon.setImageResource(R.drawable.home_scroll_default);
            }
            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(4000);
                    connection.setConnectTimeout(4000);
                    connection.connect();
                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        if (bitmap != null) {
                            //请求成功 缓存到一二级缓存中
                            setBitmapFromLocal(url,bitmap);
                            setBitmapFromMemory(url, bitmap);
                            return bitmap;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                //获取成功设置图片 如果失败设置失败的图片
                if (bitmap != null) {
                    ivIcon.setImageBitmap(bitmap);
                }else{
                    ivIcon.setImageResource(R.drawable.home_scroll_default);
                }
            }
        }.execute();

    }


    /**
     * 根据内存获取数据
     *
     * @param url
     * @return
     */
    private Bitmap getBitmapFromMemory(String url) {
        return lruCache.get(url);
    }

    /**
     * 把图片保存到内存中
     *
     * @param url
     * @param imageBitmap
     */
    private void setBitmapFromMemory(String url, Bitmap imageBitmap) {
        lruCache.put(url, imageBitmap);
    }

}

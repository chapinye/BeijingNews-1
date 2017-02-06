package com.atguigu.ljt.beijingnews.pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.atguigu.ljt.beijingnews.activity.MainActivity;
import com.atguigu.ljt.beijingnews.base.BasePager;
import com.atguigu.ljt.beijingnews.base.MenuDetailBasePager;
import com.atguigu.ljt.beijingnews.bean.NewsCenterBean;
import com.atguigu.ljt.beijingnews.detailpager.InteractMenuDetailPager;
import com.atguigu.ljt.beijingnews.detailpager.NewsMenuDetailPager;
import com.atguigu.ljt.beijingnews.detailpager.PhotosMenuDetailPager;
import com.atguigu.ljt.beijingnews.detailpager.TopicMenuDetailPager;
import com.atguigu.ljt.beijingnews.fragment.LeftMenuFragment;
import com.atguigu.ljt.beijingnews.util.CacheUtils;
import com.atguigu.ljt.beijingnews.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 李金桐 on 2017/2/5.
 * QQ: 474297694
 * 功能: xxxx
 */

public class NewsPager extends BasePager {

    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;
    private List<NewsCenterBean.DataBean> dataBeanList;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        tv_title.setText("新闻");
        tv_title.setTextSize(25);
        ib_menu.setVisibility(View.VISIBLE);

        String cacheJson = CacheUtils.getString(mContext, Constants.NEWSCENTER_PAGER_URL);
        if (!TextUtils.isEmpty(cacheJson)) {
            processData(cacheJson);
        }
        getDataFromNet();

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "NewsPager onSuccess()请求成功==");
                CacheUtils.putString(mContext, Constants.NEWSCENTER_PAGER_URL, result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "NewsPager onError()请求失败=" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "NewsPager onCancelled()=  " + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "NewsPager onFinished()");
            }
        });
    }

    private void processData(String json) {
//       NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);
//        dataBeanList = newsCenterBean.getData();
        NewsCenterBean newsCenterBean = paraseJson(json);
        dataBeanList = newsCenterBean.getData();
//        Log.e("TAG", "NewsPager processData()"+newsCenterBean.getData().get(0).getTitle());
        /**
         * 功过MainActivity的把数据传递给左侧的侧滑菜单
         */
        MainActivity mainActivity = (MainActivity) mContext;

        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();

        menuDetailBasePagers = new ArrayList<>();
        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity, dataBeanList.get(0)));//新闻详情页面
        menuDetailBasePagers.add(new TopicMenuDetailPager(mainActivity));//专题详情页面
        menuDetailBasePagers.add(new PhotosMenuDetailPager(mainActivity));//组图详情页面
        menuDetailBasePagers.add(new InteractMenuDetailPager(mainActivity));//互动详情页面
        leftMenuFragment.setData(dataBeanList);
    }

    private NewsCenterBean paraseJson(String json) {
        NewsCenterBean newsCenterBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            newsCenterBean.setRetcode(retcode);
            JSONArray data = jsonObject.optJSONArray("data");

            List<NewsCenterBean.DataBean> datas = new ArrayList<>();
            newsCenterBean.setData(datas);

            for (int i = 0; i < data.length(); i++) {
                JSONObject itemObject = (JSONObject) data.get(i);
                if (itemObject != null) {
                    NewsCenterBean.DataBean dataBean = new NewsCenterBean.DataBean();
                    datas.add(dataBean);

                    int id = itemObject.optInt("id");
                    dataBean.setId(id);
                    String title = itemObject.optString("title");
                    dataBean.setTitle(title);
                    int type = itemObject.optInt("type");
                    dataBean.setType(type);
                    String url = itemObject.optString("url");
                    dataBean.setUrl(url);
                    String url1 = itemObject.optString("url1");
                    dataBean.setUrl1(url1);
                    String excurl = itemObject.optString("excurl");
                    dataBean.setExcurl(excurl);
                    String dayurl = itemObject.optString("dayurl");
                    dataBean.setDayurl(dayurl);
                    String weekurl = itemObject.optString("weekurl");
                    dataBean.setWeekurl(weekurl);

                    JSONArray children = itemObject.optJSONArray("children");
                    if (children != null && children.length() > 0) {
                        List<NewsCenterBean.DataBean.ChildrenBean> childrenBeans = new ArrayList<>();
                        dataBean.setChildren(childrenBeans);
                        for (int j = 0; j < children.length(); j++) {
                            NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                            childrenBeans.add(childrenBean);
                            JSONObject childenObje = (JSONObject) children.get(j);
                            int idc = childenObje.optInt("id");
                            childrenBean.setId(idc);
                            String titlec = childenObje.optString("title");
                            childrenBean.setTitle(titlec);
                            int typec = childenObje.optInt("type");
                            childrenBean.setType(typec);
                            String urlc = childenObje.optString("url");
                            childrenBean.setUrl(urlc);
                        }

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return newsCenterBean;
    }


    public void switchPager(int mPosition) {

        tv_title.setText(dataBeanList.get(mPosition).getTitle());
        menuDetailBasePagers.get(mPosition).initData();

        fl_main.removeAllViews();
        fl_main.addView(menuDetailBasePagers.get(mPosition).rootView);
    }
}

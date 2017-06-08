package com.ceyu.carsteward.ad.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.ad.bean.AdBean;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 */
public class AdAdapter extends PagerAdapter {

    private List<AdBean> adList = new ArrayList<>();
    private Activity mActivity;
    private CheImageLoader loader;
    private AppContext appContext;
    private List<View> views = new ArrayList<>();

    public AdAdapter(Activity activity) {
        this.mActivity = activity;
        this.appContext = (AppContext) mActivity.getApplicationContext();
        loader = new CheImageLoader(appContext.queue(), mActivity);
    }

    public void setData(List<AdBean> list) {
        if (list == null || list.size() == 0)
            return;
        this.adList = list;
        List<View> view = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            View imageView = LayoutInflater.from(mActivity).inflate(R.layout.home_banner_item_image_layout, null);
            view.add(imageView);
        }
        views.addAll(view);
    }

    public void cleanData() {
        this.adList.clear();
    }

    @Override
    public int getCount() {
        if (adList == null)
            return 0;
        else
            return adList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = (ImageView) views.get(position);
//        if (adList.size() > 1) {
//            if (position == 0) position = getCount() - 2;
//            if (position == getCount() - 1) position = 1;
//
//            position--;
//        }
        final AdBean adBean = adList.get(position);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(view, R.mipmap.default_img, R.mipmap.default_img);
        loader.get(adBean.getPic(), imageListener);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(adBean.getAct())) return;
                UIHelper.ToastMessage(mActivity, adBean.getAct());
            }
        });
        container.addView(view);
        return view;
    }
}

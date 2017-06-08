package com.ceyu.carsteward.maintain.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainPagerAdapter extends PagerAdapter {

    private ArrayList<CarInfoBean> datas;
    private LayoutInflater layoutInflater;
    private ArrayList<View> views;
    private CheImageLoader imageLoader;
    public MaintainPagerAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        datas = new ArrayList<>();
        views = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    public void setDatas(ArrayList<CarInfoBean> beans){
        if (beans != null && beans.size() > 0){
            this.datas = beans;
            views.clear();
            for (int i = 0; i < beans.size(); i++){
                View view = layoutInflater.inflate(R.layout.maintain_main_view_pager_item_view_layout, null);
                views.add(view);
            }
        }
    }

    public ArrayList<CarInfoBean> getData(){
        return datas;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.maintain_page_image_id);
        TextView textView = (TextView) view.findViewById(R.id.maintain_page_item_content);
        CarInfoBean infoBean = datas.get(position);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.icon_my, R.mipmap.icon_my);
        imageLoader.get(infoBean.get_modelPic(), imageListener);
        CarBrandInfoBean bean = infoBean.getBrandInfoBean();
        textView.setText(bean.get_subBrandName() + " " +bean.get_modelName() + " " +bean.get_capacity() + " " + bean.get_auto());
        return view;
    }
}

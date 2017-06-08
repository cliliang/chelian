package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarBrandBean;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/15.
 */
public class ChoiceBrandAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CarBrandBean> list;
    private RequestQueue requestQueue;
    private CheImageLoader imageLoader;
    public ChoiceBrandAdapter(Context cnt, RequestQueue rq){
        this.mContext = cnt;
        list = new ArrayList<>();
        this.requestQueue = rq;
        imageLoader = new CheImageLoader(requestQueue, mContext);
    }

    public void setData(ArrayList<CarBrandBean> l){
        this.list = l;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.choice_brand_item_layout, null);
        TextView indexView = (TextView) convertView.findViewById(R.id.add_car_choice_brand_item1);
        ImageView iconView = (ImageView) convertView.findViewById(R.id.add_car_broad_icon_id);
        TextView nameView = (TextView) convertView.findViewById(R.id.add_car_choice_brand_item2);
        CarBrandBean bean = list.get(position);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(iconView, R.mipmap.default_car, R.mipmap.default_car);
        imageLoader.get(bean.get_pic(), imageListener);
        String nowIndex = bean.get_index();
        indexView.setText(nowIndex);
        nameView.setText(bean.get_name());
        if (position > 0){
            CarBrandBean front = list.get(position - 1);
            if (front.get_index().equals(nowIndex)){
                indexView.setVisibility(View.GONE);
            }else {
                indexView.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
}

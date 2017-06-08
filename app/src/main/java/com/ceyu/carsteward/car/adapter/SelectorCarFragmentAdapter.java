package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.StringUtils;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/15.
 */
public class SelectorCarFragmentAdapter extends PagerAdapter {

    private ArrayList<CarInfoBean> list;
    private ArrayList<View> views;
    private LayoutInflater layoutInflater;
    private Context mContext;
    private CheImageLoader imageLoader;
    private final int maxCount = 5;
    public SelectorCarFragmentAdapter(Context context) {
        this.mContext = context;
        list = new ArrayList<>();
        views = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
        imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
    }

    public void setData(ArrayList<CarInfoBean> l){
        this.list = l;
        views.clear();
        if (l.size() < maxCount){
            for (int i = 0; i < list.size() + 1; i++){
                View view = layoutInflater.inflate(R.layout.selector_car_info_layout, null);
                views.add(view);
            }
        }
        else {
            for (int i = 0; i < maxCount; i++){
                View view = layoutInflater.inflate(R.layout.selector_car_info_layout, null);
                views.add(view);
            }
        }
    }

    public ArrayList<CarInfoBean> getData(){
        return list;
    }

    @Override
    public int getCount() {
        return views.size();
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
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = views.get(position);
        container.addView(view);
        View infoLayout = view.findViewById(R.id.car_facade_info_layout);
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                CarInfoBean carInfoBean = list.get(position);
                CarBrandInfoBean brandInfoBean = carInfoBean.getBrandInfoBean();
                int year = 0;
                try {
                    year = Integer.parseInt(brandInfoBean.get_year());
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                bundle.putInt(CarEvent.carYearId, year);
                bundle.putInt(CarEvent.carDetailId, carInfoBean.get_modelId());
                bundle.putString(CarEvent.carSeriesName, brandInfoBean.get_brandName() + brandInfoBean.get_modelName());
                bundle.putString(CarEvent.carDetailName, brandInfoBean.get_capacity() + brandInfoBean.get_auto());
                bundle.putParcelable(CarEvent.carBean, carInfoBean);
                bundle.putBoolean(CarEvent.fromHomeEdit, true);
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Car, CarUI.addCarMileage, bundle);
            }
        });
        View addLayout = view.findViewById(R.id.add_new_car_layout);
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarRouter.getInstance(mContext).showActivity(CarUI.addCarBrand);
            }
        });

        if (list.size() < maxCount){
            if (position == views.size() - 1){
                addLayout.setVisibility(View.VISIBLE);
                infoLayout.setVisibility(View.GONE);
            }else {
                addLayout.setVisibility(View.GONE);
                infoLayout.setVisibility(View.VISIBLE);
                setViewData(view, position);
            }
        }else {
            addLayout.setVisibility(View.GONE);
            infoLayout.setVisibility(View.VISIBLE);
            setViewData(view, position);
        }
        return view;
    }

    private void setViewData(View view, final int position){
        CarInfoBean bean = list.get(position);

        TextView nameView = (TextView) view.findViewById(R.id.car_facade_name_id);
        final TextView unitView = (TextView) view.findViewById(R.id.car_facade_kilometre_unit);
        ImageView iconView = (ImageView) view.findViewById(R.id.car_facade_icon_id);
        EditText kiloView = (EditText) view.findViewById(R.id.car_facade_kilometre_id);
        kiloView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null){
                    String input = s.toString();
                    list.get(position).set_mileage(input);
                    if (StringUtils.isEmpty(input)){
                        unitView.setVisibility(View.GONE);
                    }else {
                        unitView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        kiloView.setText(bean.get_mileage());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(iconView, R.mipmap.default_car, R.mipmap.default_car);
        imageLoader.get(bean.get_modelPic(), imageListener);
        CarBrandInfoBean infoBean = bean.getBrandInfoBean();
        StringBuilder builder = new StringBuilder();
        builder.append(infoBean.get_subBrandName())
                .append(" ")
                .append(infoBean.get_modelName())
                .append("\n").append(infoBean.get_capacity())
                .append(" ")
                .append(infoBean.get_auto());
        nameView.setText(builder.toString());
    }
}
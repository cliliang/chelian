package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
 * Created by chen on 15/6/19.
 */
public class CarOfMineAdapter extends BaseAdapter {
    private ArrayList<CarInfoBean> list;
    private LayoutInflater layoutInflater;
    private CheImageLoader imageLoader;
    private boolean fromTuan = false;
    private int choiceId = 0;
    private Context mContext;
    public CarOfMineAdapter(Context context, boolean tuan, int id){
        layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.fromTuan = tuan;
        this.choiceId = id;
        list = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
    }
    public void setData(ArrayList<CarInfoBean> l){
        this.list = l;
    }

    public void setCheckedId(int id){
        this.choiceId = id;
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
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.car_of_mine_list_item_layout, null);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.car_of_mine_icon_id);
        TextView boardView = (TextView) convertView.findViewById(R.id.car_of_mine_board_id);
        TextView modelView = (TextView) convertView.findViewById(R.id.car_of_mine_model_id);
        TextView licenseView = (TextView) convertView.findViewById(R.id.car_of_mine_license_id);
        TextView capacityView = (TextView) convertView.findViewById(R.id.car_of_mine_capacity);
        TextView autoView = (TextView) convertView.findViewById(R.id.car_of_mine_auto);
        ImageView checkBox = (ImageView) convertView.findViewById(R.id.tuan_reserve_choice_car);
        CarInfoBean bean = list.get(position);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(iconView, R.mipmap.default_car, R.mipmap.default_car);
        imageLoader.get(bean.get_modelPic(), imageListener);
        CarBrandInfoBean brandInfoBean = bean.getBrandInfoBean();
        boardView.setText(brandInfoBean.get_brandName());
        modelView.setText(brandInfoBean.get_subBrandName());
        licenseView.setText(bean.get_plate());
        capacityView.setText(brandInfoBean.get_capacity());
        autoView.setText(brandInfoBean.get_auto());
        if (fromTuan){
            checkBox.setVisibility(View.VISIBLE);
            if (choiceId == bean.get_id()){
                checkBox.setImageResource(R.mipmap.check_orange_select);
            }else {
                checkBox.setImageResource(R.mipmap.check_orange_normal);
            }
        }else {
            checkBox.setVisibility(View.GONE);
        }
        return convertView;
    }
}

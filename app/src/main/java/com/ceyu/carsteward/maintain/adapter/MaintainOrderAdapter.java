package com.ceyu.carsteward.maintain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.maintain.bean.MaintainOrderListBean;
import com.ceyu.carsteward.maintain.main.MaintainEvent;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chen on 15/7/1.
 */
public class MaintainOrderAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MaintainOrderListBean> listBeans;
    private LayoutInflater layoutInflater;
    private CheImageLoader imageLoader;
    public MaintainOrderAdapter(Context context){
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        listBeans = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(mContext), mContext);
    }

    public void setData(ArrayList<MaintainOrderListBean> list, boolean firstPage){
        if (list != null){
            if (firstPage){
                this.listBeans = list;
            }else {
                listBeans.addAll(list);
            }
        }
    }
    @Override
    public int getCount() {
        return listBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return listBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.maintain_order_list_item_layout, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.maintain_item_icon);
            TextView carNameView = (TextView) convertView.findViewById(R.id.maintain_item_car_model);
            TextView orderSnView = (TextView) convertView.findViewById(R.id.maintain_item_sn);
            TextView carPlateView = (TextView) convertView.findViewById(R.id.maintain_item_plate);
            TextView orderStateView = (TextView) convertView.findViewById(R.id.maintain_item_state);
            TextView nameView = (TextView) convertView.findViewById(R.id.maintain_item_shop_name);
            TextView shopClassView = (TextView) convertView.findViewById(R.id.maintain_item_shop_class);
            TextView shopPriceView = (TextView) convertView.findViewById(R.id.maintain_item_price);
            TextView addressView = (TextView) convertView.findViewById(R.id.maintain_item_address);
            TextView shopDistanceView = (TextView) convertView.findViewById(R.id.maintain_item_distance);
            TextView shopTimeView = (TextView) convertView.findViewById(R.id.maintain_item_time);
            TextView titleView = (TextView) convertView.findViewById(R.id.tuan_order_title_layout);
            LinearLayout maintainLayout = (LinearLayout) convertView.findViewById(R.id.maintain_order_title_layout);
            placeHolder = new PlaceHolder();
            placeHolder.iconView = imageView;
            placeHolder.nameView = carNameView;
            placeHolder.snView = orderSnView;
            placeHolder.stateView = orderStateView;
            placeHolder.shopNameView = nameView;
            placeHolder.shopClassView = shopClassView;
            placeHolder.shopAddressView = addressView;
            placeHolder.priceView = shopPriceView;
            placeHolder.distanceView = shopDistanceView;
            placeHolder.timeView = shopTimeView;
            placeHolder.plateView = carPlateView;
            placeHolder.tuanTitleView = titleView;
            placeHolder.maintainTitleLayout = maintainLayout;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        MaintainOrderListBean listBean = listBeans.get(position);
        if (listBean != null){
            String orderClass = listBean.get_class();
            if (orderClass.equals(MaintainEvent.orderMaintain) || orderClass.equals(MaintainEvent.orderSelf)){ //保养订单
                placeHolder.maintainTitleLayout.setVisibility(View.VISIBLE);
                placeHolder.tuanTitleView.setVisibility(View.GONE);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(placeHolder.iconView, R.mipmap.icon_my, R.mipmap.icon_my);
                imageLoader.get(listBean.get_carPic(), imageListener);
                CarBrandInfoBean infoBean = listBean.get_car();
                if (infoBean != null){
                    placeHolder.nameView.setText(infoBean.get_brandName() + " " + infoBean.get_modelName() + " " + infoBean.get_capacity());
                }
                placeHolder.plateView.setText(listBean.get_carPlate());
                placeHolder.shopClassView.setText(listBean.get_storeClass());
            }else if (orderClass.equals(MaintainEvent.orderTuan)){ //团购订单
                placeHolder.maintainTitleLayout.setVisibility(View.GONE);
                placeHolder.tuanTitleView.setVisibility(View.VISIBLE);
                placeHolder.tuanTitleView.setText(listBean.get_title());
                placeHolder.shopClassView.setText(mContext.getResources().getString(R.string.tuan_order_list_type));
            }
            placeHolder.snView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.reserve_order_sn), listBean.get_sn()));
            placeHolder.stateView.setText(listBean.get_orderState());
            placeHolder.shopNameView.setText(listBean.get_name());
            placeHolder.shopAddressView.setText(listBean.get_storeAddress());
            placeHolder.priceView.setText(listBean.get_money());
            placeHolder.distanceView.setText(listBean.get_storeDistance());
            placeHolder.timeView.setText(listBean.get_time());
        }

        return convertView;
    }

    private class PlaceHolder{
        private ImageView iconView;
        private TextView nameView, plateView, snView, stateView, shopNameView, shopClassView,shopAddressView, priceView, distanceView, timeView;
        private TextView tuanTitleView;
        private LinearLayout maintainTitleLayout;
    }
}

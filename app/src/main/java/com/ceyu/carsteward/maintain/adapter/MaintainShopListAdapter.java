package com.ceyu.carsteward.maintain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.maintain.bean.ListShopBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/24.
 */
public class MaintainShopListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ListShopBean> shopBeans;
    private CheImageLoader imageLoader;
    public MaintainShopListAdapter(Context cnt){
        this.mContext = cnt;
        shopBeans = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(mContext), mContext);
    }

    public void setData(ArrayList<ListShopBean> beans){
        if (beans != null){
            this.shopBeans = beans;
        }
    }
    @Override
    public int getCount() {
        return shopBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return shopBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.maintain_shop_list_item_layout, null);
            RoundCornerImageView shopImage = (RoundCornerImageView) convertView.findViewById(R.id.maintain_list_shop_image);
            TextView shopName = (TextView) convertView.findViewById(R.id.maintain_list_shop_name);
            TextView shopAddress = (TextView) convertView.findViewById(R.id.maintain_list_shop_address);
            TextView shopPrice = (TextView) convertView.findViewById(R.id.maintain_list_shop_price);
            TextView shopDistance = (TextView) convertView.findViewById(R.id.maintain_list_shop_distance);
            TextView shopDiscount = (TextView) convertView.findViewById(R.id.maintain_list_shop_discount);
            LinearLayout shopDiscountLayout = (LinearLayout) convertView.findViewById(R.id.maintain_list_shop_discount_layout);
            placeHolder = new PlaceHolder();
            placeHolder.nameView = shopName;
            placeHolder.addressView = shopAddress;
            placeHolder.priceView = shopPrice;
            placeHolder.distanceView = shopDistance;
            placeHolder.discountLayout = shopDiscountLayout;
            placeHolder.discountView = shopDiscount;
            placeHolder.imageView = shopImage;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        ListShopBean bean = shopBeans.get(position);
        if (bean != null){
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(placeHolder.imageView, R.mipmap.default_img, R.mipmap.default_img);
            imageLoader.get(bean.get_pic(), imageListener);
            placeHolder.nameView.setText(bean.get_name());
            String address = bean.get_address();
            String add = "";
            if (address.contains("*")){
                add =address.replace("*", "\n");
                placeHolder.addressView.setText(add);
            }else {
                placeHolder.addressView.setText(address);
            }

            placeHolder.priceView.setText(bean.get_quote());
            placeHolder.distanceView.setText(bean.get_distance());
            String discount = bean.get_onlinePay();
            if (!StringUtils.isEmpty(discount)){
                placeHolder.discountLayout.setVisibility(View.VISIBLE);
                placeHolder.discountView.setText(discount);
            }else {
                placeHolder.discountLayout.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    private class PlaceHolder{
        private TextView nameView, addressView, priceView, distanceView, discountView;
        private RoundCornerImageView imageView;
        private LinearLayout discountLayout;
    }
}

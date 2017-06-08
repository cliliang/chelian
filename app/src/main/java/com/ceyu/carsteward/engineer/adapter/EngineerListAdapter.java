package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.views.CircleHeadImageView;
import com.ceyu.carsteward.engineer.bean.EngineerBean;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chen on 15/6/4.
 */
public class EngineerListAdapter extends BaseAdapter{

    private ArrayList<EngineerBean> list1;
    private ArrayList<EngineerBean> list2;
    private ArrayList<EngineerBean> data;
    private Context context;
    private RequestQueue requestQueue;
    private CheImageLoader imageLoader;
    public EngineerListAdapter(Context cnt, RequestQueue queue){
        this.context = cnt;
        this.requestQueue = queue;
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        data = new ArrayList<>();
        imageLoader = new CheImageLoader(requestQueue, context);
    }

    public void setData(ArrayList<EngineerBean> l1, ArrayList<EngineerBean> l2, boolean clearOld){
        if (clearOld){
            list1.clear();
            list2.clear();
            data.clear();
        }

        if (l1 != null && l1.size() > 0){
            if (list1.size() == 0){
                list1 = l1;
            }
            data.addAll(l1);
        }
        if (l2 != null && l2.size() > 0){
            if (list2.size() == 0){
                list2 = l2;
            }else {
                list2.addAll(l2);
            }
            data.addAll(l2);
        }

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.engineer_list_adapter_item_layout, null);
            CircleHeadImageView mHeadView = (CircleHeadImageView) convertView.findViewById(R.id.engineer_item_head_id);
            TextView mNameView = (TextView) convertView.findViewById(R.id.engineer_item_name_id);
            TextView mLocalView = (TextView) convertView.findViewById(R.id.engineer_item_local_id);
            TextView mFunctionlView = (TextView) convertView.findViewById(R.id.engineer_item_function_id);
            TextView mBrandView = (TextView) convertView.findViewById(R.id.engineer_item_brand_id);
            TextView mOrderView = (TextView) convertView.findViewById(R.id.engineer_item_order_id);
            TextView mMoneyView = (TextView) convertView.findViewById(R.id.engineer_item_money_id);
            TextView mHideView = (TextView) convertView.findViewById(R.id.engineer_item_hide_view);
            holder = new PlaceHolder();
            holder.headView = mHeadView;
            holder.nameView = mNameView;
            holder.functionView = mFunctionlView;
            holder.localView = mLocalView;
            holder.orderView = mOrderView;
            holder.brandView = mBrandView;
            holder.moneyView = mMoneyView;
            holder.hideView = mHideView;
            convertView.setTag(holder);
        }else {
            holder = (PlaceHolder) convertView.getTag();
        }
        EngineerBean bean = data.get(position);
        if (list1.size() > 0 && list2.size() > 0){
            if (position == list1.size()){
                holder.hideView.setVisibility(View.VISIBLE);
            }else {
                holder.hideView.setVisibility(View.GONE);
            }
        }else {
            holder.hideView.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(bean.get_pic())){
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.headView, R.mipmap.default_head, R.mipmap.default_head);
            imageLoader.get(bean.get_pic(), imageListener);
        }
        holder.nameView.setText(bean.get_name());
        holder.functionView.setText(bean.get_function());
        holder.localView.setText(bean.get_city());
        holder.orderView.setText(String.format(Locale.US, context.getResources().getString(R.string.engineer_had_service_orders), bean.get_num()));
        holder.brandView.setText(bean.get_model());
        holder.moneyView.setText(String.format(Locale.US, context.getResources().getString(R.string.engineer_service_money_string), bean.get_money()));
        return convertView;
    }

    private class PlaceHolder{
        private CircleHeadImageView headView;
        private TextView nameView, functionView, localView, orderView, brandView, moneyView;
        private TextView hideView;
    }
}

package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.engineer.bean.BrandBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerBrandAdapter extends BaseAdapter {

    private ArrayList<BrandBean> list;
    private Context context;
    public EngineerBrandAdapter(Context cnt) {
        this.context = cnt;
        list = new ArrayList<>();
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

    public void setData(ArrayList<BrandBean> l){
        this.list = l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.engineer_brand_list_item_layout, null);
        TextView item1 = (TextView) convertView.findViewById(R.id.engineer_choice_brand_item1);
        TextView item2 = (TextView) convertView.findViewById(R.id.engineer_choice_brand_item2);
        BrandBean bean = list.get(position);
        String index = bean.getBrandIndex();
        item1.setText(index);
        item2.setText(bean.getBrandName());
        if (position == 0){
            item1.setVisibility(View.GONE);
        }else {
            BrandBean frontBean = list.get(position - 1);
            if (index.equals(frontBean.getBrandIndex())){
                item1.setVisibility(View.GONE);
            }else {
                item1.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }
}

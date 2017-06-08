package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarYearBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/15.
 */
public class ChoiceYearAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<CarYearBean> list;

    public ChoiceYearAdapter(Context cnt){
        layoutInflater = LayoutInflater.from(cnt);
        list = new ArrayList<>();
    }
    public void setData(ArrayList<CarYearBean> l){
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
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.simple_list_item_view_layout, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.simple_item_text);
        CarYearBean yearBean = list.get(position);
        textView.setText(String.valueOf(yearBean.get_year()));
        return convertView;
    }
}

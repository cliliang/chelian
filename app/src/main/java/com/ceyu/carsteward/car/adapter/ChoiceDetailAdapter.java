package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarDetailBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/16.
 */
public class ChoiceDetailAdapter extends BaseAdapter {

    private ArrayList<CarDetailBean> list;
    private LayoutInflater layoutInflater;

    public ChoiceDetailAdapter(Context cnt){
        layoutInflater = LayoutInflater.from(cnt);
        list = new ArrayList<>();
    }

    public void setData(ArrayList<CarDetailBean> l){
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
        CarDetailBean detailBean = list.get(position);
        textView.setText(detailBean.get_name());
        return convertView;
    }
}

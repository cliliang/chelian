package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.engineer.bean.ProvinceBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerLocalAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProvinceBean> provinceList;

    public EngineerLocalAdapter(Context cnt){
        this.context = cnt;
        provinceList = new ArrayList<>();
    }

    public void setData(ArrayList<ProvinceBean> l){
        this.provinceList = l;
    }
    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public Object getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.simple_list_item_view_layout, null);
            TextView item = (TextView) convertView.findViewById(R.id.simple_item_text);
            placeHolder = new PlaceHolder();
            placeHolder.textView = item;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        ProvinceBean bean = provinceList.get(position);
        placeHolder.textView.setText(bean.getProvinceName());
        return convertView;
    }

    private class PlaceHolder{
        private TextView textView;
    }
}

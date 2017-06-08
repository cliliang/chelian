package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.engineer.bean.CityBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/4.
 */
public class EngineerCityAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CityBean> cityList;

    public EngineerCityAdapter(Context cnt){
        this.context = cnt;
        cityList = new ArrayList<>();
    }

    public void setData(ArrayList<CityBean> l){
        this.cityList = l;
    }
    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
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
        CityBean bean = cityList.get(position);
        placeHolder.textView.setText(bean.getCityName());
        return convertView;
    }

    private class PlaceHolder{
        private TextView textView;
    }
}

package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.engineer.bean.LevelBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerTypeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LevelBean> list;
    public EngineerTypeAdapter(Context cnt) {
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

    public void setData(ArrayList<LevelBean> l){
        this.list = l;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.simple_list_item_view_layout, null);
            TextView itemView = (TextView) convertView.findViewById(R.id.simple_item_text);
            placeHolder = new PlaceHolder();
            placeHolder.textView = itemView;
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        LevelBean bean = list.get(position);
        placeHolder.textView.setText(bean.getLevelName());
        return convertView;
    }

    private class PlaceHolder{
        private TextView textView;
    }
}

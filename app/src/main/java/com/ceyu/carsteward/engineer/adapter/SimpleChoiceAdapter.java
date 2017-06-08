package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/3.
 */
public class SimpleChoiceAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> list;
    public SimpleChoiceAdapter(Context context){
        this.mContext = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item_view_layout, null);
            TextView itemView = (TextView) convertView.findViewById(R.id.simple_item_text);
            holder = new PlaceHolder();
            holder.textView = itemView;
            convertView.setTag(holder);
        }else {
            holder = (PlaceHolder) convertView.getTag();
        }
        String text = list.get(position);
        holder.textView.setText(text);
        return convertView;
    }

    private class PlaceHolder{
        public TextView textView;
    }
}

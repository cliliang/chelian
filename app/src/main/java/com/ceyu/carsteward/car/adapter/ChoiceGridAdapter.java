package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;

/**
 * Created by chen on 15/6/16.
 */
public class ChoiceGridAdapter extends BaseAdapter {

    private String[] resources;
    private LayoutInflater layoutInflater;
    public ChoiceGridAdapter(Context cnt, String[] res){
        layoutInflater = LayoutInflater.from(cnt);
        resources = res;
    }

    @Override
    public int getCount() {
        return resources.length;
    }

    @Override
    public Object getItem(int position) {
        return resources[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.choice_grid_window_item_layout, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.grid_view_item_text);
        String text = resources[position];
        if (!StringUtils.isEmpty(text)){
            textView.setText(text);
        }
        return convertView;
    }
}

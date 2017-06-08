package com.ceyu.carsteward.car.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarModelBean;
import com.ceyu.carsteward.car.bean.CarSeries;

import java.util.ArrayList;

import static com.ceyu.carsteward.R.id.choice_series_parent_item;

/**
 * Created by chen on 15/6/15.
 */
public class ChoiceModelAdapter extends BaseExpandableListAdapter {

    private ArrayList<CarSeries> carSeries;
    private LayoutInflater inflater;
    public ChoiceModelAdapter(Context cnt){
        carSeries = new ArrayList<>();
        inflater = LayoutInflater.from(cnt);
    }

    public void setData(ArrayList<CarSeries> list){
        this.carSeries = list;
    }

    @Override
    public int getGroupCount() {
        return carSeries.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return carSeries.get(groupPosition).getModelList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return carSeries.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return carSeries.get(groupPosition).getModelList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.choice_model_parent_layout, null);
        }
        TextView nameView = (TextView) convertView.findViewById(choice_series_parent_item);
        CarSeries car = carSeries.get(groupPosition);
        nameView.setText(car.get_name());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.choice_model_child_layout, null);
        }
        TextView nameView = (TextView) convertView.findViewById(R.id.choice_series_child_item);
        CarModelBean bean = carSeries.get(groupPosition).getModelList().get(childPosition);
        nameView.setText(bean.get_name());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package com.ceyu.carsteward.maintain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.maintain.bean.MaintainDetailContent;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainComboAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<MaintainSubContent> data;

    public MaintainComboAdapter(Context context) {
        this.mContext = context;
        data = new ArrayList<>();
    }

    public void setData(ArrayList<MaintainSubContent> set) {
        if (set != null && set.size() > 0) {
            this.data = set;
        }
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       MaintainSubContent subContent = data.get(groupPosition);
        if (subContent != null){
            ArrayList<MaintainDetailContent> detailContents = subContent.get_sub();
            if (detailContents != null){
                return detailContents.size();
            }
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).get_sub().get(childPosition);
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.maintain_combo_group_item_layout, null);
        }
        TextView nameView = (TextView) convertView.findViewById(R.id.maintain_combo_group_name);
        TextView contentView = (TextView) convertView.findViewById(R.id.maintain_combo_group_content);
        TextView priceView = (TextView) convertView.findViewById(R.id.maintain_combo_group_price);
        ImageView arrowView = (ImageView) convertView.findViewById(R.id.maintain_combo_group_arrow);
        MaintainSubContent subContent = data.get(groupPosition);
        if (subContent != null){
            ArrayList<MaintainDetailContent> detailContents = subContent.get_sub();
            if (detailContents != null && detailContents.size() > 0) {
                arrowView.setVisibility(View.VISIBLE);
            }else {
                arrowView.setVisibility(View.INVISIBLE);
            }
            String contentString = subContent.get_content();
            if (!StringUtils.isEmpty(contentString)){
                nameView.setText(subContent.get_name() + "：");
                contentView.setText(contentString);
            }else {
                nameView.setText(subContent.get_name());
            }
            priceView.setText(subContent.get_price());
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.maintain_combo_child_item_layout, null);
        }
        TextView contentView = (TextView) convertView.findViewById(R.id.maintain_combo_child_info);
        TextView priceView = (TextView) convertView.findViewById(R.id.maintain_combo_child_price);
        MaintainDetailContent detailContent = data.get(groupPosition).get_sub().get(childPosition);
        if (detailContent != null){
            contentView.setText(detailContent.get_info());
            priceView.setText(detailContent.get_moneyTxt());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }
}

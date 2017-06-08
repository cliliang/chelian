package com.ceyu.carsteward.maintain.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.maintain.bean.MileageBean;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chen on 15/6/23.
 */
public class MaintainListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<MileageBean> beans;
    private Context mContext;
    private int selection = 0;
    public MaintainListAdapter(Context context){
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        beans = new ArrayList<>();
    }

    public void setData(ArrayList<MileageBean> data){
        if (data != null){
            beans.clear();
            this.beans = data;
        }
    }

    public void clearData(){
        beans.clear();
    }

    public void setSelection(int index){
        this.selection = index;
    }
    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.maintain_main_list_item_layout, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.mileage_now_dot);
            TextView kiloTextView = (TextView) convertView.findViewById(R.id.mileage_kilo_id);
            TextView partsTextView = (TextView) convertView.findViewById(R.id.mileage_parts_view);
            TextView bangTextView = (TextView) convertView.findViewById(R.id.mileage_bang_money_view);
            TextView factoryTextView = (TextView) convertView.findViewById(R.id.mileage_factory_view);
            placeHolder = new PlaceHolder();
            placeHolder.dotView = imageView;
            placeHolder.kiloView = kiloTextView;
            placeHolder.partsView = partsTextView;
            placeHolder.bangMoneyView = bangTextView;
            placeHolder.factoryMoneyView = factoryTextView;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }

        if (position == selection){
            placeHolder.dotView.setImageResource(R.mipmap.dot_selected);
        }else {
            placeHolder.dotView.setImageResource(R.mipmap.dot_unselected);
        }
        MileageBean bean = beans.get(position);
        placeHolder.kiloView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.mileage_unit), bean.get_num()));
        String[] partsArray  = bean.get_parts();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i< partsArray.length; i++){
            builder.append(partsArray[i]);
            builder.append("\n");
        }
        String partsString = builder.toString();
        placeHolder.partsView.setText(partsString.substring(0, partsString.length() - 1));
        placeHolder.bangMoneyView.setText(bean.get_discount());
        placeHolder.factoryMoneyView.setText(bean.get_factory());
        return convertView;
    }

    private class PlaceHolder{
        private ImageView dotView;
        private TextView kiloView, partsView, bangMoneyView, factoryMoneyView;
    }
}

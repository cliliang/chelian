package com.ceyu.carsteward.tribe.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.tribe.bean.TribeHomeBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/9/8.
 */
public class TribeMyMessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TribeHomeBean> data;
    public TribeMyMessageAdapter(Context cnt){
        this.context = cnt;
        data = new ArrayList<>();
    }

    public void setData(ArrayList<TribeHomeBean> beans){
        if (beans != null){
            this.data = beans;
        }
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.tribe_my_message_item_layout, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.tribe_my_message_id);
        TribeHomeBean bean = data.get(position);
        if (bean != null){
            String txt = context.getResources().getString(R.string.tribe_my_message_hint1)  + bean.get_title() + context.getResources().getString(R.string.tribe_my_message_hint2);
            SpannableStringBuilder builder = new SpannableStringBuilder(txt);
            ForegroundColorSpan hintSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.text_hint));
            ForegroundColorSpan orangeSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.orange));
            builder.setSpan(hintSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(orangeSpan, 3, txt.length() - 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(hintSpan, txt.length() - 5, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
        return convertView;
    }
}

package com.ceyu.carsteward.breakrule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ceyu.carsteward.R;

/**
 * Created by Administrator on 2015/7/6.
 * 暂无记录
 */
public class NoRecordAdapter  extends BaseAdapter{

    private Context mContext;

    public NoRecordAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.breakrule_record_item_norecord, parent, false);
    }


}

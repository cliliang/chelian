package com.ceyu.carsteward.tuan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.tuan.bean.TuanListBean;
import com.ceyu.carsteward.tuan.bean.TuanModBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chen on 15/7/20.
 */
public class TuanHomeListAdapter extends BaseAdapter {

    private Context context;
    private List<TuanListBean> list;
    private CheImageLoader imageLoader;
    public TuanHomeListAdapter(Context cnt){
        this.context = cnt;
        list = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
    }

    public void setData(List<TuanListBean> data){
        if (data != null){
            this.list = data;
        }
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
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.tuan_facade_list_item_layout, null);
        }
        RoundCornerImageView imageView = (RoundCornerImageView) convertView.findViewById(R.id.tuan_facade_list_item_image);
        TextView nameView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_name);
        TextView titleView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_title);
        TextView moneyView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_money);
        TextView marketView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_market);
        marketView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        TextView peopleView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_people);
        TextView addressView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_address);
        TextView distanceView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_distance);
        TextView typeView = (TextView) convertView.findViewById(R.id.tuan_facade_list_item_type);
        TuanListBean listBean = list.get(position);
        if (listBean != null){
            TuanModBean modBean = listBean.get_store();
            if (modBean != null){
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.default_img, R.mipmap.default_img);
                imageLoader.get(modBean.get_pic(), imageListener);
                nameView.setText(modBean.get_name());
                addressView.setText(modBean.get_address().replace("*", ""));
                typeView.setText(modBean.get_class());
            }
            titleView.setText(listBean.get_title());
            moneyView.setText(listBean.get_money());
            marketView.setText(listBean.get_market());
            peopleView.setText(String.format(Locale.US, context.getResources().getString(R.string.tuan_facade_num_of_people), listBean.get_num()));
            distanceView.setText(listBean.get_distance());
        }
        return convertView;
    }
}

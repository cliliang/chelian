package com.ceyu.carsteward.maintain.views;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.tuan.bean.TuanListBean;

import java.util.Locale;

/**
 * Created by chen on 15/8/18.
 */
public class ShopTuanView extends LinearLayout {
    private Context mContext;
    private TextView titleView, peopleView, factoryView, bangView;
    private OnShopTuanClickListener listener;
    public ShopTuanView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init(){
        LayoutInflater.from(mContext).inflate(R.layout.shop_tuan_view_layout, this);
        titleView = (TextView) findViewById(R.id.shop_info_tuan_title);
        peopleView = (TextView) findViewById(R.id.shop_info_tuan_people);
        factoryView = (TextView) findViewById(R.id.shop_info_tuan_factory_money);
        factoryView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        bangView = (TextView) findViewById(R.id.shop_info_tuan_bang_money);
    }

    public void setData(TuanListBean bean){
        if (bean == null){
            return;
        }
        titleView.setText(bean.get_title());
        peopleView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.tuan_facade_num_of_people), bean.get_num()));
        factoryView.setText(bean.get_market());
        bangView.setText(bean.get_money());
        if (listener != null){
            listener.onShopTuanClick(bean.get_gid());
        }
    }

    public interface OnShopTuanClickListener{
        void onShopTuanClick(String gid);
    }

    public void setOnShopTuanClickListener(OnShopTuanClickListener l){
        this.listener = l;
    }
}

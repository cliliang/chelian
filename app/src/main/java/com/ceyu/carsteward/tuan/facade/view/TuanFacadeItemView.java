package com.ceyu.carsteward.tuan.facade.view;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.tuan.bean.TuanListBean;
import com.ceyu.carsteward.tuan.bean.TuanModBean;
import com.ceyu.carsteward.tuan.main.TuanEvent;
import com.ceyu.carsteward.tuan.router.TuanUI;

import java.util.Locale;

/**
 * Created by chen on 15/7/22.
 */
public class TuanFacadeItemView extends LinearLayout {

    private RoundCornerImageView imageView;
    private TextView nameView;
    private TextView titleView;
    private TextView moneyView;
    private TextView peopleView;
    private TextView addressView;
    private TextView distanceView;
    private TextView marketView;
    private TextView typeView;
    private CheImageLoader imageLoader;
    private Context context;

    public TuanFacadeItemView(Context cnt) {
        super(cnt);
        this.context = cnt;
        imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.tuan_facade_list_item_layout, this);
        setClickable(true);
        imageView = (RoundCornerImageView) findViewById(R.id.tuan_facade_list_item_image);
        nameView = (TextView) findViewById(R.id.tuan_facade_list_item_name);
        titleView = (TextView) findViewById(R.id.tuan_facade_list_item_title);
        moneyView = (TextView) findViewById(R.id.tuan_facade_list_item_money);
        peopleView = (TextView) findViewById(R.id.tuan_facade_list_item_people);
        addressView = (TextView) findViewById(R.id.tuan_facade_list_item_address);
        distanceView = (TextView) findViewById(R.id.tuan_facade_list_item_distance);
        typeView = (TextView) findViewById(R.id.tuan_facade_list_item_type);
        marketView = (TextView) findViewById(R.id.tuan_facade_list_item_market);
        marketView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void setData(final TuanListBean listBean){
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
            peopleView.setText(String.format(Locale.US, context.getResources().getString(R.string.tuan_facade_num_of_people), listBean.get_num()));
            distanceView.setText(listBean.get_distance());
            marketView.setText(listBean.get_market());
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TuanEvent.shopId, listBean.get_gid());
                    MainRouter.getInstance(context).showActivity(ModuleNames.Tuan, TuanUI.tuanContent, bundle);
                }
            });
        }
    }
}

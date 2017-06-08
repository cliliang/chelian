package com.ceyu.carsteward.tuan.facade.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.tuan.adapter.TuanHomeListAdapter;
import com.ceyu.carsteward.tuan.bean.TuanListBean;
import com.ceyu.carsteward.tuan.main.TuanEvent;
import com.ceyu.carsteward.tuan.router.TuanRouter;
import com.ceyu.carsteward.tuan.router.TuanUI;

import java.util.List;

/**
 * Created by chen on 15/7/20.
 */
public class TuanFacadeView extends LinearLayout {

    private LinearLayout containerLayout;
    private Context context;
    public TuanFacadeView(Context cnt) {
        super(cnt);
        this.context = cnt;
        init();
    }

    private void init(){
        LayoutInflater.from(context).inflate(R.layout.tuan_facade_view_layout, this);
        containerLayout = (LinearLayout) findViewById(R.id.bang_tuan_facade_container);
        findViewById(R.id.tuan_facade_more_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(context).showActivity(ModuleNames.Tuan, TuanUI.tuanMain);
            }
        });
    }

    public void setData(List<TuanListBean> data){
        containerLayout.removeAllViews();
        if (data != null){
            for (int i = 0; i < data.size(); i++){
                TuanFacadeItemView itemView = new TuanFacadeItemView(context);
                TuanListBean bean = data.get(i);
                if (bean != null){
                    itemView.setData(bean);
                    containerLayout.addView(itemView);
                }
            }
        }
    }
}

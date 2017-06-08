package com.ceyu.carsteward.engineer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.views.HorizontalListView;
import com.ceyu.carsteward.engineer.adapter.OnlineListAdapter;
import com.ceyu.carsteward.engineer.bean.OnlineEngineerInfo;
import com.ceyu.carsteward.engineer.main.EngineerEvent;
import com.ceyu.carsteward.engineer.router.EngineerUI;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/12.
 */
public class OnlineEngineerView extends LinearLayout {
    private OnlineListAdapter adapter;
    private ArrayList<OnlineEngineerInfo> infos = new ArrayList<>();
    public OnlineEngineerView(Context context) {
        super(context);
        init(context);
    }

    public OnlineEngineerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void  init(final Context context){
        LayoutInflater.from(context).inflate(R.layout.online_engineer_view_layout, this);
        HorizontalListView listView = (HorizontalListView) findViewById(R.id.online_engineer_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (infos != null && infos.size() > position){
                    String token = infos.get(position).get_token();
                    Bundle bundle = new Bundle();
                    bundle.putString(EngineerEvent.ENGINEER_TOKEN, token);
                    MainRouter.getInstance(context).showActivity(ModuleNames.Engineer, EngineerUI.engineerInfo, bundle);
                }
            }
        });
        adapter = new OnlineListAdapter(context);
        listView.setAdapter(adapter);
    }

    public void setData(ArrayList<OnlineEngineerInfo> list){
        infos = list;
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
}

package com.ceyu.carsteward.user.facade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.user.facade.view.HomeBlockView;

/**
 * Created by chen on 15/6/1.
 */
public class UserFacadeView extends ModFacadeView {
    public UserFacadeView(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void init(final Context context){
//        View view = LayoutInflater.from(context).inflate(R.layout.home_up_facade_view_layout, null);
//        insertView(view, null);
//        HomeBlockView blockView = (HomeBlockView) view.findViewById(R.id.home_block_layout_view);
//        blockView.setOnUpkeepClickedListener(new HomeBlockView.OnUpkeepClickListener() {
//            @Override
//            public void onUpkeepClicked() {
//                MainRouter.getInstance(context).showActivity(ModuleNames.Maintain, MaintainUI.getMaintain);
//            }
//        });
//        blockView.setOnBangClickedListener(new HomeBlockView.OnBangClickListener() {
//            @Override
//            public void onBangClicked() {
//                MainRouter.getInstance(context).showActivity(ModuleNames.Engineer, EngineerUI.engineerMain);
//            }
//        });
//        blockView.setOnRepairClickedListener(new HomeBlockView.OnRepairClickListener() {
//            @Override
//            public void onRepairClicked() {
//
//            }
//        });
    }
}

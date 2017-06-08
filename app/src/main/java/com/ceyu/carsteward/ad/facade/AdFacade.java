package com.ceyu.carsteward.ad.facade;

import android.content.Context;

import com.ceyu.carsteward.ad.views.AdFacadeView;
import com.ceyu.carsteward.common.module.ModFacadeView;

public class AdFacade extends ModFacadeView{
    private AdFacadeView facadeView;
    public AdFacade(Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context){
        facadeView = new AdFacadeView(context);
        insertView(facadeView, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (facadeView != null){
            facadeView.updateDot();
        }
    }
}

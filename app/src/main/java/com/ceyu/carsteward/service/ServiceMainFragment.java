package com.ceyu.carsteward.service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.maintain.router.MaintainUI;

/**
 * Created by chen on 15/6/1.
 */
public class ServiceMainFragment extends BaseFragment {
    private Context mContext;
    private OnSearchOrderClickListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_main_fragment_layout, null);
        view.findViewById(R.id.main_service_phone_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneTheService();
            }
        });
        view.findViewById(R.id.main_service_look_combo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CarEvent.shopClass, 1);
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Maintain, MaintainUI.getMaintain, bundle);
            }
        });
        view.findViewById(R.id.main_service_look_treasure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(CarEvent.shopClass, 1);
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Maintain, MaintainUI.getMaintain, bundle);
            }
        });
        view.findViewById(R.id.main_service_look_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onSearchOrderClicked();
                }
            }
        });
        return view;
    }

    //联系客服
    private void phoneTheService(){
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.wait_for_engineer_title))
                .setMessage(getString(R.string.bang_service_phonenum_servicenumber) + getString(R.string.bang_service_phonenum_formatted))
                .setPositiveButton(getString(R.string.dial),
                        new DialogInterface.OnClickListener() { //确认拨打
                            @Override
                            public void onClick(DialogInterface dialog,  int which) {
                                Intent intent = new Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:" + getString(R.string.bang_service_phonenum)));
                                startActivity(intent);
                            }
                        })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    public interface OnSearchOrderClickListener{
        void onSearchOrderClicked();
    }

    public void setOnSearchOrderClickedListener(OnSearchOrderClickListener l){
        this.listener = l;
    }
}

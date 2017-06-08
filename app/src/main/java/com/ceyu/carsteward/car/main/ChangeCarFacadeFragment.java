package com.ceyu.carsteward.car.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.BaseFragment;

/**
 * Created by chen on 15/6/15.
 */
public class ChangeCarFacadeFragment extends BaseFragment {

    private ImageView iconImageView;
    private TextView carInfoView;
    private EditText kilometreView;
    private LinearLayout addCarLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selector_car_info_layout, container, false);
        iconImageView = (ImageView) view.findViewById(R.id.car_facade_icon_id);
        carInfoView = (TextView) view.findViewById(R.id.car_facade_name_id);
        kilometreView = (EditText) view.findViewById(R.id.car_facade_kilometre_id);
        addCarLayout = (LinearLayout) view.findViewById(R.id.add_new_car_layout);
        return view;
    }

    public void setData(){
        addCarLayout.setVisibility(View.INVISIBLE);
    }
}

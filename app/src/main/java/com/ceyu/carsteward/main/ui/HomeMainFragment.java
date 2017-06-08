package com.ceyu.carsteward.main.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.module.ModBase;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.ui.BaseFragment;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

/**
 * Created by chen on 15/6/1.
 */
public class HomeMainFragment extends BaseFragment {

    private ArrayList<ModFacadeView> facadeViews = new ArrayList<>();
    private LinearLayout linearView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.general_main_fragment, container, false);
        if (getActivity() != null) {
            linearView = (LinearLayout) view.findViewById(R.id.viewContent);
            if (linearView != null) {
                genSubViews();
                for (ModFacadeView facadeView : facadeViews) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    linearView.addView(facadeView, lp);
                }
            }
        }
        return view;
    }

    private void updateSubView() {
        for (ModFacadeView view : facadeViews) {
            view.onResume();
        }

    }

    @Override
    public void onResume() {
        updateSubView();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        for (ModFacadeView view : facadeViews){
            view.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        linearView.removeAllViews();
    }

    private void genSubViews() {
        if (facadeViews.size() > 0)
            return;

        AppContext appContext = (AppContext) getActivity().getApplicationContext();
        if (appContext != null) {
            for (ModBase mod : appContext.getMods()) {
                ModFacadeView facadeView = mod.getFacadeView(getActivity());
                if (facadeView != null) {
                    facadeViews.add(facadeView);
                }
            }
        }
    }
}

package com.ceyu.carsteward.wiki.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.BaseFragment;

/**
 * Created by chen on 15/8/27.
 */
public class WikiMainFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wiki_main_fragment_layout, container, false);
        return view;
    }
}

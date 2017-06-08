package com.ceyu.carsteward.main.guard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.ui.AppManager;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.views.IndicatorView;

import java.util.ArrayList;

/**
 * Created by ygc on 14-10-23.
 */

public class GuardActivity extends BaseActivity {

    private IndicatorView indicatorView;
    private ViewPager viewPager;
    private GuideAdapter adapter;

    private int currentPageNo = 0;
    private static final String  CURRENT_PAGE_KEY = "currentPageNo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.main_guard_activity);

        indicatorView = (IndicatorView) findViewById(R.id.guard_indicator);
        viewPager = (ViewPager) findViewById(R.id.guard_pager);

        init();
    }

    private void init() {
        ArrayList<GuideAdapter.ViewPageItem> items = new ArrayList<GuideAdapter.ViewPageItem>();
        int layoutId[] = {R.layout.main_guard_frame1_fragment, R.layout.main_guard_frame2_fragment,R.layout.main_guard_frame3_fragment,R.layout.main_guard_frame4_fragment, R.layout.main_guard_frame5_fragment};


        for (int i = 0; i < layoutId.length; i++) {
            Bundle bundle = new Bundle();
            GuideAdapter.ViewPageItem item = new GuideAdapter.ViewPageItem();
            bundle.putInt(GuardFragment.PARAM_LAYOUTID, layoutId[i]);
            bundle.putString("title", "start");
            item.mArgs = bundle;
            items.add(item);
        }
        adapter = new GuideAdapter(getSupportFragmentManager(), items);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new GuardOnPageChangeListener());
        indicatorView.updateScreen(0, items.size());
    }

    @Override
    protected void onResume() {
        viewPager.setCurrentItem(currentPageNo);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().AppExit(this);
        } else {
            flag = super.onKeyDown(keyCode, event);
        }
        return flag;
    }


    public class GuardOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            currentPageNo = arg0;
            indicatorView.updateScreen(arg0, adapter.getCount());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_PAGE_KEY,currentPageNo);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentPageNo = savedInstanceState.getInt(CURRENT_PAGE_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }
}

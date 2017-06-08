package com.ceyu.carsteward.self.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.views.IndicatorView;
import com.ceyu.carsteward.maintain.adapter.MaintainPagerAdapter;
import com.ceyu.carsteward.self.views.SelfViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/9.
 */
public class SelfMainActivity extends BaseActivity {
    private int[] sTabId = new int[]{
            R.id.self_recommend_tab,
            R.id.self_manual_tab
    };

    private Context mContext;
    private SelfViewPager mainPager;
    private SelfViewPager carsViewPager;
    private IndicatorView indicatorView;
    private MaintainPagerAdapter carsAdapter;
    private SelfKiloMaintainFragment kiloFragment;
    private SelfManualMaintainFragment choiceFragment;
    private SelfManualMaintainFragment manualFragment;
    private Bundle choiceBundle;
    private AppConfig appConfig;
    private ArrayList<CarInfoBean> carInfoBeans = null;
    private CarInfoBean selectedCar;

    //推荐保养是否已点击里程
    private boolean showChoiceFragment = false;
    private int dotCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_main_activity_layout);
        mContext = this;
        appConfig = AppConfig.getInstance(mContext);
        carsViewPager = (SelfViewPager) findViewById(R.id.self_top_view_pager);
        carsViewPager.setScrollable(true);
        indicatorView = (IndicatorView) findViewById(R.id.self_top_view_indicator);
        carsAdapter = new MaintainPagerAdapter(mContext);
        carsViewPager.setAdapter(carsAdapter);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            carInfoBeans = bundle.getParcelableArrayList(CarEvent.carInfoList);
            if (carInfoBeans != null && carInfoBeans.size() > 0){
                carsAdapter.setDatas(carInfoBeans);
                carsAdapter.notifyDataSetChanged();
                dotCount = carInfoBeans.size();
                indicatorView.updateScreen(0, dotCount);
                selectedCar = carInfoBeans.get(0);
                appConfig.setModelId(selectedCar.get_modelId());
            }else {
                return;
            }
        }else {
            return;
        }

        mainPager = (SelfViewPager) findViewById(R.id.self_main_view_pager);
        final List<Fragment> fragments = new ArrayList<>();

        kiloFragment = new SelfKiloMaintainFragment();
        bundle.putParcelable(CarEvent.selectedCar, selectedCar);
        kiloFragment.setArguments(bundle);
        fragments.add(kiloFragment);

        manualFragment = new SelfManualMaintainFragment();
        Bundle manualBundle = new Bundle();
        manualBundle.putBoolean(CarEvent.getData, true);
        manualBundle.putParcelable(CarEvent.selectedCar, selectedCar);
        manualFragment.setArguments(manualBundle);
        fragments.add(manualFragment);

        choiceFragment = new SelfManualMaintainFragment();
        choiceBundle = new Bundle();
        choiceBundle.putParcelable(CarEvent.selectedCar, selectedCar);
        choiceBundle.putBoolean(CarEvent.kiloFragment, true);
        choiceFragment.setArguments(choiceBundle);
        fragments.add(choiceFragment);

        SelfAdapter adapter = new SelfAdapter(getSupportFragmentManager(), fragments);
        mainPager.setAdapter(adapter);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.self_radio_group);
        radioGroup.check(R.id.self_recommend_tab);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int index = 0; index < sTabId.length; index++) {
                    if (sTabId[index] == checkedId) {
                        if (mainPager.getCurrentItem() != index) {
                            if (index == 0) {
                                if (showChoiceFragment) {
                                    mainPager.setCurrentItem(2, false);
                                    carsViewPager.setScrollable(false);
                                } else {
                                    mainPager.setCurrentItem(0);
                                    carsViewPager.setScrollable(true);
                                }
                            } else {
                                if (mainPager.getCurrentItem() == 0) {
                                    mainPager.setCurrentItem(1);
                                    carsViewPager.setScrollable(true);
                                } else {
                                    mainPager.setCurrentItem(1, false);
                                    carsViewPager.setScrollable(true);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        });

        kiloFragment.setOnKiloItemClickListener(new SelfKiloMaintainFragment.OnKiloItemClickedListener() {
            @Override
            public void onKiloItemClick(final String kilo) {
                if (mainPager.getCurrentItem() != 2) {
                    showChoiceFragment = true;
                    mainPager.setCurrentItem(2, false);
                    carsViewPager.setScrollable(false);
                    choiceFragment.getData(kilo);
                }
            }
        });

        appConfig = AppConfig.getInstance(mContext);
        carsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicatorView.updateScreen(position, dotCount);
                ArrayList<CarInfoBean> infoBeans = carsAdapter.getData();
                if (infoBeans != null && infoBeans.size() > position) {
                    CarInfoBean bean = infoBeans.get(position);
                    if (bean != null) {
//                        carId = bean.get_id();
//                        modelId = bean.get_modelId();
//                        getMileageList(bean.get_modelId(), false);
                        selectedCar = bean;
                        kiloFragment.changeCar(bean);
                        manualFragment.changeCar(bean);
                        appConfig.setModelId(bean.get_modelId());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class SelfAdapter extends FragmentPagerAdapter{

        private List<Fragment> data = new ArrayList<>();
        public SelfAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.data = list;
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }

    @Override
    public void onBackPressed() {
        if (mainPager.getCurrentItem() == 2){
            mainPager.setCurrentItem(0, false);
            carsViewPager.setScrollable(true);
            showChoiceFragment = false;
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackClick() {
        if (mainPager.getCurrentItem() == 2){
            mainPager.setCurrentItem(0, false);
            carsViewPager.setScrollable(true);
            showChoiceFragment = false;
        }else {
            super.onBackClick();
        }
    }
}

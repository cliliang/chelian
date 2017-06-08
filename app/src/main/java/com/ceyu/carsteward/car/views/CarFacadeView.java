package com.ceyu.carsteward.car.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.adapter.SelectorCarFragmentAdapter;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.car.main.CarURLs;
import com.ceyu.carsteward.car.main.ChangeCarFacadeFragment;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.views.IndicatorView;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.ceyu.carsteward.maintain.main.MaintainURLs;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.packet.router.PacketUI;
import com.ceyu.carsteward.record.router.RecordUI;
import com.ceyu.carsteward.self.router.SelfUI;
import com.ceyu.carsteward.tuan.router.TuanUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.user.facade.view.HomeBlockView;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/15.
 */
public class CarFacadeView extends LinearLayout {
    private IndicatorView indicatorView;
    private SelectorCarFragmentAdapter adapter;
    private int indicatorCount;
    private ViewPager viewPager;
    private Context mContext;
    private ArrayList<CarInfoBean> dataList = new ArrayList<>();
    private AppContext appContext;
    private User activeUser;
    private HomeBlockView blockView;
    private int carId;
    private int page = 0;
    private Intent broadcastIntent;
    public CarFacadeView(Context context) {
        super(context);
        this.mContext = context;
        appContext = (AppContext) mContext.getApplicationContext();
        activeUser = appContext.getActiveUser();
        broadcastIntent = new Intent();
        broadcastIntent.setAction(CarEvent.refreshOnlineAction);
        broadcastIntent.putExtra(CarEvent.clearCarCache, false);
        init(context);
    }

    private void init(final Context context){
        LayoutInflater.from(context).inflate(R.layout.car_facade_view_layout, this);
        blockView = (HomeBlockView) findViewById(R.id.home_block_layout_view);
        viewPager = (ViewPager) findViewById(R.id.car_facade_viewpager);
        viewPager.setOnPageChangeListener(new OnCarChangeListener());
        indicatorView = (IndicatorView) findViewById(R.id.car_facade_indicator);
        indicatorView.updateScreen(0, 1);
        adapter = new SelectorCarFragmentAdapter(context);
        viewPager.setAdapter(adapter);
        dataList = new ArrayList<>();
        //4s店保养
        blockView.setOnUpkeepClickedListener(new HomeBlockView.OnUpkeepClickListener() {
            @Override
            public void onUpkeepClicked() {
                dataList = adapter.getData();
                if (dataList != null && dataList.size() > 0){
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(CarEvent.carInfoList, dataList);
                    bundle.putInt(CarEvent.shopClass, 1);
                    if (dataList.size() > page){
                        CarInfoBean bean = dataList.get(page);
                        bundle.putString(CarEvent.carMileage, bean.get_mileage());
                        updateMileage(carId, bean.get_mileage());
                    }
                    MainRouter.getInstance(context).showActivity(ModuleNames.Maintain, MaintainUI.getMaintain, bundle);
                }else {
                    UIHelper.ToastMessage(mContext, R.string.please_add_car_first);
                }
            }
        });
        //技师帮帮
        blockView.setOnBangClickedListener(new HomeBlockView.OnBangClickListener() {
            @Override
            public void onBangClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.Engineer, EngineerUI.engineerMain);
            }
        });
        //维修厂保养
        blockView.setOnRepairClickedListener(new HomeBlockView.OnRepairClickListener() {
            @Override
            public void onRepairClicked() {
                Bundle bundle = new Bundle();
                if (dataList != null && dataList.size() > 0){
                    bundle.putParcelableArrayList(CarEvent.carInfoList, dataList);
                    bundle.putInt(CarEvent.shopClass, 2);
                    if (dataList.size() > page){
                        CarInfoBean bean = dataList.get(page);
                        bundle.putString(CarEvent.carMileage, bean.get_mileage());
                        updateMileage(carId, bean.get_mileage());
                    }
                    MainRouter.getInstance(context).showActivity(ModuleNames.Maintain, MaintainUI.getMaintain, bundle);
                }else {
                    UIHelper.ToastMessage(mContext, R.string.please_add_car_first);
                }
            }
        });
        //养车记录
        blockView.setOnRecordClickListener(new HomeBlockView.OnRecordClickListener() {
            @Override
            public void onRecordClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.Record, RecordUI.recordList);
            }
        });
        //帮帮团购
        blockView.setOnTuanClickListener(new HomeBlockView.OnTuanClickListener() {
            @Override
            public void onTuanClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.Tuan, TuanUI.tuanMain);
            }
        });
        //违章查询
        blockView.setOnWzClickListener(new HomeBlockView.OnWzClickListener() {
            @Override
            public void onWzClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.BreakRuls, PacketUI.breakRulesRecord);
            }
        });
        //领取红包
        blockView.setOnRedbagClickListener(new HomeBlockView.OnRedbagClickListener() {
            @Override
            public void onRedbagClicked() {
                MainRouter.getInstance(context).showActivity(ModuleNames.Packet, PacketUI.takePacket);
            }
        });
        //敬请期待
        blockView.setOnHopeClickListener(new HomeBlockView.OnHopeClickListener() {
            @Override
            public void onHopeClicked() {
//                UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_wait_for_a_moment));
                Bundle bundle = new Bundle();
                if (dataList != null && dataList.size() > 0){
                    bundle.putParcelableArrayList(CarEvent.carInfoList, dataList);
                    bundle.putInt(CarEvent.shopClass, 2);
                    if (dataList.size() > page){
                        CarInfoBean bean = dataList.get(page);
                        bundle.putString(CarEvent.carMileage, bean.get_mileage());
                        updateMileage(carId, bean.get_mileage());
                    }
                    MainRouter.getInstance(context).showActivity(ModuleNames.Self, SelfUI.selfMain, bundle);
                }else {
                    UIHelper.ToastMessage(mContext, R.string.please_add_car_first);
                }
            }
        });
    }

    public void setData(ArrayList<CarInfoBean> list){
        dataList = list;
        adapter = new SelectorCarFragmentAdapter(mContext);
        adapter.setData(list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0, true);
        indicatorCount = list.size() < 5 ? list.size() + 1 : 5;
        indicatorView.updateScreen(0, indicatorCount);
        if (list.size() > 0){
            CarInfoBean carInfoBean = list.get(0);
            if (carInfoBean != null){
                appContext.setCarInfo(carInfoBean);
                carId = carInfoBean.get_id();
                mContext.sendBroadcast(broadcastIntent);
            }
        }
    }

    public void updateIndicatro(){
        if (indicatorCount > 0){
            indicatorView.updateScreen(page, indicatorCount);
        }
    }

    private class OnCarChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            indicatorView.updateScreen(position, indicatorCount);
            if (!(dataList.size() < 5 && position == dataList.size())){
                CarInfoBean carInfoBean = dataList.get(position);
                if (carInfoBean != null){
                    appContext.setCarInfo(carInfoBean);
                    carId = carInfoBean.get_id();
                }
                page = position;
            }else {
                page = dataList.size();
            }
            mContext.sendBroadcast(broadcastIntent);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private void updateMileage(int cid, String kilo){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("mileage", kilo);
        map.put("cid", String.valueOf(cid));
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(CarURLs.modifyCar, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(objectRequest);
        requestQueue.start();
    }

}

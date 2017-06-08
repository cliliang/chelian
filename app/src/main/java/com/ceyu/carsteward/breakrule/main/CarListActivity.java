package com.ceyu.carsteward.breakrule.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.breakrule.adapter.CarListAdapter;
import com.ceyu.carsteward.breakrule.router.BreakRulesRouter;
import com.ceyu.carsteward.breakrule.router.BreakRulesUI;
import com.ceyu.carsteward.car.bean.CarBrandInfoBean;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.car.router.CarRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;

import com.ceyu.carsteward.common.tools.BaiduLBS;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.ui.BaseActivity;

import org.json.JSONArray;


import java.util.List;

/**
 * Created by Administrator on 2015/6/29.
 * 违章记录车列表
 */
public class CarListActivity extends BaseActivity implements AdapterView.OnItemClickListener,Response.Listener<JSONArray>, Response.ErrorListener {

    private ListView listView;
    private List<CarInfoBean> carInfos;
    private CarListAdapter adapter;

    /*
    判断是否刷新的布尔值
    如果去添加车辆回页面后刷新页面
     */
    private boolean doFresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breakrule_carlist);
        setTitle(R.string.check_break_rules);
        getCarList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(doFresh){
            //if(listView!=null) listView.setAdapter(null);
            getCarList();
            doFresh = false;
        }
    }

    private void getCarList(){
        progressDialog.show(this, true);
        AppContext appContext = (AppContext) this.getApplicationContext();
        Utility.LogUtils.e("wz", BreakRulesURLs.myCarList+"&token="+appContext.getActiveUser().getToken());
        CheJSONArrayRequest request = new CheJSONArrayRequest(BreakRulesURLs.myCarList,
                new Utility.ParamsBuilder(appContext).build()
                , this, this);
        appContext.queue().add(request);
    }

    //是否有headerview的判断
    private boolean hasHeader = true;
    private View headerView;

    private void loadData(){
        listView = (ListView) findViewById(R.id.lv_breakrules_carlist_mainlist);
        listView.setOnItemClickListener(this);

        if(carInfos == null){
            listView.setAdapter(null);
        }else{
            adapter = new CarListAdapter(carInfos, this);
            listView.setDivider(new ColorDrawable(getResources().getColor(R.color.line_color)));
            listView.setDividerHeight(1);
        }
        //如果车辆少于5辆显示添加车辆的header
        if(headerView==null) headerView = getLayoutInflater().inflate(R.layout.breakrule_carlist_headerview, listView, false);
        if(carLessThanFive(carInfos)){  //如果车辆小于5辆
            if(listView.getHeaderViewsCount()==0) { //如果没添加过HeaderView
                listView.addHeaderView(headerView);
            }
            hasHeader = true;
        }else{
            if(listView.getHeaderViewsCount()>0){   //如果添加过headerview则移除
                listView.removeHeaderView(headerView);
            }
            hasHeader = false;
        }
        listView.setSelector(android.R.color.transparent);
        listView.setAdapter(adapter);
    }

    private boolean carLessThanFive(List<CarInfoBean> carInfos){
        return carInfos == null || carInfos.size()<5;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        dismissDialog(this);
        Utility.LogUtils.ex(volleyError);
        UIHelper.ToastMessage(this, R.string.xml_parser_failed);
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        dismissDialog(this);
        Utility.LogUtils.e("wz", jsonArray.toString());
        if(jsonArray == null || TextUtils.isEmpty(jsonArray.toString())){
            onErrorResponse(null);
        }else{
            carInfos = CarInfoBean.fromJsonArray(jsonArray);
        }
        loadData();
    }

    private int position;   //记录点击的是哪辆车，如果进入违章列表界面但是信息不全则返回后跳转到编辑界面

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(hasHeader) {
            if (position == 0) {  //添加车辆
                Bundle bundle = new Bundle();
                bundle.putBoolean(CarEvent.fromBreakRule, true);
                CarRouter.getInstance(this).showActivity(CarUI.addCarBrand, bundle);
                doFresh = true;
            } else {
                jumpToOtherPage(position-1);
            }
        }else{
            jumpToOtherPage(position);
        }
    }

    //跳转
    private void jumpToOtherPage(int position){
        if (position < 0) return;
        CarInfoBean carInfo = carInfos.get(position);
        String frame = carInfo.get_frame();
        String motor = carInfo.get_motor();
        if (TextUtils.isEmpty(frame) || TextUtils.isEmpty(motor)) {   //车架号或发动机号为空，编辑车辆详情
            toEditCar(position);
        } else { //进入违章详情页
            this.position = position;   //储存点击位置
            Bundle bundle = new Bundle();
            bundle.putParcelable("car", carInfos.get(position));
            bundle.putString(BreakRulesURLs.TAG_CID, String.valueOf(carInfos.get(position).get_id()));
            BreakRulesRouter.getInstance(this).showActivityForResult(this, BreakRulesUI.recordList, BreakRulesURLs.FORRSUTL_BREAKRULES, bundle);

        }
    }

    private void toEditCar(int position){
        CarInfoBean carInfo = carInfos.get(position);
        //从CarOfMineActivity复制{
        CarBrandInfoBean brandInfoBean = carInfo.getBrandInfoBean();
        Bundle bundle = new Bundle();
        bundle.putInt(CarEvent.carId, carInfo.get_id());
        bundle.putInt(CarEvent.carYearId, Integer.parseInt(brandInfoBean.get_year()));
        bundle.putInt(CarEvent.carDetailId, carInfo.get_modelId());
        bundle.putString(CarEvent.carDetailName, brandInfoBean.get_capacity() + brandInfoBean.get_auto());
        bundle.putString(CarEvent.carSeriesName, brandInfoBean.get_brandName() + brandInfoBean.get_modelName());
        bundle.putParcelable(CarEvent.carBean, carInfo);
        bundle.putBoolean(CarEvent.fromBreakRule, true);
        //CarRouter.getInstance(this).showActivity(CarUI.addCarMileage, bundle);
        CarRouter.getInstance(this).showActivity(CarUI.addCarMileage, bundle);
        //}
        doFresh = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BreakRulesURLs.FORRSUTL_BREAKRULES){  //查询违章
            if(resultCode==BreakRulesURLs.RESULT_NOTHINGH){ //什么都不做

            }else if(resultCode==BreakRulesURLs.RESULT_TOEDITCAR){  //去编辑车辆
                UIHelper.ToastMessage(this, BreakRulesURLs.B0014);
                toEditCar(this.position);
            }else if(resultCode==BreakRulesURLs.RESULT_TOSETTINGLOC){   //去打开定位设置
                UIHelper.ToastMessage(this, BreakRulesURLs.B0015);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                startActivityForResult(intent, BreakRulesURLs.FORRESULT_LOCSETTING);
                //startActivity(intent);
            }
        }else if(requestCode == BreakRulesURLs.FORRESULT_LOCSETTING){   //打开定位设置页
            BaiduLBS baiduLBS = BaiduLBS.getInstanse(this);
            //开始定位
            baiduLBS.startDetailLocation();
            UIHelper.ToastMessage(this, getString(R.string.breakrules_wait_for_location));
        }
    }
}

package com.ceyu.carsteward.engineer.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.CacheFile;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.common.views.CommonDialog;
import com.ceyu.carsteward.engineer.adapter.EngineerBrandAdapter;
import com.ceyu.carsteward.engineer.adapter.EngineerListAdapter;
import com.ceyu.carsteward.engineer.adapter.EngineerTypeAdapter;
import com.ceyu.carsteward.engineer.bean.BrandBean;
import com.ceyu.carsteward.engineer.bean.EngineerBean;
import com.ceyu.carsteward.engineer.bean.LevelBean;
import com.ceyu.carsteward.engineer.bean.MechanicMenuBean;
import com.ceyu.carsteward.engineer.bean.ProvinceBean;
import com.ceyu.carsteward.engineer.router.EngineerRouter;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.ceyu.carsteward.engineer.view.ChoiceEngineerView;
import com.ceyu.carsteward.engineer.view.ChoiceEngineerWindow;
import com.ceyu.carsteward.user.bean.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/6/3.
 */
public class EngineerMainActivity extends BaseActivity{

    private ChoiceEngineerView choiceEngineerView;
    private ArrayList<LevelBean> levelList;
    private ArrayList<BrandBean> brandList;
    private ArrayList<ProvinceBean> localList;
    private ProvinceBean nowProvince;
    private MechanicMenuBean mechanicMenuBean;
    private EngineerListAdapter adapter;
    private EngineerListBean listBean;
    private ProgressDialog progressDialog;
    private ChoiceEngineerWindow provinceWindow, brandWindow, levelWindow;
    private PullToRefreshListView engineerList;
    private TextView nothingView;
    private Context mContext;
    private AppContext appContext;
    private User activeUser;
    private int mBrand = 0;
    private int mFunction = 0;
    private int mPage = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engineer_main_activity_layout);
        setTitle(getResources().getString(R.string.user_need_help_form_engineer));
        setRightTitle(getResources().getString(R.string.engineer_bang_right_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(mContext, getResources().getString(R.string.engineer_bang_dialog_title), getResources().getString(R.string.engineer_bang_dialog_content));
                dialog.show();
            }
        });
        mContext = EngineerMainActivity.this;
        appContext = (AppContext) mContext.getApplicationContext();
        activeUser = appContext.getActiveUser();
        nothingView = (TextView) findViewById(R.id.user_choice_engineer_not_hint);
        choiceEngineerView = (ChoiceEngineerView) findViewById(R.id.user_choice_engineer_view_id);
        CarInfoBean carInfoBean = appContext.getCarInfo();
        if (carInfoBean != null){
            mBrand = carInfoBean.get_brandId();
            String defaultBrandName = carInfoBean.getBrandInfoBean().get_brandName();
            if (!StringUtils.isEmpty(defaultBrandName)){
                choiceEngineerView.setBrandName(defaultBrandName);
            }
        }
        choiceEngineerView.setOnBrandClickListener(new ChoiceEngineerView.OnBrandClickListener() {
            @Override
            public void onBrandClicked(boolean show) {
                if (listBean != null && show) {
                    showChoiceBrandWindow();
                }
            }
        });
        choiceEngineerView.setOnTypeClickListener(new ChoiceEngineerView.OnTypeClickListener() {
            @Override
            public void onTypeClicked(boolean show) {
                if (listBean != null && show) {
                    showChoiceTypeWindow();
                }
            }
        });
        choiceEngineerView.setOnLocalClickListener(new ChoiceEngineerView.OnLocalClickListener() {
            @Override
            public void onLocalClicked(boolean show) {
                if (listBean != null && show) {
//                    showChoiceLocalWindow();
                }
            }
        });

        engineerList = (PullToRefreshListView) findViewById(R.id.show_choice_engineer_list);
        engineerList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        engineerList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isLoading) {
                    return;
                }
                if (listBean != null && listBean.isHaveNext()) {
                    getEngineerList(false);
                } else {
                    engineerList.onRefreshComplete(true);
                }
            }
        });
        engineerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int item = 0;
                if (position > 0) {
                    item = position - 1;
                }
                EngineerBean bean = (EngineerBean) adapter.getItem(item);
                Bundle bundle = new Bundle();
                bundle.putString(EngineerEvent.ENGINEER_TOKEN, bean.get_token());
                EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerInfo, bundle);
            }
        });
//        getAllData();
        adapter = new EngineerListAdapter(mContext, requestQueue);
        engineerList.setAdapter(adapter);
        progressDialog = ProgressDialog.getInstance();
        getMechanicMenu();
        getEngineerList(true);
    }

    private void dismissDialog(){
        if (progressDialog == null){
            return;
        }
        if (progressDialog.isShowing() && !isFinishing()){
            progressDialog.dismiss();
        }
    }

    private void showChoiceBrandWindow(){
        if (brandWindow == null){
            brandWindow = new ChoiceEngineerWindow(EngineerMainActivity.this);
            View view = LayoutInflater.from(EngineerMainActivity.this).inflate(R.layout.simple_listview_layout, null);
            brandWindow.setContentView(view);
            view.findViewById(R.id.engineer_line1).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.engineer_line2).setVisibility(View.VISIBLE);
            ListView brandListView = (ListView) view.findViewById(R.id.simple_item_text_id);
            final EngineerBrandAdapter adapter = new EngineerBrandAdapter(EngineerMainActivity.this);
            if (brandList != null && brandList.size() > 1){
                adapter.setData(brandList);
            }
            brandListView.setAdapter(adapter);
            brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BrandBean brandBean = (BrandBean) adapter.getItem(position);
                    if (brandBean != null) {
                        mBrand = brandBean.getBrandId();
                        choiceEngineerView.setBrandName(brandBean.getBrandName());
                        getEngineerList(true);
                    }
                    if (brandWindow.isShowing() && !EngineerMainActivity.this.isFinishing()) {
                        brandWindow.dismiss();
                    }
                }
            });
            brandWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    choiceEngineerView.downArrow();
                }
            });
        }
        brandWindow.showAsDropDown(choiceEngineerView);
    }

    private void showChoiceTypeWindow() {
        levelWindow = new ChoiceEngineerWindow(EngineerMainActivity.this);
        View view = LayoutInflater.from(EngineerMainActivity.this).inflate(R.layout.simple_listview_layout, null);
        levelWindow.setContentView(view);
        view.findViewById(R.id.engineer_line1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.engineer_line2).setVisibility(View.INVISIBLE);
        ListView typeListView = (ListView) view.findViewById(R.id.simple_item_text_id);
        final EngineerTypeAdapter adapter = new EngineerTypeAdapter(EngineerMainActivity.this);
        if (levelList != null){
            adapter.setData(levelList);
        }
        typeListView.setAdapter(adapter);
        typeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LevelBean bean = (LevelBean) adapter.getItem(position);
                if (bean != null) {
                    mFunction = bean.getLevelId();
                    getEngineerList(true);
                    choiceEngineerView.setTypeName(bean.getLevelName());
                }
                if (levelWindow.isShowing() && !EngineerMainActivity.this.isFinishing()) {
                    levelWindow.dismiss();
                }
            }
        });
        levelWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                choiceEngineerView.downArrow();
            }
        });
        levelWindow.setOutsideTouchable(true);

        levelWindow.showAsDropDown(choiceEngineerView);
    }

    private void getEngineerList(final boolean isNew){

        if (!Utils.isNetworkConnected(mContext)) {
            UIHelper.ToastMessage(mContext, R.string.network_not_connected);
        }
        if (progressDialog != null && isNew){
            progressDialog.show(mContext);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("model", String.valueOf(mBrand));
        map.put("function", String.valueOf(mFunction));
        if (isNew){
            mPage = 1;
        }else {
            mPage += 1;
        }
        map.put("page", String.valueOf(mPage));
        Log.i("chen", "page:" + mPage);
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getEngineerList, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                isLoading = false;
                dismissDialog();
                if (jsonObject.has(ResponseCode.ResponseState)){
                    String state = jsonObject.optString(ResponseCode.ResponseState);
                    if (state.equals(ResponseCode.ResponseError)){
                        String info = jsonObject.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }else {
                    listBean = new EngineerListBean();
                    listBean.fromObject(jsonObject);
                    if (listBean != null){
                        if (listBean.getBeanList1() != null && listBean.getBeanList2() != null &&  listBean.getBeanList1().size() == 0 && listBean.getBeanList2().size() == 0){
                            engineerList.setVisibility(View.GONE);
                            nothingView.setVisibility(View.VISIBLE);
                        }else {
                            engineerList.setVisibility(View.VISIBLE);
                            nothingView.setVisibility(View.GONE);
                            adapter.setData(listBean.getBeanList1(), listBean.getBeanList2(), isNew);
                            adapter.notifyDataSetChanged();
                            engineerList.onRefreshComplete(true);
                            if (!listBean.isHaveNext()){
                                engineerList.setMode(PullToRefreshBase.Mode.DISABLED);
                            }else if(isNew){
                                engineerList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                            }
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                isLoading = false;
                dismissDialog();
                engineerList.onRefreshComplete(true);
            }
        });
        isLoading = true;
        requestQueue.add(request);
        requestQueue.start();
    }

    private class EngineerListBean{
        private boolean haveNext;
        private ArrayList<EngineerBean> beanList1;
        private ArrayList<EngineerBean> beanList2;

        public EngineerListBean() {
        }

        public boolean isHaveNext() {
            return haveNext;
        }

        public void setHaveNext(boolean haveNext) {
            this.haveNext = haveNext;
        }

        public ArrayList<EngineerBean> getBeanList1() {
            return beanList1;
        }

        public void setBeanList1(ArrayList<EngineerBean> beanList1) {
            this.beanList1 = beanList1;
        }

        public ArrayList<EngineerBean> getBeanList2() {
            return beanList2;
        }

        public void setBeanList2(ArrayList<EngineerBean> beanList2) {
            this.beanList2 = beanList2;
        }

        public void fromObject(JSONObject object){
            if (object == null){
                return;
            }
            String item = "list1";
            if (object.has(item)){
                String res1 = object.optString(item);
                setBeanList1(EngineerBean.fromEngineerArrayString(res1));
            }
            item = "list2";
            if (object.has(item)){
                String res2 = object.optString(item);
                setBeanList2(EngineerBean.fromEngineerArrayString(res2));
            }
            item = "more";
            if (object.has(item)){
                setHaveNext(object.optInt(item) > 0);
            }
        }
    }

    private void getMechanicMenu(){
        String cacheString = CacheFile.getInstance(mContext).readCacheString(CacheFile.MechanicMenu);
        if (StringUtils.isEmpty(cacheString)){
            //data from net
            getMechanicMenuFromNet("");
        }else {
            //data from cache
            mechanicMenuBean = MechanicMenuBean.fromString(cacheString);
            getMechanicMenuFromNet(mechanicMenuBean.getCode());
            getAllData(mechanicMenuBean);
        }
    }

    private void getMechanicMenuFromNet(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getMechanicMenu, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (object.has(ResponseCode.ResponseState)){
                    String state = object.optString(ResponseCode.ResponseState);
                    if (!state.equals("nochang")){
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(object.optString(ResponseCode.ResponseInfo)));
                    }
                }else {
                    mechanicMenuBean = MechanicMenuBean.fromJson(object);
                    getAllData(mechanicMenuBean);
                    CacheFile.getInstance(mContext).writeCacheString(object.toString(), CacheFile.MechanicMenu);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void getAllData(MechanicMenuBean mechanicMenuBean) {
        if (mechanicMenuBean == null) {
            return;
        }
        levelList = mechanicMenuBean.getLevelBeans();
        brandList = mechanicMenuBean.getBrandBeans();

        LevelBean levelBean = new LevelBean();
        levelBean.setLevelId(0);
        levelBean.setLevelName(getResources().getString(R.string.engineer_all_function));
        levelList.add(0, levelBean);

        BrandBean brandBean = new BrandBean();
        brandBean.setBrandIndex("0");
        brandBean.setBrandId(0);
        brandBean.setBrandName(getResources().getString(R.string.engineer_all_brand));
        brandList.add(0, brandBean);
    }
}

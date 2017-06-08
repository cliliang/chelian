package com.ceyu.carsteward.self.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.self.bean.SelfMapBean;
import com.ceyu.carsteward.self.bean.SelfMyLocalBean;
import com.ceyu.carsteward.self.bean.SelfShopLocalBean;
import com.ceyu.carsteward.self.router.SelfRouter;
import com.ceyu.carsteward.self.router.SelfUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chen on 15/9/16.
 */
public class SelfShopMapActivity extends BaseActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private Context mContext;
    private User activeUser;
    private TextView shopNameView, shopShortView, shopDistanceView, shopAddressView, nextView;
    private final String MARK_ID = "markId";
    private final String LOCAL_INFO = "local_info";
    private String defaultShopId = "";
    private List<Marker> markerList = new ArrayList<>();
    private String shopId;
    private SelfShopLocalBean choiceShopLocal;
    private boolean resetShop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_shop_map_activity_layout);
        setTitle(R.string.self_choice_shop);
        mContext = this;
        activeUser = ((AppContext)getApplicationContext()).getActiveUser();

        shopNameView = (TextView) findViewById(R.id.self_map_shop_name);
        shopShortView = (TextView) findViewById(R.id.self_map_shop_short);
        shopDistanceView = (TextView) findViewById(R.id.self_map_shop_distance);
        shopAddressView = (TextView) findViewById(R.id.self_map_shop_address);
        nextView = (TextView) findViewById(R.id.self_map_next_step);
        mapView = (MapView) findViewById(R.id.self_shop_map_view);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .zoomTo(11);
        baiduMap.animateMapStatus(mapStatusUpdate);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraBundle = marker.getExtraInfo();
                if (extraBundle != null) {
                    SelfShopLocalBean localBean = extraBundle.getParcelable(LOCAL_INFO);
                    if (localBean != null){
                        setInfoData(localBean);
                        resetMarker(localBean);
                        if (resetShop){
                            choiceShopLocal = localBean;
                            Intent intent = new Intent();
                            intent.putExtra(SelfEvent.shopDetail, choiceShopLocal);
                            setResult(SelfEvent.CHOICE_SHOP_RESULT, intent);
                            finish();
                        }else {
                            shopId = localBean.get_id();
                        }
                    }
                }
                return false;
            }
        });
        nextView.setEnabled(false);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            resetShop = bundle.getBoolean(SelfEvent.choiceShop);
            if (resetShop){
                shopId = bundle.getString(SelfEvent.choiceShopId);
                nextView.setVisibility(View.GONE);
            }else {
                nextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString(CarEvent.shopId, shopId);
                        SelfRouter.getInstance(mContext).showActivity(SelfUI.selfMechanic, bundle);
                    }
                });
            }
        }
        getAllShops();
    }

    private void getAllShops(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.getAllShops, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog(SelfShopMapActivity.this);
                SelfMapBean bean = SelfMapBean.fromJson(response);
                setMapData(bean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog(SelfShopMapActivity.this);
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setMapData(SelfMapBean bean){
        if (bean == null){
            return;
        }
        nextView.setEnabled(true);
        List<SelfShopLocalBean> shopLocalBeans = bean.getShopLocal();
        if (shopLocalBeans != null && shopLocalBeans.size() > 0){
            for (int i = 0; i < shopLocalBeans.size(); i++){
                final SelfShopLocalBean shopLocal = shopLocalBeans.get(i);
                if (shopLocal == null){
                    return;
                }
                LatLng point = new LatLng(shopLocal.get_lat(), shopLocal.get_lon());
                BitmapDescriptor bitmap;
                if (resetShop){
                    if (shopId.equals(shopLocal.get_id())){
                        choiceShopLocal = shopLocal;
                        setInfoData(shopLocal);
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.local_select);
                    }else {
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.local_normal);
                    }
                }else {
                    if (i == 0){
                        choiceShopLocal = shopLocal;
                        defaultShopId = shopLocal.get_id();
                        shopId = defaultShopId;
                        setInfoData(shopLocal);
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.local_select);
                    }else {
                        bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.local_normal);
                    }
                }
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                Marker marker = (Marker) baiduMap.addOverlay(option);
                markerList.add(marker);
                final Bundle bundle = new Bundle();
                bundle.putParcelable(LOCAL_INFO, shopLocal);
                marker.setExtraInfo(bundle);
            }
        }

        SelfMyLocalBean myLocalBean = bean.getMyLocal();
        if (myLocalBean != null){
            LatLng point = new LatLng(myLocalBean.get_lat(), myLocalBean.get_lon());
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.self_local);
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            baiduMap.addOverlay(option);
        }
    }

    private void setInfoData(SelfShopLocalBean bean){
        if (bean == null){
            return;
        }
        shopNameView.setText(bean.get_name());
        shopDistanceView.setText(bean.get_distance());
        shopAddressView.setText(bean.get_address());
        if (defaultShopId.equals(bean.get_id())){
            shopShortView.setVisibility(View.VISIBLE);
        }else {
            shopShortView.setVisibility(View.GONE);
        }
    }

    private void resetMarker(SelfShopLocalBean localBean){
        if (markerList != null && localBean != null){
            for (int i = 0; i < markerList.size(); i++){
                Marker marker = markerList.get(i);
                Bundle bundle = marker.getExtraInfo();
                if (bundle != null){
                    SelfShopLocalBean bean = bundle.getParcelable(LOCAL_INFO);
                    if (bean != null){
                        if (bean.get_id().equals(localBean.get_id())){
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.local_select));
                        }else {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.local_normal));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}

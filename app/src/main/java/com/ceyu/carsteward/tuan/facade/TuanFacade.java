package com.ceyu.carsteward.tuan.facade;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.module.ModFacadeView;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.tuan.bean.TuanListBean;
import com.ceyu.carsteward.tuan.facade.view.TuanFacadeView;
import com.ceyu.carsteward.tuan.main.TuanURLs;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

/**
 * Created by chen on 15/7/20.
 */
public class TuanFacade extends ModFacadeView {

    private TuanFacadeView facadeView;
    private RequestQueue requestQueue;
    private User user;
    public TuanFacade(Context context) {
        super(context);
        requestQueue = Volley.newRequestQueue(context);
        user = ((AppContext)context.getApplicationContext()).getActiveUser();
        clearViews();
        facadeView = new TuanFacadeView(context);
        insertView(facadeView, null);
    }

    @Override
    public void onResume() {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        CheJSONArrayRequest request = new CheJSONArrayRequest(TuanURLs.getHomeTuan, map, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                if (array != null){
                    List<TuanListBean> tuanListBeans = TuanListBean.fromJsonArray(array);
                    facadeView.setData(tuanListBeans);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(request);
        requestQueue.start();
        super.onResume();
    }
}

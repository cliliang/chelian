package com.ceyu.carsteward.tribe.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.common.ui.views.PLAView.PLA_AdapterView;
import com.ceyu.carsteward.common.ui.views.PLAView.XListView;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.tribe.adapter.TribeGalleryAdapter;
import com.ceyu.carsteward.tribe.bean.TribeHomeBean;
import com.ceyu.carsteward.tribe.bean.TribeMainBean;
import com.ceyu.carsteward.tribe.router.TribeRouter;
import com.ceyu.carsteward.tribe.router.TribeUI;
import com.ceyu.carsteward.tribe.views.TribeViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/8/27.
 */
public class TribeMainFragment extends BaseFragment {

    private Context mContext;
    private AppContext appContext;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private TribeGalleryAdapter adapter;
    private TribeViewPager tribeViewPager;
    private XListView listView;
    private TribeMainBean bean;
    private OnTribeMainLoadDataListener listener;
    private Boolean isLoading = false;
    private int onCreateCount = 0;
    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        appContext = (AppContext) mContext.getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);
        progressDialog = ProgressDialog.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        onCreateCount++;
        View view = inflater.inflate(R.layout.tribe_main_fragment_layout, container, false);
        listView = (XListView) view.findViewById(R.id.tribe_gallery_list);
        tribeViewPager = new TribeViewPager(mContext);
        listView.addHeaderView(tribeViewPager);
        adapter = new TribeGalleryAdapter(mContext, Utils.getScreenWidth(getActivity()));
        listView.setAdapter(adapter);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                getData(false);
            }

            @Override
            public void onLoadMore() {
                if (isLoading) {
                    return;
                }
                if (bean != null && bean.is_more()) {
                    getData(true);
                } else {
                    listView.stopLoadMore();
                }
            }
        });
        listView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                if (position > 1) {
                    TribeHomeBean homeBean = (TribeHomeBean) adapter.getItem(position - 2);
                    if (homeBean != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(TribeEvent.tribeDetailId, homeBean.get_id());
                        TribeRouter.getInstance(mContext).showActivity(TribeUI.tribeDetail, bundle);
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        if (onCreateCount == 1 || bean == null){
            progressDialog.show(mContext);
        }else {
            setViewData(bean, false);
        }
        getData(false);
        super.onResume();
    }

    private void getData(final boolean loadMore){
        isLoading = true;
        HashMap<String, String> map = new HashMap<>();
        map.put("token", appContext.getActiveUser().getToken());
        if (loadMore){
            page++;
        }else {
            page = 1;
        }
        map.put("page", String.valueOf(page));
        CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.mainTribe, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                progressDialog.dismiss();
                isLoading = false;
                if (loadMore){
                    listView.stopLoadMore();
                }else {
                    listView.stopRefresh();
                }
                TribeMainBean  mainBean = TribeMainBean.fromJson(object);
                if (mainBean != null){
                    if (loadMore){
                        if (bean == null){
                            bean = mainBean;
                        }else {
                            listView.stopLoadMore();
                            ArrayList<TribeHomeBean> tempBeans = mainBean.getHomeBeans();
                            ArrayList<TribeHomeBean> oldBeans = bean.getHomeBeans();
                            if (tempBeans != null && oldBeans != null){
                                oldBeans.addAll(oldBeans.size(), tempBeans);
                                bean.setHomeBeans(oldBeans);
                            }
                        }
                    }else {
                        bean = mainBean;
                    }
                    if (!mainBean.is_more()){
                        listView.setPullLoadEnable(false);
                    }
                    if (listener != null){
                        listener.onTribeMainLoadData(mainBean.get_tome());
                    }
                }
                setViewData(mainBean, !loadMore);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                listView.stopLoadMore();
                isLoading = false;
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(TribeMainBean bean, boolean clearData){
        if (bean == null){
            return;
        }
        listView.setPullLoadEnable(bean.is_more());
        adapter.setData(bean.getHomeBeans(), clearData);
        adapter.notifyDataSetChanged();
        tribeViewPager.setData(bean.getHotBeans());
    }

    public interface OnTribeMainLoadDataListener{
        void onTribeMainLoadData(int messageCount);
    }

    public void setOnTribeMainLoadData(OnTribeMainLoadDataListener l){
        this.listener = l;
    }
}

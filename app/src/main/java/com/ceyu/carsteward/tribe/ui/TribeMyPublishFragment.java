package com.ceyu.carsteward.tribe.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.common.ui.views.PLAView.PLA_AdapterView;
import com.ceyu.carsteward.common.ui.views.PLAView.XListView;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.tribe.adapter.TribeGalleryAdapter;
import com.ceyu.carsteward.tribe.bean.TribeDetailBean;
import com.ceyu.carsteward.tribe.bean.TribeHomeBean;
import com.ceyu.carsteward.tribe.bean.TribeMainBean;
import com.ceyu.carsteward.tribe.router.TribeRouter;
import com.ceyu.carsteward.tribe.router.TribeUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 15/9/2.
 */
public class TribeMyPublishFragment extends BaseFragment {
    private XListView listView;
    private User activeUser;
    private Context mContext;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private TribeGalleryAdapter adapter;
    private int page = 1;
    private int onCreateViewCount = 0;
    private boolean loading = false;
    private TribeMainBean detailBean;
    private OnPublishLoadListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        requestQueue = Volley.newRequestQueue(mContext);
        progressDialog = ProgressDialog.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tribe_my_publish_fragment_layout, container, false);
        listView = (XListView) view.findViewById(R.id.tribe_mine_my_publish_list);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(false);
        adapter = new TribeGalleryAdapter(mContext, Utils.getScreenWidth(getActivity()));
        listView.setAdapter(adapter);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (loading) {
                    return;
                }
                if (detailBean != null && detailBean.is_more()) {
                    getMinePublish(true);
                } else {
                    listView.stopLoadMore();
                }

            }
        });
        listView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    TribeHomeBean homeBean = (TribeHomeBean) adapter.getItem(position - 1);
                    if (homeBean != null){
                        Bundle bundle = new Bundle();
                        bundle.putInt(TribeEvent.tribeDetailId, homeBean.get_id());
                        TribeRouter.getInstance(mContext).showActivity(TribeUI.tribeDetail, bundle);
                    }
                }
            }
        });
        onCreateViewCount++;
        if (onCreateViewCount == 1 || detailBean == null){
            progressDialog.show(mContext);
            getMinePublish(false);
        }else {
            listView.setPullLoadEnable(detailBean.is_more());
            adapter.setData(detailBean.getHomeBeans(), false);
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    private void getMinePublish(final boolean loadMore){
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        if (loadMore){
            page++;
        }else {
            page = 1;
        }
        map.put("page", String.valueOf(page));
        map.put("class", "my");
        CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.getMine, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading = false;
                progressDialog.dismiss();
                TribeMainBean mainBean = TribeMainBean.fromJson(response);
                if (mainBean != null){
                    if (detailBean == null){
                        detailBean = mainBean;
                    }else {
                        ArrayList<TribeHomeBean> tempBeans = mainBean.getHomeBeans();
                        ArrayList<TribeHomeBean> oldBeans = detailBean.getHomeBeans();
                        if (tempBeans != null && oldBeans != null){
                            oldBeans.addAll(oldBeans.size(), tempBeans);
                            detailBean.setHomeBeans(oldBeans);
                        }
                    }
                    if (loadMore){
                        listView.stopLoadMore();
                    }
                    if (!mainBean.is_more()){
                        listView.setPullLoadEnable(false);
                    }
                    if (listener != null){
                        listener.onPublishLoad(mainBean.get_tome());
                    }
                    adapter.setData(mainBean.getHomeBeans(), false);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading = false;
                progressDialog.dismiss();
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
        loading = true;
    }

    public interface OnPublishLoadListener{
        void onPublishLoad(int count);
    }

    public void setOnPublishLoadListener(OnPublishLoadListener l){
        this.listener = l;
    }

}

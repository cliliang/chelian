package com.ceyu.carsteward.tribe.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.tribe.adapter.TribeMyMessageAdapter;
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
public class TribeMyMessageFragment extends BaseFragment {
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;
    private TribeMyMessageAdapter adapter;
    private Context mContext;
    private User activeUser;
    private int onCreateViewCount = 0;
    private ArrayList<TribeHomeBean> messageBean;
    private OnMessageLoadListener listener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        progressDialog = ProgressDialog.getInstance();
        requestQueue = Volley.newRequestQueue(mContext);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tribe_my_message_fragment_layout, container, false);
        ListView listView = (ListView) view.findViewById(R.id.tribe_mine_message_list);
        adapter = new TribeMyMessageAdapter(mContext);
        listView.setAdapter(adapter);
        onCreateViewCount++;
        if (onCreateViewCount == 1 || messageBean == null){
            progressDialog.show(mContext);
        }else {
            adapter.setData(messageBean);
            adapter.notifyDataSetChanged();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TribeHomeBean bean = (TribeHomeBean) adapter.getItem(position);
                if (bean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TribeEvent.tribeDetailId, bean.get_id());
                    TribeRouter.getInstance(mContext).showActivity(TribeUI.tribeDetail, bundle);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        getData();
        super.onResume();
    }

    private void getData(){
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("class", "tome");
        CheJSONObjectRequest request = new CheJSONObjectRequest(TribeURLs.getMine, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (getActivity() != null && !getActivity().isFinishing()){
                    progressDialog.dismiss();
                }
                TribeMainBean bean = TribeMainBean.fromJson(response);
                if (bean != null){
                    messageBean = bean.getHomeBeans();
                    adapter.setData(messageBean);
                    adapter.notifyDataSetChanged();
                    if (listener != null){
                        listener.onMessageLoad(bean.get_tome());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (getActivity() != null && !getActivity().isFinishing()){
                    progressDialog.dismiss();
                }
                HandleVolleyError.showErrorMessage(mContext, error);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    public interface OnMessageLoadListener{
        void onMessageLoad(int count);
    }

    public void setOnMessageLoadListener(OnMessageLoadListener l){
        this.listener = l;
    }
}

package com.ceyu.carsteward.self.ui;

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
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.maintain.adapter.MaintainListAdapter;
import com.ceyu.carsteward.maintain.bean.MileageBean;
import com.ceyu.carsteward.maintain.main.MaintainURLs;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chen on 15/9/10.
 */
public class SelfKiloMaintainFragment extends BaseFragment {

    private ArrayList<CarInfoBean> carInfoBeans = null;
    private HashMap<Integer, JSONArray> cacheData = new HashMap<>();
    private RequestQueue requestQueue;
    private AppContext appContext;
    private MaintainListAdapter adapter;
    private ListView listView;
    private Context mContext;
    private OnKiloItemClickedListener listener;
    private ProgressDialog progressDialog;
    private ArrayList<MileageBean> tempData;
    private CarInfoBean selectedCar;

    private String kiloString;
    private int carId, modelId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        appContext = (AppContext) mContext.getApplicationContext();
        progressDialog = ProgressDialog.getInstance();
        requestQueue = Volley.newRequestQueue(mContext);
        Bundle bundle = getArguments();
        if (bundle != null){
            carInfoBeans = bundle.getParcelableArrayList(CarEvent.carInfoList);
            kiloString = bundle.getString(CarEvent.carMileage);
            selectedCar = bundle.getParcelable(CarEvent.selectedCar);
        }

        CarInfoBean carInfoBean = appContext.getCarInfo();
        if (carInfoBean != null){
            carId = carInfoBean.get_id();
        }
        if (carInfoBeans != null && carInfoBeans.size() > 0) {
            for (int i = 0; i < carInfoBeans.size(); i++){
                int id = carInfoBeans.get(i).get_id();
                if (id == carId){
                    modelId = carInfoBeans.get(i).get_modelId();
                    getMileageList(modelId, true);
                    break;
                }
            }
        }
    }

    public void changeCar(CarInfoBean bean){
        selectedCar = bean;
        kiloString = bean.get_mileage();
        getMileageList(bean.get_modelId(), false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.self_kilo_maintain_fragment_layout, container, false);
        adapter = new MaintainListAdapter(mContext);
        listView = (ListView) view.findViewById(R.id.self_recommend_kilo_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MileageBean bean = (MileageBean) adapter.getItem(position);
                if (bean != null && listener != null) {
                    listener.onKiloItemClick(String.valueOf(bean.get_num()));
                }
            }
        });

        if (tempData != null){
            int selection = getItemIndex(tempData);
            adapter.setSelection(selection);
            adapter.setData(tempData);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setSelection(selection);
        }
        return view;
    }

    private void getMileageList(final int id, final boolean scroll){
        JSONArray array = cacheData.get(id);
        if (array != null){
            updateUI(array, scroll);
        }else {
            if (requestQueue != null){
                requestQueue.cancelAll(this);
            }
            progressDialog.show(mContext);
            HashMap<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            map.put("class", String.valueOf(3));
            map.put("city", String.valueOf(appContext.getActiveUser().getCityId()));
            CheJSONArrayRequest request = new CheJSONArrayRequest(SelfURLs.getMileageList, map, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray array) {
                    progressDialog.dismiss();
                    if (array != null){
                        cacheData.put(id, array);
                        updateUI(array, scroll);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();
                    String message = volleyError.getMessage();
                    if (!StringUtils.isEmpty(message) && message.contains("B0017")){
                        UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_dont_support));
                    }else {
                        HandleVolleyError.showErrorMessage(mContext, volleyError);
                    }
                    adapter.clearData();
                    adapter.notifyDataSetChanged();
                }
            });
            requestQueue.add(request);
            requestQueue.start();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void updateUI(JSONArray array, boolean scroll){
        ArrayList<MileageBean> beans = MileageBean.fromJsonArray(array);
        int selection = 0;
        if (scroll){
            selection = getItemIndex(beans);
        }
        tempData = beans;
        adapter.setSelection(selection);
        adapter.setData(beans);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setSelection(selection);

    }

    private int getItemIndex(ArrayList<MileageBean> mileageBeans){
        List<Integer> kiloList = new ArrayList<>();
        if (mileageBeans != null && mileageBeans.size() > 0){
            for (int i = 0; i < mileageBeans.size(); i++){
                kiloList.add(mileageBeans.get(i).get_num());
            }
            try {
                int mileage = Integer.parseInt(kiloString);
                if (kiloList.size() > 1) {
                    for (int j = 0; j < kiloList.size() - 1; j++) {
                        int indexMileage = kiloList.get(j);
                        int nextMileage = kiloList.get(j + 1);
                        if (mileage >= indexMileage && mileage < nextMileage){
                            return j;
                        }
                    }
                    if (kiloList.get(kiloList.size() - 1) <= mileage){
                        return kiloList.size() - 1;
                    }
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public interface OnKiloItemClickedListener{
        void onKiloItemClick(String kilo);
    }

    public void setOnKiloItemClickListener(OnKiloItemClickedListener l){
        this.listener = l;
    }
}

package com.ceyu.carsteward.self.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.maintain.bean.MaintainRule;
import com.ceyu.carsteward.self.adapter.SelfManualListAdapter;
import com.ceyu.carsteward.self.bean.SelectManualBean;
import com.ceyu.carsteward.self.bean.SelfCommodityBean;
import com.ceyu.carsteward.self.bean.SelfSelectBean;
import com.ceyu.carsteward.self.bean.SelfSelectMoney;
import com.ceyu.carsteward.self.router.SelfRouter;
import com.ceyu.carsteward.self.router.SelfUI;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.ceyu.carsteward.R.id.self_detail_money;

/**
 * Created by chen on 15/9/10.
 */
public class SelfManualMaintainFragment extends BaseFragment {
    private Context mContext;
    private SelfManualListAdapter adapter;
    private TextView moneyView, buttonView;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private CarInfoBean selectedCar;
    private String kiloString = "";
    private AppConfig appConfig;
    private LinearLayout freeLayout, debitLayout;
    private TextView noticeView, freeContentView, freeCountView;
    private TextView useTimeView, orderInfoView, ruleNoticeView, debitNoteView;
    private TextView manualMoneyView;
    private int[] ids;
    private String idStrings;
    private int cid;
//    private boolean firstKiloLoadData = true;
    private boolean choiceItem = false;
    private boolean kiloFragment = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        appConfig = AppConfig.getInstance(mContext);
        requestQueue = Volley.newRequestQueue(mContext);
        progressDialog = ProgressDialog.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null){
            selectedCar = bundle.getParcelable(CarEvent.selectedCar);
            kiloFragment = bundle.getBoolean(CarEvent.kiloFragment);
            if (selectedCar != null){
                kiloString = selectedCar.get_mileage();
//                cid = selectedCar.get_modelId();
                cid = appConfig.getModelId();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.self_manual_maintain_fragment_layout, container, false);
        moneyView = (TextView) view.findViewById(R.id.self_combo_total_money);
        buttonView = (TextView) view.findViewById(R.id.self_combo_total_button);
        ListView listView = (ListView) view.findViewById(R.id.self_manual_fragment_list);
        View headView = inflater.inflate(R.layout.self_manual_maintain_list_top_view, null);
        listView.addHeaderView(headView);
        View footView = inflater.inflate(R.layout.self_list_bottom_view_layout, null);
        listView.addFooterView(footView);
        noticeView = (TextView) footView.findViewById(R.id.self_combo_notice_text);
        freeLayout = (LinearLayout) footView.findViewById(R.id.self_combo_free_layout);
        freeContentView = (TextView) footView.findViewById(R.id.self_combo_free_content);
        freeCountView = (TextView) footView.findViewById(R.id.self_combo_free_count);
        useTimeView = (TextView) footView.findViewById(R.id.self_combo_use_time);
        orderInfoView = (TextView) footView.findViewById(R.id.self_combo_make_order_info);
        ruleNoticeView = (TextView) footView.findViewById(R.id.self_combo_rule_notice);
        debitLayout = (LinearLayout) footView.findViewById(R.id.self_combo_debit_layout);
        debitNoteView = (TextView) footView.findViewById(R.id.self_combo_debit_note);
        manualMoneyView = (TextView) footView.findViewById(R.id.self_manual_item_money_view);
        adapter = new SelfManualListAdapter(mContext);
        listView.setAdapter(adapter);
        adapter.setOnManaulSelectListener(new SelfManualListAdapter.OnManualSelectListener() {
            @Override
            public void onManualSelectListener(int position, int selectId) {
                ids[position] = selectId;
                progressDialog.show(mContext, false);
                getData();
            }
        });
        adapter.setOnManaulCheckedListener(new SelfManualListAdapter.OnManualCheckedListener() {
            @Override
            public void onManualCheck(int position, boolean isCheck) {
                int oldId = ids[position];
                if (isCheck) {
                    ids[position] = Math.abs(oldId);
                } else {
                    ids[position] = -Math.abs(oldId);
                }
                getData();
            }
        });
        adapter.setOnManualSelectDetailListener(new SelfManualListAdapter.OnManualSelectDetailListener() {
            @Override
            public void onManualSelectDetail(int position, SelfCommodityBean bean) {
                if (bean != null) {
                    showDetailDialog(position, bean);
                }
            }
        });
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(idStrings) || !choiceItem) {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_choice_maintain_info));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(CarEvent.carModelId, String.valueOf(cid));
                bundle.putString(CarEvent.carMileage, kiloString);
                bundle.putString(CarEvent.selectedIds, idStrings);
                bundle.putString(CarEvent.carId, String.valueOf(selectedCar.get_id()));
                SelfRouter.getInstance(mContext).showActivity(SelfUI.selfMap, bundle);
            }
        });
        getData();
        return view;
    }

    private void getData(){
        requestQueue.cancelAll(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("cid", String.valueOf(cid));
        if (kiloFragment && StringUtils.isEmpty(kiloString)){
            map.put("mileage", "10000");
        }else {
            map.put("mileage", kiloString);
        }
        map.put("select", getSelectIds());
        CheJSONObjectRequest request = new CheJSONObjectRequest(SelfURLs.getSelfMaintain, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                SelectManualBean bean = SelectManualBean.fromJson(response);
                if (bean != null){
                    List<SelfSelectBean> selfSelectBeans = bean.getSelectBean();
                    adapter.setData(selfSelectBeans);
                    adapter.notifyDataSetChanged();
                    if (selfSelectBeans != null){
                        ids = new int[selfSelectBeans.size()];
                        for (int i = 0; i < selfSelectBeans.size(); i++){
                            ids[i] = selfSelectBeans.get(i).get_select();
                        }
                        getSelectIds();
                    }
                    setViewData(bean);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                String message = error.getMessage();
                if (message.contains("B0017")){
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.self_dont_support));
                }else {
                    HandleVolleyError.showErrorMessage(mContext, error);
                }
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(SelectManualBean bean){
        SelfSelectMoney selectMoney = bean.getMoneyBean();
        if (selectMoney != null){
            moneyView.setText(String.valueOf(selectMoney.get_material()));
            buttonView.setEnabled(true);
        }

        noticeView.setText(bean.getNotice());
        String freeContentString = bean.getFree();
        if (StringUtils.isEmpty(freeContentString)){
            freeLayout.setVisibility(View.GONE);
        }else {
            freeLayout.setVisibility(View.VISIBLE);
            freeContentView.setText(freeContentString);
            freeCountView.setText(String.format(Locale.US, getResources().getString(R.string.maintain_free_count), bean.getFreeCount()));
        }

        MaintainRule rule = bean.getRule();
        if (rule != null) {
            useTimeView.setText(rule.get_start() + " - " + rule.get_end());
            orderInfoView.setText(rule.get_info1());
            ruleNoticeView.setText(rule.get_info2());
            String rule3 = rule.get_info3();
            if (StringUtils.isEmpty(rule3)){
                debitLayout.setVisibility(View.GONE);
            }else {
                debitNoteView.setText(rule3);
            }
        }

        SelfSelectMoney money = bean.getMoneyBean();
        if (money != null){
            manualMoneyView.setText(String.valueOf(money.get_human()));
        }
    }

    public void getData(String kilo){
        if (progressDialog != null){
            this.kiloString = kilo;
            getData();
        }
    }

    public void changeCar(CarInfoBean bean){
        selectedCar = bean;
        cid = selectedCar.get_modelId();
        getData(bean.get_mileage());
    }

    private String getSelectIds(){
        if (ids != null){
            choiceItem = false;
            for (int choiced : ids){
                if (choiced > 0){
                    choiceItem = true;
                    break;
                }
            }
//            if (!choiceItem && kiloFragment){
//                firstKiloLoadData = false;
//                return idStrings = "";
//            }
            String result = Arrays.toString(ids);
            idStrings = result.replace("[", "").replace("]", "").replace(" ", "").trim();
            return idStrings;
        }
        return "";
    }

    private void showDetailDialog(final int position, final SelfCommodityBean bean){
        if (bean == null){
            return;
        }
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.self_detail_good_dialog_layout, null);
        dialog.setContentView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.self_detail_pic);
        TextView nameView = (TextView) view.findViewById(R.id.self_detail_name);
        TextView shortView = (TextView) view.findViewById(R.id.self_detail_short);
        TextView contentView = (TextView) view.findViewById(R.id.self_detail_content);
        TextView cancelButton = (TextView) view.findViewById(R.id.self_detail_cancel);
        TextView choiceButton = (TextView) view.findViewById(R.id.self_detail_choice);
        TextView moneyView = (TextView) view.findViewById(self_detail_money);

        CheImageLoader imageLoader = new CheImageLoader(requestQueue, mContext);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.default_img, R.mipmap.default_img);
        imageLoader.get(bean.get_pic(), listener);
        nameView.setText(bean.get_subject());
        shortView.setText(bean.get_info());
        moneyView.setText(String.valueOf(bean.get_material()));
        contentView.setText(bean.get_content());
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        choiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ids[position] = bean.get_id();
                progressDialog.show(mContext, false);
                getData();
            }
        });

        if (!dialog.isShowing() && !getActivity().isFinishing()){
            dialog.show();
        }
        dialog.getWindow().setLayout(Utils.getScreenWidth(getActivity()) - 100, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}

package com.ceyu.carsteward.maintain.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.views.CommonDialog;
import com.ceyu.carsteward.maintain.adapter.MaintainComboAdapter;
import com.ceyu.carsteward.maintain.bean.MaintainDetailContent;
import com.ceyu.carsteward.maintain.bean.MaintainParts;
import com.ceyu.carsteward.maintain.bean.MaintainPayBean;
import com.ceyu.carsteward.maintain.bean.MaintainRule;
import com.ceyu.carsteward.maintain.bean.MaintainShopInfo;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.maintain.views.MaintainChoiceView;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import static com.ceyu.carsteward.R.id.maintain_optional_item_title;

/**
 * Created by chen on 15/6/25.
 */
public class MaintainComboActivity extends BaseActivity {

    private Context mContext;
    private MaintainComboAdapter adapter;
    private int modelId, kilo, shopId, carId;
    private ImageView shopImageView, discountIcon, hintIcon;
    private LinearLayout mainLayout, freeLayout, debitLayout, optionalLayout, checkShopLayout;
    private ExpandableListView listView;
    private TextView shopNameView, shopAddressView, shopDistanceView, carKiloView, freeCountView, optionalTitle;
    private TextView maintainHumanPriceView, useTimeView, orderInfoView, ruleNoticeView, debitNoteView, maintainNoticeView, freeContentView;
    private TextView discountInfoView, payMoneyView, factoryView, bangView;
    private MaintainShopInfo shopInfo;
    private boolean onlinePay = false;
    private Bundle bundle;
    private String select, optional;
    private int[] ids;
    private int[] optinalIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.maintain_combo);
        setContentView(R.layout.maintain_combo_activity_layout);
        mContext = MaintainComboActivity.this;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View topView = layoutInflater.inflate(R.layout.maintain_combo_list_top_view, null);
        View footView = layoutInflater.inflate(R.layout.maintain_combo_list_footer_view, null);
        initView(topView, footView);
        mainLayout = (LinearLayout) findViewById(R.id.maintain_main_layout);
        mainLayout.setVisibility(View.INVISIBLE);
        discountIcon = (ImageView) findViewById(R.id.maintain_combo_discount_icon);
        listView = (ExpandableListView) findViewById(R.id.maintain_combo_list);
        listView.addHeaderView(topView);
        listView.addFooterView(footView);
        adapter = new MaintainComboAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                MaintainDetailContent content = (MaintainDetailContent) adapter.getChild(groupPosition, childPosition);
                if (content != null) {
                    ids[groupPosition] = content.get_id();
                    getComboData();
                }
                return false;
            }
        });
        bundle = getIntent().getExtras();
        if (bundle != null) {
            modelId = bundle.getInt(MaintainEvent.CarModelId);
            kilo = bundle.getInt(MaintainEvent.CarKilo);
            shopId = bundle.getInt(MaintainEvent.shopId);
            carId = bundle.getInt(MaintainEvent.CarId);
            getComboData();
        }
    }

    private void initView(View headView, View footView) {
        shopImageView = (ImageView) headView.findViewById(R.id.maintain_combo_shop_image);
        shopNameView = (TextView) headView.findViewById(R.id.maintain_combo_shop_name);
        shopAddressView = (TextView) headView.findViewById(R.id.maintain_combo_shop_address);
        shopDistanceView = (TextView) headView.findViewById(R.id.maintain_combo_shop_distance);
        carKiloView = (TextView) headView.findViewById(R.id.maintain_combo_part_content);
        checkShopLayout = (LinearLayout) headView.findViewById(R.id.maintain_check_shop_layout);
        checkShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(MaintainEvent.shopId, shopId);
                MaintainRouter.getInstance(mContext).showActivity(MaintainUI.getShopInfo, bundle);
            }
        });
        hintIcon = (ImageView) headView.findViewById(R.id.maintain_combo_part_question);
        hintIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(mContext, getResources().getString(R.string.maintain_combo_dialog_title), getResources().getString(R.string.maintain_combo_dialog_content));
                dialog.show();
            }
        });

        maintainHumanPriceView = (TextView) footView.findViewById(R.id.maintain_combo_human_price);
        useTimeView = (TextView) footView.findViewById(R.id.maintain_combo_use_time);
        orderInfoView = (TextView) footView.findViewById(R.id.maintain_combo_make_order_info);
        ruleNoticeView = (TextView) footView.findViewById(R.id.maintain_combo_rule_notice);
        debitNoteView = (TextView) footView.findViewById(R.id.maintain_combo_debit_note);
        maintainNoticeView = (TextView) footView.findViewById(R.id.maintain_combo_notice_text);
        debitLayout = (LinearLayout) footView.findViewById(R.id.maintain_combo_debit_layout);
        freeContentView = (TextView) footView.findViewById(R.id.maintain_combo_free_content);

        discountInfoView = (TextView) findViewById(R.id.maintain_combo_discount_info);
        optionalTitle = (TextView) footView.findViewById(maintain_optional_item_title);
        freeLayout = (LinearLayout) footView.findViewById(R.id.maintain_combo_free_layout);
        freeCountView = (TextView) footView.findViewById(R.id.maintain_combo_free_count);
        payMoneyView = (TextView) findViewById(R.id.maintain_combo_pay_money);
        factoryView = (TextView) findViewById(R.id.maintain_combo_factory_price);
        factoryView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        bangView = (TextView) findViewById(R.id.maintain_combo_bang_price);
        findViewById(R.id.maintain_combo_take_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putString(MaintainEvent.selectIds, select);
                bundle.putString(MaintainEvent.optionalIds, optional);
                bundle.putBoolean(MaintainEvent.onlinePay, onlinePay);
                MaintainRouter.getInstance(mContext).showActivity(MaintainUI.maintainReserve, bundle);
            }
        });
        optionalLayout = (LinearLayout) footView.findViewById(R.id.maintain_optional_item_layout);
    }

    private void getComboData() {
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        User user = appContext.getActiveUser();
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        map.put("cid", String.valueOf(modelId));
        map.put("sid", String.valueOf(shopId));
        map.put("mileage", String.valueOf(kilo));
        map.put("select", getIdsString(ids, 1));
        map.put("optional", getIdsString(optinalIds, 2));
        map.put("mycid", String.valueOf(carId));
        CheJSONObjectRequest request = new CheJSONObjectRequest(MaintainURLs.getMaintainInfo_v2, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(MaintainComboActivity.this);
                MaintainShopInfo info = MaintainShopInfo.fromJsonObject(object);
                if (info != null) {
                    mainLayout.setVisibility(View.VISIBLE);
                    shopInfo = info;
                    setViewData(info);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(MaintainComboActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setViewData(MaintainShopInfo shopInfo) {
        MaintainParts parts = shopInfo.get_parts();
        if (parts != null) {
            ArrayList<MaintainSubContent> subContents = parts.getSubContents();
            ids = new int[subContents.size()];
            for (int i = 0; i < subContents.size(); i ++){
                MaintainSubContent subContent = subContents.get(i);
                int select = subContent.get_sel();
                ids[i] = select;
            }
            adapter.setData(subContents);
            adapter.notifyDataSetChanged();
            for (int i = 0; i < adapter.getGroupCount(); i++){
                listView.collapseGroup(i);
            }
        }

        optionalLayout.removeAllViews();
        MaintainParts optional = shopInfo.get_optional();
        if (optional != null){
            ArrayList<MaintainSubContent> subContents = optional.getSubContents();
            if (subContents == null || subContents.size() == 0){
                optionalTitle.setText(getResources().getString(R.string.maintain_text_notify));
                optionalLayout.setVisibility(View.GONE);
            }else {
                optinalIds = new int[subContents.size()];
                for (int i = 0;  i < subContents.size(); i++){
                    final int itemId = i;
                    MaintainSubContent subContent = subContents.get(i);
                    optinalIds[i] = subContent.get_sel();
                    final MaintainChoiceView choiceView = new MaintainChoiceView(mContext);
                    choiceView.setData(subContent);
                    choiceView.setOnSubOptionalItemClicked(new MaintainChoiceView.OnSubOptinalItemClick() {
                        @Override
                        public void onSubOptinalItemClicked(MaintainDetailContent detailContent) {
                            int selectId = detailContent.get_id();
                            if (choiceView.getCheckboxState()){
                                optinalIds[itemId] = Math.abs(selectId);
                            }else {
                                optinalIds[itemId] = -Math.abs(selectId);
                            }
                            getComboData();
                        }
                    });
                    choiceView.setOnSubOptinalCheckChanged(new MaintainChoiceView.OnSubOptionalCheckChanged() {
                        @Override
                        public void onSubOptinalCheckChanged(boolean isChecked) {
                            int nowId = optinalIds[itemId];
                            if (isChecked){
                                optinalIds[itemId] = Math.abs(nowId);
                            }else {
                                optinalIds[itemId] = -Math.abs(nowId);
                            }
                            getComboData();
                        }
                    });
                    optionalLayout.addView(choiceView);
                }
            }
        }else {
            optionalTitle.setText(getResources().getString(R.string.maintain_text_notify));
            optionalLayout.setVisibility(View.GONE);
        }

        MaintainPayBean payBean = shopInfo.get_money();
        if (payBean != null){
            maintainHumanPriceView.setText(payBean.get_human_txt());
            payMoneyView.setText(payBean.get_sum());
            factoryView.setText(payBean.get_factory_txt());
            bangView.setText(payBean.getSum_txt());
        }

        shopDistanceView.setText(shopInfo.get_distance());
        CheImageLoader imageLoader = new CheImageLoader(requestQueue, mContext);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(shopImageView, R.mipmap.default_img, R.mipmap.default_img);
        imageLoader.get(shopInfo.get_pic(), imageListener);
        shopNameView.setText(shopInfo.get_name());
        shopAddressView.setText(shopInfo.get_address().replace("*", "\n"));
        carKiloView.setText(String.format(Locale.US, getResources().getString(R.string.maintain_content_kilo), kilo));

        MaintainRule rule = shopInfo.get_rule();
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
        maintainNoticeView.setText(shopInfo.get_notice());
        String freeContentString = shopInfo.get_free();
        if (StringUtils.isEmpty(freeContentString)){
            freeLayout.setVisibility(View.GONE);
        }else {
            freeLayout.setVisibility(View.VISIBLE);
            freeContentView.setText(freeContentString);
            freeCountView.setText(String.format(Locale.US, getResources().getString(R.string.maintain_free_count), shopInfo.get_freeCount()));
        }
        onlinePay = shopInfo.is_onlinePayState();
        String discountInfo = shopInfo.get_onlinePay();
        if (!StringUtils.isEmpty(discountInfo)){
            discountInfoView.setText(discountInfo);
            discountIcon.setVisibility(View.VISIBLE);
        }
        checkShopLayout.setVisibility(shopInfo.isHasDetail() ? View.VISIBLE : View.GONE);
    }

    private String getIdsString(int[] ids, int type){
        String idString = "";
        if (ids != null){
            String s = Arrays.toString(ids);
            idString = s.replace("[", "").replace("]", "").replace(" ", "");
        }
        if (type == 1){
            select = idString;
        }else if (type == 2){
            optional = idString;
        }

        return idString;
    }

    private void showHintDialog(String content){
        final Dialog dialog = new Dialog(mContext, R.style.showPhoto);
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_alert_dialog_bg, null);
        dialog.setContentView(view);
        TextView contentView = (TextView) view.findViewById(R.id.common_alert_dialog_content);
        contentView.setText(content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext != null && !isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

}

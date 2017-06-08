package com.ceyu.carsteward.tuan.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.tuan.bean.TuanContentBean;
import com.ceyu.carsteward.tuan.bean.TuanModBean;
import com.ceyu.carsteward.tuan.router.TuanRouter;
import com.ceyu.carsteward.tuan.router.TuanUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/7/20.
 */
public class TuanContentActivity extends BaseActivity {

    private RoundCornerImageView imageView;
    private TextView titleView, endTimeView ,nameView, addressView, serviceEndTimeView, contentDetailView, knowView, peopleView;
    private TextView priceView, buttonView, describeView;
    private LinearLayout mainLayout;
    private Context mContext;
    private User activeUser;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tuan_facade_title);
        setContentView(R.layout.tuan_content_activity_layout);
        mContext = TuanContentActivity.this;
        activeUser = ((AppContext)mContext.getApplicationContext()).getActiveUser();
        bundle = getIntent().getExtras();
        if (bundle != null){
            String gid = bundle.getString(TuanEvent.shopId);
            getViewData(gid);
            initView();
        }else {
            finish();
        }
        mainLayout.setVisibility(View.INVISIBLE);
    }

    private void initView(){
        mainLayout = (LinearLayout) findViewById(R.id.tuan_content_main_layout);
        imageView = (RoundCornerImageView) findViewById(R.id.tuan_content_shop_image);
        titleView = (TextView) findViewById(R.id.tuan_content_shop_title);
        endTimeView = (TextView) findViewById(R.id.tuan_content_end_time);
        nameView = (TextView) findViewById(R.id.tuan_content_shop_name);
        addressView = (TextView) findViewById(R.id.tuan_content_shop_address);
        serviceEndTimeView = (TextView) findViewById(R.id.tuan_content_service_end_time);
        contentDetailView = (TextView) findViewById(R.id.tuan_content_detail);
        knowView = (TextView) findViewById(R.id.tuan_content_use_should_know);
        peopleView = (TextView) findViewById(R.id.tuan_content_people_number);
        priceView = (TextView) findViewById(R.id.tuan_content_pay_money);
        describeView = (TextView) findViewById(R.id.tuan_content_describe_sub);
        buttonView = (TextView) findViewById(R.id.tuan_content_pay_button);
    }

    private void setViewData(final TuanContentBean contentBean){
        if (contentBean != null){
            mainLayout.setVisibility(View.VISIBLE);
            TuanModBean modBean = contentBean.get_store();
            if (modBean != null){
                CheImageLoader loader = new CheImageLoader(requestQueue, mContext);
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.default_img, R.mipmap.default_img);
                loader.get(modBean.get_pic(), imageListener);
                nameView.setText(modBean.get_name());
                addressView.setText(modBean.get_address().replace("*", ""));
            }
            describeView.setText(contentBean.get_txt());
            titleView.setText(contentBean.get_title());
            endTimeView.setText(contentBean.get_end_order());
            contentDetailView.setText(contentBean.get_item());
            serviceEndTimeView.setText(String.format(Locale.US, getResources().getString(R.string.tuan_content_end_time), contentBean.get_end_service()));
            knowView.setText(contentBean.get_notice());
            peopleView.setText(String.format(Locale.US, getResources().getString(R.string.tuan_facade_num_of_people), contentBean.get_num()));
            priceView.setText(contentBean.get_money());
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bundle.putParcelable(TuanEvent.shopContent, contentBean);
                    TuanRouter.getInstance(mContext).showActivity(TuanUI.tuanReserve, bundle);
                }
            });
        }
    }

    private void getViewData(String shopId){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        map.put("gid", shopId);
        CheJSONObjectRequest request = new CheJSONObjectRequest(TuanURLs.getTuanDetail, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog(TuanContentActivity.this);
                TuanContentBean contentBean = TuanContentBean.fromJson(object);
                if (contentBean != null){
                    setViewData(contentBean);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(TuanContentActivity.this);
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();

    }

}

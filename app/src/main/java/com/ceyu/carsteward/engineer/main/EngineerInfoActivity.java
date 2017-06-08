package com.ceyu.carsteward.engineer.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.config.AppConfig;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.CircleHeadImageView;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.engineer.adapter.EngineerCommentAdapter;
import com.ceyu.carsteward.engineer.bean.CommentContent;
import com.ceyu.carsteward.engineer.bean.EngineerBean;
import com.ceyu.carsteward.engineer.bean.EngineerComment;
import com.ceyu.carsteward.engineer.router.EngineerRouter;
import com.ceyu.carsteward.engineer.router.EngineerUI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/5.
 */
public class EngineerInfoActivity extends BaseActivity {

    private Context mContext;
    private ProgressDialog progressDialog;
    private CircleHeadImageView headImageView;
    private ImageLoader imageLoader;
    private TextView moneyView, nameView, levelView, localView, yearView, brandView, describView, goodView, payView, orderCountView, functionView;
    private ImageView phote1View, phote2View;
    private LinearLayout lvPhotoParent, commentLayout;
    private TextView mButton;
    private RelativeLayout contentLayout;
    private ListView commentListView;
    private String engineerToken;
    private AlertDialog alertDialog;
    private EngineerCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engineer_info_activity_layout);
        mContext = EngineerInfoActivity.this;
        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            engineerToken = bundle.getString(EngineerEvent.ENGINEER_TOKEN);
            int payResult = bundle.getInt(EngineerEvent.payResult, -1);
            if (payResult == 0){
                showPaySuccessDialog();
            }
            if (StringUtils.isEmpty(engineerToken)) {
                finish();
            }
        } else {
            finish();
        }
        progressDialog = ProgressDialog.getInstance();
        imageLoader = new CheImageLoader(requestQueue, mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentLayout.setVisibility(View.INVISIBLE);
        getEngineerInfo(engineerToken);
        dismissAlertDialog();
    }

    private void initView(){
        contentLayout = (RelativeLayout) findViewById(R.id.engineer_info_main_layout);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.engineer_info_list_head_view, null);
        headImageView = (CircleHeadImageView) headView.findViewById(R.id.engineer_info_head_id);
        commentLayout = (LinearLayout) headView.findViewById(R.id.engineer_info_comment_layout);
        moneyView = (TextView) headView.findViewById(R.id.engineer_info_money_id);
        nameView = (TextView) headView.findViewById(R.id.engineer_info_name_id);
        levelView = (TextView) headView.findViewById(R.id.engineer_info_level_id);
        localView = (TextView) headView.findViewById(R.id.engineer_info_local_id);
        yearView = (TextView) headView.findViewById(R.id.engineer_info_year_id);
        brandView = (TextView) headView.findViewById(R.id.engineer_info_brand_id);
        describView = (TextView) headView.findViewById(R.id.engineer_info_describe_id);
        functionView = (TextView) headView.findViewById(R.id.engineer_info_order_function);
        goodView = (TextView) headView.findViewById(R.id.engineer_info_good_id);
        orderCountView = (TextView) headView.findViewById(R.id.engineer_info_order_count);
        phote1View = (ImageView) headView.findViewById(R.id.engineer_info_image1);
        phote2View = (ImageView) headView.findViewById(R.id.engineer_info_image2);
        lvPhotoParent = (LinearLayout) headView.findViewById(R.id.engineer_info_imageparent);
        payView = (TextView) findViewById(R.id.engineer_info_play_id);
        mButton = (TextView) findViewById(R.id.engineer_info_button_id);
        commentListView = (ListView) findViewById(R.id.engineer_info_comment_list);
        commentListView.addHeaderView(headView);
        adapter = new EngineerCommentAdapter(mContext);
        commentListView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        int payResult = bundle.getInt(EngineerEvent.payResult, -1);
        if (payResult == 0){
            showPaySuccessDialog();
        }
        super.onNewIntent(intent);
    }

    private void showPaySuccessDialog(){
        final Dialog dialog = new Dialog(mContext, R.style.showPay);
        dialog.setContentView(R.layout.pay_success_dialog_bg);
        Button laterButton = (Button) dialog.findViewById(R.id.pay_success_later_button);
        laterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EngineerInfoActivity.this.isFinishing()) {
                    dialog.dismiss();
                }
            }
        });
        Button nowButton = (Button) dialog.findViewById(R.id.pay_success_now_button);
        nowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!EngineerInfoActivity.this.isFinishing()) {
                    dialog.dismiss();
                }
                AppConfig config = AppConfig.getInstance(mContext);
                callToEngineer(config.getCurrentOrder());
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(Utils.getScreenWidth(EngineerInfoActivity.this) - 50, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void getEngineerInfo(String engineerToken) {
        Log.i("chen", "EngineerActivity getData");
        progressDialog.show(mContext);
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("mtoken", engineerToken);
        map.put("token", appContext.getActiveUser().getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.getEngineerInfo, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                Log.i("chen", "info:" + object.toString());
                if (object.has(ResponseCode.ResponseInfo)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    EngineerBean bean = EngineerBean.fromJsonObject(object);
                    if (bean != null){
                        setViewData(bean);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void dismissDialog() {
        if (progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
    }

    private void callToEngineer(String sn){
        showWaitDialog();
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        HashMap<String, String> map = new HashMap<>();
        map.put("sn", sn);
        map.put("token", appContext.getActiveUser().getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(EngineerURLs.callToEngineer, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                if (object.has(ResponseCode.ResponseError)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, info);
                    dismissAlertDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HandleVolleyError.showErrorMessage(mContext, volleyError);
                dismissAlertDialog();
            }
        });
        requestQueue.add(request);
        requestQueue.start();

    }
    private void showWaitDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.wait_for_engineer_title);
        builder.setMessage(getResources().getString(R.string.wait_for_engineer_content));
        alertDialog = builder.show();
    }

    private void dismissAlertDialog(){
        if (alertDialog != null){
            if (alertDialog.isShowing() && !isFinishing()){
                alertDialog.dismiss();
            }
        }
    }

    public void setViewData(final EngineerBean bean){
        contentLayout.setVisibility(View.VISIBLE);
        if (!StringUtils.isEmpty(bean.get_pic())){
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(headImageView, R.mipmap.default_head, R.mipmap.default_head);
            imageLoader.get(bean.get_pic(), listener);
        }
        final String[] photos = bean.get_photo();
        if (photos.length >= 1){
            final String[] photo1 = photos[0].split(",");
            //ImageLoader.ImageListener photo1Listener = ImageLoader.getImageListener(phote1View, R.mipmap.image_certificate, R.mipmap.image_certificate);
            ImageLoader.ImageListener photo1Listener = new MyImageLoaderListener(phote1View, lvPhotoParent);
            imageLoader.get(photo1[0], photo1Listener);
            phote1View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhotoDialog(photo1[0]);
                }
            });
//            float viewHeight = Utils.dip2px(mContext, getResources().getDimension(R.dimen.engineer_certificate_height));
//            float ratio = Float.parseFloat(photo1[1]);
//            int viewWidth = (int)(viewHeight * ratio);
//            ViewGroup.LayoutParams params = phote1View.getLayoutParams();
//            params.width = viewWidth;
//            phote1View.setLayoutParams(params);

        }
        if (photos.length >= 2){
            final String[] photo2 = photos[1].split(",");
            //ImageLoader.ImageListener photo2Listener = ImageLoader.getImageListener(phote2View, R.mipmap.image_certificate, R.mipmap.image_certificate);
            ImageLoader.ImageListener photo2Listener = new MyImageLoaderListener(phote2View, lvPhotoParent);
            imageLoader.get(photo2[0], photo2Listener);
            phote2View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPhotoDialog(photo2[0]);
                }
            });

//            int viewHeight = getResources().getDimensionPixelSize(R.dimen.engineer_certificate_height);
//            float ratio = Float.parseFloat(photo2[1]);
//            int viewWidth = (int)(viewHeight * ratio);
//            ViewGroup.LayoutParams params = phote1View.getLayoutParams();
//            params.width = viewWidth;
//            phote1View.setLayoutParams(params);
        }
        StringBuilder title = new StringBuilder();
        String item = bean.get_name();
        if (!StringUtils.isEmpty(item)){
            nameView.setText(item);
            title.append(item);
        }

        item = bean.get_level();
        if (!StringUtils.isEmpty(item)){
            levelView.setText(item);
            title.append(item);
        }
        if (!StringUtils.isEmpty(title.toString())){
            setTitle(title.toString());
        }
        item = bean.get_city();
        if (!StringUtils.isEmpty(item)){
            localView.setText(item);
        }
        item = bean.get_year();
        if (!StringUtils.isEmpty(item)){
            yearView.setText(String.format(Locale.US, getResources().getString(R.string.engineer_service_year_string), item));
        }
        item = bean.get_model();
        if (!StringUtils.isEmpty(item)){
            brandView.setText(item);
        }
        item = bean.get_exp();
        if (!StringUtils.isEmpty(item)){
            describView.setText(item);
        }
        item = bean.get_function();
        if (!StringUtils.isEmpty(item)){
            functionView.setText(item);
        }
        int orderCount = bean.get_num();
        orderCountView.setText(String.format(Locale.US, getResources().getString(R.string.engineer_had_service_orders), orderCount));

        float money = bean.get_money();
        moneyView.setText(String.format(Locale.US, "%.0f", money));
        final int orderState = bean.get_state();
//        0 未付费        30元/15分钟          立即联系
//        1 已付费        剩余15分钟            立即联系
//        2 未接通        剩余15分钟            立即联系
//        3 不足3分钟    已经通话：x分x秒   再次联系
        if (orderState == 0){
            payView.setText(String.format(Locale.US, getResources().getString(R.string.engineer_money_per), money));
        }else if (orderState == 1 || orderState == 2){
            payView.setText(getResources().getString(R.string.engineer_service_15));
            mButton.setText(getResources().getString(R.string.contact_engineer_now));
        }else if (orderState == 3){
            int haveLong = bean.get_orderLong();
            int minute = haveLong / 60;
            int second = haveLong%60;
            payView.setText(String.format(Locale.US, getResources().getString(R.string.engineer_service_time), minute, second));
            mButton.setText(getResources().getString(R.string.contact_engineer_return));
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderState == 0){
                    Bundle bundle = new Bundle();
                    bundle.putString(EngineerEvent.ENGINEER_TOKEN, engineerToken);
                    EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerPay, bundle);
                }else {
                    callToEngineer(bean.get_orderSn());
                }
            }
        });

        EngineerComment engineerComment = bean.get_comment();
        ArrayList<CommentContent> commentList = engineerComment.getCommentContents();
        if (commentList != null && commentList.size() > 0){
            goodView.setText(engineerComment.getSumValue());
            adapter.setData(commentList);
            adapter.notifyDataSetChanged();
        }else {
            commentLayout.setVisibility(View.GONE);
        }
    }

    private void showPhotoDialog(String url){
        final Dialog dialog = new Dialog(mContext, R.style.showPhoto);
        View view = LayoutInflater.from(mContext).inflate(R.layout.engineer_show_photo_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.engineer_show_photo_dialog_id);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (dialog.isShowing() && !EngineerInfoActivity.this.isFinishing()){
                    dialog.dismiss();
                }
            }
        });
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.image_certificate, R.mipmap.image_certificate);
        imageLoader.get(url, imageListener);
        dialog.setContentView(view);
        dialog.show();
    }

    private class MyImageLoaderListener implements ImageLoader.ImageListener{

        ImageView iv;
        int height;
        int width;

        public MyImageLoaderListener(ImageView imageView, View parentView){
            this.iv = imageView;
            this.height = parentView.getHeight();
            this.width = parentView.getWidth();
        }

        @Override
        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
            if(imageContainer!=null && imageContainer.getBitmap()!=null) {
                this.iv.getLayoutParams();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)iv.getLayoutParams();
                Bitmap bitmap = imageContainer.getBitmap();
                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                params.width = imageWidth * this.height / imageHeight;
                params.height = this.height;
                params.weight = 0;
                this.iv.setLayoutParams(params);
                this.iv.setImageBitmap(bitmap);
            }else{
                this.iv.setImageResource(R.mipmap.image_certificate);
            }
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            this.iv.setImageResource(R.mipmap.image_certificate);
        }
    }
}

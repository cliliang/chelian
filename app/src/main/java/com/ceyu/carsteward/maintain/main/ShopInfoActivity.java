package com.ceyu.carsteward.maintain.main;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.maintain.bean.ShopInfo;
import com.ceyu.carsteward.maintain.bean.ShopPhotoDescribe;
import com.ceyu.carsteward.maintain.bean.ShopPhotos;
import com.ceyu.carsteward.maintain.router.MaintainRouter;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.maintain.views.ShopTuanView;
import com.ceyu.carsteward.tuan.bean.TuanListBean;
import com.ceyu.carsteward.tuan.main.TuanEvent;
import com.ceyu.carsteward.tuan.router.TuanUI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chen on 15/8/17.
 */
public class ShopInfoActivity extends BaseActivity {
    private int shopId;
    private Context mContext;
    private ImageView photoView;
    private TextView nameView, addressView, describeView, differentView, allView, numberView;
    private TextView tuanTitleView;
    private LinearLayout tuanLayout;
    private ShopInfo shopInfo;
    private CheImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.shop_info);
        setContentView(R.layout.maintain_shop_info_activity_layout);
        mContext = ShopInfoActivity.this;
        imageLoader = new CheImageLoader(requestQueue, mContext);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            shopId = bundle.getInt(MaintainEvent.shopId);
        }
        getData();
        initView();
    }

    private void initView(){
        numberView = (TextView) findViewById(R.id.shop_info_image_number);
        photoView = (ImageView) findViewById(R.id.shop_info_image_view);
        nameView = (TextView) findViewById(R.id.shop_info_shop_name);
        addressView = (TextView) findViewById(R.id.shop_info_shop_address);
        describeView = (TextView) findViewById(R.id.shop_info_describe_id);
        differentView = (TextView) findViewById(R.id.shop_info_different_id);
        allView = (TextView) findViewById(R.id.shop_info_view_all);
        tuanLayout = (LinearLayout) findViewById(R.id.shop_info_about_tuan);
        tuanTitleView = (TextView) findViewById(R.id.shop_info_tuan_layout_tilte);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopInfo != null){
                    ShopPhotos photos = shopInfo.get_pic();
                    if (photos != null){
                        ArrayList<ShopPhotoDescribe> describes = photos.get_list();
                        if (describes != null && describes.size() > 0){
                            Bundle bundle = new Bundle();
                            ArrayList<String> arrayList = new ArrayList<>();
                            ArrayList<String> infoList = new ArrayList<>();
                            for (ShopPhotoDescribe describe : describes){
                                if (describe != null){
                                    arrayList.add(describe.get_url());
                                    infoList.add(describe.get_info());
                                }
                            }
                            bundle.putStringArrayList(MaintainEvent.photoList, arrayList);
                            bundle.putStringArrayList(MaintainEvent.photoInfo, infoList);
                            MaintainRouter.getInstance(mContext).showActivity(MaintainUI.getShopPhoto, bundle);
                        }
                    }
                }

            }
        });
    }

    public void getData(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("sid", String.valueOf(shopId));
        map.put("token", ((AppContext)mContext.getApplicationContext()).getActiveUser().getToken());
        CheJSONObjectRequest objectRequest = new CheJSONObjectRequest(MaintainURLs.getShopInfo, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dismissDialog(ShopInfoActivity.this);
                ShopInfo info = ShopInfo.fromJson(jsonObject);
                if (info != null){
                    shopInfo = info;
                    setViewData();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog(ShopInfoActivity.this);
            }
        });
        requestQueue.add(objectRequest);
    }

    public void setViewData(){
        if (shopInfo != null){
            ArrayList<ShopPhotoDescribe> photoDescribes = shopInfo.get_pic().get_list();
            if (photoDescribes != null && photoDescribes.size() > 0){
                numberView.setText(String.format(Locale.US, getResources().getString(R.string.shop_info_number_of_photo), photoDescribes.size()));
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(photoView, R.mipmap.default_img, R.mipmap.default_img);
                imageLoader.get(photoDescribes.get(0).get_url(), listener);
            }
            nameView.setText(shopInfo.get_name());
            addressView.setText(shopInfo.get_address().replace("*", ""));
            describeView.setText(shopInfo.get_content());
            differentView.setText(shopInfo.get_characteristic());
            final int lines = describeView.getLineCount();
            if (lines > 3){
                allView.setVisibility(View.VISIBLE);
                describeView.setEllipsize(TextUtils.TruncateAt.END);
                describeView.setMaxLines(3);
                allView.setOnClickListener(new View.OnClickListener() {
                    boolean flag = true;
                    @Override
                    public void onClick(View v) {
                        if (!flag){
                            flag = true;
                            describeView.setEllipsize(TextUtils.TruncateAt.END);
                            describeView.setLines(3);
                            allView.setText(getResources().getString(R.string.shop_info_all_txt));
                        }else {
                            flag = false;
                            describeView.setEllipsize(null);
                            describeView.setLines(lines);
                            allView.setText(getResources().getString(R.string.shop_info_hidden_txt));
                        }
                    }
                });
            }else {
                allView.setVisibility(View.GONE);
            }
            List<TuanListBean> listBeans = shopInfo.get_group();
            tuanLayout.removeAllViews();
            if (listBeans != null && listBeans.size() > 0){
                for (TuanListBean bean : listBeans){
                    ShopTuanView view = new ShopTuanView(mContext);
                    view.setOnShopTuanClickListener(new ShopTuanView.OnShopTuanClickListener() {
                        @Override
                        public void onShopTuanClick(String gid) {
                            Bundle bundle = new Bundle();
                            bundle.putString(TuanEvent.shopId, gid);
                            MainRouter.getInstance(mContext).showActivity(ModuleNames.Tuan, TuanUI.tuanContent, bundle);
                        }
                    });
                    view.setData(bean);
                    tuanLayout.addView(view);
                }
            }else {
                tuanTitleView.setVisibility(View.GONE);
            }
        }
    }

}

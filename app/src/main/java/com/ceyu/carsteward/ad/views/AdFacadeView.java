package com.ceyu.carsteward.ad.views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.ad.adapter.AdAdapter;
import com.ceyu.carsteward.ad.bean.AdBean;
import com.ceyu.carsteward.ad.main.AdURLs;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONArrayRequest;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.views.IndicatorView;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by chen on 15/6/8.
 */
public class AdFacadeView extends LinearLayout{

    private ViewPager vpMain;
    private IndicatorView indicatorView;
    private Context mContext;
    private AppContext appContext;
//    private int page;

    //广告总数
    private int indicatorCount;
    private int currentDot = 0;

    public AdFacadeView(Context context) {
        super(context);
        init(context);
    }

    public AdFacadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdFacadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void  init(Context context){
        this.mContext = context;
        appContext = (AppContext) mContext.getApplicationContext();
        LayoutInflater.from(context).inflate(R.layout.ad_homepage_ad, this);
        indicatorView = (IndicatorView) findViewById(R.id.iv_ad_homepage_indicator);
        vpMain = (ViewPager) findViewById(R.id.vp_ad_homepage_viewpager);
        vpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                page = position;
//                if(position<1){
//                    indicatorView.updateScreen(adAdapter.getCount()-2, indicatorCount);
//                    return;
//                }
//                if(position == adAdapter.getCount()-1){
//                    indicatorView.updateScreen(0, indicatorCount);
//                    return;
//                }
                currentDot = position;
                indicatorView.updateScreen(position, indicatorCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if(state == ViewPager.SCROLL_STATE_IDLE){
//                    if(page<1){
//                        vpMain.setCurrentItem(adAdapter.getCount() - 2, false);;
//                        return;
//                    }
//                    if(page == adAdapter.getCount()-1){
//                        vpMain.setCurrentItem(1, false);
//                        return;
//                    }
//                }
            }
        });
        connect();  //联网抓取广告
    }

    public void updateDot(){
        if (indicatorCount > 0){
            indicatorView.updateScreen(currentDot, indicatorCount);
        }
    }

    private int count = 1;
    private void connect(){
        CheJSONArrayRequest request = new CheJSONArrayRequest(AdURLs.homepageAdvertisement,
                new Utility.ParamsBuilder(appContext).build(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                try {
                    AdBean parentBean = AdBean.parse(array);
                    if(parentBean == null || parentBean.noAd()){
                        setVisibility(View.GONE);
//                        UIHelper.ToastMessage(mContext, "GONE");
                    }
                    else initViews(parentBean.getList());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (count < 3){
                    connect();
                    count++;
                }
            }
        });
        appContext.queue().add(request);
    }

    private AdAdapter adAdapter;

    private void initViews(List<AdBean> list) {
        if(list==null||list.size()<1){
            this.setVisibility(View.GONE);
            return;
        }
        if(list.size()<2){
            indicatorView.setVisibility(GONE);
            vpMain.setOnPageChangeListener(null);
            return;
        }
        indicatorCount = list.size();
        indicatorView.updateScreen(0, indicatorCount);
        adAdapter = new AdAdapter((Activity)mContext);
        adAdapter.setData(list);
        vpMain.setAdapter(adAdapter);
        vpMain.setCurrentItem(0);
    }
}

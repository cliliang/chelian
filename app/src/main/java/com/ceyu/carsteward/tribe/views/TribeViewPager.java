package com.ceyu.carsteward.tribe.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.views.IndicatorView;
import com.ceyu.carsteward.tribe.bean.TribeHotBean;
import com.ceyu.carsteward.tribe.router.TribeRouter;
import com.ceyu.carsteward.tribe.router.TribeUI;
import com.ceyu.carsteward.tribe.ui.TribeEvent;

import java.util.ArrayList;

import static com.ceyu.carsteward.R.id.tribe_view_pager_message;

/**
 * Created by chen on 15/8/28.
 */
public class TribeViewPager extends RelativeLayout{

    private Context mContext;
    private IndicatorView indicatorView;
    private TribeViewPagerAdapter adapter;
    public TribeViewPager(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.tribe_scroll_view_layout, this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.tribe_scroll_view);
        indicatorView = (IndicatorView) findViewById(R.id.tribe_scroll_dot);
        adapter = new TribeViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorView.updateScreen(position, adapter.getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setData(ArrayList<TribeHotBean> hotBeans){
        if (hotBeans == null){
            return;
        }
        adapter.setData(hotBeans);
        adapter.notifyDataSetChanged();
        indicatorView.updateScreen(0, hotBeans.size());
    }

    private class TribeViewPagerAdapter extends PagerAdapter{

        private ArrayList<TribeHotBean> beans;
        private CheImageLoader imageLoader;
        public TribeViewPagerAdapter(){
            beans = new ArrayList<>();
            imageLoader = new CheImageLoader(Volley.newRequestQueue(mContext), mContext);
        }

        public void setData(ArrayList<TribeHotBean> data){
            if (data != null){
                beans = data;
            }
        }

        @Override
        public int getCount() {
            return beans.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.tribe_view_pager_item_layout, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.tribe_view_pager_image);
            TextView visibleView = (TextView) view.findViewById(R.id.tribe_view_pager_visible);
            TextView messageView = (TextView) view.findViewById(tribe_view_pager_message);
            TextView goodView = (TextView) view.findViewById(R.id.tribe_view_pager_support);
            final TribeHotBean hotBean = beans.get(position);
            if (hotBean != null){
                ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.default_img, R.mipmap.default_img);
                imageLoader.get(hotBean.get_pic(), imageListener);
                visibleView.setText(String.valueOf(hotBean.get_click()));
                messageView.setText(String.valueOf(hotBean.get_reply()));
                goodView.setText(String.valueOf(hotBean.get_good()));
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(TribeEvent.tribeDetailId, hotBean.get_id());
                        TribeRouter.getInstance(mContext).showActivity(TribeUI.tribeDetail, bundle);
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           if (object != null && object instanceof View){
               container.removeView((View) object);
           }
        }

    }
}

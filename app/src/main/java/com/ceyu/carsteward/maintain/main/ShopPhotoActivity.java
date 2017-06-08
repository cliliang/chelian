package com.ceyu.carsteward.maintain.main;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.maintain.bean.ShopPhotoDescribe;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/21.
 */
public class ShopPhotoActivity extends BaseActivity {

    /** Called when the activity is first created. */
    private RecyclerViewPager mRecyclerView;
    private TextView numberView, describeView;
    private ArrayList<String> photos = null;
    private ArrayList<String> infos = null;
    private final float scaleRadio = 0.9f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_shop_photo_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            photos = bundle.getStringArrayList(MaintainEvent.photoList);
            infos =  bundle.getStringArrayList(MaintainEvent.photoInfo);
        }
        numberView = (TextView) findViewById(R.id.shop_shop_photo_number);
        describeView = (TextView) findViewById(R.id.shop_shop_photo_describe);
        if (photos != null && photos.size() > 0){
            numberView.setText(1 + "/" + photos.size());
            if (infos != null && infos.size() > 0){
                describeView.setText(infos.get(0));
            }
        }
        mRecyclerView = (RecyclerViewPager) findViewById(R.id.maintain_shop_photo_view_pager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(new PhotoAdapter(this, photos));
//        mRecyclerView.setAdapter(new LayoutAdapter(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = mRecyclerView.getChildCount();
                int width = mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });

        mRecyclerView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int i, int i1) {
                if (photos != null){
                    numberView.setText((i1 + 1) + "/" + photos.size());
                }
                if (infos != null && infos.size() > i1){
                    describeView.setText(infos.get(i1));
                }
            }
        });

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mRecyclerView.getChildCount() < 3) {
                    if (mRecyclerView.getChildAt(1) != null) {
                        if (mRecyclerView.getCurrentPosition() == 0) {
                            View v1 = mRecyclerView.getChildAt(1);
                            v1.setScaleY(scaleRadio);
                            v1.setScaleX(scaleRadio);
                        } else {
                            View v1 = mRecyclerView.getChildAt(0);
                            v1.setScaleY(scaleRadio);
                            v1.setScaleX(scaleRadio);
                        }
                    }
                } else {
                    if (mRecyclerView.getChildAt(0) != null) {
                        View v0 = mRecyclerView.getChildAt(0);
                        v0.setScaleY(scaleRadio);
                        v0.setScaleX(scaleRadio);
                    }
                    if (mRecyclerView.getChildAt(2) != null) {
                        View v2 = mRecyclerView.getChildAt(2);
                        v2.setScaleY(scaleRadio);
                        v2.setScaleX(scaleRadio);
                    }
                }

            }
        });
//    */
    }

    class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder>{
        private Context context;
        private ArrayList<String> photoList = new ArrayList<>();
        private CheImageLoader imageLoader;
        public PhotoAdapter(Context cnt, ArrayList<String> photoArray){
            this.context = cnt;
            imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
            if (photoArray != null){
                photoList = photoArray;
            }
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PhotoHolder(LayoutInflater.from(context).inflate(R.layout.show_shop_photo_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.imageView, R.mipmap.default_img, R.mipmap.default_img);
            imageLoader.get(photoList.get(position), imageListener);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }

        class PhotoHolder extends RecyclerView.ViewHolder{
            private ImageView imageView;
            public PhotoHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.show_shop_photo_image);
            }
        }
    }

}

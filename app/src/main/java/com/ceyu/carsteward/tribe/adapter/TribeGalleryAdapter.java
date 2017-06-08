package com.ceyu.carsteward.tribe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageCache;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.tribe.bean.TribeHomeBean;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/28.
 */
public class TribeGalleryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TribeHomeBean> beans;
    private CheImageLoader imageLoader;
    private int viewWidth;
    private CheImageCache imageCache;

    public TribeGalleryAdapter(Context cnt, int screenWidth) {
        mContext = cnt;
        int marginWidth = (int)Utils.dip2px(mContext, 20);
        viewWidth = (screenWidth - marginWidth) / 2;
        beans = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(mContext), mContext);
        imageCache = CheImageCache.getInstance(mContext);
    }

    public interface OnXListViewScrollListener{
//        void onXList
    }

    public void setData(ArrayList<TribeHomeBean> b, boolean clearData) {
        if (b != null) {
            if (clearData){
                beans.clear();
            }
            if (beans.size() == 0){
                beans = (ArrayList<TribeHomeBean>) b.clone();
            }else {
                beans.addAll(beans.size(), b);
            }
        }
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHold viewHold;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tribe_home_grallery_item_layout, parent, false);
            TextView vView = (TextView) convertView.findViewById(R.id.tribe_item_visible);
            TextView mView = (TextView) convertView.findViewById(R.id.tribe_item_message);
            TextView sView = (TextView) convertView.findViewById(R.id.tribe_item_support);
            TextView cView = (TextView) convertView.findViewById(R.id.tribe_item_content);
            TextView nView = (TextView) convertView.findViewById(R.id.tribe_item_name);
            ImageView iView = (ImageView) convertView.findViewById(R.id.tribe_item_image);
            ImageView hView = (ImageView) convertView.findViewById(R.id.tribe_item_head);
            viewHold = new ViewHold();
            viewHold.headView = hView;
            viewHold.visibleView = vView;
            viewHold.imageView = iView;
            viewHold.messageView = mView;
            viewHold.supportView = sView;
            viewHold.contentView = cView;
            viewHold.nameView = nView;
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }
        TribeHomeBean homeBean = beans.get(position);
        viewHold.visibleView.setText(String.valueOf(homeBean.get_click()));
        viewHold.messageView.setText(String.valueOf(homeBean.get_reply()));
        viewHold.supportView.setText(String.valueOf(homeBean.get_good()));
        viewHold.contentView.setText(homeBean.get_title());
        viewHold.nameView.setText(homeBean.get_uname());
        ImageLoader.ImageListener headImageListener = ImageLoader.getImageListener(viewHold.headView, R.mipmap.default_head, R.mipmap.default_head);
        imageLoader.get(homeBean.get_upic(), headImageListener);

        String imageURl = homeBean.get_pic();
//        viewHold.imageView.setTag(imageURl);
        Bitmap bitmap = imageCache.getBitmap(imageURl);
        if (bitmap != null){
            setImageBitmap(viewHold.imageView, bitmap);
        }else {
            viewHold.imageView.setImageResource(R.mipmap.default_img);
            ImageLoader.ImageListener imageListener = new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    if (imageContainer != null){
                        Bitmap bitmap = imageContainer.getBitmap();
                        setImageBitmap(viewHold.imageView, bitmap);
                    }
                }
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            };
            imageLoader.get(homeBean.get_pic(), imageListener);
        }
        return convertView;
    }

    private void setImageBitmap(ImageView imageView, Bitmap bitmap){
        if (bitmap != null){
            int imageWidth = bitmap.getWidth();
            int imageHeight = bitmap.getHeight();
            int viewHeight = (imageHeight * viewWidth) / imageWidth;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = viewWidth;
            layoutParams.height = viewHeight;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(bitmap);
        }
    }

    private class ViewHold {
        private TextView visibleView, messageView, supportView, contentView, nameView;
        private ImageView imageView, headView;
    }
}

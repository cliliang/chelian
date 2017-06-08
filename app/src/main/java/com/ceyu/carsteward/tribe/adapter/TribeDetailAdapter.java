package com.ceyu.carsteward.tribe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.tribe.bean.TribeDetailBean;
import com.ceyu.carsteward.tribe.bean.TribeDetailList;
import com.ceyu.carsteward.tribe.bean.TribeDetailListBean;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chen on 15/9/6.
 */
public class TribeDetailAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private TribeDetailBean bean;
    private Context context;
    private ArrayList<TribeDetailListBean> detailListBeans;
    private OnReplyClickListener listener;
    private OnMessageClickListener messageListener;
    private OnSupportClickListener supportListener;
    private CheImageLoader imageLoader;
    private int imageWidth;
    public TribeDetailAdapter(Context cnt, int screenWidth){
        this.context = cnt;
        layoutInflater = LayoutInflater.from(cnt);
        imageLoader = new CheImageLoader(Volley.newRequestQueue(cnt), cnt);
        imageWidth = screenWidth - (int)Utils.dip2px(context, 20);
    }

    public void setData(TribeDetailBean detailBean){
        if (detailBean != null){
            this.bean = detailBean;
            TribeDetailList detailList = bean.getDetailList();
            if (detailList != null){
                detailListBeans = detailList.getDetails();
            }
        }
    }

    @Override
    public int getCount() {
        if (detailListBeans != null){
            return detailListBeans.size() + 1;
        }else {
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0){
            convertView = layoutInflater.inflate(R.layout.tribe_detail_head_view_layout, parent, false);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.tribe_detail_image);
            RoundCornerImageView headView = (RoundCornerImageView) convertView.findViewById(R.id.tribe_detail_head);
            TextView nameView = (TextView) convertView.findViewById(R.id.tribe_detail_name);
            TextView visibleView = (TextView) convertView.findViewById(R.id.tribe_detail_visible);
            TextView messageView = (TextView) convertView.findViewById(R.id.tribe_detail_message);
            TextView supportView = (TextView) convertView.findViewById(R.id.tribe_detail_support);
            TextView dateView = (TextView) convertView.findViewById(R.id.tribe_detail_date);
            final TextView contentView = (TextView) convertView.findViewById(R.id.tribe_detail_content);
            TextView numView = (TextView) convertView.findViewById(R.id.tribe_detail_num);
            View messageLayout = convertView.findViewById(R.id.tribe_detail_comment_message_layout);
            View supportLayout = convertView.findViewById(R.id.tribe_detail_support_layout);
            ImageLoader.ImageListener imageListener = new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response != null){
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null){
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();
                            int imageheight = imageWidth * height / width;
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                            params.height = imageheight;
                            imageView.setLayoutParams(params);
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.mipmap.default_img);
                }
            };
            ImageLoader.ImageListener headListener = ImageLoader.getImageListener(headView, R.mipmap.default_img, R.mipmap.default_img);
            if (bean != null){
                imageLoader.get(bean.get_pic(), imageListener);
                imageLoader.get(bean.get_upic(), headListener);
                nameView.setText(bean.get_uname());
                visibleView.setText(String.valueOf(bean.get_click()));
                if (detailListBeans != null){
                    messageView.setText(String.valueOf(detailListBeans.size()));
                }else {
                    messageView.setText("0");
                }
                supportView.setText(String.valueOf(bean.get_good()));
                dateView.setText(bean.get_date());
                contentView.setText(bean.get_title());
                if (detailListBeans != null && detailListBeans.size() > 0){
                    numView.setText(String.format(Locale.US, context.getResources().getString(R.string.tribe_comment_num), detailListBeans.size()));
                }else {
                    numView.setText(context.getResources().getString(R.string.tribe_comment_null));
                }
                messageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (messageListener != null){
                            messageListener.onMessageClick();
                        }
                    }
                });
                supportLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (supportListener != null){
                            supportListener.onSupportClick();
                        }
                    }
                });
            }
        }else {
            convertView = layoutInflater.inflate(R.layout.tribe_detail_item_layout, parent, false);
            RoundCornerImageView headView = (RoundCornerImageView) convertView.findViewById(R.id.tribe_detail_comment_head);
            TextView nameView = (TextView) convertView.findViewById(R.id.tribe_detail_comment_name);
            TextView dateView = (TextView) convertView.findViewById(R.id.tribe_detail_comment_date);
            TextView contentView = (TextView) convertView.findViewById(R.id.tribe_detail_comment_content);
            TextView replyView = (TextView) convertView.findViewById(R.id.tribe_detail_comment_reply);
            final TribeDetailListBean listBean = detailListBeans.get(position - 1);
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(headView, R.mipmap.default_img, R.mipmap.default_img);
            if (listBean != null){
                imageLoader.get(listBean.get_upic(), imageListener);
                nameView.setText(listBean.get_uname());
                dateView.setText(listBean.get_date());
                contentView.setText(listBean.get_info());
                replyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null){
                            listener.onReplyClick(listBean);
                        }
                    }
                });
            }
        }
        return convertView;
    }

    public interface OnReplyClickListener{
        void onReplyClick(TribeDetailListBean bean);
    }

    public void setOnReplyClickedListener(OnReplyClickListener l){
        this.listener = l;
    }

    public interface OnMessageClickListener{
        void onMessageClick();
    }

    public void setOnMessageClickListener(OnMessageClickListener l){
        this.messageListener = l;
    }

    public interface OnSupportClickListener{
        void onSupportClick();
    }

    public void setOnSupportClickListener(OnSupportClickListener l){
        this.supportListener = l;
    }
}

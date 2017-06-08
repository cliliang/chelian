package com.ceyu.carsteward.points.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.points.PointsURLs;
import com.ceyu.carsteward.points.bean.PointGoodsBean;
import com.ceyu.carsteward.points.router.PointsRouter;
import com.ceyu.carsteward.points.router.PointsUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chen on 15/10/13.
 */
public class PointMainItemAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private OnBuyClickListener listener;
    private List<PointGoodsBean> list;
    private CheImageLoader imageLoader;
    public PointMainItemAdapter(Context cnt){
        this.mContext = cnt;
        inflater = LayoutInflater.from(mContext);
        list = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(mContext), mContext);
    }

    public void setData(List<PointGoodsBean> beans){
        if (beans != null){
            this.list = beans;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.points_list_item_layout, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.points_item_image_view);
            TextView nameView = (TextView) convertView.findViewById(R.id.points_item_name_view);
            TextView contentView = (TextView) convertView.findViewById(R.id.points_item_describe_view);
            TextView pointsView = (TextView) convertView.findViewById(R.id.points_item_points_view);
            TextView moneyView = (TextView) convertView.findViewById(R.id.points_item_money_view);
            TextView exchangeView = (TextView) convertView.findViewById(R.id.points_item_exchange_view);
            holder = new PlaceHolder();
            holder.imageView = imageView;
            holder.nameView = nameView;
            holder.describeView = contentView;
            holder.pointsView = pointsView;
            holder.moneyView = moneyView;
            holder.exchangeView = exchangeView;
            convertView.setTag(holder);
        }else {
            holder = (PlaceHolder) convertView.getTag();
        }
        final PointGoodsBean bean = list.get(position);
        if (bean != null){
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.imageView, R.mipmap.default_img, R.mipmap.default_img);
            imageLoader.get(bean.get_pic(), listener);
            holder.nameView.setText(bean.get_subject());
            holder.describeView.setText(bean.get_content());
            holder.moneyView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.pointsView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.point_number_jifen), bean.get_integral()));
            holder.moneyView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.point_number_money), bean.get_money()));
            if (bean.is_state()){
                holder.exchangeView.setEnabled(true);
            }else {
                holder.exchangeView.setEnabled(false);
            }
            holder.exchangeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("points", bean);
                    PointsRouter.getInstance(mContext).showActivity(PointsUI.pointsBuy, bundle);
                }
            });
        }

        return convertView;
    }

    private class PlaceHolder{
        private ImageView imageView;
        private TextView nameView, describeView, pointsView, moneyView, exchangeView;
    }

    public interface OnBuyClickListener{
        void onBuyClick();
    }

    public void setOnBuyClickListener(OnBuyClickListener l){
        this.listener = l;
    }

}

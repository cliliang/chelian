package com.ceyu.carsteward.self.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.self.bean.SelfCommodityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 15/9/11.
 */
public class SelfSelectAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<SelfCommodityBean> beans;
    private CheImageLoader imageLoader;
    private OnItemDetailClickListener listener;
    private int itemWidth;
    private int selectedId = 0;
    private Context mContext;
    public SelfSelectAdapter(Context cnt){
        this.mContext = cnt;
        layoutInflater = LayoutInflater.from(cnt);
        beans = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(cnt), cnt);
    }

    public void setData(List<SelfCommodityBean> b, int id){
        if (b != null){
            beans = b;
            this.selectedId = id;
        }
    }

    public void setItemWidth(int w){
        this.itemWidth = w;
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
        PlaceHolder holder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.self_select_item_layout, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.self_select_image);
            TextView titleView = (TextView) convertView.findViewById(R.id.self_select_name);
            TextView priceView = (TextView) convertView.findViewById(R.id.self_select_price);
            TextView moreView = (TextView) convertView.findViewById(R.id.self_select_detail);
            View mainView = convertView.findViewById(R.id.self_select_root_layout);
            holder = new PlaceHolder();
            holder.photoView = imageView;
            holder.nameView = titleView;
            holder.moneyView = priceView;
            holder.detailView = moreView;
            holder.rootLayout = mainView;
            convertView.setTag(holder);
        }else {
            holder = (PlaceHolder) convertView.getTag();
        }
        if (itemWidth > 0){
            View layoutView = convertView.findViewById(R.id.self_select_item_layout);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutView.getLayoutParams();
            params.width = itemWidth;
            layoutView.setLayoutParams(params);

            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.photoView, R.mipmap.default_img, R.mipmap.default_img);
            final SelfCommodityBean bean = beans.get(position);
            if (bean != null){
                imageLoader.get(bean.get_pic(), imageListener);
                holder.nameView.setText(bean.get_subject());
                holder.moneyView.setText("￥" + bean.get_material());
                holder.detailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null){
                            listener.onItemDetailClick(bean);
                        }
                    }
                });

                if (bean.get_id() == Math.abs(selectedId)){
                    holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
                }else {
                    holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                }
            }
        }
        return convertView;
    }

    private class PlaceHolder{
        private ImageView photoView;
        private TextView nameView, moneyView, detailView;
        private View rootLayout;
    }

    public interface OnItemDetailClickListener{
        void onItemDetailClick(SelfCommodityBean bean);
    }

    public void setOnItemDetailClickListener(OnItemDetailClickListener l){
        this.listener = l;
    }
}

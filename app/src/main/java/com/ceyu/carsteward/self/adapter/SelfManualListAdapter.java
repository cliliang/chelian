package com.ceyu.carsteward.self.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.self.bean.SelfCommodityBean;
import com.ceyu.carsteward.self.bean.SelfSelectBean;
import com.ceyu.carsteward.self.views.SelfSelectCommodityView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 15/9/11.
 */
public class SelfManualListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SelfSelectBean> data;
    private OnManualSelectListener selectListener;
    private OnManualCheckedListener checkListener;
    private OnManualSelectDetailListener detailListener;
    private Map<Integer, Boolean> selectIds = new HashMap<>();
    public SelfManualListAdapter(Context cnt){
        this.mContext = cnt;
        data = new ArrayList<>();
    }

    public void setData(List<SelfSelectBean> list){
        if (list != null){
            this.data = list;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PlaceHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.self_manaul_list_view_item_layout, null);
            ImageView boxView = (ImageView) convertView.findViewById(R.id.manual_item_check_box);
            TextView nameView = (TextView) convertView.findViewById(R.id.manual_item_title_view);
            TextView moneyView = (TextView) convertView.findViewById(R.id.manual_item_money_view);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.manual_item_more_view);
            View moreLayout = convertView.findViewById(R.id.manual_more_layout);
            SelfSelectCommodityView contentView = (SelfSelectCommodityView) convertView.findViewById(R.id.self_manual_select_view);
            holder = new PlaceHolder();
            holder.checkBox = boxView;
            holder.titleView = nameView;
            holder.priceView = moneyView;
            holder.arrowView = imageView;
            holder.selfView = contentView;
            holder.layout = moreLayout;
            convertView.setTag(holder);
        }else {
            holder = (PlaceHolder) convertView.getTag();
        }
        SelfSelectBean bean = data.get(position);
        if (bean != null){
            int defaultSelect = bean.get_select();
            if (defaultSelect > 0){
                selectIds.put(position, true);
                holder.checkBox.setImageResource(R.mipmap.box_selected);
                holder.titleView.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
                holder.priceView.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
            }else {
                selectIds.put(position, false);
                holder.checkBox.setImageResource(R.mipmap.box_normal);
                holder.titleView.setTextColor(mContext.getResources().getColor(R.color.text_hint));
                holder.priceView.setTextColor(mContext.getResources().getColor(R.color.text_hint));
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.selfView.setVisibility(holder.selfView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            int absId = Math.abs(defaultSelect);
            SelfCommodityBean commodityBean = null;
            List<SelfCommodityBean> commodityBeans = bean.getSelectBeans();
            for (SelfCommodityBean b : commodityBeans){
                if (absId == b.get_id()){
                    commodityBean = b;
                }
            }
            if (commodityBean != null){
                holder.titleView.setText(bean.get_partsName() + ":" + commodityBean.get_subject());
                holder.priceView.setText(String.valueOf(commodityBean.get_material()));
                holder.selfView.setData(commodityBeans, commodityBean.get_id());
                holder.selfView.setOnSelectItemClickedListener(new SelfSelectCommodityView.OnSelectItemClickListener() {
                    @Override
                    public void onSelectItemClick(SelfCommodityBean bean) {
                        if (selectListener != null) {
                            selectListener.onManualSelectListener(position, bean.get_id());
                        }
                    }
                });

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkListener != null) {
                            checkListener.onManualCheck(position, !selectIds.get(position));
                        }
                    }
                });
                holder.selfView.setOnSelectItemDetailClickListener(new SelfSelectCommodityView.OnSelectItemDetailClickListener() {
                    @Override
                    public void onSelectItemDetailClick(SelfCommodityBean bean) {
                        if (detailListener != null){
                            detailListener.onManualSelectDetail(position, bean);
                        }
                    }
                });
            }
        }
        return convertView;
    }

    private class PlaceHolder{
        public ImageView checkBox;
        private TextView titleView, priceView;
        private ImageView arrowView;
        private SelfSelectCommodityView selfView;
        private View layout;
    }

    public interface OnManualSelectListener{
        void onManualSelectListener(int position, int selectId);
    }

    public void setOnManaulSelectListener(OnManualSelectListener l){
        this.selectListener = l;
    }

    public interface OnManualCheckedListener{
        void onManualCheck(int position, boolean isCheck);
    }

    public void setOnManaulCheckedListener(OnManualCheckedListener l){
        this.checkListener = l;
    }

    public interface OnManualSelectDetailListener{
        void onManualSelectDetail(int position, SelfCommodityBean bean);
    }

    public void setOnManualSelectDetailListener(OnManualSelectDetailListener l){
        this.detailListener = l;
    }
}

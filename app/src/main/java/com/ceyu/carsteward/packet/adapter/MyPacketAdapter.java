package com.ceyu.carsteward.packet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.packet.bean.PacketInfo;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chen on 15/6/8.
 */
public class MyPacketAdapter extends BaseAdapter {

    private ArrayList<PacketInfo> list;
    private Context mContext;
    private int choiceId;
    private boolean show = false;

    public MyPacketAdapter(Context context, boolean showChoice){
        this.mContext = context;
        list = new ArrayList<>();
        this.show = showChoice;
    }

    public void setData(ArrayList<PacketInfo> l){
        if (l == null || l.size() == 0){
            return;
        }
        if (list.size() == 0){
            this.list = l;
        }else {
            list.addAll(0, l);
        }
    }

    public void setChoiceId(int id){
        this.choiceId = id;
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
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.red_packet_new_item_layout, null);
            TextView mView = (TextView) convertView.findViewById(R.id.packet_of_mine_money);
            TextView lView = (TextView) convertView.findViewById(R.id.packet_of_mine_limit);
            TextView tView = (TextView) convertView.findViewById(R.id.packet_of_mine_type);
            TextView dView = (TextView) convertView.findViewById(R.id.packet_of_mine_describe);
            View mLayout = convertView.findViewById(R.id.packet_of_mine_main_layout);
            View maLayout = convertView.findViewById(R.id.packet_of_mine_money_layout);
            ImageView cView = (ImageView) convertView.findViewById(R.id.packet_of_mine_bean_choice);
            ImageView tImageView = (ImageView) convertView.findViewById(R.id.packet_of_mine_limit_image);
            placeHolder = new PlaceHolder();
            placeHolder.moneyView = mView;
            placeHolder.dateView = lView;
            placeHolder.typeView = tView;
            placeHolder.describeView = dView;
            placeHolder.mainLayout = mLayout;
            placeHolder.moneyLayout = maLayout;
            placeHolder.ticketView = tImageView;
            placeHolder.choiceView = cView;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        PacketInfo info = list.get(position);
        placeHolder.moneyView.setText(String.format(Locale.US, "%d", info.getNum()));
        placeHolder.dateView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.user_red_packet_use_date), info.get_limit()));
        placeHolder.typeView.setText(info.get_ticketType());
        placeHolder.describeView.setText(info.get_ticketDescribe());
        if (info.is_usable()){
            placeHolder.mainLayout.setBackgroundResource(R.drawable.red_packet_orange_foue_line);
            placeHolder.moneyLayout.setBackgroundResource(R.mipmap.red_orange_background);
            placeHolder.ticketView.setImageResource(R.mipmap.bangbang_ticket);
        }else {
            placeHolder.mainLayout.setBackgroundResource(R.drawable.red_packet_gary_four_lie);
            placeHolder.moneyLayout.setBackgroundResource(R.mipmap.red_gary_background);
            placeHolder.ticketView.setImageResource(R.mipmap.bangbang_hui);
        }
        if (show){
            placeHolder.choiceView.setVisibility(View.VISIBLE);
            if (info.get_id() == choiceId){
                placeHolder.choiceView.setImageResource(R.mipmap.packet_selected);
            }else {
                placeHolder.choiceView.setImageResource(R.mipmap.packet_normal);
            }
        }else {
            placeHolder.choiceView.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class PlaceHolder{
        private TextView moneyView, dateView, typeView, describeView;
        private View mainLayout, moneyLayout;
        private ImageView ticketView, choiceView;
    }
}

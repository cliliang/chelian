package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class PacketPayAdapter extends BaseAdapter {

    private ArrayList<PacketInfo> list;
    private Context mContext;
    private ArrayList<Integer> checkedIds = new ArrayList<>();

    public PacketPayAdapter(Context context){
        this.mContext = context;
        list = new ArrayList<>();
    }

    public void setData(ArrayList<PacketInfo> l){
        if (l == null || l.size() == 0){
            return;
        }
        this.list = l;
    }

    public void setCheckedIds(ArrayList<Integer> ids){
        this.checkedIds = ids;
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
        final PacketInfo info = list.get(position);
        convertView = LayoutInflater.from(mContext).inflate(R.layout.engineer_packet_pay_new_item_layout, null);
        TextView moneyView = (TextView) convertView.findViewById(R.id.packet_pay_money_value);
        TextView typeView = (TextView) convertView.findViewById(R.id.packet_pay_type_id);
        TextView describeView = (TextView) convertView.findViewById(R.id.packet_pay_describe_id);
        TextView limitView = (TextView) convertView.findViewById(R.id.packet_pay_limit_id);
        View mainLayout = convertView.findViewById(R.id.packet_pay_main_layout);
        View moneyLayout = convertView.findViewById(R.id.packet_pay_money_layout);
        ImageView usableView = (ImageView) convertView.findViewById(R.id.packet_pay_usable_id);
        ImageView box = (ImageView) convertView.findViewById(R.id.packet_pay_check_id);
        if (checkedIds.contains(info.get_id())){
            box.setImageResource(R.mipmap.packet_selected);
        }else {
            box.setImageResource(R.mipmap.packet_normal);
        }
        moneyView.setText(String.format(Locale.US, "%d", info.getNum()));
        typeView.setText(info.get_ticketType());
        describeView.setText(info.get_ticketDescribe());
        limitView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.user_red_packet_use_date), info.get_limit()));
        if (info.is_usable()){
            mainLayout.setBackgroundResource(R.drawable.red_packet_orange_foue_line);
            moneyLayout.setBackgroundResource(R.mipmap.red_orange_background);
            usableView.setImageResource(R.mipmap.bangbang_ticket);
        }else {
            mainLayout.setBackgroundResource(R.drawable.red_packet_gary_four_lie);
            moneyLayout.setBackgroundResource(R.mipmap.red_gary_background);
            usableView.setImageResource(R.mipmap.bangbang_hui);
        }
        return convertView;
    }

}

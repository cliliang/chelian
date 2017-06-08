package com.ceyu.carsteward.packet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;

/**
 * Created by chen on 15/6/8.
 */
public class PacketFacadeView extends LinearLayout {

    private OnBreakRulesOnClick breakRulesListener;
    private OnRedPacketOnClick redPacketListener;
    public PacketFacadeView(Context context) {
        super(context);
        init(context);
    }

    public PacketFacadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PacketFacadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void  init(Context context){
        LayoutInflater.from(context).inflate(R.layout.packet_facade_view_layout, this);
        findViewById(R.id.home_check_break_rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (breakRulesListener != null){
                    breakRulesListener.onBreakRulesClicked();
                }
            }
        });
        findViewById(R.id.home_take_red_packet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (redPacketListener != null){
                    redPacketListener.onRedPacketClicked();
                }
            }
        });
    }

    public void setOnBreadRulesClickedListener(OnBreakRulesOnClick l){
        this.breakRulesListener = l;
    }

    public void setOnRedPacketClickedListener(OnRedPacketOnClick l){
        this.redPacketListener = l;
    }

    public interface OnBreakRulesOnClick{
        void onBreakRulesClicked();
    }

    public interface OnRedPacketOnClick{
        void onRedPacketClicked();
    }
}

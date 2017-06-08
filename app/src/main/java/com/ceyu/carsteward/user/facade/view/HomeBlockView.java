package com.ceyu.carsteward.user.facade.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;

/**
 * Created by chen on 15/6/3.
 */
public class HomeBlockView extends LinearLayout {

    private OnUpkeepClickListener upkeepListener;
    private OnBangClickListener bangListener;
    private OnRepairClickListener repairListener;

    public HomeBlockView(Context context) {
        super(context);
        init(context);
    }

    public HomeBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeBlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.main_home_block_view_layout, this);
        findViewById(R.id.home_icon_4s).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upkeepListener != null) {
                    upkeepListener.onUpkeepClicked();
                }
            }
        });
        findViewById(R.id.home_icon_bang).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (bangListener != null){
                    bangListener.onBangClicked();
                }
            }
        });
        findViewById(R.id.home_icon_repair).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (repairListener != null){
                    repairListener.onRepairClicked();
                }
            }
        });
        //养车记录
        findViewById(R.id.home_icon_record).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(recordListener!=null) recordListener.onRecordClicked();
            }
        });
        //帮帮团购
        findViewById(R.id.home_icon_tuan).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(tuanListener!=null) tuanListener.onTuanClicked();
            }
        });
        //违章查询
        findViewById(R.id.home_icon_break_rule).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(wzListener!=null) wzListener.onWzClicked();
            }
        });
        //领取红包
        findViewById(R.id.home_icon_red_packet).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(redbagListener!=null) redbagListener.onRedbagClicked();
            }
        });
        //敬请期待
        findViewById(R.id.home_icon_home).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(hopeListener!=null) hopeListener.onHopeClicked();
            }
        });
    }

    public interface OnUpkeepClickListener{
        void onUpkeepClicked();
    }

    public interface OnBangClickListener{
        void onBangClicked();
    }

    public interface OnRepairClickListener{
        void onRepairClicked();
    }

    public void setOnUpkeepClickedListener(OnUpkeepClickListener listener){
        this.upkeepListener = listener;
    }

    public void setOnBangClickedListener(OnBangClickListener listener){
        this.bangListener = listener;
    }

    public void setOnRepairClickedListener(OnRepairClickListener listener){
        this.repairListener = listener;
    }




    //养车记录
    private OnRecordClickListener recordListener;
    public interface OnRecordClickListener{
        void onRecordClicked();
    }
    public void setOnRecordClickListener(OnRecordClickListener listener){
        this.recordListener = listener;
    }

    //帮帮团购
    private OnTuanClickListener tuanListener;
    public interface OnTuanClickListener{
        void onTuanClicked();
    }
    public void setOnTuanClickListener(OnTuanClickListener listener){
        this.tuanListener = listener;
    }

    //违章查询
    private OnWzClickListener wzListener;
    public interface OnWzClickListener{
        void onWzClicked();
    }
    public void setOnWzClickListener(OnWzClickListener listener){
        this.wzListener = listener;
    }

    //领取红包
    private OnRedbagClickListener redbagListener;
    public interface OnRedbagClickListener{
        void onRedbagClicked();
    }
    public void setOnRedbagClickListener(OnRedbagClickListener listener){
        this.redbagListener  = listener;
    }


    //敬请期待
    private OnHopeClickListener hopeListener;
    public interface OnHopeClickListener{
        void onHopeClicked();
    }
    public void setOnHopeClickListener(OnHopeClickListener listener){
        this.hopeListener = listener;
    }
}

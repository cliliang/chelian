package com.ceyu.carsteward.engineer.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.ui.views.CircleHeadImageView;
import com.ceyu.carsteward.engineer.bean.EngineerOrderInfo;
import com.ceyu.carsteward.engineer.main.EngineerEvent;
import com.ceyu.carsteward.engineer.router.EngineerRouter;
import com.ceyu.carsteward.engineer.router.EngineerUI;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chen on 15/6/11.
 */
public class EngineerOfMyAdapter extends BaseAdapter {

    private ArrayList<EngineerOrderInfo> infos;
    private Context mContext;
    private RequestQueue requestQueue;
    private CheImageLoader imageLoader;
    private OnStateButtonClickListener stateListener;
    public EngineerOfMyAdapter(Context cnt, RequestQueue rq){
        this.mContext = cnt;
        this.requestQueue = rq;
        imageLoader = new CheImageLoader(requestQueue, mContext);
        infos = new ArrayList<>();
    }

    public void setData(ArrayList<EngineerOrderInfo> list, boolean clearOld){
        if (clearOld){
            infos.clear();
        }

        if (list == null || list.size() == 0){
            return;
        }
        if (infos.size() == 0){
            infos = list;
        }else {
            infos.addAll(list);
        }
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.engineer_of_my_item_layout, null);
            CircleHeadImageView mHeadview = (CircleHeadImageView) convertView.findViewById(R.id.engineer_of_my_head);
            TextView mOrderView = (TextView) convertView.findViewById(R.id.engineer_of_my_order);
            TextView mStateView = (TextView) convertView.findViewById(R.id.engineer_of_my_state);
            TextView mNameView = (TextView) convertView.findViewById(R.id.engineer_of_my_name);
            TextView mLevelView = (TextView) convertView.findViewById(R.id.engineer_of_my_level);
            TextView mBrandView = (TextView) convertView.findViewById(R.id.engineer_of_my_brand);
            TextView mDateView = (TextView) convertView.findViewById(R.id.engineer_of_my_date);
            TextView mMoneyView = (TextView) convertView.findViewById(R.id.engineer_of_my_dollar);
            TextView mLongView = (TextView) convertView.findViewById(R.id.engineer_of_my_connect_long);
            Button mButton = (Button) convertView.findViewById(R.id.engineer_of_my_button);
            TextView mComment = (TextView) convertView.findViewById(R.id.engineer_of_my_comment_action);
            placeHolder = new PlaceHolder();
            placeHolder.headImageView = mHeadview;
            placeHolder.orderView = mOrderView;
            placeHolder.stateView = mStateView;
            placeHolder.nameView = mNameView;
            placeHolder.levelView = mLevelView;
            placeHolder.brandView = mBrandView;
            placeHolder.dateView = mDateView;
            placeHolder.moneyView = mMoneyView;
            placeHolder.stateButton = mButton;
            placeHolder.commenView = mComment;
            placeHolder.longView = mLongView;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        final EngineerOrderInfo info = infos.get(position);
        final ImageLoader.ImageListener listener = ImageLoader.getImageListener(placeHolder.headImageView, R.mipmap.icon_my, R.mipmap.icon_my);
        imageLoader.get(info.get_pic(), listener);
        placeHolder.orderView.setText(String.format(Locale.US, mContext.getResources().getString(R.string.engineer_order_number), info.get_sn()));
        placeHolder.nameView.setText(info.get_name());
        placeHolder.levelView.setText(info.get_level());
        placeHolder.brandView.setText(info.get_model());
        placeHolder.dateView.setText(info.get_date());
        placeHolder.commenView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        placeHolder.moneyView.setText(String.format(Locale.US, "%.0f", info.get_money()));
        int connectLong = info.get_long();
        int minute = connectLong / 60;
        int second = connectLong % 60;
        String connectString = mContext.getResources().getString(R.string.engineer_of_my_connect_long);
        String connectFromat = String.format(Locale.US, connectString, minute, second);
        final int state = info.get_orderStateCode();
        //        0 未付费        30元/15分钟          立即联系
        //        1 已付费        剩余15分钟            立即联系
        //        2 未接通        剩余15分钟            立即联系
        //        3 不足3分钟    已经通话：x分x秒   再次联系
        placeHolder.stateView.setText(info.get_orderState());
        if (state == 0){
            if (info.is_comment()){
                placeHolder.commenView.setVisibility(View.VISIBLE);
            }else {
                placeHolder.commenView.setVisibility(View.INVISIBLE);
            }
            placeHolder.longView.setVisibility(View.VISIBLE);
            placeHolder.longView.setText(connectFromat);
        }else if (state == 1){
            placeHolder.commenView.setVisibility(View.INVISIBLE);
            placeHolder.longView.setVisibility(View.GONE);
        }else if (state == 2){
            placeHolder.commenView.setVisibility(View.INVISIBLE);
            placeHolder.longView.setVisibility(View.GONE);
        }else if (state == 3){
            if (info.is_comment()){
                placeHolder.commenView.setVisibility(View.VISIBLE);
            }else {
                placeHolder.commenView.setVisibility(View.INVISIBLE);
            }
            placeHolder.longView.setVisibility(View.VISIBLE);
            placeHolder.longView.setText(connectFromat);
        }
        if (state == 1 || state == 2 || state == 3){
            placeHolder.stateButton.setText(mContext.getResources().getString(R.string.contact_engineer_now));
        }else {
            placeHolder.stateButton.setText(mContext.getResources().getString(R.string.contact_engineer_return));
        }

        if (!info.is_onLine()){
            placeHolder.stateButton.setEnabled(false);
            placeHolder.stateButton.setText(mContext.getResources().getString(R.string.engineer_service_off_line));
        }else {
            placeHolder.stateButton.setEnabled(true);
        }

        placeHolder.stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateListener != null){
                    placeHolder.stateButton.setEnabled(false);
                    stateListener.onStateClickedListener(info);
                }
            }
        });
        placeHolder.commenView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAlertDialog(info);
            }
        });
        return convertView;
    }

    private class PlaceHolder{
        private CircleHeadImageView headImageView;
        private TextView orderView, stateView, nameView, levelView, brandView, dateView, moneyView, commenView, longView;
        private Button stateButton;
    }

    public void setOnStateButtonClickedListener(OnStateButtonClickListener l){
        this.stateListener = l;
    }

    public interface OnStateButtonClickListener{
        void onStateClickedListener(EngineerOrderInfo info);
    }

    private void showAlertDialog(final EngineerOrderInfo info){
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setMessage(mContext.getResources().getString(R.string.late_3_minute_content))
                .setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(mContext.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EngineerEvent.orderInfo, info);
                        EngineerRouter.getInstance(mContext).showActivity(EngineerUI.engineerComment, bundle);
                    }
                }).setTitle(mContext.getResources().getString(R.string.late_3_minute_title))
                .create();
        dialog.show();
    }
}

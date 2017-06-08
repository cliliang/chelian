package com.ceyu.carsteward.common.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ceyu.carsteward.R;

import java.util.Calendar;

/**
 * Created by chen on 15/7/13.
 */
public class CommonDialog extends Dialog {

    private long donwTime = 0;
    private long upTime = 0;
    public CommonDialog(Context context, String title, String content){
        super(context, R.style.showPhoto);
        init(context, title, content);
    }

    private void init(Context mContext, String title, String content){
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_alert_dialog_bg, null);
        setContentView(view);
        TextView contentView = (TextView) view.findViewById(R.id.common_alert_dialog_content);
        contentView.setText(content);
        TextView titleView = (TextView) view.findViewById(R.id.common_alert_dialog_title);
        titleView.setText(title);
        view.findViewById(R.id.comment_dialog_content_id).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        donwTime = Calendar.getInstance().getTimeInMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        upTime = Calendar.getInstance().getTimeInMillis();
                        if (upTime - donwTime < 100) {
                            dismiss();
                        }
                        break;
                }
                return false;
            }
        });
        show();
    }


}

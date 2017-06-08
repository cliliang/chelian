package com.ceyu.carsteward.common.ui.views.timepick;

import android.app.Dialog;
import android.content.Context;

import com.ceyu.carsteward.R;

/**
 * Created by chen on 15/7/2.
 */
public class DateTimePickerDialog extends Dialog {

    private OnClickSetDateTimeListener listener;

    public DateTimePickerDialog(Context context,String title,  int year, int month, int day, int hour, int min) {
        super(context, R.style.showPay);
        WheelMain view = new WheelMain(context, title);
        view.setViewData(year, month, day, hour, min);
        setContentView(view);
        view.setOnSureClickedListener(new WheelMain.OnSureClickListener() {
            @Override
            public void onSureClicked(String dateTime) {
                if (listener != null){
                    listener.setDateTime(dateTime);
                }
            }
        });
    }

    public interface OnClickSetDateTimeListener{
        void setDateTime(String dateTime);
    }

    public void setOnClickSetDateTimeListener(OnClickSetDateTimeListener l){
        this.listener = l;
    }
}

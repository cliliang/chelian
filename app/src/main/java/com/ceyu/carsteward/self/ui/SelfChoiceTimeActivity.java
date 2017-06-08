package com.ceyu.carsteward.self.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.car.main.CarEvent;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.self.router.SelfRouter;
import com.ceyu.carsteward.self.router.SelfUI;
import com.ceyu.carsteward.self.views.SelfChoiceTimeView;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by chen on 15/9/18.
 */
public class SelfChoiceTimeActivity extends BaseActivity {
    private Context mContext;
    private String dateString, timeString;
    private TextView stepView;
    private boolean resetTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.self_choice_mechanic_time);
        mContext = this;
        setContentView(R.layout.self_choice_time_activity_layout);
        SelfChoiceTimeView choiceTimeView = (SelfChoiceTimeView) findViewById(R.id.self_choice_time_view);
        choiceTimeView.setOnDateChoiceListener(new SelfChoiceTimeView.OnDateChoiceListener() {
            @Override
            public void onDateChoice(String date, String time) {
                dateString = date;
                if (StringUtils.isEmpty(time)){
                    stepView.setEnabled(false);
                }else {
                    stepView.setEnabled(true);
                    timeString = time;
                }

            }
        });
        choiceTimeView.setOnTimeChoiceListener(new SelfChoiceTimeView.OnTimeChoiceListener() {
            @Override
            public void onTimeChoice(String time) {
                stepView.setEnabled(true);
                timeString = time;
                if (resetTime) {
                    Intent intent = new Intent();
                    intent.putExtra(SelfEvent.choiceTimeInfo, dateString + " " + timeString);
                    setResult(SelfEvent.CHOICE_TIME_RESULT, intent);
                    finish();
                }
            }
        });
        dateString = String.format(Locale.US, "%tF", Calendar.getInstance().getTime());
        stepView = (TextView) findViewById(R.id.self_choice_time_next_step);
        stepView.setEnabled(false);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            resetTime = bundle.getBoolean(SelfEvent.choiceTime);
            if (!resetTime){
                stepView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString(CarEvent.mechanicDate, dateString + " " + timeString);
                        SelfRouter.getInstance(mContext).showActivity(SelfUI.selfReserve, bundle);
                    }
                });
            }else {
                stepView.setVisibility(View.GONE);
            }
        }
    }
}

package com.ceyu.carsteward.self.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chen on 15/9/21.
 */
public class SelfChoiceTimeView extends LinearLayout{
    private Context mContext;
    private OnDateChoiceListener dateChoiceListener;
    private OnTimeChoiceListener timeChoiceListener;
    private int[] titleIds = {
            R.id.choice_time_title1,
            R.id.choice_time_title2,
            R.id.choice_time_title3,
            R.id.choice_time_title4,
            R.id.choice_time_title5,
            R.id.choice_time_title6,
            R.id.choice_time_title7
    };
    private int[] blockIds = {
            R.id.choice_time_block1,
            R.id.choice_time_block2,
            R.id.choice_time_block3,
            R.id.choice_time_block4,
            R.id.choice_time_block5,
            R.id.choice_time_block6,
            R.id.choice_time_block7,
            R.id.choice_time_block8,
            R.id.choice_time_block9,
            R.id.choice_time_block10,
            R.id.choice_time_block11,
            R.id.choice_time_block12,
            R.id.choice_time_block13,
            R.id.choice_time_block14,
            R.id.choice_time_block15,
            R.id.choice_time_block16,
            R.id.choice_time_block17,
            R.id.choice_time_block18,
            R.id.choice_time_block19,
            R.id.choice_time_block20,
    };
    private String[] blockStrings = {
            "08:00",
            "08:30",
            "09:00",
            "09:30",
            "10:00",
            "10:30",
            "11:00",
            "11:30",
            "12:00",
            "12:30",
            "13:00",
            "13:30",
            "14:00",
            "14:30",
            "15:00",
            "15:30",
            "16:00",
            "16:30",
            "17:00",
            "17:30",
    };

    private String[] selectableTime = {
            "09:00",
            "13:00",
            "17:30"
    };

    private List<TextView> selectableViews = new ArrayList<>();
    private List<TextView> selectableTitleViews = new ArrayList<>();
    private HashMap<TextView, String> cacheMap = new HashMap<>();
    private TextView currentTitleView;

    public SelfChoiceTimeView(Context context) {
        super(context);
        init(context);
    }

    public SelfChoiceTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelfChoiceTimeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context cnt){
        this.mContext = cnt;
        LayoutInflater.from(mContext).inflate(R.layout.self_choice_time_view_layout, this);
        for (int i = 0; i < blockIds.length; i++){
            final TextView blockView = (TextView) findViewById(blockIds[i]);
            final String timeString = blockStrings[i];
            int result = Arrays.binarySearch(selectableTime, timeString);
            if (result < 0){
                blockView.setClickable(false);
                blockView.setEnabled(false);
            }else {
                selectableViews.add(blockView);
            }
            blockView.setText(timeString);
            blockView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String blockString = blockView.getText().toString();
                    if (timeChoiceListener != null){
                        timeChoiceListener.onTimeChoice(blockString);
                    }
                    cacheMap.clear();
                    cacheMap.put(currentTitleView, timeString);

                    for (TextView textView : selectableViews){
                        if (textView.getId() != v.getId()){
                            textView.setSelected(false);
                        }else {
                            textView.setSelected(true);
                        }
                    }
                }
            });
        }

        final Calendar calendar = Calendar.getInstance();
        String[] week = new String[7];
        Calendar[] calendars = new Calendar[7];
        for (int i = 0; i < 7; i++){
            StringBuilder builder = new StringBuilder();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == 1){
                builder.append("日");
            }else if (dayOfWeek == 2){
                builder.append("一");
            }
            else if (dayOfWeek == 3){
                builder.append("二");
            }
            else if (dayOfWeek == 4){
                builder.append("三");
            }
            else if (dayOfWeek == 5){
                builder.append("四");
            }
            else if (dayOfWeek == 6){
                builder.append("五");
            }else {
                builder.append("六");
            }

            builder.append("\n");
            Date date = calendar.getTime();
            builder.append(String.format(Locale.US, "%tm", date))
                    .append("/")
                    .append(String.format(Locale.US, "%td", date));
            week[i] = builder.toString();
            calendars[i] = (Calendar) calendar.clone();
            calendar.roll(Calendar.DAY_OF_YEAR, 1);
        }

        for (int i = 0; i < titleIds.length; i++){
            final TextView titleView = (TextView) findViewById(titleIds[i]);
            if (i == 0){
                titleView.setSelected(true);
                currentTitleView = titleView;
            }
            titleView.setText(week[i]);
            final Calendar selectCalendar = calendars[i];
            selectableTitleViews.add(titleView);
            titleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTitleView != null && titleView.getId() == currentTitleView.getId()){
                        return;
                    }
                    currentTitleView = titleView;
                    //重绘七天界面
                    for (TextView textView : selectableTitleViews){
                        if (textView.getId() != v.getId()){
                            textView.setSelected(false);
                        }else {
                            textView.setSelected(true);
                        }
                    }
                    //重绘时间界面
                    String cacheString = cacheMap.get(currentTitleView);
                    for (TextView textView : selectableViews){
                        if (StringUtils.isEmpty(cacheString)){
                            textView.setSelected(false);
                        }else {
                            String s = textView.getText().toString();
                            if (cacheString.equals(s)){
                                textView.setSelected(true);
                            }else {
                                textView.setSelected(false);
                            }
                        }

                    }
                    //点击日时向界面提供日，时数据
                    if (dateChoiceListener != null){
                        dateChoiceListener.onDateChoice(String.format(Locale.US, "%tF", selectCalendar.getTime()), cacheString);
                    }
                }
            });
        }
    }

    public interface OnDateChoiceListener{
        void onDateChoice(String date, String time);
    }

    public interface OnTimeChoiceListener{
        void onTimeChoice(String time);
    }

    public void setOnDateChoiceListener(OnDateChoiceListener listener){
        this.dateChoiceListener = listener;
    }

    public void setOnTimeChoiceListener(OnTimeChoiceListener listener){
        this.timeChoiceListener = listener;
    }

}

package com.ceyu.carsteward.common.ui.views.timepick;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.ceyu.carsteward.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WheelMain extends LinearLayout{

	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hour;
	private WheelView wv_min;
	private TextView titleView;
	private static int START_YEAR = Calendar.getInstance().get(Calendar.YEAR);
	private static int END_YEAR = START_YEAR + 5;
	private List<String> list_big;
	private List<String> list_little;
    private OnSureClickListener listener;
	private String dialogTitle;
	public WheelMain(Context context, String title) {
		super(context);
		this.dialogTitle = title;
		initView(context);
	}

	public WheelMain(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context){
		LayoutInflater.from(context).inflate(R.layout.view_time_pick, this);
		titleView = (TextView) findViewById(R.id.view_time_picker_title);
		titleView.setText(dialogTitle);

		wv_year = (WheelView) findViewById(R.id.wv_year);
		wv_month = (WheelView) findViewById(R.id.wv_month);
		wv_day = (WheelView) findViewById(R.id.wv_day);
		wv_hour = (WheelView) findViewById(R.id.wv_hour);
		wv_min = (WheelView) findViewById(R.id.wv_minute);
        findViewById(R.id.btn_timepic_sure).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onSureClicked(getTime());
                }
            }
        });

		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		// 年
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字

		// 月
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");

		// 日
		wv_day.setCyclic(true);
		wv_day.setLabel("日");

		// 小时
		wv_hour.setAdapter(new NumericWheelAdapter(9, 17));
		wv_hour.setCyclic(true);
		wv_hour.setLabel("时");

		// 分钟
		wv_min.setAdapter(new MinuteWheelAdapter(0, 30));
		wv_min.setCyclic(true);
		wv_min.setLabel("分");

		Calendar calendar = Calendar.getInstance();
		setViewData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	}

	public void setViewData(int year, int month, int day, int hour, int min){
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
		wv_month.setCurrentItem(month);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setCurrentItem(day - 1);
		wv_hour.setCurrentItem(hour);
        wv_min.setCurrentItem(1);


		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue,
								  int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue,
								  int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
	}

	public String getTime() {
		StringBuilder sb = new StringBuilder();
        String month = String.valueOf(wv_month.getCurrentItem() + 1);
        String day = String.valueOf((wv_day.getCurrentItem() + 1));
		String mins = String.valueOf(wv_min.getCurrentItem()*30);
		String hours =  String.valueOf((wv_hour.getCurrentItem()+9));
        if (month.length() == 1){
            month = "0" + month;
        }
        if (day.length() == 1){
            day = "0" + day;
        }
		if (mins.length() == 1) {
			mins = "0" + mins;
		}
		if (hours.length() == 1) {
			hours = "0" + hours;
		}

		sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
				.append(month).append("-")
				.append(day).append(" ")
				.append(hours).append(":").append(mins);
		return sb.toString();
	}

    public interface OnSureClickListener{
        void onSureClicked(String dateTime);
    }

    public void setOnSureClickedListener(OnSureClickListener l){
        this.listener = l;
    }

}

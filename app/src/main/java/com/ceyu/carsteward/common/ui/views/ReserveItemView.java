package com.ceyu.carsteward.common.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;
import com.ceyu.carsteward.self.bean.SelfPartBean;

import java.util.Locale;

/**
 * Created by chen on 15/6/29.
 */
public class ReserveItemView extends LinearLayout {

    private TextView titleView, contentView, priceView;
    public ReserveItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, false);
    }

    public ReserveItemView(Context context) {
        super(context);
        init(context, false);
    }

    public ReserveItemView(Context context, boolean showLine) {
        super(context);
        init(context, showLine);
    }

    private void init(Context context, boolean show){
        LayoutInflater.from(context).inflate(R.layout.reserve_maintain_item_layout, this);
        titleView = (TextView) findViewById(R.id.reserve_maintain_title);
        contentView = (TextView) findViewById(R.id.reserve_maintain_content);
        priceView = (TextView) findViewById(R.id.reserve_maintain_price);
        TextView lineView = (TextView) findViewById(R.id.reserve_item_line);
        lineView.setVisibility(show ? VISIBLE : GONE);
    }

    public void setViewData(MaintainSubContent subContent){
        if (subContent != null){
            String content = subContent.get_content();
            contentView.setText(content);
            if (!StringUtils.isEmpty(content)){
                titleView.setText(subContent.get_name() + "：");
            }else {
                titleView.setText(subContent.get_name());
            }
            priceView.setText(subContent.get_price());
        }
    }

    public void setViewData(SelfPartBean bean){
        if (bean != null){
            contentView.setText(bean.get_info());
            if (!StringUtils.isEmpty(bean.get_info())){
                titleView.setText(bean.get_name() + "：");
            }else {
                titleView.setText(bean.get_name());
            }
            priceView.setText(bean.get_money());
        }
    }
}

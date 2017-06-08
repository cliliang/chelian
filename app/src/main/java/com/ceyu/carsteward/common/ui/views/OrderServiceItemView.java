package com.ceyu.carsteward.common.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.maintain.bean.MaintainContent;
import com.ceyu.carsteward.maintain.bean.MaintainDiscount;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;

/**
 * Created by chen on 15/7/2.
 */
public class OrderServiceItemView extends LinearLayout{
    private TextView titleView, contentView, priceView;
    public OrderServiceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, false);
    }

    public OrderServiceItemView(Context context) {
        super(context);
        init(context, false);
    }

    public OrderServiceItemView(Context context, boolean showLine) {
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

    public void setViewData(MaintainContent subContent){
        if (subContent != null){
            String content = subContent.get_info();
            contentView.setText(content);
            if (!StringUtils.isEmpty(content)){
                titleView.setText(subContent.get_part()+ "：");
            }else {
                titleView.setText(subContent.get_part());
            }
            priceView.setText(subContent.get_money());
        }
    }

    public void setViewData(MaintainDiscount discount){
        if (discount != null){
            titleView.setText(discount.get_title());
            priceView.setText(discount.get_num());
        }
    }
}

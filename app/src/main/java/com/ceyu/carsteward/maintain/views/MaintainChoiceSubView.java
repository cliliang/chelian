package com.ceyu.carsteward.maintain.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.maintain.bean.MaintainDetailContent;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/5.
 */
public class MaintainChoiceSubView extends LinearLayout {
    private TextView titleView, contentView, priceView;
    private ImageView imageView;

    public MaintainChoiceSubView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.maintain_reserve_choice_item_layout, this);
        titleView = (TextView) findViewById(R.id.maintain_reserve_item_name);
        contentView = (TextView) findViewById(R.id.maintain_reserve_item_content);
        priceView = (TextView) findViewById(R.id.maintain_reserve_item_price);
        imageView = (ImageView) findViewById(R.id.maintain_reserve_item_icon);
    }

    public void setData(MaintainSubContent subContent){
        if (subContent != null){
            String contentString = subContent.get_content();
            contentView.setText(contentString);
            if (!StringUtils.isEmpty(contentString)){
                titleView.setText(subContent.get_name() + "：");
            }else {
                titleView.setText(subContent.get_name());
            }
            priceView.setText(subContent.get_price());
            ArrayList<MaintainDetailContent> detailContents = subContent.get_sub();
            if (detailContents.size() > 0){
                imageView.setImageResource(R.mipmap.arraw_d);
            }else {
                imageView.setVisibility(INVISIBLE);
            }
        }

    }

    public void setData(MaintainDetailContent detailContent){
        if (detailContent != null){
            contentView.setText(detailContent.get_info());
            priceView.setText(detailContent.get_moneyTxt());
            imageView.setImageResource(R.mipmap.icon_circle);
        }
    }


}

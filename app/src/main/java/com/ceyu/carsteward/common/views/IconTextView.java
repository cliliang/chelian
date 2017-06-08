package com.ceyu.carsteward.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;

/**
 * Created by chen on 15/6/8.
 */
public class IconTextView extends LinearLayout {

    private ImageView imageView;
    private TextView textView;

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_icon_text_view_layout,this);
        imageView = (ImageView) findViewById(R.id.common_icon_text_icon_id);
        textView = (TextView) findViewById(R.id.common_icon_text_text_id);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IconText);
        int imageRes = array.getResourceId(R.styleable.IconText_iconRes, 0);
        int testRes = array.getResourceId(R.styleable.IconText_textRes, 0);
        if (imageRes > 0){
            imageView.setImageResource(imageRes);
        }
        if (testRes > 0){
            textView.setText(testRes);
        }
        array.recycle();
    }

}

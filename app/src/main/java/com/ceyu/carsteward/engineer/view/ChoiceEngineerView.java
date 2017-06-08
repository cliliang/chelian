package com.ceyu.carsteward.engineer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceyu.carsteward.R;

/**
 * Created by chen on 15/6/3.
 */
public class ChoiceEngineerView extends LinearLayout {

    private OnBrandClickListener brandListener;
    private OnTypeClickListener typeListener;
    private OnLocalClickListener localListener;
    private ImageView brandArrow, typeArrow, localArrow;
    private TextView brandName, typeName, localName;
    private boolean showWindow;
    public ChoiceEngineerView(Context context) {
        super(context);
        init(context);
    }

    public ChoiceEngineerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        showWindow = true;
        LayoutInflater.from(context).inflate(R.layout.choice_engineer_view_layout, this);
        brandArrow = (ImageView) findViewById(R.id.engineer_choice_brand_arrow);
        typeArrow = (ImageView) findViewById(R.id.engineer_choice_type_arrow);
        localArrow = (ImageView) findViewById(R.id.engineer_choice_local_arrow);
        brandName = (TextView) findViewById(R.id.engineer_brand_name);
        typeName = (TextView) findViewById(R.id.engineer_type_name);
        localName = (TextView) findViewById(R.id.engineer_local_name);
        findViewById(R.id.engineer_choice_brand_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brandListener != null){
                    if (showWindow){
                        brandArrow.setImageResource(R.mipmap.arraw_u);
                    }
                    brandListener.onBrandClicked(showWindow);
                    showWindow = !showWindow;
                }
            }
        });
        findViewById(R.id.engineer_choice_type_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeListener != null) {
                    if (showWindow) {
                        typeArrow.setImageResource(R.mipmap.arraw_u);
                    }
                    typeListener.onTypeClicked(showWindow);
                    showWindow = !showWindow;
                }
            }
        });
        findViewById(R.id.engineer_choice_local_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localListener != null) {
                    if (showWindow) {
                        localArrow.setImageResource(R.mipmap.arraw_u);
                    }
                    localListener.onLocalClicked(showWindow);
                    showWindow = !showWindow;
                }
            }
        });
    }

    public void setBrandName(String brand){
        brandName.setText(brand);
    }

    public void setTypeName(String name){
        typeName.setText(name);
    }

    public void setLocalName(String name){
        localName.setText(name);
    }

    public void setShowWindow(boolean show){
        this.showWindow = show;
    }

    public void downArrow(){
        brandArrow.setImageResource(R.mipmap.arraw_d);
        typeArrow.setImageResource(R.mipmap.arraw_d);
        localArrow.setImageResource(R.mipmap.arraw_d);
        showWindow = true;
    }

    public interface OnBrandClickListener{
        void onBrandClicked(boolean show);
    }

    public interface OnTypeClickListener{
        void onTypeClicked(boolean show);
    }

    public interface OnLocalClickListener{
        void onLocalClicked(boolean show);
    }

    public void setOnBrandClickListener(OnBrandClickListener listener){
        this.brandListener = listener;
    }

    public void setOnTypeClickListener(OnTypeClickListener listener){
        this.typeListener = listener;
    }

    public void setOnLocalClickListener(OnLocalClickListener listener){
        this.localListener = listener;
    }

}

package com.ceyu.carsteward.maintain.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.maintain.bean.MaintainDetailContent;
import com.ceyu.carsteward.maintain.bean.MaintainSubContent;

import java.util.ArrayList;

/**
 * Created by chen on 15/8/6.
 */
public class MaintainChoiceView extends LinearLayout {
    private CheckBox checkBox;
    private LinearLayout containerLayout;
    private Context mContext;
    private boolean hidden = true;
    private OnSubOptinalItemClick listener;
    private OnSubOptionalCheckChanged changedListener;

    public MaintainChoiceView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.maintain_choice_item_view, this);
        checkBox = (CheckBox) findViewById(R.id.maintain_choice_image_view);
        containerLayout = (LinearLayout) findViewById(R.id.maintain_choice_container_layout);
    }

    public boolean getCheckboxState(){
        return checkBox.isChecked();
    }

    public void setData(MaintainSubContent subContent) {
        if (subContent == null){
            return;
        }
        MaintainChoiceSubView subView = new MaintainChoiceSubView(mContext);
        checkBox.setChecked(subContent.isSelect());
        subView.setData(subContent);
        containerLayout.addView(subView);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (changedListener != null){
                    changedListener.onSubOptinalCheckChanged(isChecked);
                }
            }
        });

        ArrayList<MaintainDetailContent> detailContents = subContent.get_sub();
        final ArrayList<MaintainChoiceSubView> choiceSubViews = new ArrayList<>();
        if (detailContents != null && detailContents.size() > 0){
            for (final MaintainDetailContent detailContent : detailContents){
                MaintainChoiceSubView detailSubView = new MaintainChoiceSubView(mContext);
                detailSubView.setData(detailContent);
                detailSubView.setVisibility(GONE);
                choiceSubViews.add(detailSubView);
                containerLayout.addView(detailSubView);
                detailSubView.setClickable(true);
                detailSubView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null){
                            listener.onSubOptinalItemClicked(detailContent);
                        }
                    }
                });
            }
            subView.setClickable(true);
            subView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hidden){
                        hidden = false;
                        for (MaintainChoiceSubView view : choiceSubViews){
                            view.setVisibility(VISIBLE);
                        }
                    }else {
                        hidden = true;
                        for (MaintainChoiceSubView view : choiceSubViews){
                            view.setVisibility(GONE);
                        }
                    }
                }
            });
        }
    }

    public interface OnSubOptinalItemClick{
        void onSubOptinalItemClicked(MaintainDetailContent detailContent);
    }

    public void setOnSubOptionalItemClicked(OnSubOptinalItemClick l){
        this.listener = l;
    }

    public interface OnSubOptionalCheckChanged{
        void onSubOptinalCheckChanged(boolean isChecked);
    }

    public void setOnSubOptinalCheckChanged(OnSubOptionalCheckChanged l){
        this.changedListener = l;
    }
}

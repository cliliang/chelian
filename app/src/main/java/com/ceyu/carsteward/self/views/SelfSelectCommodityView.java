package com.ceyu.carsteward.self.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.self.adapter.SelfSelectAdapter;
import com.ceyu.carsteward.self.bean.SelfCommodityBean;

import java.util.List;

/**
 * Created by chen on 15/9/11.
 */
public class SelfSelectCommodityView extends LinearLayout {

    private Context mContext;
    private SelfSelectAdapter adapter;
    private SelfHorizontalListView listView;
    private OnSelectItemClickListener listener;
    private OnSelectItemDetailClickListener detailClickListener;
    private ImageView leftMoreIcon, rightMoreIcon;
    private boolean notify = false;
    private int itemWidth = 0;
    private int selection = 0;
    public SelfSelectCommodityView(Context context) {
        super(context);
        init(context);
    }

    public SelfSelectCommodityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context cnt){
        this.mContext = cnt;
        LayoutInflater.from(mContext).inflate(R.layout.self_select_commodity_view_layout, this);
        listView = (SelfHorizontalListView) findViewById(R.id.self_select_list);
        leftMoreIcon = (ImageView) findViewById(R.id.self_select_left_more_icon);
        View leftMoreView = findViewById(R.id.self_select_left_more);
        leftMoreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection > 0){
                    selection--;
                    listView.setSelection(selection);
                }
            }
        });
        rightMoreIcon = (ImageView) findViewById(R.id.self_select_right_more_icon);
        View rightMoreView = findViewById(R.id.self_select_right_more);
        rightMoreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCount = adapter.getCount();
                if (selection < itemCount - 1){
                    selection++;
                    listView.setSelection(selection);
                }
            }
        });
        adapter = new SelfSelectAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelfCommodityBean bean = (SelfCommodityBean) adapter.getItem(position);
                if (bean != null && listener != null) {
                    listener.onSelectItemClick(bean);
                }
            }
        });
        adapter.setOnItemDetailClickListener(new SelfSelectAdapter.OnItemDetailClickListener() {
            @Override
            public void onItemDetailClick(SelfCommodityBean bean) {
                if (detailClickListener != null) {
                    detailClickListener.onSelectItemDetailClick(bean);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (itemWidth == 0){
            int itemWidth = listView.getItemWidth();
            if (itemWidth > 0 && !notify){
                notify = true;
                adapter.setItemWidth(itemWidth);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setData(List<SelfCommodityBean> beans, int selectedId){
        if (beans != null && adapter != null){
            adapter.setData(beans, selectedId);
            adapter.notifyDataSetChanged();
            if (beans.size() < 3){
                leftMoreIcon.setImageResource(R.mipmap.self_arrow_left_normal);
                rightMoreIcon.setImageResource(R.mipmap.self_arrow_right_normal);
            }
        }
    }

    public interface OnSelectItemClickListener{
        void onSelectItemClick(SelfCommodityBean bean);
    }

    public void setOnSelectItemClickedListener(OnSelectItemClickListener l){
        this.listener = l;
    }

    public interface OnSelectItemDetailClickListener{
        void onSelectItemDetailClick(SelfCommodityBean bean);
    }

    public void setOnSelectItemDetailClickListener(OnSelectItemDetailClickListener l){
        this.detailClickListener = l;
    }

}

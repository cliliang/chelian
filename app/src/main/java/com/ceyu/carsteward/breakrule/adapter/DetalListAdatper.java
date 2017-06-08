package com.ceyu.carsteward.breakrule.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.breakrule.bean.BreakRulesBean;
import com.ceyu.carsteward.common.tools.Utility;

import java.util.List;

/**
 * Created by Administrator on 2015/7/6.
 */
public class DetalListAdatper extends BaseAdapter{

    private Activity mActivity;

    private List<BreakRulesBean.BreakRulesDetailBean> mList;

    public DetalListAdatper(Activity activity){
       this.mActivity = activity;
    }

    public void injectList(List<BreakRulesBean.BreakRulesDetailBean> list){
        if(mList == null) mList = list;
        else mList.addAll(list);
    }

    public void clearList(){mList.clear();}

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        Object object = null;
        try{
            object = mList.get(position);
        }catch (Exception e){
            Utility.LogUtils.ex(e);
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null || convertView.getTag() == null){
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.breakrule_record_item, parent, false);
            holder = initView(convertView);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        initData(holder, position);
        return convertView;
    }

    private void initData(ViewHolder holder, int position){
        BreakRulesBean.BreakRulesDetailBean bean = mList.get(position);
        holder.tvDate.setText(bean.getDate());
        holder.tvPosition.setText(bean.getArea());
        holder.tvEvent.setText(bean.getEvent());
        holder.tvPoint.setText(bean.getPoints());
        holder.tvFine.setText(bean.getFine());
        holder.tvStatus.setText(bean.getState());
    }

    private ViewHolder initView(View view){
        ViewHolder holder = new ViewHolder();
        holder.tvDate = (TextView) view.findViewById(R.id.tv_breakrules_record_item_datetime);
        holder.tvPosition = (TextView) view.findViewById(R.id.tv_breakrules_record_item_position);
        holder.tvEvent = (TextView) view.findViewById(R.id.tv_breakrules_record_item_event);
        holder.tvPoint = (TextView) view.findViewById(R.id.tv_breakrules_record_item_point);
        holder.tvFine = (TextView) view.findViewById(R.id.tv_breakrules_record_item_fine);
        holder.tvStatus = (TextView) view.findViewById(R.id.tv_breakrules_record_item_status);
        return holder;
    }

    private class ViewHolder{
        TextView tvDate,    //日期时间
        tvPosition, //地点
        tvEvent,    //违章原因
        tvPoint,    //扣分
        tvFine, //罚款
        tvStatus;   //状态（已处理、未处理）
    }

}

package com.ceyu.carsteward.record.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.record.bean.RecordBean;
import com.ceyu.carsteward.record.main.RecordListActivity;
import com.ceyu.carsteward.record.main.RecordURLs;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/7/1.
 * 养车记录适配器
 */
public class RecordListAdapter extends BaseAdapter implements Response.Listener<JSONObject>, Response.ErrorListener{

    private List<RecordBean> list;
    private RecordListActivity activity;
    private ImageLoader loader;
    private int position;
    private View tvDelete;

    public RecordListAdapter(RecordListActivity activity){
        this.activity = activity;
        AppContext appContext = (AppContext) activity.getApplicationContext();
        this.loader = new CheImageLoader(appContext.queue(), activity);
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<RecordBean> list){
        if(list==null) return;
        if(this.list==null) this.list = list;
        else this.list.addAll(list);
    }

    public void cleanData(){
        if(this.list == null) return;
        else this.list.clear();


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null||convertView.getTag()==null){
            convertView = activity.getLayoutInflater().inflate(R.layout.record_list_item, null, false);
            holder = initViews(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final RecordBean recordBean = list.get(position);
        setListeners(recordBean, holder);
        loadData(recordBean, holder);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordListAdapter.this.tvDelete = view;
                view.setClickable(false);
                RecordListAdapter.this.position = position;
                RecordListAdapter.this.showDeleteDialog(recordBean);
            }
        });
        return convertView;
    }

    //装载数据
    private void loadData(final RecordBean recordBean, ViewHolder holder){
        //养车记录内容
        //holder.tvContent.setText(recordBean.getInfo());
        setContent(recordBean.getInfo(), holder.tvContent, holder.tvExpand);
        //上传日期
        holder.tvDate.setText(recordBean.getDate());
        //上传地点
        holder.tvLocation.setText(recordBean.getCity()==null?"":recordBean.getCity());

        //养车记录照片
        String[] pics = recordBean.getPic();
        loadPics(pics, holder);
        Utility.LogUtils.e("record", "Picture Count===>" + pics.length);
        Utility.LogUtils.e("record", Arrays.toString(pics));


    }

    private void setContent(String content, final TextView holder, final TextView exp){
        holder.setText(content);
        holder.post(new Runnable() {
            @Override
            public void run() {
                int lines = holder.getLineCount();
                if(lines>3) exp.setVisibility(View.VISIBLE);
                else exp.setVisibility(View.GONE);
            }
        });
        int lines = holder.getLineCount();
        exp.setVisibility(View.VISIBLE);
        holder.setTag(true);
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) holder.getTag()) {   //展开
                    holder.setMaxLines(Integer.MAX_VALUE);
                    holder.setTag(false);
                    exp.setText(activity.getString(R.string.record_hide));
                } else {  //收起
                    holder.setMaxLines(3);
                    holder.setTag(true);
                    exp.setText(activity.getString(R.string.record_expand));
                }
            }
        });
    }

    private void loadPics(String[] pics, ViewHolder holder){
        switch (pics.length){
            case 3:
                Utility.LogUtils.e("record","load "+3+" pics");
                if(!TextUtils.isEmpty(pics[2])) {
                    Utility.LogUtils.e("record", "load " + 3 + " pics if");
                    ImageLoader.ImageListener imageListener2 = ImageLoader.getImageListener(holder.iv2, R.mipmap.loading, R.mipmap.loading);
                    loader.get(pics[2].trim(), imageListener2);
                    holder.iv2.setOnClickListener(new ShowBigListener(pics[2]));
                }
            case 2:
                Utility.LogUtils.e("record","load "+2+" pics");
                if(!TextUtils.isEmpty(pics[1])) {
                    Utility.LogUtils.e("record", "load " + 2 + " pics if");

                    ImageLoader.ImageListener imageListener1 = ImageLoader.getImageListener(holder.iv1, R.mipmap.loading, R.mipmap.loading);
                    loader.get(pics[1].trim(), imageListener1);
                    holder.iv1.setOnClickListener(new ShowBigListener(pics[1]));
                }
            case 1:
                Utility.LogUtils.e("record","load "+1+" pics");
                if(TextUtils.isEmpty(pics[0])) {
                    Utility.LogUtils.e("record","load "+1+" pics noif");
                    holder.llImageParent.setVisibility(View.GONE);
                }else {
                    holder.llImageParent.setVisibility(View.VISIBLE);
                    Utility.LogUtils.e("record","load "+1+" pics if");
                    ImageLoader.ImageListener imageListener0 = ImageLoader.getImageListener(holder.iv0, R.mipmap.loading, R.mipmap.loading);
                    loader.get(pics[0].trim(), imageListener0);
                    holder.iv0.setOnClickListener(new ShowBigListener(pics[0]));
                }
                break;
            case 0:
                Utility.LogUtils.e("record","load no pics");
                holder.llImageParent.setVisibility(View.GONE);
                break;
        }
    }

    private void setListeners(final RecordBean recordBean, ViewHolder holder){
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordListAdapter.this.shareRecord(recordBean);
            }
        });

    }

    //初始化convertView
    private ViewHolder initViews(View convertView){

        ViewHolder holder = new ViewHolder();

        holder.tvContent = (TextView)convertView.findViewById(R.id.tv_recordlistitem_content);   //养车记录内容
        holder.tvDate = (TextView)convertView.findViewById(R.id.tv_recordlistitem_date);   //日期时间
        holder.tvLocation = (TextView)convertView.findViewById(R.id.tv_recordlistitem_location);   //地点
        holder.tvExpand = (TextView)convertView.findViewById(R.id.tv_recordlistitem_expand);   //地点
        holder.share = convertView.findViewById(R.id.tv_recordlistitem_share);   //分享按钮
        holder.delete = convertView.findViewById(R.id.tv_recordlistitem_delete);   //删除按钮
        holder.iv0 = (ImageView)convertView.findViewById(R.id.iv_recordlistitem_image0);
        holder.iv1 = (ImageView)convertView.findViewById(R.id.iv_recordlistitem_image1);
        holder.iv2 = (ImageView)convertView.findViewById(R.id.iv_recordlistitem_image2);
        holder.llImageParent = convertView.findViewById(R.id.ll_recordListitem_imageparent);

        return holder;
    }


    private class ViewHolder{

        TextView tvDate,tvLocation, tvExpand;
        View share,delete,llImageParent;
        ImageView iv0,iv1,iv2;
        TextView tvContent;

    }

    //分享养车记录
    private void shareRecord(RecordBean bean){
        Utility.LogUtils.i("record", bean.toString());
    }

    //删除养车记录
    private void showDeleteDialog(final RecordBean bean){
        new AlertDialog.Builder(activity)
                .setTitle(R.string.record_deletedialog_title)
                .setMessage(R.string.record_deletedialog_content)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecord(bean);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvDelete.setClickable(true);
                    }
                })
                .show();
    }

    /*
    删除养车记录
    app?act=record,del&token=UID:4767&id=1
     */
    private void deleteRecord(RecordBean record){
        ProgressDialog.getInstance().show(activity,true);
        AppContext appContext = (AppContext)activity.getApplicationContext();
        CheJSONObjectRequest request = new CheJSONObjectRequest(RecordURLs.deleteRecord,
                new Utility.ParamsBuilder(appContext)
                        .set("id", record.getId()).build(),
                this, this);
        Utility.LogUtils.e("record", RecordURLs.deleteRecord+"&token="+appContext.getActiveUser().getToken()+"&id="+record.getId());
        appContext.queue().add(request);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {
        ProgressDialog.getInstance().dismiss();
        if(tvDelete!=null) tvDelete.setClickable(true);
        Utility.LogUtils.ex(volleyError);
        UIHelper.ToastMessage(activity, activity.getString(R.string.record_deletefailed));
    }

    //删除成功
    @Override
    public void onResponse(JSONObject jsonObject) {
        ProgressDialog.getInstance().dismiss();
        Utility.LogUtils.e("record", jsonObject.toString());
        if(Utility.errorCodeOk(jsonObject)){
            list.remove(position);
            this.notifyDataSetChanged();
            if(list.size()<1) activity.addHeader();
        }else{
            onErrorResponse(null);
        }
    }


    //点击查看大图
    private void showPhotoDialog(String url){
        final Dialog dialog = new Dialog(activity, R.style.showPhoto);
        View view = LayoutInflater.from(activity).inflate(R.layout.engineer_show_photo_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.engineer_show_photo_dialog_id);
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (dialog.isShowing() && !activity.isFinishing()){
                    dialog.dismiss();
                }
            }
        });
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.loading, R.mipmap.loading);
        loader.get(url, imageListener);
        dialog.setContentView(view);
        dialog.show();
    }

    private class ShowBigListener implements View.OnClickListener{

        private String url;

        public ShowBigListener(String url){
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            showPhotoDialog(url);
        }
    }

}

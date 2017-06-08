package com.ceyu.carsteward.self.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.self.bean.SelfMechanicBean;
import com.ceyu.carsteward.self.bean.SelfMechanicComments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ceyu.carsteward.R.id.self_mechanic_pic;

/**
 * Created by chen on 15/9/17.
 */
public class SelfMechanicAdapter extends BaseAdapter {

    private Context context;
    private List<SelfMechanicBean> mechanicBeans;
    private CheImageLoader imageLoader;
    private OnCommentClickListener commentClickListener;
    private OnImageClickListener imageClickListener;
    private String defaultID = "";

    public SelfMechanicAdapter(Context cnt) {
        this.context = cnt;
        mechanicBeans = new ArrayList<>();
        imageLoader = new CheImageLoader(Volley.newRequestQueue(context), context);
    }

    public void setData(List<SelfMechanicBean> beans, String id) {
        if (beans != null) {
            mechanicBeans.addAll(beans);
            this.defaultID = id;
        }
    }

    public void setDefaultID(String id){
        this.defaultID = id;
    }

    @Override
    public int getCount() {
        return mechanicBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mechanicBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.self_mechanic_item_layout, null);
            RoundCornerImageView imageView = (RoundCornerImageView) convertView.findViewById(R.id.self_mechanic_image);
            TextView nameView = (TextView) convertView.findViewById(R.id.self_mechanic_name);
            TextView detailView = (TextView) convertView.findViewById(R.id.self_mechanic_comment);
            ImageView choiceView = (ImageView) convertView.findViewById(R.id.self_mechanic_choice);
            TextView modelView = (TextView) convertView.findViewById(R.id.self_mechanic_mode);
            TextView yearView = (TextView) convertView.findViewById(R.id.self_mechanic_year);
            TextView timeView = (TextView) convertView.findViewById(R.id.self_mechanic_time);
            TextView picView = (TextView) convertView.findViewById(self_mechanic_pic);
            holder = new PlaceHolder();
            holder.imageView = imageView;
            holder.nameView = nameView;
            holder.detailView = detailView;
            holder.choiceView = choiceView;
            holder.modeView = modelView;
            holder.yearView = yearView;
            holder.timeView = timeView;
            holder.picView = picView;
            convertView.setTag(holder);
        } else {
            holder = (PlaceHolder) convertView.getTag();
        }
        holder.detailView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        holder.picView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        final SelfMechanicBean bean = mechanicBeans.get(position);
        if (bean != null) {
            final ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.imageView, R.mipmap.default_img, R.mipmap.default_img);
            imageLoader.get(bean.get_pic(), imageListener);
            holder.nameView.setText(bean.get_name());
            holder.modeView.setText(String.format(Locale.US, context.getResources().getString(R.string.self_mechanic_look), bean.get_model()));
            holder.yearView.setText(String.format(Locale.US, context.getResources().getString(R.string.self_mechanic_year), bean.get_year()));
            holder.timeView.setText(String.format(Locale.US, context.getResources().getString(R.string.self_mechanic_time), bean.get_free()));
            if (bean.get_token().equals(defaultID)){
                holder.choiceView.setImageResource(R.mipmap.self_choice);
            }else {
                holder.choiceView.setImageResource(R.mipmap.self_normal);
            }
            final String[] photos = bean.get_photo();
            holder.picView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageClickListener != null){
                        imageClickListener.onImageClick(photos);
                    }
                }
            });
            holder.detailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentClickListener != null){
                        commentClickListener.onDetailClick(bean);
                    }
                }
            });
        }
        return convertView;
    }

    private class PlaceHolder {
        private RoundCornerImageView imageView;
        private TextView nameView, detailView, modeView, yearView, timeView, picView;
        private ImageView choiceView;
    }

    public interface OnCommentClickListener{
        void onDetailClick(SelfMechanicBean bean);
    }

    public interface OnImageClickListener{
        void onImageClick(String[] res);
    }

    public void setOnCommentClickListener(OnCommentClickListener l){
        this.commentClickListener = l;
    }

    public void setOnImageClickListener(OnImageClickListener l){
        this.imageClickListener = l;
    }
}

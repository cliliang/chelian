package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.engineer.bean.CommentContent;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/5.
 */
public class EngineerCommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CommentContent> contents;
    public EngineerCommentAdapter(Context cnt){
        this.context = cnt;
        contents = new ArrayList<>();
    }

    public void setData(ArrayList<CommentContent> cnt){
        this.contents = cnt;
    }

    @Override
    public int getCount() {
        return contents.size();
    }

    @Override
    public Object getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.engineer_comment_item_layout, null);
            TextView userView = (TextView) convertView.findViewById(R.id.engineer_comment_user_id);
            RatingBar ratingView = (RatingBar) convertView.findViewById(R.id.engineer_comment_rating);
            TextView descView = (TextView) convertView.findViewById(R.id.engineer_comment_desc_id);
            placeHolder = new PlaceHolder();
            placeHolder.userView = userView;
            placeHolder.ratingBar = ratingView;
            placeHolder.descView = descView;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        CommentContent content = contents.get(position);
        placeHolder.userView.setText(content.get_user());
        placeHolder.ratingBar.setRating(content.get_assess());
        placeHolder.descView.setText(content.get_info());
        return convertView;
    }

    private class PlaceHolder{
        private TextView userView, descView;
        private RatingBar ratingBar;
    }
}

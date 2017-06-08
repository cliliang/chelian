package com.ceyu.carsteward.engineer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.ui.views.CircleHeadImageView;
import com.ceyu.carsteward.engineer.bean.OnlineEngineerInfo;

import java.util.ArrayList;

/**
 * Created by chen on 15/6/12.
 */
public class OnlineListAdapter extends ArrayAdapter<OnlineEngineerInfo> {

    private Context mContext;
    private ArrayList<OnlineEngineerInfo> list;
    private CheImageLoader imageLoader;
    private RequestQueue requestQueue;
    public OnlineListAdapter(Context context) {
        super(context, R.layout.online_engineer_item_view);
        mContext = context;
        requestQueue = Volley.newRequestQueue(mContext);
        imageLoader = new CheImageLoader(requestQueue, mContext);
        list = new ArrayList<>();
    }

    public void setData(ArrayList<OnlineEngineerInfo> l){
        if (l != null){
            list = l;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceHolder placeHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.online_engineer_item_view, parent, false);
            CircleHeadImageView headView = (CircleHeadImageView) convertView.findViewById(R.id.online_engineer_head_id);
            TextView nameView = (TextView) convertView.findViewById(R.id.online_engineer_name_id);
            TextView boardview = (TextView) convertView.findViewById(R.id.online_engineer_model_id);
            placeHolder = new PlaceHolder();
            placeHolder.nameView = nameView;
            placeHolder.imageView = headView;
            placeHolder.modelView = boardview;
            convertView.setTag(placeHolder);
        }else {
            placeHolder = (PlaceHolder) convertView.getTag();
        }
        OnlineEngineerInfo info = list.get(position);
        placeHolder.nameView.setText(info.get_name());
        placeHolder.modelView.setText(info.get_model());
        if (!StringUtils.isEmpty(info.get_pic())){
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(placeHolder.imageView, R.mipmap.default_head, R.mipmap.default_head);
            imageLoader.get(info.get_pic(), imageListener);
        }
        return convertView;
    }

    private class PlaceHolder{
        private CircleHeadImageView imageView;
        private TextView nameView, modelView;
    }
}

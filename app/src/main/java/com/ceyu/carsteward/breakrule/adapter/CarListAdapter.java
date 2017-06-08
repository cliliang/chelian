package com.ceyu.carsteward.breakrule.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.car.bean.CarInfoBean;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/6/30.
 */
public class CarListAdapter extends BaseAdapter{

    private List<CarInfoBean> cars;
    private Context mContext;
    private ImageLoader imageLoader;

    public CarListAdapter(List<CarInfoBean> list, Context context){
        this.cars = list;
        this.mContext = context;
        AppContext appContext = (AppContext)mContext.getApplicationContext();
        imageLoader = new CheImageLoader(appContext.queue(), appContext);
    }

    @Override
    public int getCount() {
        return cars==null?0:cars.size();
    }

    @Override
    public Object getItem(int position) {

        return cars == null?null:cars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.breakrule_carlist_item, null);
            holder = new ViewHolder();
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_breakrules_carlist_item_branicon);
            holder.tvBrand = (TextView) convertView.findViewById(R.id.tv_breakrules_carlist_item_brand);
            holder.tvAddInfo = (TextView) convertView.findViewById(R.id.tv_breakrules_carlist_item_bottomtv0);
            holder.tvCarnum = (TextView) convertView.findViewById(R.id.tv_breakrules_carlist_item_carnum);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        CarInfoBean car = cars.get(position);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(holder.ivIcon, R.mipmap.icon_my, R.mipmap.icon_my);
        imageLoader.get(car.get_modelPic(), imageListener);
        holder.tvBrand.setText(car.getBrandInfoBean().get_brandName() + " " + car.getBrandInfoBean().get_modelName());
        holder.tvCarnum.setText(car.get_plate());
        holder.tvAddInfo.setVisibility(TextUtils.isEmpty(car.get_frame())?View.VISIBLE:View.INVISIBLE);
        return convertView;
    }

}
class ViewHolder{
    TextView tvBrand, tvAddInfo, tvCarnum;
    ImageView ivIcon;
}

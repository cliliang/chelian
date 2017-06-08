package com.ceyu.carsteward.packet.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;
import com.ceyu.carsteward.packet.router.PacketRouter;
import com.ceyu.carsteward.packet.router.PacketUI;
import com.ceyu.carsteward.user.bean.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by chen on 15/6/8.
 */
public class TakeRedPacketActivity extends BaseActivity {

    private Context mContext;
    private TextView shareView, packetview;
    private ImageView topImage, bottomImage;
    private ProgressDialog progressDialog;
    private CheImageLoader imageLoader;
    private ScrollView mainScroll;
    private User user;
    private Button getPacketButton;
    private int screemWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_red_packet_activity_layout);
        shareView = (TextView) findViewById(R.id.home_packet_share);
        packetview = (TextView) findViewById(R.id.home_packet_my_packet);
        topImage = (ImageView) findViewById(R.id.take_red_packet_top_image);
        bottomImage = (ImageView) findViewById(R.id.take_red_packet_bottom_image);
        getPacketButton = (Button) findViewById(R.id.take_red_packet_button_id);
        mainScroll = (ScrollView) findViewById(R.id.take_red_main_scroll);
        packetview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PacketRouter.getInstance(mContext).showActivity(PacketUI.myPacket);
            }
        });
        getPacketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeRedPacket();
            }
        });
        getPacketButton.setEnabled(false);
        getPacketButton.setText(getResources().getString(R.string.check_my_red_packet));
        mContext = TakeRedPacketActivity.this;
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        user = appContext.getActiveUser();
        progressDialog = ProgressDialog.getInstance();
        progressDialog.show(mContext);
        imageLoader = new CheImageLoader(requestQueue, mContext);
        setTitle(R.string.take_red_packet_weekly);
        getPacketState();
        screemWidth = Utils.getScreenWidth(TakeRedPacketActivity.this);
    }

    private void getPacketState(){

        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(PacketURLs.homePacket, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                dismissDialog();
                Log.i("chen", object.toString());
                if (object.has(ResponseCode.ResponseState)){
                    String info = object.optString(ResponseCode.ResponseInfo);
                    UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                }else {
                    mainScroll.setVisibility(View.VISIBLE);
                    RedState redState = new RedState();
                    redState.fromObject(object);
                    if (redState.is_canTake()){
                        getPacketButton.setEnabled(true);
                        getPacketButton.setText(getResources().getString(R.string.check_my_red_packet));
                    }else {
                        getPacketButton.setEnabled(false);
                        getPacketButton.setText(getResources().getString(R.string.week_red_packet_have_take));
                    }
                    ViewGroup.LayoutParams params = topImage.getLayoutParams();
                    params.width = screemWidth;
                    params.height = screemWidth;
                    topImage.setLayoutParams(params);
                    ImageLoader.ImageListener imageListener1 = ImageLoader.getImageListener(topImage, R.mipmap.default_img, R.mipmap.default_img);
                    imageLoader.get(redState.get_imageTop(), imageListener1);
//                    ImageLoader.ImageListener imageListener2 = ImageLoader.getImageListener(bottomImage, R.mipmap.redenvelopedown, R.mipmap.redenvelopedown);
                    ImageLoader.ImageListener imageListener2 = new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                            if (imageContainer != null && imageContainer.getBitmap() != null){
                                Bitmap bitmap = imageContainer.getBitmap();
                                int bitmapWidth = bitmap.getWidth();
                                int bitmapHeight = bitmap.getHeight();
                                int imageHeight = screemWidth * bitmapHeight / bitmapWidth;
                                ViewGroup.LayoutParams layoutParams = bottomImage.getLayoutParams();
                                layoutParams.width = screemWidth;
                                layoutParams.height = imageHeight;
                                bottomImage.setLayoutParams(layoutParams);
                                bottomImage.setImageBitmap(bitmap);
                            }else {
                                bottomImage.setImageResource(R.mipmap.redenvelopedown);
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    };
                    imageLoader.get(redState.get_imageBottom(), imageListener2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void takeRedPacket(){
        progressDialog.show(mContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("token", user.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(PacketURLs.getPacket, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                Log.i("chen", "object:" + object.toString());
                dismissDialog();
                if (object.has(ResponseCode.ResponseState)){
                    String state = object.optString(ResponseCode.ResponseState);
                    if (state.equals(ResponseCode.ResponseOK)){
                        getPacketButton.setEnabled(false);
                        float num = (float) object.optDouble("num");
                        getPacketButton.setText(String.format(Locale.US, getResources().getString(R.string.have_take_red_packet), num));
                    }else {
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dismissDialog();
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private class RedState{
        private String _imageTop;
        private String _imageBottom;
        private boolean _canTake;

        public String get_imageTop() {
            return _imageTop;
        }

        public void set_imageTop(String _imageTop) {
            this._imageTop = _imageTop;
        }

        public String get_imageBottom() {
            return _imageBottom;
        }

        public void set_imageBottom(String _imageBottom) {
            this._imageBottom = _imageBottom;
        }

        public boolean is_canTake() {
            return _canTake;
        }

        public void set_canTake(boolean _canTake) {
            this._canTake = _canTake;
        }

        public void fromObject(JSONObject object){
            if (object == null){
                return;
            }
            String index = "url1";
            if (object.has(index)){
                set_imageTop(object.optString(index));
            }
            index = "url2";
            if (object.has(index)){
                set_imageBottom(object.optString(index));
            }
            index = "getState";
            if (object.has(index)){
                set_canTake(object.optInt(index) == 1);
            }
        }
    }
    private void dismissDialog(){
        if (progressDialog.isShowing() && !isFinishing()){
            progressDialog.dismiss();
        }
    }
}

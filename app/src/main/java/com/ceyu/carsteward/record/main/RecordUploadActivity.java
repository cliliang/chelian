package com.ceyu.carsteward.record.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.Config;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.tools.ImagePickException;
import com.ceyu.carsteward.common.tools.ImagePicker;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utility;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.BaseActivity;
import com.ceyu.carsteward.common.ui.views.ProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/30.
 * 上传养车记录
 */
public class RecordUploadActivity extends BaseActivity implements View.OnClickListener,Response.Listener<JSONObject>, Response.ErrorListener{

    private ImagePicker picker;
    private int position = -1;
    private String[] images;
    private ImageView[] imageViews;
    //正在处理图片，不允许继续拍照
    private boolean doingPic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_pickimage);
        setTitle(R.string.home_block_string_record);
        picker = new ImagePicker(this);
        images = new String[3];
        initViews();
    }

    private void initViews(){
        setRightTitle(getString(R.string.upload), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doUpload();
            }
        });
        imageViews = new ImageView[3];
        imageViews[0] = (ImageView) findViewById(R.id.iv_record_pickimage_picker0);
        imageViews[1] = (ImageView) findViewById(R.id.iv_record_pickimage_picker1);
        imageViews[2] = (ImageView) findViewById(R.id.iv_record_pickimage_picker2);
        for(View view:imageViews){
            view.setOnClickListener(this);
        }

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_record_pickimage_picker0:
                showImagePickerDialog(0);
                break;
            case R.id.iv_record_pickimage_picker1:
                showImagePickerDialog(1);
                break;
            case R.id.iv_record_pickimage_picker2:
                showImagePickerDialog(2);
                break;
        }
    }

    private void showImagePickerDialog(int position){
        if(this.position != position && doingPic){  //选择的不是同一个位置并且上次一拍照还未处理完成
            UIHelper.ToastMessage(this, getString(R.string.record_handlingimage));
        }else {
            this.position = position;
            picker.showDialog(getString(R.string.record_chooseimage));
        }
    }

    //拍照返回的data可能为null
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(resultCode == Activity.RESULT_OK){
            UIHelper.ToastMessage(this, R.string.record_handlingimage);
            ProgressDialog.getInstance().show(this,true);
            doingPic = true;
            imageViews[position].setImageResource(R.mipmap.loading);
            new Thread(){
                @Override
                public void run() {
                    try {
                        doPhoto(picker.onImageResult(requestCode, data));
                    }catch (ImagePickException e){
                        RecordUploadActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UIHelper.ToastMessage(RecordUploadActivity.this, getString(R.string.record_pickimagefale));
                            }
                        });
                    }
                }
            }.start();
        }else if(resultCode!=Activity.RESULT_CANCELED){
            UIHelper.ToastMessage(this, getString(R.string.record_pickimagefale));
        }
    }

    private void doPhoto(final Bitmap bitmap){
        if(bitmap!=null){
            images[position] = Utils.bitmapToString(bitmap);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialog.getInstance().dismiss();
                    imageViews[position].setImageBitmap(bitmap);
                    UIHelper.ToastMessage(RecordUploadActivity.this, R.string.record_handledimage);
                    doingPic = false;
                }
            });
        }else{
            this.onActivityResult(Activity.RESULT_CANCELED, Activity.RESULT_CANCELED, null);
        }
    }


    /*
    token: "浏览者ID",
    info: "备注信息",
    pic1: "图片",
    ext1: "扩展名",
    pic2: "图片",
    ext2: "扩展名",
    pic3: "图片",
    ext3: "扩展名"
    */
    private void doUpload(){
        EditText et = (EditText)findViewById(R.id.et_record_pickimage_editbox);
        if(TextUtils.isEmpty(et.getText().toString())){
            UIHelper.ToastMessage(this, R.string.record_pleaseedit);
            return;
        }

        ProgressDialog.getInstance().show(RecordUploadActivity.this, false);

        AppContext appContext = (AppContext)getApplicationContext();
        Map<String, String> params = new HashMap<>();
        params.put("token", appContext.getActiveUser().getToken());
        params.put("info", et.getText().toString());
        for (int i = 0; i < images.length; i++) {   //脚标0~2，图片pic1~pic3
            if (!TextUtils.isEmpty(images[i])){ //图片不为空
                params.put("pic" + (i + 1), images[i]); //图片base64转码后字符串
                params.put("ext" + (i + 1), "jpg");     //文件后缀名
            }
        }
        CheJSONObjectRequest request = new CheJSONObjectRequest(RecordURLs.uploadImage, params, this, this);
        appContext.queue().add(request);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        ProgressDialog.getInstance().dismiss();
        Utility.LogUtils.ex(volleyError);
        UIHelper.ToastMessage(this, getString(R.string.record_uploadimagefailed));
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        ProgressDialog.getInstance().dismiss();
        if(Utility.errorCodeOk(jsonObject)) {
            UIHelper.ToastMessage(this, R.string.record_uploadsuccess);
            finish();
        }
        else Utility.errorCodeToaster(jsonObject, this);
    }

}

//class ImageUpload implements Response.Listener<JSONObject>, Response.ErrorListener{
//
//    private int mPostion;
//    private JSONListener mListener;
//
//    public ImageUpload(int position, JSONListener listener){
//        this.mPostion = position;
//        this.mListener = listener;
//    }
//
//    @Override
//    public void onErrorResponse(VolleyError volleyError) {
//        mListener.onErrorResponse(volleyError, mPostion);
//    }
//
//    @Override
//    public void onResponse(JSONObject jsonObject) {
//        mListener.onResponse(jsonObject, mPostion);
//    }
//}

//interface JSONListener{
//    void onResponse(JSONObject jsonObject, int postion);
//    void onErrorResponse(VolleyError volleyError, int postion);
//}

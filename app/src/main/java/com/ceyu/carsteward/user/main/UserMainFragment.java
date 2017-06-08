package com.ceyu.carsteward.user.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.app.MainRouter;
import com.ceyu.carsteward.car.router.CarUI;
import com.ceyu.carsteward.common.db.CheDBM;
import com.ceyu.carsteward.common.module.ModuleNames;
import com.ceyu.carsteward.common.net.ResponseCode;
import com.ceyu.carsteward.common.net.volley.CheJSONObjectRequest;
import com.ceyu.carsteward.common.net.volley.CheImageLoader;
import com.ceyu.carsteward.common.net.volley.HandleVolleyError;
import com.ceyu.carsteward.common.tools.ErrorCode;
import com.ceyu.carsteward.common.tools.ImagePickException;
import com.ceyu.carsteward.common.tools.ImagePicker;
import com.ceyu.carsteward.common.tools.ImageUtils;
import com.ceyu.carsteward.common.tools.StringUtils;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.tools.Utils;
import com.ceyu.carsteward.common.ui.AppManager;
import com.ceyu.carsteward.common.ui.BaseFragment;
import com.ceyu.carsteward.common.ui.views.RoundCornerImageView;
import com.ceyu.carsteward.common.views.IconTextView;
import com.ceyu.carsteward.engineer.router.EngineerUI;
import com.ceyu.carsteward.main.ui.MainActivity;
import com.ceyu.carsteward.main.ui.MainURLs;
import com.ceyu.carsteward.maintain.router.MaintainUI;
import com.ceyu.carsteward.packet.router.PacketUI;
import com.ceyu.carsteward.points.PointsURLs;
import com.ceyu.carsteward.points.router.PointsUI;
import com.ceyu.carsteward.user.bean.User;
import com.ceyu.carsteward.user.router.UserRouter;
import com.ceyu.carsteward.user.router.UserUI;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by chen on 15/6/1.
 */
public class UserMainFragment extends BaseFragment {

    private RoundCornerImageView headImageView;
    private EditText nameView;
    private ImageView inputView;
    private IconTextView mCarView, mPacketView, mMoneyView, mOrderView, mEngineerView, mAboutBang, mExitView;
    private TextView qianDaoButton;
    private Context mContext;
    private CheImageLoader imageLoader;
    private String headString;
    private User activeUser;
    private CheDBM dbm;
    private AppContext appContext;
    private RequestQueue requestQueue;
    //照相保存路径
    private File cameraFile;
    private boolean enable = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_main_fragment_layout, null);
        initView(view);
        mContext = getActivity();
        requestQueue = Volley.newRequestQueue(mContext);
        imageLoader = new CheImageLoader(requestQueue, mContext);
        appContext = (AppContext) mContext.getApplicationContext();
        return view;
    }

    @Override
    public void onResume() {
        activeUser = appContext.getActiveUser();
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(headImageView, R.mipmap.icon_logo, R.mipmap.icon_logo);
        imageLoader.get(activeUser.getUserPic(), imageListener);
        String nameString = activeUser.getName();
        if (StringUtils.isEmpty(nameString)){
            nameView.setText(activeUser.getPhoneNumber());
        }else {
            nameView.setText(nameString);
        }
        setViewData();
        checkPointState();
        super.onResume();
    }

    private void initView(View rootView){
        headImageView = (RoundCornerImageView) rootView.findViewById(R.id.user_home_head_image);
        headImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoiceDialog();
            }
        });
        nameView = (EditText) rootView.findViewById(R.id.user_home_name);
        nameView.setEnabled(false);
        inputView = (ImageView) rootView.findViewById(R.id.user_input_name);
        inputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showModifyDialog();
            }
        });
        mCarView = (IconTextView) rootView.findViewById(R.id.user_home_my_car);
        mPacketView = (IconTextView) rootView.findViewById(R.id.user_home_my_packet);
        mMoneyView = (IconTextView) rootView.findViewById(R.id.user_home_my_money);
        mOrderView = (IconTextView) rootView.findViewById(R.id.user_home_my_order);
        mEngineerView = (IconTextView) rootView.findViewById(R.id.user_home_my_engineer);
        mAboutBang = (IconTextView) rootView.findViewById(R.id.user_home_about_bang);
        mExitView = (IconTextView) rootView.findViewById(R.id.user_home_exit);
        qianDaoButton = (TextView) rootView.findViewById(R.id.user_qian_dao_button);
        if (enable){
            qianDaoButton.setEnabled(true);
        }else {
            qianDaoButton.setEnabled(false);
        }
    }

    private void setViewData(){

        mCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Car, CarUI.carOfMine);
            }
        });
        mPacketView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Packet, PacketUI.myPacket);
            }
        });
        mMoneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Points, PointsUI.firstPage);
            }
        });
        mOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Maintain, MaintainUI.getOrderList);
            }
        });
        mEngineerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.Engineer, EngineerUI.engineerOfMine);
            }
        });
        mAboutBang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainRouter.getInstance(mContext).showActivity(ModuleNames.User, UserUI.aboutBang);
            }
        });
        mExitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage(R.string.exit_current_user)
                        .setNegativeButton(R.string.exit_bang_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                CheDBM dbm = CheDBM.getInstance(mContext);
                                dbm.quitAccount();
                                UserRouter.getInstance(mContext).showActivity(UserUI.userLogin);
                                AppManager.getAppManager().finishActivity(MainActivity.class);
                            }
                        })
                        .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        qianDaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enable = false;
                qianDaoButton.setEnabled(false);
                setPointState();
            }
        });
    }

    private void showModifyDialog(){
        final Dialog dialog = new Dialog(mContext, R.style.NormalDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.modify_nick_name_dialog_layout, null);
        dialog.setContentView(view);
        final EditText inputEdit = (EditText) view.findViewById(R.id.modify_nick_name_input);
        view.findViewById(R.id.modify_nick_name_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.modify_nick_name_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = inputEdit.getText().toString();
                if (StringUtils.isEmpty(inputString)) {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.please_input_nick_name));
                } else {
                    dialog.dismiss();
                    nameView.setText(inputString);
                    modifyNickName(inputString);
                }

            }
        });
        if (!dialog.isShowing() && !getActivity().isFinishing()){
            dialog.show();
        }
        dialog.getWindow().setLayout(Utils.getScreenWidth(getActivity()) - 100, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void checkPointState(){
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(PointsURLs.getPointState, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String state = response.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals("yes")){
                    enable = true;
                    qianDaoButton.setEnabled(true);
                    qianDaoButton.setText(getResources().getString(R.string.qian_dao_no));
                }else {
                    enable = false;
                    qianDaoButton.setEnabled(false);
                    qianDaoButton.setText(getResources().getString(R.string.qian_dao_yes));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void setPointState(){
        HashMap<String, String> map = new HashMap<>();
        map.put("token", activeUser.getToken());
        CheJSONObjectRequest request = new CheJSONObjectRequest(PointsURLs.changePointState, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String state = response.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    int point = response.optInt("integral");
                    String res = String.format(Locale.US, getResources().getString(R.string.qian_dao_success), point);
                    UIHelper.ToastMessage(mContext, res);
                    qianDaoButton.setEnabled(false);
                    qianDaoButton.setText(getResources().getString(R.string.qian_dao_yes));
                    enable = false;
                }else {
                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.qian_dao_fail));
                    qianDaoButton.setEnabled(true);
                    enable = true;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                qianDaoButton.setEnabled(true);
                enable = true;
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void modifyNickName(final String name){
        HashMap<String, String> map = new HashMap<>();
        map.put("class", "User_Name");
        map.put("token", activeUser.getToken());
        map.put("value", name);
        CheJSONObjectRequest request = new CheJSONObjectRequest(MainURLs.modifyName, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state)){
                    if (state.equals(ResponseCode.ResponseError)){
                        String info = object.optString(ResponseCode.ResponseInfo);
                        UIHelper.ToastMessage(mContext, ErrorCode.getInstance(mContext).getErrorCode(info));
                    }else if (state.equals(ResponseCode.ResponseOK)){
                        activeUser.setName(name);
                        updateUser();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HandleVolleyError.showErrorMessage(mContext, volleyError);
            }
        });
        requestQueue.add(request);
        requestQueue.start();

    }

    private void showChoiceDialog() {
        final String[] items ={
                mContext.getString(R.string.gallery),
                mContext.getString(R.string.camera)
        };
        new AlertDialog.Builder(mContext)
                .setTitle(getResources().getString(R.string.choice_head_image))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// 本地相册
                                Intent intentGalary;
                                if (Build.VERSION.SDK_INT < 19) {
                                    intentGalary = new Intent(Intent.ACTION_GET_CONTENT);
                                    intentGalary.setType("image/*");

                                } else {
                                    intentGalary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                }
                                startActivityForResult(intentGalary, ImageUtils.HEAD_GALARY_TAG);
                                break;
                            case 1:// 打开相机
                                String sdState = Environment.getExternalStorageState();
                                if (sdState.equals(Environment.MEDIA_MOUNTED)) {
                                    String path = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                                            + "/"
                                            + ("icar" + System.currentTimeMillis())
                                            + ".jpg";
                                    cameraFile = new File(path);
                                    cameraFile.getParentFile().mkdirs();

                                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
                                    intentCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                                    startActivityForResult(intentCamera, ImageUtils.HEAD_CAMERA_TAG);
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.nostore), Toast.LENGTH_LONG).show();
                                }
                                break;
                        }
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), null).show();

    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageUtils.HEAD_CAMERA_TAG || requestCode == ImageUtils.HEAD_GALARY_TAG){
            if (resultCode == Activity.RESULT_OK){
                headImageView.setImageResource(R.mipmap.loading);
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            doPhoto(onImageResult(requestCode, data));
                            doUpload();
                        }catch (ImagePickException e){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UIHelper.ToastMessage(mContext, getString(R.string.record_pickimagefale));
                                }
                            });
                        }
                    }
                }.start();
            }else if(resultCode!=Activity.RESULT_CANCELED){
                UIHelper.ToastMessage(mContext, getString(R.string.record_pickimagefale));
            }
        }
    }

    private void doUpload(){
        if (StringUtils.isEmpty(headString)){
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", activeUser.getToken());
        params.put("pic", headString); //图片base64转码后字符串
        params.put("ext", "png");     //文件后缀名
        CheJSONObjectRequest request = new CheJSONObjectRequest(MainURLs.modifyPic, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                String state = object.optString(ResponseCode.ResponseState);
                if (!StringUtils.isEmpty(state) && state.equals(ResponseCode.ResponseOK)){
                    activeUser.setUserPic(object.optString("url"));
                    updateUser();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIHelper.ToastMessage(mContext, getString(R.string.record_uploadimagefailed));
                    }
                });
            }
        });
        requestQueue.add(request);
        requestQueue.start();
    }

    private void updateUser(){
        if (dbm == null){
            dbm = CheDBM.getInstance(mContext);
        }
        dbm.updateUser(activeUser);
    }

    private void doPhoto(final Bitmap bitmap){
        if(bitmap!=null){
            headString = Utils.bitmapToString(bitmap);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    headImageView.setImageBitmap(bitmap);
                }
            });
        }else{
            this.onActivityResult(Activity.RESULT_CANCELED, Activity.RESULT_CANCELED, null);
        }
    }

    public Bitmap onImageResult(int requestCode, Intent data) throws ImagePickException {
        Bitmap bitmap = null;
        switch(requestCode){
            case ImageUtils.HEAD_GALARY_TAG://从相册取图片，有些手机有异常情况，请注意
                String imgPath = "";
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        imgPath = ImageUtils.getPictruePath(mContext, selectedImage);
                    }
                }
                if(!TextUtils.isEmpty(imgPath))  {
                    bitmap = ImageUtils.getRotatedImage(imgPath);
                }else{
                    throw new ImagePickException();
                }
                break;
            case ImageUtils.HEAD_CAMERA_TAG:
                if (cameraFile != null && cameraFile.exists())
                    bitmap = ImageUtils.getRotatedImage(cameraFile.getAbsolutePath());
                else if(data!=null){
                    Uri uri = data.getData();
                    if(uri == null){
                        Bundle bundle = data.getExtras();
                        if(bundle != null){
                            bitmap = (Bitmap) bundle.get("data"); // get
                        }
                    }
                }else{
                    throw new ImagePickException();
                }
                break;
        }
        return ImageUtils.compressImage(bitmap);
    }



}

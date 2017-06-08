package com.ceyu.carsteward.common.tools;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.widget.Toast;

import com.ceyu.carsteward.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/6/30.
 */
public class ImagePicker
{

    private Activity mContext;

    public ImagePicker(Activity context){
        this.mContext = context;
    }

    //照相保存路径
    private File cameraFile;

    public void showDialog(final String title) {
        final String[] items ={
                mContext.getString(R.string.gallery),
                mContext.getString(R.string.camera)
        };
        new AlertDialog.Builder(mContext)
                .setTitle(title)
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
                                mContext.startActivityForResult(intentGalary, ImageUtils.DEFAULT_GALARY_TAG);
                                break;
                            case 1:// 打开相机
                                String sdState = Environment
                                        .getExternalStorageState();
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

                                    mContext.startActivityForResult(intentCamera, ImageUtils.DEFAULT_CAMERA_TAG);
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.nostore), Toast.LENGTH_LONG).show();
                                }
                                break;
                        }
                    }
                })
                .setNegativeButton(mContext.getString(R.string.cancel), null).show();

    }

    public Bitmap onImageResult(int requestCode, Intent data) throws ImagePickException{
        Bitmap bitmap = null;
        switch(requestCode){
            case ImageUtils.DEFAULT_GALARY_TAG://从相册取图片，有些手机有异常情况，请注意
                String imgPath = "";
                if (data != null){
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        imgPath = getPictruePath(selectedImage);
                    }
                    if(!TextUtils.isEmpty(imgPath))  {
                        bitmap = getRotatedImage(imgPath);
                    }else{
                        throw new ImagePickException();
                    }
                }

                break;
            case ImageUtils.DEFAULT_CAMERA_TAG:
                if (cameraFile != null && cameraFile.exists())
                    bitmap = getRotatedImage(cameraFile.getAbsolutePath());
                else{
                    if (data != null){
                        Uri uri = data.getData();
                        if(uri == null){
                            Bundle bundle = data.getExtras();
                            if(bundle != null){
                                bitmap = (Bitmap) bundle.get("data"); // get
                            }
                        }
                    }
                }
                break;
        }
        return compressImage(bitmap);
    }

    private String getPictruePath(Uri selectedImage) throws ImagePickException {
        Cursor cursor = mContext.getContentResolver().query(selectedImage, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;
            if (picturePath == null || picturePath.equals("null")) {
                throw new ImagePickException();
            }
            return picturePath;
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                throw new ImagePickException();
            }
            return file.getAbsolutePath();
        }

    }

    //获得调整过方向并压缩过的bitmap对象
    public Bitmap getRotatedImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return rotaingImageView(rotateDegree(srcPath), compressImage(bitmap));//压缩好比例大小后再进行质量压缩  , 返回调整好方向的图片
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    private Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        if (bitmap == null){
            return null;
        }
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    private int rotateDegree(String path){
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    //重载的方法, 自定义压缩图片大小限制
    public static Bitmap compressImage(Bitmap image) {
        if (image == null){
            return null;
        }
        //如果未传文件大小, 则filesize为-1, 改为128kb默认值
        int imageFileSize = 256;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>imageFileSize && options>0) {  //循环判断如果压缩后图片是否大于imageFileSize,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= options<10? 3:10;//option大于10时每次都减少10,否则减少3
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}

package com.ceyu.carsteward.common.tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chen on 15/8/25.
 */
public class FileUtils {
    private Context mContext;
    private static FileUtils instance;

    private FileUtils(Context context) {
        mContext = context;
    }

    public static FileUtils getInstance(Context context) {
        if (instance == null) {
            instance = new FileUtils(context);
        }
        return instance;
    }


    /**
     * 获取储存Image的目录
     *
     * @return
     */
    private String getStorageDirectory() throws NullPointerException {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param fileName
     * @param bitmap
     * @throws IOException
     */
    public void putBitmap(final String fileName, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bitmap == null) {
                    return;
                }
                if (isFileExists(fileName)) {
                    return;
                }
                String path = getStorageDirectory();
                File folderFile = new File(path);
                if (!folderFile.exists()) {
                    folderFile.mkdir();
                }
                File file = new File(path + File.separator + fileName);
                try {
                    if (file.createNewFile()) {
                        FileOutputStream fos = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public String putPublishBitmap(String fileName, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return null;
        }
        deleteFile(fileName);
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        String filePath = path + File.separator + fileName;
        File file = new File(filePath);
        if (file.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }
        return filePath;
    }

    /**
     * 从手机或者sd卡获取Bitmap
     *
     * @param fileName
     * @return
     */

    public Bitmap getBitmap(String fileName) {
        if (isFileExists(fileName)) {
            return getBitmap(new File(fileName), 150, 150);
        } else {
            return null;
        }
    }

    public Bitmap getPublishBitmap(String path) {
        File file = new File(path);
        if (file.exists()) {
            return BitmapFactory.decodeFile(path);
        } else {
            return null;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    private boolean isFileExists(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     *
     * @param fileName
     * @return
     */
    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }


    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    private void deleteFile(String fileName) {
        File dirFile = new File(getStorageDirectory() + File.separator + fileName);
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
        dirFile.delete();
    }

    public Bitmap getBitmap(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            try {
                if (width > 0 && height > 0) {
                    opts = new BitmapFactory.Options();            //设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                    opts.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(dst.getPath(), opts);
                    // 计算图片缩放比例
                    final int minSideLength = Math.min(width, height);
                    opts.inSampleSize = computeSampleSize(opts, minSideLength,
                            width * height);            //这里一定要将其设置回false，因为之前我们将其设置成了true
                    opts.inJustDecodeBounds = false;
                    opts.inInputShareable = true;
                    opts.inPurgeable = true;
                }
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}

package com.ceyu.carsteward.common.net.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.ceyu.carsteward.common.tools.FileUtils;

import java.io.IOException;

/**
 * Created by chen on 15/8/25.
 */
public class CheImageCache implements ImageLoader.ImageCache {

    private static CheImageCache lruImageCache; //单例
    private LruCache<String, Bitmap> memoryCache; //硬引用
    private FileUtils storageCache; // 外部文件缓存

    public static CheImageCache getInstance(Context context) {
        if (lruImageCache == null) {
            lruImageCache = new CheImageCache(context);
        }
        return lruImageCache;
    }

    private CheImageCache(Context context) {
        /**
         * 初始化硬引用
         */
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

        };

        /**
         * 初始化外部文件缓存
         */
        storageCache =  FileUtils.getInstance(context);
    }

    @Override
    public Bitmap getBitmap(String s) {
        s = s.replaceAll("[^\\w]", "");
        //从缓存中获取bitmap
        Bitmap memoryBitmap = memoryCache.get(s);
        if (memoryBitmap != null){
            return memoryBitmap;
        }
        //从外部文件读取
        Bitmap storageBitmap = storageCache.getBitmap(s);
        if (storageBitmap != null) {
            memoryCache.put(s, storageBitmap); //加入到硬引用中
            return storageBitmap;
        }
        return null;
    }

    public Bitmap getBitmap(String s, boolean onlyMemory){
        s = s.replaceAll("[^\\w]", "");
        if (onlyMemory){
            Bitmap memoryBitmap = memoryCache.get(s);
            if (memoryBitmap != null){
                return memoryBitmap;
            }
        }else {
            return getBitmap(s);
        }
        return null;
    }
    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        s = s.replaceAll("[^\\w]", "");
        if (memoryCache.get(s) == null &&  bitmap != null) {
            memoryCache.put(s, bitmap);
        }
        storageCache.putBitmap(s, bitmap);
    }
}


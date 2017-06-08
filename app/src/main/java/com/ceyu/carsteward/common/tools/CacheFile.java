package com.ceyu.carsteward.common.tools;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dnurse3 on 15/4/15.
 */
public class CacheFile {
    public static final String MechanicMenu = "mechanicMenu";

    private static CacheFile instance;
    private File rootFile;

    public static CacheFile getInstance(Context cnt){
        if (instance == null){
            instance = new CacheFile(cnt);
        }
        return instance;
    }

    private CacheFile(Context context){
        rootFile = getDiskCacheDir(context);
    }

    private File getDiskCacheDir(Context context) {
        File cacheFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File file = context.getExternalCacheDir();
            if (file != null){
                cacheFile = file;
            }else {
                cacheFile = context.getCacheDir();
            }
        } else {
            cacheFile = context.getCacheDir();
        }
        return cacheFile;
    }

    private File createCache(File rootFile, String fileName){
        File cacheFile = new File(rootFile, fileName);
        if (cacheFile.exists() && cacheFile.isFile()){
            cacheFile.delete();
        }
        try {
            if (cacheFile.createNewFile()){
                return cacheFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getCacheFile(String fileName){
        File cacheFile = new File(rootFile, fileName);
        if (cacheFile.exists() && cacheFile.isFile()){
            return cacheFile;
        }
        return null;
    }

    public void writeCacheString(String content, String fileName){
        File cacheFile = createCache(rootFile, fileName);
        inputContent(cacheFile, content);
    }

    public String readCacheString(String fileName){
        String content = "";
        File cacheFile = getCacheFile(fileName);
        if (cacheFile != null){
            content = outputContent(cacheFile);
        }
        return content;
    }

    private String outputContent(File file) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            StringBuffer sb=new StringBuffer();
            try {
                while((fis.read(buf))!=-1){
                    sb.append(new String(buf));
                    buf=new byte[1024];//重新生成，避免和上次读取的数据重复
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void inputContent(File file, String content) {
        if (file != null && file.exists()){
            byte[] bt = content.getBytes();
            try {
                FileOutputStream in = new FileOutputStream(file);
                try {
                    in.write(bt, 0, bt.length);
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

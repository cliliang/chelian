package com.ceyu.carsteward.common.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.app.AppContext;
import com.ceyu.carsteward.user.bean.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TreeSet;

/**
 * Created by chen on 15/7/7.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private static final String CRASH_REPORTER_EXTENSION = ".cr";
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            public void run() {
                Looper.prepare();
                UIHelper.ToastMessage(mContext, R.string.app_error_message_toast);
                Looper.loop();
            }

        }.start();
        saveCrashInfoToFile(mContext, ex);
        return true;
    }

    private String saveCrashInfoToFile(Context context, Throwable ex) {
        final String crashReport = getCrashReport(context, ex);
        try {
            Time t = new Time("GMT+8");
            t.setToNow(); // 取得系统时间
            int date = t.year * 10000 + t.month * 100 + t.monthDay;
            int time = t.hour * 10000 + t.minute * 100 + t.second;
            String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION;

            File cr = new File(context.getFilesDir(), fileName);
            FileWriter writer = new FileWriter(cr);
            writer.write(crashReport);
            writer.close();
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing report file...", e);
        }
        return null;
    }

    public static void sendCrashReportsToServer(Context ctx) {
        String[] crFiles = getCrashReportFiles(ctx);
        if (crFiles != null && crFiles.length > 0) {
            TreeSet<String> sortedFiles = new TreeSet<>();
            sortedFiles.addAll(Arrays.asList(crFiles));
            for (String fileName : sortedFiles) {
                File cr = new File(ctx.getFilesDir(), fileName);
                String log = readFileByLines(cr);
                if (log.length() > 0) {
                    Utils.sendBug(log, ctx);
                    cr.delete();// 删除已发送的报告'
                }
            }
        }
    }

    private static String[] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    public static String readFileByLines(File file) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                stringBuilder.append(tempString);
                stringBuilder.append("\n");
                line++;
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return stringBuilder.toString();
    }

    private String getCrashReport(Context context, Throwable ex) {
        AppContext appContext = (AppContext) mContext.getApplicationContext();
        PackageInfo pinfo = appContext.getPackageInfo();
        String username = "temp";
        User user = ((AppContext) context.getApplicationContext()).getActiveUser();
        if (user != null && user.getPhoneNumber() != null) {
            username = user.getPhoneNumber();
        }
        StringBuilder exceptionStr = new StringBuilder();
        exceptionStr.append("dattime: " + String.format(Locale.CHINA, "%tc", new Date()) + "\n");
        exceptionStr.append("name: " + username + "\n");
        exceptionStr.append("appid: " + pinfo.packageName + "\n");
        exceptionStr.append(getPhoneInfo());
        exceptionStr.append("Version: " + pinfo.versionName + "("
                + pinfo.versionCode + ")\n");
        exceptionStr.append("\nException: \n" + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        if (ex.getCause() != null) {
            elements = ex.getCause().getStackTrace();

            exceptionStr.append("\n\nCaused by:\n");
            for (int i = 0; i < elements.length; i++) {
                exceptionStr.append(elements[i].toString() + "\n");
            }
        }

        return exceptionStr.toString();
    }

    public String getPhoneInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("android:");
        buffer.append(android.os.Build.VERSION.RELEASE);

        buffer.append(" manufacturer:");
        buffer.append(android.os.Build.MANUFACTURER);

        buffer.append(" model:");
        buffer.append(android.os.Build.MODEL);

        buffer.append(" brand:");
        buffer.append(android.os.Build.BRAND);

        buffer.append(" device:");
        buffer.append(android.os.Build.DEVICE);

        buffer.append(" hardware:");
        buffer.append(android.os.Build.HARDWARE);

        buffer.append(" display:");
        buffer.append(android.os.Build.DISPLAY);

        buffer.append("\n");

        return buffer.toString();
    }
}

package com.ceyu.carsteward.app;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.UIHelper;
import com.ceyu.carsteward.common.ui.AppManager;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;


public class AppException extends Exception implements UncaughtExceptionHandler {
    private static final String TAG = "AppException";

    /**
     * 定义异常类型
     */
    public final static byte TYPE_NETWORK = 0x01;
    public final static byte TYPE_SOCKET = 0x02;
    public final static byte TYPE_HTTP_CODE = 0x03;
    public final static byte TYPE_HTTP_ERROR = 0x04;
    public final static byte TYPE_XML = 0x05;
    public final static byte TYPE_IO = 0x06;
    public final static byte TYPE_RUN = 0x07;
    public final static byte TYPE_JSON = 0x08;
    public final static byte TYPE_FORBIDDEN = 0x09;
    public final static byte TYPE_PARAM = 0x0A;
    /**
     *
     */
    private static final long serialVersionUID = 8392861922148780972L;
    private final static boolean Debug = false;// 是否保存错误日志
    private byte type;
    private int code;

    /**
     * 系统默认的UncaughtException处理类
     */
    private UncaughtExceptionHandler mDefaultHandler;

    private AppException() {
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    private AppException(byte type, int code, Exception excp) {
        super(excp);
        this.type = type;
        this.code = code;
    }

    public static AppException http(int code) {
        return new AppException(TYPE_HTTP_CODE, code, null);
    }

    public static AppException http(Exception e) {
        return new AppException(TYPE_HTTP_ERROR, 0, e);
    }

    public static AppException socket(Exception e) {
        return new AppException(TYPE_SOCKET, 0, e);
    }

    public static AppException json(Exception e) {
        return new AppException(TYPE_JSON, 0, e);
    }

    public static AppException forbidden(Exception e) {
        return new AppException(TYPE_FORBIDDEN, 0, e);
    }

    public static AppException param(Exception e) {
        return new AppException(TYPE_PARAM, 0, e);
    }

    public static AppException io(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if (e instanceof IOException) {
            return new AppException(TYPE_IO, 0, e);
        }
        return run(e);
    }

    public static AppException xml(Exception e) {
        return new AppException(TYPE_XML, 0, e);
    }

    public static AppException network(Exception e) {
        if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return new AppException(TYPE_NETWORK, 0, e);
        } else if (e instanceof SocketException) {
            return socket(e);
        }
        return http(e);
    }

    public static AppException run(Exception e) {
        return new AppException(TYPE_RUN, 0, e);
    }

    public int getCode() {
        return this.code;
    }

    public int getType() {
        return this.type;
    }

    /**
     * 提示友好的错误信息
     *
     * @param ctx
     */
    public void makeToast(Context ctx) {
        switch (this.getType()) {
            case TYPE_HTTP_CODE:
                String err = ctx.getString(R.string.http_status_code_error,
                        this.getCode());
                UIHelper.ToastMessage(ctx, err);
                break;
            case TYPE_HTTP_ERROR:
                UIHelper.ToastMessage(ctx, R.string.http_exception_error);
                break;
            case TYPE_SOCKET:
                UIHelper.ToastMessage(ctx, R.string.socket_exception_error);
                break;
            case TYPE_NETWORK:
                UIHelper.ToastMessage(ctx, R.string.network_not_connected);
                break;
            case TYPE_XML:
                UIHelper.ToastMessage(ctx, R.string.xml_parser_failed);
                break;
            case TYPE_IO:

                UIHelper.ToastMessage(ctx, R.string.io_exception_error);
                break;
            case TYPE_RUN:
                UIHelper.ToastMessage(ctx, R.string.app_run_code_error);
                break;
            case TYPE_JSON:
                UIHelper.ToastMessage(ctx, R.string.json_exception_error);
                break;
            case TYPE_FORBIDDEN:
                UIHelper.ToastMessage(ctx, R.string.forbidden_exception_error);
                break;
            case TYPE_PARAM:
                UIHelper.ToastMessage(ctx, R.string.param_exception_error);
                break;
        }
    }

    public String getExceptionDescprit(Context ctx) {
        String reason = null;

        switch (this.getType()) {
            case TYPE_HTTP_CODE:
                reason = ctx.getString(R.string.http_status_code_error);
                break;
            case TYPE_HTTP_ERROR:
                reason = ctx.getString(R.string.http_exception_error);
                break;
            case TYPE_SOCKET:
                reason = ctx.getString(R.string.socket_exception_error);
                break;
            case TYPE_NETWORK:
                reason = ctx.getString(R.string.network_not_connected);
                break;
            case TYPE_XML:
                reason = ctx.getString(R.string.xml_parser_failed);
                break;
            case TYPE_IO:
                reason = ctx.getString(R.string.io_exception_error);
                break;
            case TYPE_RUN:
                reason = ctx.getString(R.string.app_run_code_error);
                break;
            case TYPE_JSON:
                reason = ctx.getString(R.string.json_exception_error);
                break;
            case TYPE_FORBIDDEN:
                reason = ctx.getString(R.string.forbidden_exception_error);
                break;
            case TYPE_PARAM:
                reason = ctx.getString(R.string.param_exception_error);
                break;
        }

        return reason;
    }

    public static AppException getAppExceptionHandler() {
        return new AppException();
    }



    public String getPhoneInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" android:");
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

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.i(TAG, "Error:" + e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        final Context context = AppManager.getAppManager().currentActivity();

        if (context == null) {
            return false;
        }
        // 显示异常信息&发送报告
        new Thread() {
            public void run() {
                Looper.prepare();
                UIHelper.ToastMessage(context, R.string.app_error_message_toast);
                Looper.loop();
            }

        }.start();
        return true;
    }
}

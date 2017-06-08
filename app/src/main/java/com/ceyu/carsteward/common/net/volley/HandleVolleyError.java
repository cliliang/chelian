package com.ceyu.carsteward.common.net.volley;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ceyu.carsteward.R;
import com.ceyu.carsteward.common.tools.StringUtils;


public class HandleVolleyError {
    private HandleVolleyError() {

    }

    public static String getMessage(Context context, VolleyError volleyError) {
        Log.e("HandleVolleyError", "error:" + volleyError.getClass().getName());
        Log.e("HandleVolleyError", "message:" + volleyError.getMessage());

        String message = null;
        if (volleyError instanceof TimeoutError) {
            message = context.getString(R.string.http_connection_timeout);
        } else if (volleyError instanceof NoConnectionError) {
            String errorMsg = volleyError.getMessage();
            if ("java.io.InterruptedIOException".equals(errorMsg)) {
                message = context.getString(R.string.io_exception_error);
            } else {
                message = context.getString(R.string.network_not_connected);
            }
        } else if (volleyError instanceof NetworkError) {
            message = context.getString(R.string.json_exception_error);
        } else {
            message = volleyError.getMessage();
        }
        return message;
    }

    public static void showErrorMessage(Context context, VolleyError volleyError) {
        if (context == null) {
            return;
        }

        String message = getMessage(context, volleyError);
        if (!StringUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static Response.ErrorListener getDefaultErrListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showErrorMessage(context, volleyError);
            }
        };
    }
}

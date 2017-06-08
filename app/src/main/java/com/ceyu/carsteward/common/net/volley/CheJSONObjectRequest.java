package com.ceyu.carsteward.common.net.volley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CheJSONObjectRequest extends CheJSONRequest<JSONObject> {
    private final static String TAG = "DNUJSONObjectRequest";

    public CheJSONObjectRequest(int method, String url, Map<String, String> param, Response.Listener<JSONObject> mListener, Response.ErrorListener listener) {
        super(method, url, param, mListener, listener);
        setMyDefaultPolicy();
    }


    public CheJSONObjectRequest(String url, Map<String, String> param, Response.Listener<JSONObject> mListener, Response.ErrorListener listener) {
        super(param == null ? Method.GET : Method.POST, url, param, mListener, listener);
        setMyDefaultPolicy();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    public void setMyDefaultPolicy() {
        setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 3, 2));
    }
}


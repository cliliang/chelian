package com.ceyu.carsteward.common.net.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class CheJSONArrayRequest extends CheJSONRequest<JSONArray> {
    private final static String TAG = "DNUJSONObjectRequest";

    public CheJSONArrayRequest(int method, String url, Map<String, String> param, Response.Listener<JSONArray> mListener, Response.ErrorListener listener) {
        super(method, url, param, mListener, listener);
    }

    public CheJSONArrayRequest(String url, Map<String, String> param, Response.Listener<JSONArray> mListener, Response.ErrorListener listener) {
        super(param == null ? Request.Method.GET : Request.Method.POST, url, param, mListener, listener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}

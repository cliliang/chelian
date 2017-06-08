package com.ceyu.carsteward.common.net.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

public abstract class CheJSONRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;
    private Map<String, String> mParam;
    public CheJSONRequest(int method, String url, Map<String, String> param, Response.Listener<T> mListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.mListener = mListener;
        mParam = param;
    }

    @Override
    abstract protected Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParam;
    }
}

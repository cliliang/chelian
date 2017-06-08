package com.ceyu.carsteward.common.net.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class CheImageLoader extends ImageLoader {
    public CheImageLoader(RequestQueue queue, Context context) {
        super(queue, CheImageCache.getInstance(context));
    }
}

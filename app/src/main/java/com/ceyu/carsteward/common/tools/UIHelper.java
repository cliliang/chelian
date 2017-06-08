package com.ceyu.carsteward.common.tools;

import android.content.Context;
import android.widget.Toast;

public final class UIHelper {

    static Toast toast1 = null;
    static Toast toast = null;
    private UIHelper() {
    }

    public static void ToastMessage(Context cont, String msg) {
        if (toast1 == null) {
            toast1 = Toast.makeText(cont, msg, Toast.LENGTH_LONG);
        } else {
            toast1.setText(msg);
        }
        toast1.show();
    }

    public static void ToastMessage(Context cont, int msg) {
        if (toast == null) {
            toast = Toast.makeText(cont, cont.getResources().getString(msg), Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}

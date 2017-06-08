package com.ceyu.carsteward.common.ui.views;

import android.content.Context;
import android.content.DialogInterface;

public class ProgressDialog {

    private static ProgressDialog instance = null;
    private ProgressHUD dialog;
    private DialogInterface.OnCancelListener onCancelListener;

    private ProgressDialog(){

    }

    public static synchronized ProgressDialog getInstance() {
        if (instance == null) {
            instance = new ProgressDialog();
        }
        return instance;
    }

    public void show(Context context) {
        dialog = ProgressHUD.get(context);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onCancelListener != null) {
                    onCancelListener.onCancel(dialog);
                }
            }
        });

        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void show(Context context, boolean cancelable) {
        dialog = ProgressHUD.get(context);
        dialog.setCancelable(cancelable);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public boolean isShowing() {
        if (dialog == null) {
            return false;
        }

        return dialog.isShowing();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
}

package com.arcthos.arcthosmart.helper;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.StringRes;


/**
 * Created by Vinicius on 11/22/2016.
 */
public class LoadingDialogHelper {
    private static ProgressDialog pd;

    private LoadingDialogHelper() {
    }

    public static void show(Context context, @StringRes int message) {
        show(context, context.getResources().getString(message));
    }

    public static void show(Context context, String message) {
        if (pd == null) {
            pd = new ProgressDialog(context);
            pd.setMessage(message);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();
        } else {
            pd.setMessage(message);
        }
    }

    public static void setCancelable(Boolean boo) {
        pd.setCancelable(boo);
        pd.setCanceledOnTouchOutside(boo);
    }

    public static void message(String message) {
        if (pd != null) {
            pd.setMessage(message);
        }
    }

    public static void dismiss() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
            pd = null;
        }
    }
}

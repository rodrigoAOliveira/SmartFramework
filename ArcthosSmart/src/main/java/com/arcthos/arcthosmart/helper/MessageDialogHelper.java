package com.arcthos.arcthosmart.helper;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.StringRes;

/**
 * Created by Vinicius Damiati on 19-Oct-17.
 */

public class MessageDialogHelper {
    public static void showMessage(Context context, @StringRes int message, ConfirmationListener confirmationListener) {
        showMessage(context, context.getResources().getString(message), confirmationListener);
    }

    public static void showMessage(Context context, @StringRes int message) {
        showMessage(context, context.getResources().getString(message));
    }

    public static void showMessage(Context context, String message) {
        AlertDialog.Builder confirmacao = new AlertDialog.Builder(context);

        confirmacao.setMessage(message);

        confirmacao.setNeutralButton("Ok", null);

        AlertDialog alert = confirmacao.create();

        alert.show();
    }

    public static void showMessage(Context context, String message, ConfirmationListener confirmationListener) {
        AlertDialog.Builder confirmacao = new AlertDialog.Builder(context);

        confirmacao.setMessage(message);

        confirmacao.setNeutralButton("Ok", (dialogInterface, i) -> confirmationListener.onOkClick());

        AlertDialog alert = confirmacao.create();
        alert.show();
    }

    public interface ConfirmationListener {
        void onOkClick();
    }
}

package com.arcthos.arcthosmart.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.ContextThemeWrapper;

import androidx.annotation.StringRes;

import com.arcthos.arcthosmart.R;

/**
 * Created by Vinicius Damiati on 20-Dec-17.
 */

public class ConfirmDialogHelper {

    private String message;
    private ConfirmSwitch confirmSwitch;
    private ConfirmSwitchOnlyYes confirmSwitchOnlyYes;
    private Context context;
    private String title;

    private ConfirmDialogHelper(Context context, String message, ConfirmSwitch confirmSwitch) {
        this.confirmSwitch = confirmSwitch;
        this.message = message;
        this.context = context;
    }

    private ConfirmDialogHelper(Context context, String message, ConfirmSwitchOnlyYes confirmSwitchOnlyYes) {
        this.confirmSwitchOnlyYes = confirmSwitchOnlyYes;
        this.message = message;
        this.context = context;
    }

    private ConfirmDialogHelper(Context context, String title, String message, ConfirmSwitch confirmSwitch) {
        this.confirmSwitch = confirmSwitch;
        this.message = message;
        this.context = context;
        this.title = title;
    }

    public static ConfirmDialogHelper getInstance(Context context, String title, String message, ConfirmSwitch confirmSwitch) {
        ConfirmDialogHelper confirmDialogHelper = new ConfirmDialogHelper(context, title, message, confirmSwitch);
        return confirmDialogHelper;
    }

    public static ConfirmDialogHelper getInstance(Context context, String message, ConfirmSwitchOnlyYes confirmSwitchOnlyYes) {
        ConfirmDialogHelper confirmDialogHelper = new ConfirmDialogHelper(context, message, confirmSwitchOnlyYes);
        return confirmDialogHelper;
    }

    public static ConfirmDialogHelper getInstance(Context context, @StringRes int message, ConfirmSwitch confirmSwitch) {
        ConfirmDialogHelper confirmDialogHelper = new ConfirmDialogHelper(context, context.getResources().getString(message), confirmSwitch);
        return confirmDialogHelper;
    }

    public static ConfirmDialogHelper getInstance(Context context, @StringRes int message, ConfirmSwitchOnlyYes confirmSwitchOnlyYes) {
        ConfirmDialogHelper confirmDialogHelper = new ConfirmDialogHelper(context, context.getResources().getString(message), confirmSwitchOnlyYes);
        return confirmDialogHelper;
    }


    public static ConfirmDialogHelper getInstance(Context context, @StringRes int title, @StringRes int message, ConfirmSwitch confirmSwitch) {
        return getInstance(context, context.getResources().getString(title), context.getResources().getString(message), confirmSwitch);
    }

    public void show() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (confirmSwitch != null) {
                        confirmSwitch.onYesPressed();
                    } else {
                        confirmSwitchOnlyYes.onYesPressed();
                    }

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    if (confirmSwitch != null) {
                        confirmSwitch.onNoPressed();
                    }
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogDanger));
        if (title != null) {
            builder.setTitle(Html.fromHtml("<font color='#205BA3'>" + title + "</font>"));
        }
        builder.setMessage(message).setPositiveButton(context.getString(R.string.yes), dialogClickListener)
                .setNegativeButton(context.getString(R.string.no), dialogClickListener).show();
    }
}

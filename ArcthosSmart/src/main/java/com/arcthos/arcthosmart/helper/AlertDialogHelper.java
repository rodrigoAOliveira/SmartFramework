package com.arcthos.arcthosmart.helper;

import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogHelper extends Dialog {
    private OnClickListener onClickListener;
    private Context context;
    private String title;
    private int view;

    public AlertDialogHelper(Context context, String title, int view, OnClickListener onClickListener) {
        super(context);

        this.context = context;
        this.title = title;
        this.view = view;
        this.onClickListener = onClickListener;
    }

    public static AlertDialogHelper getInstance(Context context, String title, int view, OnClickListener onClickListener) {
        return new AlertDialogHelper(context, title, view, onClickListener);
    }


    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(view);
        builder.setOnCancelListener(dialog -> dialog.dismiss());

        getWindow().setLayout(500, 480);
        builder.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(500, 480);
    }
}
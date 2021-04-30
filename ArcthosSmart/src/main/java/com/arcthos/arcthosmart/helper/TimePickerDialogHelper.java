package com.arcthos.arcthosmart.helper;

import android.app.TimePickerDialog;
import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

/**
 * Created by Vinicius Damiati on 29-Dec-17.
 */

public class TimePickerDialogHelper {
    private Context context;
    private TextInputLayout textInputLayout;

    private TimePickerDialogHelper(Context context, TextInputLayout textInputLayout) {
        this.context = context;
        this.textInputLayout = textInputLayout;
    }

    public static TimePickerDialogHelper getInstance(Context context, TextInputLayout textInputLayout) {
        TimePickerDialogHelper timePickerDialogHelper = new TimePickerDialogHelper(context, textInputLayout);
        return timePickerDialogHelper;
    }

    public void show() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hourOfDay, minute) -> textInputLayout.getEditText().setText( (hourOfDay > 9 ? String.valueOf(hourOfDay) : "0" + String.valueOf(hourOfDay) )  + ":" + (minute > 9 ? String.valueOf(minute) : "0" + String.valueOf(minute)) + ":00"), mHour, mMinute, true);
        timePickerDialog.show();
    }
}

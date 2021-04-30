package com.arcthos.arcthosmart.helper;

import android.app.DatePickerDialog;
import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vinicius Damiati on 29-Dec-17.
 */

public class DatePickerDialogHelper {
    private DatePickerDialogCallback datePickerDialogCallback;
    private Context context;
    private TextInputLayout textInputLayout;
    private boolean allowPreviousDate;

    private DatePickerDialogHelper(Context context, TextInputLayout textInputLayout, boolean allowPreviousDate) {
        this.context = context;
        this.textInputLayout = textInputLayout;
        this.allowPreviousDate = allowPreviousDate;
    }

    private DatePickerDialogHelper(Context context, TextInputLayout textInputLayout, boolean allowPreviousDate, DatePickerDialogCallback datePickerDialogCallback) {
        this.context = context;
        this.textInputLayout = textInputLayout;
        this.allowPreviousDate = allowPreviousDate;
        this.datePickerDialogCallback = datePickerDialogCallback;
    }

    private DatePickerDialogHelper(Context context, DatePickerDialogCallback datePickerDialogCallback) {
        this.context = context;
        this.datePickerDialogCallback = datePickerDialogCallback;
    }

    public static DatePickerDialogHelper getInstance(Context context, TextInputLayout textInputLayout) {
        return getInstance(context, textInputLayout, true);
    }

    public static DatePickerDialogHelper getInstance(Context context, TextInputLayout textInputLayout, boolean allowPreviousDate) {
        DatePickerDialogHelper datePickerDialogHelper = new DatePickerDialogHelper(context, textInputLayout, allowPreviousDate);
        return datePickerDialogHelper;
    }

    public static DatePickerDialogHelper getInstance(Context context, TextInputLayout textInputLayout, DatePickerDialogCallback datePickerDialogCallback) {
        return getInstance(context, textInputLayout, true, datePickerDialogCallback);
    }

    public static DatePickerDialogHelper getInstance(Context context, TextInputLayout textInputLayout, boolean allowPreviousDate, DatePickerDialogCallback datePickerDialogCallback) {
        DatePickerDialogHelper datePickerDialogHelper = new DatePickerDialogHelper(context, textInputLayout, allowPreviousDate, datePickerDialogCallback);
        return datePickerDialogHelper;
    }

    public static DatePickerDialogHelper getSimpleInstance(Context context,DatePickerDialogCallback datePickerDialogCallback) {
        DatePickerDialogHelper datePickerDialogHelper = new DatePickerDialogHelper(context, datePickerDialogCallback);
        return datePickerDialogHelper;
    }

    public void show() {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final DatePickerDialog datePicker = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            Calendar currentDate = Calendar.getInstance();

            newDate.set(year, monthOfYear, dayOfMonth, 10, 0);
            Date selectedDate;
            String selectedString;
            if (newDate.before(currentDate) && !allowPreviousDate) {
                selectedString = dataFormatter.format(currentDate.getTime());
                selectedDate = removePrecision(currentDate).getTime();
            } else {
                selectedString = dataFormatter.format(newDate.getTime());
                selectedDate = removePrecision(newDate).getTime();
            }
            textInputLayout.getEditText().setText(selectedString);

            if (datePickerDialogCallback != null)
                datePickerDialogCallback.onSelectDate(selectedString, selectedDate);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    public void showSimple() {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final DatePickerDialog datePicker = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();

            newDate.set(year, monthOfYear, dayOfMonth, 10, 0);
            Date selectedDate = removePrecision(newDate).getTime();;
            String selectedString = dataFormatter.format(newDate.getTime());;

            if (datePickerDialogCallback != null)
                datePickerDialogCallback.onSelectDate(selectedString, selectedDate);

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private Calendar removePrecision(Calendar calendar) {
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(calendar.getTime());
        startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);
        startDateCalendar.set(Calendar.SECOND, 0);
        startDateCalendar.set(Calendar.MILLISECOND, 0);
        return startDateCalendar;
    }
}

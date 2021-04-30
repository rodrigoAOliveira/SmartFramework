package com.arcthos.arcthosmart.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Vinicius Damiati on 27-Dec-17.
 */

public class DateHelper {
    public static final Locale BRAZILIAN_LOCALE = new Locale("pt", "BR");

    public static String convertToAppDate(String date) {
        if (date == null) {
            return "";
        }

        if (date.equals("")) {
            return "";
        }

        String aux = "";
        aux = date.split("-")[2] + "/" + date.split("-")[1] + "/" + date.split("-")[0];
        return aux;
    }

    public static String convertToAppDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", BRAZILIAN_LOCALE);
        return sdf.format(date);
    }

    public static String convertToLogDateTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", BRAZILIAN_LOCALE);
        return sdf.format(date);
    }

    public static String convertToSfDate(String date) {
        String aux = "";

        if (date == null) {
            return "";
        }

        if (date.equals("")) {
            return "";
        }

        aux = date.split("/")[2] + "-" + date.split("/")[1] + "-" + date.split("/")[0];
        return aux;
    }

    public static String convertDateTimeToAppDate(String dateTime) {
        if (dateTime == null) {
            return "";
        }

        if (dateTime.equals("")) {
            return "";
        }

        String date = dateTime.split("T")[0];
        return convertToAppDate(date);
    }

    public static String getCurrentSfDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getCurrentAppDate() {
        return convertToAppDate(getCurrentSfDate());
    }

    public static String getCurrentDayMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateAndTime = sdf.format(new Date());

        String aux = currentDateAndTime.split("-")[1] + "-" + currentDateAndTime.split("-")[2];

        return aux;
    }

    public static String getCurrentMonthYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateAndTime = sdf.format(new Date());

        String aux = currentDateAndTime.split("-")[0] + "-" + currentDateAndTime.split("-")[1];

        return aux;
    }

    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateAndTime = sdf.format(new Date());

        String aux = currentDateAndTime.split("-")[1];

        return aux;
    }

    public static String convertToCurrentMonth(String month) {
        if (month == null)
            return "";

        if (month.equals(""))
            return "";

        String aux = month.split("-")[1];
        return aux;
    }

    public static Date convertToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertAppDateToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long dateDiff(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();

        long second = diff / 1000;
        long minute = second / 60;
        long hours = minute / 60;
        long days = hours / 24;

        return days;
    }

    public static String downloadedGMT(String dateTime) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();
        try {
            date = format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getDefault());

        return format.format(calendar.getTime());
    }

    public static String convertSfDateTimeToAppTimeGMT(String dateTime) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(dateTime);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
            dateFormatter.setTimeZone(TimeZone.getDefault());
            dateTime = dateFormatter.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String toUploadGMT(String dateTime) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();
        try {
            date = inputFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(timeZone);

        return outputFormat.format(calendar.getTime());
    }

    public static String convertToSelectedDate(String date) {
        SimpleDateFormat spf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat("yyyy-MM-dd");
        return date = spf.format(newDate);
    }

    public static String toSfDateTime(String date) {
        if (date == null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return getGtmFormater("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String currentDayFromCalendar(Date date){
        return getDayOfTheWeek(date) + ", " + getDay(date) + " de " +
                getMonth(date) + " de " + getYear(date);
    }

    public static String getDay(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd", BRAZILIAN_LOCALE);
        return sdf.format(date);
    }

    public static String getMonth(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM", BRAZILIAN_LOCALE);
        String month = sdf.format(date);
        return month.substring(0,1).toUpperCase() + month.substring(1).toLowerCase();
    }

    public static String getYear(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", BRAZILIAN_LOCALE);
        return sdf.format(date);
    }

    public static String getDayOfTheWeek(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", BRAZILIAN_LOCALE);
        String dayOfTheWeek = sdf.format(date);
        return dayOfTheWeek.substring(0,1).toUpperCase() + dayOfTheWeek.substring(1).toLowerCase();
    }

    public static SimpleDateFormat getGtmFormater(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf;
    }
}
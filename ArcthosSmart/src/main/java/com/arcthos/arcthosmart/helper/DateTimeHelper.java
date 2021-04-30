package com.arcthos.arcthosmart.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTimeHelper {

    public static final String GREATER = "io.topi.stoller.helper.DateHelper.GREATER";
    public static final String LESS = "io.topi.stoller.helper.DateHelper.LESS";

    public static String convertAppDateTimeToSfDateTime(String dateTime) {
        if (dateTime == null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");

        try {
            return getGtmFormater("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(sdf.parse(dateTime));
        } catch (ParseException e) {
            e.getErrorOffset();
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateTimeToAppTime(String dateTime) {
        if (dateTime == null) {
            return "";
        }

        if (dateTime.equals("")) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        try {
            return getGtmFormater("HH:mm:ss").format(sdf.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateTimeToSfDateTime(String date, String time) {
        String dateTime = date + time;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");

        try {
            return getGtmFormater("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(sdf.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date convertDateTimeToDate(String date, String time) {
        if (date == null || time == null) return new Date();

        String dateTime = date + time;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm:ss");

        try {
            return sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getComparationResult(String selectedTime,
                                               String comparingTime,
                                               String comparison){
        if(selectedTime.isEmpty())
            return true;

        if(comparison.equals(DateTimeHelper.GREATER) && !comparingTime.isEmpty() &&
                DateTimeHelper.compareTimes(selectedTime, comparingTime) > 0){
            return false;
        } else if(comparison.equals(DateTimeHelper.LESS) && !comparingTime.isEmpty() &&
                DateTimeHelper.compareTimes(selectedTime, comparingTime) < 0) {
            return false;
        }
        return true;
    }

    public static String convertDateToSfDateTime(Date date) {
        if (date == null) return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String format = sdf.format(date);
        return format;
    }

    private static long compareTimes(String selectedTime, String comparingTime){
        Date d1 = convertTimeStringToDate(selectedTime);
        Date d2 = convertTimeStringToDate(comparingTime);
        return d2.getTime() - d1.getTime();
    }

    public static Date convertTimeStringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static SimpleDateFormat getGtmFormater(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf;
    }

    public static String convertSfDateTimeToAppDate(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "";
        }

        String date = "";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(dateTime);

            date = DateHelper.convertToAppDate(value);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentSfDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String currentDateandTime = sdf.format(new Date(new Date().getTime() + TimeUnit.HOURS.toMillis(3)));
        return currentDateandTime;
    }
}

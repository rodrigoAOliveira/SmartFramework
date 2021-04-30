package com.arcthos.arcthosmart.helper;

/**
 * Created by Vinicius Damiati on 27-Dec-17.
 */

public class TimeHelper {
    public static String convertToAppTime(String time) {
        if(time == null) {
            return "";
        }

        if(time.equals("")) {
            return "";
        }

        return time.replace(".000Z", "");
    }

    public static String convertToSfTime(String time) {
        if(time == null) {
            return "";
        }

        if(time.equals("")) {
            return "";
        }

        return time.concat(".000Z");
    }
}

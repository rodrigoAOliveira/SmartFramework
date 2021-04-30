package com.arcthos.arcthosmart.helper;

/**
 * Created by Vinicius Damiati on 11-Dec-17.
 */

public class StringHelper {
    public static String getString(String value) {
        if(value == null) {
            return "";
        } else {
            return value;
        }
    }

    public static String truncateTwoDecimalPlaces(String value) {
        String integer = value.split("\\.")[0];
        String decimal = value.split("\\.").length > 1 ? value.split("\\.")[1] : "";

        return integer + "." + (decimal.length() > 2 ? decimal.substring(0, 2) : decimal);
    }
}
package com.arcthos.arcthosmart.helper;

/**
 * Created by Vinicius Damiati on 08-Nov-18.
 */
public class BooleanHelper {
    public static boolean getBoolean(String value) {
        if (value == null) return false;
        if (value.equals("")) return false;

        return Boolean.parseBoolean(value);
    }
}

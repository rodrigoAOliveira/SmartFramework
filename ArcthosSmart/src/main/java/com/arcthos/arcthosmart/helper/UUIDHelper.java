package com.arcthos.arcthosmart.helper;

import java.util.UUID;

/**
 * Created by Vinicius Damiati on 19-Feb-18.
 */

public class UUIDHelper {
    public static boolean isUUID(String value) {
        try {
            UUID uuid = UUID.fromString(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

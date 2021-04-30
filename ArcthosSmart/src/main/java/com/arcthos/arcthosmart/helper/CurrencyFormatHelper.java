package com.arcthos.arcthosmart.helper;

/**
 * Created by Vinicius Damiati on 12-Jan-18.
 */

public class CurrencyFormatHelper {
    public static String getCurrencySymbol(String currency) {
        String symbol;

        switch (currency) {
            case "BRL":
                symbol = "R$";
                break;

            case "MXN":
                symbol = "$";
                break;

            case "COP":
                symbol = "$";
                break;

            case "USD":
                symbol = "$";
                break;

            default: symbol = "";
        }

        return symbol;
    }
}

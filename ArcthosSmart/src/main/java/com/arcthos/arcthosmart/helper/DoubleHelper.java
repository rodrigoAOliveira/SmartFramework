package com.arcthos.arcthosmart.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Vinicius Damiati on 15-Feb-18.
 */

public class DoubleHelper {

    private static Locale LOCALE = new Locale("pt", "BR");

    public static double roundTwoPlaces(double value) {
        String textValue = String.valueOf(value);
        String integerPart = textValue.split("\\.")[0];
        String decimalPart = "";
        if (textValue.split("\\.").length > 1) {
            decimalPart = textValue.split("\\.")[1];
        }

        if (decimalPart.length() > 2) {
            if (decimalPart.toCharArray()[2] == '5') {
                decimalPart = decimalPart.substring(0, 2);
            }
        }

        double newValue = Double.valueOf(integerPart.concat(".").concat(decimalPart));

        String converted = String.valueOf(Math.round(newValue * 100.0f) / 100.0f);
        return Double.parseDouble(converted);
    }

    public static double roundTwoPlaces(String value) {
        if (value.equals("")) return 0;
        return roundTwoPlaces(Double.parseDouble(value));
    }

    public static String roundTwoPlacesText(double value) {
        return String.valueOf(roundTwoPlaces(value));
    }

    public static String roundTwoPlacesText(String value) {
        return String.valueOf(roundTwoPlaces(value));
    }

    public static double truncateTwoPlaces(double value) {
        return Math.floor(value * 100) / 100;
    }

    public static double truncateTwoPlaces(String value) {
        return Math.floor(Double.parseDouble(value) * 100) / 100;
    }

    public static String truncateTwoPlacesText(double value) {
        return String.valueOf(Math.floor(value * 100) / 100);
    }

    public static String truncateTwoPlacesText(String value) {
        return String.valueOf(Math.floor(Double.parseDouble(value) * 100) / 100);
    }

    public static String formatCurrency(Double value) {
        if (value == null) return "";
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(LOCALE);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        formatter.setMinimumIntegerDigits(1);
        return formatter.format(value);
    }

    public static String formatCurrency(String value) {
        if (value == null || value.equals("")) return "";
        return formatCurrency(Double.parseDouble(value));
    }

    public static double formatCurrencyNumber(String value) {
        return Double.parseDouble(formatCurrency(value).replace(".", "").replace(",", "."));
    }

    public static double formatCurrencyNumber(double value) {
        return Double.parseDouble(formatCurrency(value).replace(".", "").replace(",", "."));
    }

    private static String applyDecimalPlaces(String value) {
        if (value == null) {
            return "";
        }

        if (value.equals("")) {
            return "";
        }

        if (!value.contains(",")) {
            return value + ",00";
        }

        String decimalPlaces = value.split(",")[1];
        if (decimalPlaces.length() < 2) {
            return value + "0";
        }

        return value;
    }

    public static String formatPercentage(String percvalue) {
        if (percvalue == null || percvalue.equals("null") || percvalue.isEmpty()) return "";
        Double value = Double.parseDouble(percvalue);
        return getPercentageFormatter().format(value);
    }

    public static DecimalFormat getPercentageFormatter() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(LOCALE);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        formatter.setMinimumIntegerDigits(1);
        return formatter;
    }
}

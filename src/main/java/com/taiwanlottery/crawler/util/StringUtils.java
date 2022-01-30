package com.taiwanlottery.crawler.util;

import java.text.DecimalFormat;

public class StringUtils {
    private static DecimalFormat COMMA_FORMATTER = new DecimalFormat("#,###");
    private static DecimalFormat COMMA_POINT_FORMATER = new DecimalFormat("#,###.00");

    public static String parseWinString(String source) {
        return source.substring(source.indexOf("NT$") + 3, source.lastIndexOf('0') + 1)
                .replaceAll(",", "");
    }

    public static String removeNonDigitCharacters(String source) {
        return source.replaceAll("[^0-9]+", "");
    }

    public static String commaFormat(double source) {
        return COMMA_FORMATTER.format(source);
    }

    public static String commaPointFormat(double source) {
        return COMMA_POINT_FORMATER.format(source);
    }

    public static String percentageFormat(double source) {
        return String.format("%.2f%%", source * 100);
    }
}

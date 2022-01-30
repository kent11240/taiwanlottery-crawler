package com.taiwanlottery.crawler.util;

public class StringUtils {

    public static String parseWinString(String source) {
        return source.substring(source.indexOf("NT$") + 3, source.lastIndexOf('0') + 1)
                .replaceAll(",", "");
    }

    public static String removeNonDigitCharacters(String source) {
        return source.replaceAll("[^0-9]+", "");
    }
}

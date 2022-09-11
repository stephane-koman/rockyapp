package com.rockyapp.rockyappbackend.utils.helpers;

import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;

public class StringHelper {
    public static String replace(String oldString, String newString){
        return StringUtils.replace(oldString, "%", newString);
    }

    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}

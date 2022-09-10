package com.rockyapp.rockyappbackend.utils.helpers;

import org.apache.commons.lang3.StringUtils;

public class StringHelper {
    public static String replace(String oldString, String newString){
        return StringUtils.replace(oldString, "%", newString);
    }
}

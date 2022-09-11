package com.rockyapp.rockyappbackend.utils.helpers;

import java.util.Arrays;

public class ArrayHelper {

    public static boolean verifyIntIsBoolean(int value){
        return Arrays.asList(0, 1).contains(value);
    }
}

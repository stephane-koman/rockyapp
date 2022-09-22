package com.rockyapp.rockyappbackend.utils.enums;

public enum VolumeEnum {
    ID("id"),
    QUANTITY("quantity"),
    MESURE("mesure"),
    ACTIVE("active"),
    DELETE("delete");

    private final String value;

    VolumeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

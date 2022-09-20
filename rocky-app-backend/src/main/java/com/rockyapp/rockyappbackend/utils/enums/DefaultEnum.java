package com.rockyapp.rockyappbackend.utils.enums;

public enum DefaultEnum {
    NAME("name"),
    DESCRIPTION("description"),
    ACTIVE("active"),
    DELETE("delete");

    private final String value;

    DefaultEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

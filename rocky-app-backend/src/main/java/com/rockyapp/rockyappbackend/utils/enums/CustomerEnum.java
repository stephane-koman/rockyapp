package com.rockyapp.rockyappbackend.utils.enums;

public enum CustomerEnum {
    ID("id"),
    NAME("name"),
    EMAIL("email"),
    FIXE("fixe"),
    MOBILE("mobile"),
    ACTIVE("active"),
    DELETE("delete");

    private final String value;

    CustomerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

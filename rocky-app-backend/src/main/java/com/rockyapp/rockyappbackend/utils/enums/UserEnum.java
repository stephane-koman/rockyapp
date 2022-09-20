package com.rockyapp.rockyappbackend.utils.enums;

public enum UserEnum {
    ID("id"),
    NAME("name"),
    USERNAME("username"),
    EMAIL("email"),
    ACTIVE("active"),
    DELETE("delete"),
    ROLES("roles");

    private final String value;

    UserEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

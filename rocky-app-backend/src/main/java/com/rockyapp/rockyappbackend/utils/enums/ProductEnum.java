package com.rockyapp.rockyappbackend.utils.enums;

public enum ProductEnum {
    ID("id"),
    NAME("name"),
    DESCRIPTION("description"),
    PRICE("price"),
    IMAGE("image"),
    ACTIVE("active"),
    DELETE("delete");

    private final String value;

    ProductEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

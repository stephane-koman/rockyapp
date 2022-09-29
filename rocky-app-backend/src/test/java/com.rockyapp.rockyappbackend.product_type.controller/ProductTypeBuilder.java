package com.rockyapp.rockyappbackend.product_type.controller;

import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;

import java.util.Collections;
import java.util.List;

public class ProductTypeBuilder {

    public static ProductTypeDTO getDto() {
        ProductTypeDTO dto = new ProductTypeDTO();
        dto.setId(1L);
        return dto;
    }

    public static ProductType getEntity() {
        ProductType entity = new ProductType();
        entity.setId(1L);
        entity.setDescription("test");
        return entity;
    }
}
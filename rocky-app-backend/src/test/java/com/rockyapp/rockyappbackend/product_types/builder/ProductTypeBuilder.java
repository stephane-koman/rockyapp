package com.rockyapp.rockyappbackend.product_types.builder;

import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;

public class ProductTypeBuilder {

    public static ProductTypeDTO getDto() {
        ProductTypeDTO dto = new ProductTypeDTO();
        dto.setId(1L);
        dto.setName("Lait");
        return dto;
    }

    public static ProductType getEntity() {
        ProductType entity = new ProductType();
        entity.setId(1L);
        entity.setName("Lait");
        return entity;
    }
}
package com.rockyapp.rockyappbackend.products.builder;

import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;

public class ProductBuilder {

    public static ProductDTO getDto() {
        ProductDTO dto = new ProductDTO();
        dto.setId("0001");
        dto.setName("fraise");
        return dto;
    }

    public static Product getEntity() {
        Product entity = new Product();
        entity.setId("0001");
        entity.setName("fraise");
        return entity;
    }
}
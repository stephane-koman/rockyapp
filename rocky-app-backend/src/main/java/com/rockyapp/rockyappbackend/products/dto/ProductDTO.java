package com.rockyapp.rockyappbackend.products.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the {@link com.rockyapp.rockyappbackend.products.entity.Product} entity
 */
@Data
@AllArgsConstructor
public class ProductDTO implements SocleDTO {
    private final int active;
    private final String id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final byte[] image;
    private final ProductTypeDTO productTypeDTO;
}
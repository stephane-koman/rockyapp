package com.rockyapp.rockyappbackend.products.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * A DTO for the {@link com.rockyapp.rockyappbackend.products.entity.Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreaDTO implements SocleDTO {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private byte[] image;
    private Long productTypeId;
    private boolean active;
}
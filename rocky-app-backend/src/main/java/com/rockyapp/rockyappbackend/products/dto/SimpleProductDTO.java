package com.rockyapp.rockyappbackend.products.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
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
public class SimpleProductDTO implements SocleDTO {
    private String id;
    private String code;
    private String name;
    private BigDecimal price;
    private ProductTypeDTO productType;
    private VolumeDTO volume;
    private boolean active;
}
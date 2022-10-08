package com.rockyapp.rockyappbackend.products.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.documents.dto.DocumentDTO;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * A DTO for the {@link com.rockyapp.rockyappbackend.products.entity.Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements SocleDTO {
    private String id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean active;
    private ProductTypeDTO productType;
    private VolumeDTO volume;
    private List<DocumentDTO> documentList;
}
package com.rockyapp.rockyappbackend.products.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.documents.dto.DocumentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

/**
 * A DTO for the {@link com.rockyapp.rockyappbackend.products.entity.Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class    ProductCreaDTO implements SocleDTO {
    private String id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Long productTypeId;
    private Long volumeId;
    private boolean active = true;
    Set<DocumentDTO> documentList;
}
package com.rockyapp.rockyappbackend.products.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * A DTO for the {@link com.rockyapp.rockyappbackend.products.entity.Product} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchCriteriaDTO implements SocleDTO {
    private String text_search;
    private String code;
    private String name;
    private BigDecimal price;
    private List<String> productTypeList;
    private List<String> volumeList;
    private int active = 2;
}
package com.rockyapp.rockyappbackend.product_types.service;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import com.rockyapp.rockyappbackend.product_types.exception.ProductTypeAlreadyExistsException;
import com.rockyapp.rockyappbackend.product_types.exception.ProductTypeNotFoundException;
import org.springframework.data.domain.Pageable;

public interface ProductTypeService {
    ResultPagine<ProductTypeDTO> search(final DefaultCriteriaDTO criteriaDTO, final Pageable pageable);
    ProductType findByName(final String name) throws ProductTypeNotFoundException;
    ProductTypeDTO findById(final Long id) throws ProductTypeNotFoundException;
    void create(ProductTypeDTO productTypeDTO) throws ProductTypeAlreadyExistsException;
    void update(Long productTypeId, ProductTypeDTO productTypeDTO) throws ProductTypeAlreadyExistsException, ProductTypeNotFoundException;
    void delete(Long productTypeId) throws ProductTypeNotFoundException;
}
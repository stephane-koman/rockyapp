package com.rockyapp.rockyappbackend.product_types.dao;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductTypeDAOCustom {
    Page<ProductType> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable);
}

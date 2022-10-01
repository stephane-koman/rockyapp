package com.rockyapp.rockyappbackend.products.dao;

import com.rockyapp.rockyappbackend.products.dto.ProductSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDAOCustom {
    Page<Product> search(ProductSearchCriteriaDTO criteriaDTO, Pageable pageable);
}

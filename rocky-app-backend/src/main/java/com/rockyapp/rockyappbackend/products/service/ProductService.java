package com.rockyapp.rockyappbackend.products.service;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.products.exception.ProductAlreadyExistsException;
import com.rockyapp.rockyappbackend.products.exception.ProductNotFoundException;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ResultPagine<SimpleProductDTO> search(final ProductSearchCriteriaDTO criteriaDTO, final Pageable pageable);
    Product findByNameAndProductTypeAndVolume(final String name, final Long productTypeId, final Long volumeId) throws ProductNotFoundException;
    ProductDTO findById(final String id) throws ProductNotFoundException;
    void create(ProductCreaDTO productDTO) throws ProductAlreadyExistsException;
    void update(String productId, ProductCreaDTO productDTO) throws ProductAlreadyExistsException, ProductNotFoundException;
    void delete(String productId) throws ProductNotFoundException;
    void changeStatus(String id, boolean active) throws ProductNotFoundException;
}

package com.rockyapp.rockyappbackend.products.service.impl;

import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.products.dao.ProductDAO;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.dto.ProductSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.products.exception.ProductAlreadyExistsException;
import com.rockyapp.rockyappbackend.products.exception.ProductNotFoundException;
import com.rockyapp.rockyappbackend.products.mapper.ProductCreaMapper;
import com.rockyapp.rockyappbackend.products.mapper.ProductMapper;
import com.rockyapp.rockyappbackend.products.mapper.SimpleProductMapper;
import com.rockyapp.rockyappbackend.products.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;
    private SimpleProductMapper simpleProductMapper;
    private ProductMapper productMapper;
    private ProductCreaMapper productCreaMapper;

    @Override
    public ResultPagine<SimpleProductDTO> search(ProductSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<Product> productPage = productDAO.search(criteriaDTO, pageable);
        return simpleProductMapper.mapFromEntity(productPage);
    }

    @Override
    public Product findByNameAndProductType(String name, Long productTypeId) throws ProductNotFoundException {
        Product product = productDAO.findProductByNameAndProductTypeAndIsNotDelete(name, productTypeId);
        if(product == null) throw new ProductNotFoundException();
        return product;
    }

    private Product findProductById(String id) throws ProductNotFoundException {
        Product product = productDAO.findProductByIdAndIsNotDelete(id);
        if(product == null) throw new ProductNotFoundException();
        return product;
    }

    @Override
    public ProductDTO findById(String id) throws ProductNotFoundException {
        Product product = this.findProductById(id);
        return productMapper.mapFromEntity(product);
    }

    @Override
    public void create(ProductCreaDTO productDTO) throws ProductAlreadyExistsException {
        Product productExists = productDAO.findProductByNameAndProductTypeAndIsNotDelete(productDTO.getName(), productDTO.getProductTypeId());

        if (productExists != null) throw new ProductAlreadyExistsException(productDTO.getName().concat(" avec le type de produit ").concat(productExists.getProductType().getName()));

        Product product = new Product();
        product = productCreaMapper.mapToEntity(productDTO, product);
        productDAO.save(product);
    }

    @Override
    public void update(String productId, ProductCreaDTO productDTO) throws ProductAlreadyExistsException, ProductNotFoundException {
        Product product = this.findByNameAndProductType(productDTO.getName(), productDTO.getProductTypeId());

        if(product != null && !product.getId().equals(productId)) throw new ProductAlreadyExistsException(productDTO.getName().concat(" avec le type de produit ").concat(product.getProductType().getName()));

        product = this.findProductById(productId);
        product = productCreaMapper.mapToEntity(productDTO, product);
        product.setUpdatedAt(LocalDateTime.now());
        productDAO.save(product);
    }

    @Override
    public void delete(String productId) throws ProductNotFoundException {
        Product product = this.findProductById(productId);
        product.setDelete(1);
        product.setActive(0);
        product.setUpdatedAt(LocalDateTime.now());
        product.setDeletedAt(LocalDateTime.now());
        productDAO.save(product);
    }

    @Override
    public void changeStatus(String id, boolean active) throws ProductNotFoundException {
        Product product = this.findProductById(id);
        product.setActive(Boolean.TRUE.equals(active) ? 1 : 0);
        product.setUpdatedAt(LocalDateTime.now());
        productDAO.save(product);
    }
}

package com.rockyapp.rockyappbackend.product_types.service.impl;

import com.rockyapp.rockyappbackend.common.dto.DefaultCriteriaDTO;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.product_types.dao.ProductTypeDAO;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import com.rockyapp.rockyappbackend.product_types.exception.ProductTypeAlreadyExistsException;
import com.rockyapp.rockyappbackend.product_types.exception.ProductTypeNotFoundException;
import com.rockyapp.rockyappbackend.product_types.mapper.ProductTypeMapper;
import com.rockyapp.rockyappbackend.product_types.service.ProductTypeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {

    private ProductTypeDAO productTypeDAO;
    private ProductTypeMapper productTypeMapper;

    @Override
    public ResultPagine<ProductTypeDTO> search(DefaultCriteriaDTO criteriaDTO, Pageable pageable) {
        Page<ProductType> productTypePage = this.productTypeDAO.search(criteriaDTO, pageable);
        return productTypeMapper.mapFromEntity(productTypePage);
    }

    @Override
    public ProductType findByName(String name) throws ProductTypeNotFoundException {
        ProductType productType = productTypeDAO.findProductTypeByNameAndIsNotDelete(name);

        if(productType == null)  throw new ProductTypeNotFoundException();

        return productType;
    }

    @Override
    public ProductTypeDTO findById(Long id) throws ProductTypeNotFoundException {
        ProductType productType = productTypeDAO.findProductTypeByIdAndIsNotDelete(id);

        if(productType == null)  throw new ProductTypeNotFoundException();

        return productTypeMapper.mapFromEntity(productType);
    }

    @Override
    public void create(ProductTypeDTO productTypeDTO) throws ProductTypeAlreadyExistsException {
        ProductType productTypeExists = this.productTypeDAO.findProductTypeByNameAndIsNotDelete(productTypeDTO.getName());

        if(productTypeExists != null) throw new ProductTypeAlreadyExistsException(productTypeDTO.getName());

        ProductType productType = new ProductType();
        productType = productTypeMapper.mapToEntity(productTypeDTO, productType);
        productTypeDAO.save(productType);
    }

    @Override
    public void update(Long productTypeId, ProductTypeDTO productTypeDTO) throws ProductTypeAlreadyExistsException, ProductTypeNotFoundException {
        ProductType productType = this.findByName(productTypeDTO.getName());

        if(productType != null && !productType.getId().equals(productTypeId)) throw new ProductTypeAlreadyExistsException(productTypeDTO.getName());

        productType = productTypeDAO.findProductTypeByIdAndIsNotDelete(productTypeId);

        if(productType == null) throw new ProductTypeNotFoundException();
        productType = productTypeMapper.mapToEntity(productTypeDTO, productType);
        productType.setUpdatedAt(LocalDateTime.now());
        productTypeDAO.save(productType);
    }

    @Override
    public void delete(Long productTypeId) throws ProductTypeNotFoundException {
        ProductType productType = productTypeDAO.findProductTypeByIdAndIsNotDelete(productTypeId);
        if(productType == null) throw new ProductTypeNotFoundException();
        productType.setDelete(1);
        productType.setActive(0);
        productType.setUpdatedAt(LocalDateTime.now());
        productType.setDeletedAt(LocalDateTime.now());
        productTypeDAO.save(productType);
    }
}

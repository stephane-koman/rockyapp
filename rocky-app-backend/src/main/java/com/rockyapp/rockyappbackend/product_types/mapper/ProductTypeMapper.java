package com.rockyapp.rockyappbackend.product_types.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.product_types.dto.ProductTypeDTO;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeMapper extends AbstractSocleMapper<ProductType, ProductTypeDTO> implements SocleMapper<ProductType, ProductTypeDTO> {
    @Override
    public ProductType mapToEntity(ProductTypeDTO model, ProductType entity){
        BeanUtils.copyProperties(model, entity, "id", "active");
        entity.setActive(model.isActive() ? 1 : 0);
        return entity;
    }

    @Override
    public ProductTypeDTO mapFromEntity(ProductType entity) {
        ProductTypeDTO productTypeDTO = new ProductTypeDTO();
        BeanUtils.copyProperties(entity, productTypeDTO, "active");
        productTypeDTO.setActive(entity.getActive() == 1);
        return productTypeDTO;
    }
}
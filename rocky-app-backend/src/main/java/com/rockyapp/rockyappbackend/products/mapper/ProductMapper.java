package com.rockyapp.rockyappbackend.products.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.product_types.mapper.ProductTypeMapper;
import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductMapper extends AbstractSocleMapper<Product, ProductDTO> implements SocleMapper<Product, ProductDTO> {

    private ProductTypeMapper productTypeMapper;

    @Override
    public Product mapToEntity(ProductDTO model, Product entity) {
        BeanUtils.copyProperties(model, entity, "id", "active", "productType");
        entity.setActive(model.isActive() ? 1 : 0);
        entity.setProductType(productTypeMapper.mapToEntity(model.getProductType(), entity.getProductType()));
        return entity;
    }

    @Override
    public ProductDTO mapFromEntity(Product entity) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(entity, productDTO, "active", "productType");
        productDTO.setActive(entity.getActive() == 1);

        if(entity.getProductType() != null)
            productDTO.setProductType(productTypeMapper.mapFromEntity(entity.getProductType()));

        return productDTO;
    }
}

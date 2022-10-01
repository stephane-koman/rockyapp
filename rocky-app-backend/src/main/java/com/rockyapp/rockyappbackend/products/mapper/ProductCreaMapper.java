package com.rockyapp.rockyappbackend.products.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.product_types.dao.ProductTypeDAO;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductCreaMapper extends AbstractSocleMapper<Product, ProductCreaDTO> implements SocleMapper<Product, ProductCreaDTO> {

    private ProductTypeDAO productTypeDAO;

    @Override
    public Product mapToEntity(ProductCreaDTO model, Product entity) {
        BeanUtils.copyProperties(model, entity, "id", "active", "productType");
        entity.setActive(model.isActive() ? 1 : 0);
        entity.setProductType(productTypeDAO.findProductTypeByIdAndIsNotDelete(model.getProductTypeId()));
        return entity;
    }

    @Override
    public ProductCreaDTO mapFromEntity(Product entity) {
        ProductCreaDTO productDTO = new ProductCreaDTO();
        BeanUtils.copyProperties(entity, productDTO, "active", "productType");
        productDTO.setActive(entity.getActive() == 1);

        if(entity.getProductType() != null)
            productDTO.setProductTypeId(entity.getProductType().getId());

        return productDTO;
    }
}

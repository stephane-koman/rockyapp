package com.rockyapp.rockyappbackend.products.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.documents.dto.DocumentCreaDTO;
import com.rockyapp.rockyappbackend.documents.mapper.DocumentCreaMapper;
import com.rockyapp.rockyappbackend.product_types.dao.ProductTypeDAO;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.volumes.dao.VolumeDAO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductCreaMapper extends AbstractSocleMapper<Product, ProductCreaDTO> implements SocleMapper<Product, ProductCreaDTO> {

    private ProductTypeDAO productTypeDAO;
    private VolumeDAO volumeDAO;

    @Override
    public Product mapToEntity(ProductCreaDTO model, Product entity) {
        BeanUtils.copyProperties(model, entity, "id", "active", "productType", "volume", "documentList");
        entity.setActive(model.isActive() ? 1 : 0);
        entity.setProductType(productTypeDAO.findProductTypeByIdAndIsNotDelete(model.getProductTypeId()));
        entity.setVolume(volumeDAO.findVolumeByIdAndIsNotDelete(model.getVolumeId()));
        return entity;
    }

    @Override
    public ProductCreaDTO mapFromEntity(Product entity) {
        ProductCreaDTO productDTO = new ProductCreaDTO();
        BeanUtils.copyProperties(entity, productDTO, "active", "productType", "volume");
        productDTO.setActive(entity.getActive() == 1);

        if(entity.getProductType() != null)
            productDTO.setProductTypeId(entity.getProductType().getId());

        if(entity.getVolume() != null)
            productDTO.setVolumeId(entity.getVolume().getId());

        return productDTO;
    }
}

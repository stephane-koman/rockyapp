package com.rockyapp.rockyappbackend.products.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.product_types.mapper.ProductTypeMapper;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.volumes.mapper.VolumeMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SimpleProductMapper extends AbstractSocleMapper<Product, SimpleProductDTO> implements SocleMapper<Product, SimpleProductDTO> {

    private ProductTypeMapper productTypeMapper;
    private VolumeMapper volumeMapper;

    @Override
    public Product mapToEntity(SimpleProductDTO model, Product entity) {
        return null;
    }

    @Override
    public SimpleProductDTO mapFromEntity(Product entity) {
        SimpleProductDTO productDTO = new SimpleProductDTO();
        BeanUtils.copyProperties(entity, productDTO, "active", "image", "productType", "volume");
        productDTO.setProductType(productTypeMapper.mapFromEntity(entity.getProductType()));
        productDTO.setVolume(volumeMapper.mapFromEntity(entity.getVolume()));
        productDTO.setActive(entity.getActive() == 1);
        return productDTO;
    }
}

package com.rockyapp.rockyappbackend.products.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.documents.dto.DocumentDTO;
import com.rockyapp.rockyappbackend.documents.entity.Document;
import com.rockyapp.rockyappbackend.documents.mapper.DocumentMapper;
import com.rockyapp.rockyappbackend.product_types.mapper.ProductTypeMapper;
import com.rockyapp.rockyappbackend.products.dto.ProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.volumes.mapper.VolumeMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductMapper extends AbstractSocleMapper<Product, ProductDTO> implements SocleMapper<Product, ProductDTO> {

    private ProductTypeMapper productTypeMapper;
    private VolumeMapper volumeMapper;
    private DocumentMapper documentMapper;

    @Override
    public Product mapToEntity(ProductDTO model, Product entity) {
        BeanUtils.copyProperties(model, entity, "id", "active", "productType", "volume", "documentList");
        entity.setActive(model.isActive() ? 1 : 0);
        entity.setProductType(productTypeMapper.mapToEntity(model.getProductType(), entity.getProductType()));
        entity.setVolume(volumeMapper.mapToEntity(model.getVolume(), entity.getVolume()));
        return entity;
    }

    @Override
    public ProductDTO mapFromEntity(Product entity) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(entity, productDTO, "active", "productType", "volume", "documents");
        productDTO.setActive(entity.getActive() == 1);

        if(entity.getProductType() != null)
            productDTO.setProductType(productTypeMapper.mapFromEntity(entity.getProductType()));

        if(entity.getVolume() != null)
            productDTO.setVolume(volumeMapper.mapFromEntity(entity.getVolume()));

        if(!entity.getDocuments().isEmpty())
            productDTO.setDocumentList(entity.getDocuments().stream().map(document -> documentMapper.mapFromEntity(document)).sorted(Comparator.comparing(DocumentDTO::getContent)).collect(Collectors.toList()));

        return productDTO;
    }
}

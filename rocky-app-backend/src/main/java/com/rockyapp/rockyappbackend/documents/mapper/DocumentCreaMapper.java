package com.rockyapp.rockyappbackend.documents.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.documents.dto.DocumentCreaDTO;
import com.rockyapp.rockyappbackend.documents.entity.Document;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocumentCreaMapper extends AbstractSocleMapper<Document, DocumentCreaDTO> implements SocleMapper<Document, DocumentCreaDTO> {

    @Override
    public Document mapToEntity(DocumentCreaDTO model, Document entity) {
        BeanUtils.copyProperties(model, entity,  "productId");
        entity.setContent(model.getContent());
        if(model.getProduct() != null)
            entity.setProduct(model.getProduct());
        return entity;
    }

    @Override
    public DocumentCreaDTO mapFromEntity(Document entity) {
        return null;
    }
}

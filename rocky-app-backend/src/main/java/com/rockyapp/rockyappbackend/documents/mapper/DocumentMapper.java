package com.rockyapp.rockyappbackend.documents.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import com.rockyapp.rockyappbackend.documents.dto.DocumentDTO;
import com.rockyapp.rockyappbackend.documents.entity.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper extends AbstractSocleMapper<Document, DocumentDTO> implements SocleMapper<Document, DocumentDTO> {
    @Override
    public Document mapToEntity(DocumentDTO model, Document entity) {
        return null;
    }

    @Override
    public DocumentDTO mapFromEntity(Document entity) {
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setContent(entity.getContent());
        documentDTO.setFileName(entity.getFileName());
        documentDTO.setMimeType(entity.getMimeType());
        return documentDTO;
    }
}

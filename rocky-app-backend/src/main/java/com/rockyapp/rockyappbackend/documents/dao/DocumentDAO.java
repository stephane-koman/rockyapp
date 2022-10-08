package com.rockyapp.rockyappbackend.documents.dao;

import com.rockyapp.rockyappbackend.documents.entity.Document;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface DocumentDAO extends CrudRepository<Document, Long> {
    Document findDocumentByContent(String content);
}
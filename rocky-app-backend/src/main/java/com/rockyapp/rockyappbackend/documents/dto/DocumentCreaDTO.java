package com.rockyapp.rockyappbackend.documents.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentCreaDTO implements SocleDTO {
    private String content;
    private String fileName;
    private String mimeType;
    private Product product;
}

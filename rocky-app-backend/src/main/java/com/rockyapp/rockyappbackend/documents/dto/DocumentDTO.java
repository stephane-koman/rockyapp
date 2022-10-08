package com.rockyapp.rockyappbackend.documents.dto;

import com.rockyapp.rockyappbackend.common.dto.SocleDTO;
import com.rockyapp.rockyappbackend.utils.enums.MesureEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO implements SocleDTO {
    private String content;
    private String fileName;
    private String mimeType;
}
